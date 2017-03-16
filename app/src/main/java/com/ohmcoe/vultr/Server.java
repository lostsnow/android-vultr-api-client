package com.ohmcoe.vultr;

import android.icu.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by OHM on 10/3/2560.
 */

public class Server {
    private String SUBID;
    private String os;
    private String ram;
    private String main_ip;
    private String label;
    private String server_state;
    private String pending_charges;
    private String current_bandwidth_gb;
    private String allowed_bandwidth_gb;

    public Server() {

    }

    public void loadAttribute(JSONObject jObj) throws JSONException {
        this.setSUBID(jObj.getString("SUBID"));
        this.setOs(jObj.getString("os"));
        this.setRam(jObj.getString("ram"));
        this.setMain_ip(jObj.getString("main_ip"));
        this.setLabel(jObj.getString("label"));
        this.setServer_state(jObj.getString("server_state"));
        this.setPending_charges(jObj.getString("pending_charges"));
        this.setCurrent_bandwidth_gb(jObj.getString("current_bandwidth_gb"));
        this.setAllowed_bandwidth_gb(jObj.getString("allowed_bandwidth_gb"));
    }

    public String getStrPendingCharges() {
        return "$" + pending_charges;
    }

    public String getBandwidth() {
        String bandwidth = "";

        Double currentBandwidth = Double.parseDouble(current_bandwidth_gb);
        Double allowedBandwidth = Double.parseDouble(allowed_bandwidth_gb);

        DecimalFormat df = new DecimalFormat("0");

        bandwidth += current_bandwidth_gb + " GB of " + allowed_bandwidth_gb + " GB (" + df.format(currentBandwidth / allowedBandwidth * 100) + "%)";
        return bandwidth;
    }

    public String getSUBID() {
        return SUBID;
    }

    public void setSUBID(String SUBID) {
        this.SUBID = SUBID;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getMain_ip() {
        return main_ip;
    }

    public void setMain_ip(String main_ip) {
        this.main_ip = main_ip;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getServer_state() {
        return server_state;
    }

    public void setServer_state(String server_state) {
        this.server_state = server_state;
    }

    public String getPending_charges() {
        return pending_charges;
    }

    public void setPending_charges(String pending_charges) {
        this.pending_charges = pending_charges;
    }

    public String getCurrent_bandwidth_gb() {
        return current_bandwidth_gb;
    }

    public void setCurrent_bandwidth_gb(String current_bandwidth_gb) {
        this.current_bandwidth_gb = current_bandwidth_gb;
    }

    public String getAllowed_bandwidth_gb() {
        return allowed_bandwidth_gb;
    }

    public void setAllowed_bandwidth_gb(String allowed_bandwidth_gb) {
        this.allowed_bandwidth_gb = allowed_bandwidth_gb;
    }
}
