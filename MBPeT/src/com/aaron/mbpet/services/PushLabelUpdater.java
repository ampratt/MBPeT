package com.aaron.mbpet.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aaron.mbpet.ui.MasterTerminalWindow;
import com.aaron.mbpet.views.sessions.SessionViewer;

public interface PushLabelUpdater {
	
	void updateMonitoringFields(final String[] values, final int numslaves, final String[] slaveresults, SessionViewer sessionViewer);
	
    void printNewestMessage(String message, SessionViewer sessionViewer); //, double current);

    void printFinalMessage(String message, final int numslaves, SessionViewer sessionViewer);

//    void printNextInput(String message, MasterTerminalWindow masterWindow);	//SessionViewer sessionViewer); //, double current);

    
//	void onRequestStart(HttpServletRequest request, HttpServletResponse response);
//
//	void onRequestEnd(HttpServletRequest request, HttpServletResponse response);

}
