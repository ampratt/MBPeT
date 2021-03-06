package com.aaron.mbpet.services;

import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aaron.mbpet.domain.ActionResponse;
import com.aaron.mbpet.ui.MasterTerminalWindow;
import com.aaron.mbpet.views.sessions.SessionViewer;

public interface PushLabelUpdater {
	
	void updateMonitoringFields(final String[] values, final int numslaves, final String[] slaveresults, final HashSet<ActionResponse> responseset,SessionViewer sessionViewer);
	
    void printNewestMessage(String message, SessionViewer sessionViewer); //, double current);

    void printFinalMessage(String message, final int numslaves, SessionViewer sessionViewer);

//	void printNextMasterOutput(StringBuilder message, MasterTerminalWindow masterWindow);

//    void printNextInput(String message, MasterTerminalWindow masterWindow);	//SessionViewer sessionViewer); //, double current);

    
//	void onRequestStart(HttpServletRequest request, HttpServletResponse response);
//
//	void onRequestEnd(HttpServletRequest request, HttpServletResponse response);

}
