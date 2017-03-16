package com.ohmcoe.vultr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
import java.util.List;

public class Bandwidth {

    @SerializedName("incoming_bytes")
    @Expose
    private List<List<String>> incomingBytes = null;
    @SerializedName("outgoing_bytes")
    @Expose
    private List<List<String>> outgoingBytes = null;

    public List<List<String>> getIncomingBytes() {
        return incomingBytes;
    }

    public void setIncomingBytes(List<List<String>> incomingBytes) {
        this.incomingBytes = incomingBytes;
    }

    public List<List<String>> getOutgoingBytes() {
        return outgoingBytes;
    }

    public void setOutgoingBytes(List<List<String>> outgoingBytes) {
        this.outgoingBytes = outgoingBytes;
    }

    public double getSumInbound() {
        double totalByte = 0.0;

        for (List<String> inbound : incomingBytes) {
            totalByte += Double.parseDouble(inbound.get(1));
        }

        return totalByte;
    }

    public double getSumOutbound() {
        double totalByte = 0.0;

        for (List<String> inbound : outgoingBytes) {
            totalByte += Double.parseDouble(inbound.get(1));
        }

        return totalByte;
    }

    public String[] getDates() {
        int size = incomingBytes.size();
        String dates[] = new String[size];
        int count = 0;

        for (List<String> inbound : incomingBytes) {
            dates[count] = inbound.get(0);
            count++;
        }

        return dates;
    }

    public int[] getXGraph() {
        int size = incomingBytes.size();
        int x[] = new int[size];

        for (int i = 0; i < size; i++) {
            x[i] = i + 1;
        }

        return x;
    }

    public double[] getInboundGraph() {
        int size = incomingBytes.size();
        double inbound[] = new double[size];
        int count = 0;

        DecimalFormat df = new DecimalFormat("0.00");

        for (List<String> incoming : incomingBytes) {
            Double dIncoming = Double.parseDouble(incoming.get(1))/1048576;
            dIncoming = Double.parseDouble(df.format(dIncoming));
            inbound[count] = dIncoming;
            count++;
        }

        return inbound;
    }

    public double[] getOutboundGraph() {
        int size = outgoingBytes.size();
        double outbound[] = new double[size];
        int count = 0;
        DecimalFormat df = new DecimalFormat("0.00");

        for (List<String> outgoing : outgoingBytes) {
            Double dOutgoing = Double.parseDouble(outgoing.get(1))/1048576;
            dOutgoing = Double.parseDouble(df.format(dOutgoing));
            outbound[count] = dOutgoing;
            count++;
        }

        return outbound;
    }
}