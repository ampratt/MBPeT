package com.aaron.mbpet.components.flot;

import org.json.JSONArray;

import com.vaadin.shared.communication.ServerRpc;

public interface FlotDropRpc extends ServerRpc {
    public void onDataDrop(int seriesIndex, int dataIndex, JSONArray dataPoint, double x1, double y1, JSONArray data);
    public void onDataDrop(int seriesIndex, int dataIndex);
}