package com.ohmcoe.vultr;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ServerList {

    private ArrayList<Server> serverLists;

    public ServerList() {

    }

    public List<Server> toList() {
        List<Server> list = serverLists;

        return list;
    }


/*    public String[] getStringArray() {
        ArrayList<String> strServerList = new ArrayList<>();

        for (Server server : serverLists) {
            String str = "";
            str += "Server name: " + server.getLabel() + "\n";
            str += "Main IP: " + server.getMain_ip() + "\n";
            str += "Pending charge: $" + server.getPending_charges() + "\n";
            strServerList.add(str);
        }

        return strServerList.toArray(new String[strServerList.size()]);
    }*/

    public ServerList(String response) {
        serverLists = new ArrayList<>();

        try {
            JSONObject jObj = new JSONObject(response);
            Iterator<?> keys = jObj.keys();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                String result = jObj.getString(key);

                JSONObject serverJObj = new JSONObject(result);

                Server server = new Server();
                server.loadAttribute(serverJObj);

                serverLists.add(server);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Server> getServerLists() {
        return serverLists;
    }


}