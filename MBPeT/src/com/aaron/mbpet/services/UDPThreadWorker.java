package com.aaron.mbpet.services;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.views.tabs.TabLayout;
import com.aaron.mbpet.views.sessions.SessionViewer;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.UIDetachedException;

public class UDPThreadWorker {
	
    public boolean stopFlag = false;
    private boolean navToReports = true;
    DatagramSocket serverSocket;
    
    SessionViewer sessionViewer;
    
	public UDPThreadWorker(SessionViewer sessionViewer) {
		this.sessionViewer = sessionViewer;
	}


	public void fetchAndUpdateDataWith(final MbpetUI mbpetUI, final int numslaves) {	//final LabelUpdater updater
    	new Thread() {
            @Override
            public void run() {
            	final PushLabelUpdater updater = mbpetUI;
                try {

                	// 1. run the server till test is finished
//                	new UDPServer(mbpetUI);
                	
                    double current = 0.0;
            		try {

            			JsonDecoderMbpet jsonDecoder = new JsonDecoderMbpet();
            			
            			// create datagram socket at port num
//            			serverSocket = new DatagramSocket(9999);
//            			serverSocket.getLocalPort();
//            			serverSocket = new DatagramSocket(9999);

            			try {
            				serverSocket = create();
            			    System.out.println("listening on port: " + serverSocket.getLocalPort());
//            			    component.getUI().getSession().getLock().lock();
//            			    try {
//            			       doStuffWith(component);
//            			    } finally {
//            			       component.getUI().getSession().getLock().unlock();
//            			    }
//
//            			    Notification.show("listening on port: " + serverSocket.getLocalPort());
            			} catch (IOException ex) {
            			    System.err.println("no available ports");
            			}
            			
            			
            			byte[] receiveBuffer;	// byte[] sendBuffer;	// = new byte[5120];
            			String sentence;
            			
            			int x=1;
            			boolean firstMessage = true;
            			serverSocket.setSoTimeout(50000);   // set the timeout in millisecounds.  
            			while(true && stopFlag==false) {

            				if( isStopFlag()==true )
                    			serverSocket.setSoTimeout(50);   // set the timeout in millisecounds.  

            				try {
            				   receiveBuffer = new byte[5120];
            				   // create space for received datagram
            		    	   DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            		    	   // receive datagram
            		    	   System.out.println("Waiting for datagram packet...");
            		    	   serverSocket.receive(receivePacket);
            		    	   
            		    	   sentence = new String(receivePacket.getData());
            		    	   
            		    	   // get ip address, port # of sender
            		    	   InetAddress IPAddress = receivePacket.getAddress();
            		    	   int port = receivePacket.getPort();
            		    	   
            		    	   System.out.println("== RECEIVED message from client ==");
            		    	   System.out.println("From: " + IPAddress + ":" + port);
            		           System.out.println("ORIGINAL message: \n" + sentence.trim() + "\n");	//new JsonDecoderMbpet(sentence.trim()));// + );
//            		           System.out.println("\nMessage: \n" + jsonDecoder.getMessage(sentence.trim()));	//new JsonDecoderMbpet(sentence.trim()));// + );
            		//           JsonDecoderMbpet jsonDecoder = new JsonDecoderMbpet(sentence);
            		           
            		           // update monitoring tab fields - thread-safely	           
            		           updater.printNewestMessage("MESSAGE #" + x + "\n" + sentence.trim() + "\n\n", sessionViewer);
//            		           PushdemointerfacedUI.addNewMessageComponent("MESSAGE #" + x + "\n" + sentence.trim() + "\n\n");
//            		           UI.getCurrent().push();
            		           if (firstMessage) {
            		        	   sessionViewer.displayProgressBar(true);
	            		   	        firstMessage=false;
            		           }
            		           if (!sentence.contains("report_address")){
            		        	   String[] results = jsonDecoder.getKeyValues(sentence.trim());
            		        	   String[] slaveresults = jsonDecoder.getSlaves(sentence.trim(), numslaves);
            		        	   
                		           // update monitoring tab fields - thread-safely	           
//                		           updater.printNewestMessage("MESSAGE #" + x + "\n" + sentence.trim() + "\n\n", current);
            		        	   updater.updateMonitoringFields(results, numslaves, slaveresults, sessionViewer);
            		        	   
            		        	   updater.printNewestMessage("\nMESSAGE #" + x + " VALUES:\n" + 
            		        			   "average - " + results[0] + 
            		        			   "\n; min/max - " + results[1] + 
            		        			   "\n; throughput - " + results[2] + 
            		        			   "\n; success - " + results[3] + 
            		        			   "\n; error - " + results[4] + 
            		        			   "\n; sent - " + results[5] + 
            		        			   "\n; received - " + results[6] + 		        			
            		        			   "\n; users targeted - " + results[7] + 
            		        			   "\n\n",
            		        			   sessionViewer);

//            	                    serverSocket.close();
            		           } else {
            		        	   sessionViewer.progressThread.setComplete();
	        		               // 2. Inform that we have stopped running
	        	                   updater.printFinalMessage("\nTest Session is finished!", numslaves, sessionViewer);
	        	        	       
	        	                   // navigate to reports
	        	                   if (navToReports) {
	        	                	   sessionViewer.tabs.refreshReports();
	        	                	   sessionViewer.tabs.setSelectedTab(2);
	        	                   }
	        	                   sessionViewer.displayProgressBar(false);
	        	                   serverSocket.close();
            		           }
            		           
//            		           if (sentence.contains("report_address")){
//            		        	   	sessionViewer.progressThread.setComplete();
//            		                // 2. Inform that we have stopped running
//            	                    updater.printFinalMessage("\nTest Session is finished!");
//            	        	        // navigate to reports
//            	                    if (navToReports)
//            	                    	sessionViewer.tabs.setSelectedTab(2);
//            	                    sessionViewer.displayProgressBar(false);
//            	                    serverSocket.close();
//            		           }
            		        	   
            		        	   
            		           x++;
            				}
            	            catch (SocketTimeoutException e) {
            	                // timeout exception.
            	                System.out.println("Timeout reached!!! " + e);
                                updater.printFinalMessage("\nTimeout reached!!!", numslaves, sessionViewer);
            	                serverSocket.close();
            	                sessionViewer.progressThread.endThread();
            	            } 
//            				catch(SocketException e){
//            	                // socket closed.
//            	                System.out.println("socket closed. " + e);
////                                updater.printFinalMessage("\nTimeout reached!!!", numslaves);
//            	                serverSocket.close();
//            	                sessionViewer.progressThread.endThread();
//            	            }
//            				finally {
//            					serverSocket.close();
//            				}
            			}
//            	    	serverSocket.setSoTimeout(50);

    	                serverSocket.close();
    	                
            		} catch (SocketException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		} catch (IOException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
                	
//	                // 2. Inform that we have stopped running
//                    updater.printFinalMessage("\nTest Session is finished!");
//        	        // navigate to reports

            		if (stopFlag)
            			sessionViewer.progressThread.endThread();
                    if (navToReports) {
                    	sessionViewer.tabs.refreshReports();
                    	sessionViewer.tabs.setSelectedTab(2);
                    }

                }
//                catch (final InterruptedException e) {
//                    e.printStackTrace();
//                    updater.printnewestMessage("Thread was interrupted");
//                } 
                catch (UIDetachedException e) {
                	 e.printStackTrace();
                     updater.printFinalMessage("Thread was interrupted", numslaves, sessionViewer);
                }
                        
            };
        }.start();
    }
	
	public DatagramSocket create() throws IOException {		//(int[] ports) throws IOException {
	    for (int port=9999; port<10100; port++) {		//(int port : ports)
	        try {
	        	System.out.println("port " + port + " should be open");
	            return new DatagramSocket(port);
	        } catch (IOException ex) {
	            continue; // try next port
	        }
	    }
	    // if the program gets here, no port in the range was found
	    throw new IOException("no free port found");
	}
	
    public void endThread() {
    	setStopFlag(true);
    	//    	stopFlag = true;
    }

    public void endThread(boolean navToReports) {
    	setStopFlag(true);
//stopFlag = true;
    	this.navToReports = navToReports;
    }


	public boolean isStopFlag() {
		return stopFlag;
	}


	public void setStopFlag(boolean stopFlag) {
		this.stopFlag = stopFlag;
	}
    
    
    
//    public void fetchAndUpdateDataWith(final MbpetUI mbpetUI) {	//final LabelUpdater updater
//        new Thread() {
//            @Override
//            public void run() {
//            	final PushLabelUpdater updater = mbpetUI;
////            	int x = 1;
//                try {
////                    // Update the data for a while
////                    while (x < 5) {
////                    	Thread.sleep(1000);
////                        updater.printnewestMessage("Loading UI, please wait..." + x);
//////                		loadingText.setValue("Loading UI, please wait..." + x);
//////                		UI.getCurrent().push();
////                		x++;
////                    	
////                    }
//                	// 1. run the server till test is finished
//                	new UDPServer(mbpetUI);
//                	
//                	
//	                // 2. Inform that we have stopped running
////                    updater.printnewestMessage("\nTest Session is finished!", 0.0);
//                	sessionViewer.resetStartStopButton();
//                    updater.printFinalMessage("\nTest Session is finished!");
//
//                } 
////                catch (final InterruptedException e) {
////                    e.printStackTrace();
////                    updater.printnewestMessage("Thread was interrupted");
////                } 
//                catch (UIDetachedException e) {
//                	 e.printStackTrace();
//                     updater.printFinalMessage("Thread was interrupted");
//                }
//                        
//            };
//        }.start();
//    }
}
