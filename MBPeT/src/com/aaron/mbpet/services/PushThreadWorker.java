package com.aaron.mbpet.services;

import com.aaron.mbpet.MbpetUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.UIDetachedException;

public class PushThreadWorker {
    public void fetchAndUpdateDataWith(final MbpetUI mbpetUI) {	//final LabelUpdater updater
        new Thread() {
            @Override
            public void run() {
            	final PushLabelUpdater updater = mbpetUI;
            	int x = 1;
                try {
//                    // Update the data for a while
//                    while (x < 5) {
//                    	Thread.sleep(1000);
//                        updater.printnewestMessage("Loading UI, please wait..." + x);
////                		loadingText.setValue("Loading UI, please wait..." + x);
////                		UI.getCurrent().push();
//                		x++;
//                    	
//                    }
                	// 1. run the server till test is finished
                	new UDPServer(mbpetUI);
                	
                	
	                // 2. Inform that we have stopped running
                    updater.printnewestMessage("\nTest Session is finished!");

                } 
//                catch (final InterruptedException e) {
//                    e.printStackTrace();
//                    updater.printnewestMessage("Thread was interrupted");
//                } 
                catch (UIDetachedException e) {
                	 e.printStackTrace();
                     updater.printnewestMessage("Thread was interrupted");
                }
                        
            };
        }.start();
    }
}
