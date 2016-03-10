package com.aaron.mbpet.services;

import com.aaron.mbpet.views.sessions.SessionViewer;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;

public class ProgressBarWorker {

	double testduration;	// = 40;
	double increaseAmount;	// = (1.0 / (testduration/3));
	boolean stopFlag = false;
	boolean completeFlag = false;
	
	SessionViewer sessionViewer;
	ProgressBar progressbar;
	Label progressstatus;
    // Volatile because read in another thread in access()
    volatile double current = 0.0;

    public ProgressBarWorker(SessionViewer sessionViewer, ProgressBar progressbar, 
    		Label progressstatus, double testduration) {		
    	this.sessionViewer = sessionViewer;
    	this.progressbar = progressbar;
    	this.progressstatus = progressstatus;
    	this.testduration = testduration;	// + 8;
    	this.increaseAmount = (1.0 / (this.testduration));	//(1.0 / (this.testduration/3));

    	System.out.println("test duration is " + String.valueOf(testduration));
    	System.out.println("increase amount is " + String.valueOf(increaseAmount));
    }
    
    public void increaseProgressBar() {
    	new Thread() {
            @Override
            public void run() {

		        // Count up until 1.0 is reached
//		        while (current < 1.0 && stopFlag==false) {
            	if (current < 1.0 && stopFlag==false){
		            current += increaseAmount;	//0.05;
		
		
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
            	else if (stopFlag==true) {
		            // Update the UI thread-safely
		            UI.getCurrent().access(new Runnable() {
		                @Override
		                public void run() {
		//                	current = 0.0;
		//                	progressbar.setValue((float) current);	//(new Float(0.0));
		                    if (current < 1.0) {
		                    	progressstatus.setValue("stopped");	//done
		                    	sessionViewer.resetStartStopButton();
		                    }
		                }
		            });
	    			System.out.println("progressThread terminated!");
		        }
		        
		        
            };
        }.start();
    }

    
    public void endThread() {
  	  	stopFlag = true;
    }
    public void setComplete() {
    	completeFlag = true;
    	increaseProgressBar();
//    	progressbar.setValue((float) current);	//new Float()
//    	progressstatus.setValue("100%");
//    	sessionViewer.resetStartStopButton();
    }
    
}
