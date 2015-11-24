package com.aaron.mbpet.services;

public interface PushLabelUpdater {
	
	void updateMonitoringPanels(final String[] values, final int numslaves, final String[] slaveresults);
	
    void printNewestMessage(String message); //, double current);

    void printFinalMessage(String message, final int numslaves);

}
