package com.aaron.mbpet.components.flot;

import com.vaadin.shared.communication.ServerRpc;

public interface FlotClickRpc extends ServerRpc {
    public void onPlotClick(int seriesIndex, int dataIndex);
}