package com.ohmcoe.vultr;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ServerList {

    private ArrayList<Server> serverLists;
    private String body;

    public ServerList() {

    }

    public List<Server> toList() {
        List<Server> list = serverLists;

        return list;
    }


    public ServerList(String response) {
        body = response;
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


    public String getBody() {
        return body;
    }
}