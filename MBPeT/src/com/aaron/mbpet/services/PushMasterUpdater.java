package com.aaron.mbpet.services;

import com.aaron.mbpet.ui.MasterTerminalWindow;
import com.aaron.mbpet.views.sessions.SessionViewer;

public interface PushMasterUpdater {
		
//    void printNextMasterOutput(String message, MasterTerminalWindow masterWindow); //, double current);

	void printNextMasterOutput(StringBuilder message, MasterTerminalWindow masterWindow);
    
}
