package com.aaron.mbpet.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PushLabelUpdater {
	
	void updateMonitoringPanels(final String[] values, final int numslaves, final String[] slaveresults);
	
    void printNewestMessage(String message); //, double current);

    void printFinalMessage(String message, final int numslaves);

//	void onRequestStart(HttpServletRequest request, HttpServletResponse response);
//
//	void onRequestEnd(HttpServletRequest request, HttpServletResponse response);

}
