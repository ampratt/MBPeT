package com.aaron.mbpet.services;

import com.aaron.mbpet.views.sessions.SessionViewer;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;

//A thread to do some work
public class ProgressBarThread extends Thread {
	double testduration;	// = 40;
	double increaseAmount;	// = (1.0 / (testduration/3));
	boolean stopFlag = false;
	boolean completeFlag = false;
	
	SessionViewer sessionViewer;
	ProgressBar progressbar;
	Label progressstatus;
    // Volatile because read in another thread in access()
    volatile double current = 0.0;

    public ProgressBarThread(SessionViewer sessionViewer, ProgressBar progressbar, 
    		Label progressstatus, double testduration) {		
    	this.sessionViewer = sessionViewer;
    	this.progressbar = progressbar;
    	this.progressstatus = progressstatus;
    	this.testduration = testduration + 8;	// = 40;
    	this.increaseAmount = (1.0 / (this.testduration/2));	//(1.0 / (this.testduration/3));

    }
    
    @Override
    public void run() {
    	System.out.println("test duration is " + String.valueOf(testduration));
    	System.out.println("increase amount is " + String.valueOf(increaseAmount));
        // Count up until 1.0 is reached
        while (current < 1.0 && stopFlag==false) {
            current += increaseAmount;	//0.05;

            // Do some "heavy work"
            try {
                sleep(2000); 	// Sleep for 2 seconds 
            } catch (InterruptedException e) {}

            if (completeFlag==true) { 
            	current = 1.0;
            } else if (completeFlag==false && current >= 1.0){
            	current = 0.99;
            }
 
            // Update the UI thread-safely
            UI.getCurrent().access(new Runnable() {
                @Override
                public void run() {
                	progressbar.setValue((float) current);	//new Float()
                    if (current < 1.0)
                    	progressstatus.setValue("" +
                            ((int)(current*100)) + "%");	//done
                    else {
                    	progressstatus.setValue("100%");
                    	sessionViewer.resetStartStopButton();
                	}
                }
            });
        }
//    	SessionViewer.resetStartStopButton();

        if (stopFlag==true) {
            // Update the UI thread-safely
            UI.getCurrent().access(new Runnable() {
                @Override
                public void run() {
                	current = 0.0;
                	progressbar.setValue((float) current);	//(new Float(0.0));
                    if (current < 1.0) {
                    	progressstatus.setValue("stopped");	//done
                    	sessionViewer.resetStartStopButton();
                    }
                }
            });
        }
//        // Show the "all done" for a while
//        try {
//            sleep(2000); // Sleep for 2 seconds
//        } catch (InterruptedException e) {}

//        // Update the UI thread-safely
//        UI.getCurrent().access(new Runnable() {
//            @Override
//            public void run() {
//                // Restore the state to initial
//            	SessionViewer.progressbar.setValue(new Float(0.0));
//            	SessionViewer.progressbar.setEnabled(false);
//                        
//                // Stop polling
//                UI.getCurrent().setPollInterval(-1);
//                
//                button.setEnabled(true);
//                status.setValue("not running");
//            }
//        });
    }
    
    public void endThread() {
  	  	stopFlag = true;
    }
    public void setComplete() {
    	completeFlag = true;
    }
    
}