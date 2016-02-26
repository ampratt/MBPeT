package com.aaron.mbpet.services;

import com.aaron.mbpet.ui.MasterTerminalWindow;

public interface PushMasterUpdater {
		
    void printNextInput(String message, MasterTerminalWindow masterWindow); //, double current);
    
}
