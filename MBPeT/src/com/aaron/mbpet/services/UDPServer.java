package com.aaron.mbpet.services;

import java.io.*;
import java.net.*;

import com.aaron.mbpet.MbpetUI;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

/**
 * a simple UDP Server which opens upa a port
 * - See more at: https://systembash.com/a-simple-java-udp-server-and-udp-client/#sthash.KQxQnEoa.dpuf
 */
public class UDPServer {
	

	public UDPServer(MbpetUI mbpetUI) {
    	final PushLabelUpdater updater = mbpetUI;
    	
    	// Volatile because read in another thread in access()
        double current = 0.0;
        
		try {
			JsonDecoderMbpet jsonDecoder = new JsonDecoderMbpet();
			
			// create datagram socket at port num
			DatagramSocket serverSocket = new DatagramSocket(9999);

			byte[] receiveBuffer;
			byte[] sendData;	// = new byte[5120];
			String sentence;
			
			int x=1;
			serverSocket.setSoTimeout(8000);   // set the timeout in millisecounds.  
			while(true) {
//		           current += 0.05;

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
//		           System.out.println("\nMessage: \n" + jsonDecoder.getMessage(sentence.trim()));	//new JsonDecoderMbpet(sentence.trim()));// + );
		//           JsonDecoderMbpet jsonDecoder = new JsonDecoderMbpet(sentence);
		           
		           // update monitoring tab fields - thread-safely	           
//		           updater.printNewestMessage("MESSAGE #" + x + "\n" + sentence.trim() + "\n\n", current);
//		           PushdemointerfacedUI.addNewMessageComponent("MESSAGE #" + x + "\n" + sentence.trim() + "\n\n");
//		           UI.getCurrent().push();
		           
//		           PushdemoUI.mainLayout.addComponent(new Label(sentence.trim()));//jsonDecoder.getMessage(sentence.trim())));	
		        		   //jsonDecoder.getKeyValues(sentence.trim())));
		           x++;
				}
	            catch (SocketTimeoutException e) {
	                // timeout exception.
	                System.out.println("Timeout reached!!! " + e);
	                serverSocket.close();
	            }
//				finally {
//					serverSocket.close();
//				}
			}

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	
	
	
//	public UDPServer() {
//	
//		try {
//			JsonDecoderMbpet jsonDecoder = new JsonDecoderMbpet();
//			
//			// create datagram socket at port num
//			DatagramSocket serverSocket = new DatagramSocket(9999);
//
//			byte[] receiveData;
//			byte[] sendData;	// = new byte[5120];
//			
//			int x=1;
//			serverSocket.setSoTimeout(40000);   // set the timeout in millisecounds.  
//			while(true) {
//				try {
//					   receiveData = new byte[5120];
//					// create space for received datagram
//			    	   DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//			    	   // receive datagram
//			    	   System.out.println("Waiting for datagram packet...");
//			    	   serverSocket.receive(receivePacket);
//			    	   
//			    	   String sentence = new String(receivePacket.getData());
//			    	   
//			    	   // get ip address, port # of sender
//			    	   InetAddress IPAddress = receivePacket.getAddress();
//			    	   int port = receivePacket.getPort();
//			    	   
//			    	   System.out.println("== RECEIVED message from client ==");
//			    	   System.out.println("From: " + IPAddress + ":" + port);
//			           System.out.println("ORIGINAL message: \n" + sentence.trim() + "\n");	//new JsonDecoderMbpet(sentence.trim()));// + );
////			           System.out.println("\nMessage: \n" + jsonDecoder.getMessage(sentence.trim()));	//new JsonDecoderMbpet(sentence.trim()));// + );
//			//           JsonDecoderMbpet jsonDecoder = new JsonDecoderMbpet(sentence);
//			           
//			           // update monitoring tab fields
//			           updater.printnewestMessage("MESSAGE #" + x + "\n" + sentence.trim() + "\n\n");
////			           PushdemointerfacedUI.addNewMessageComponent("MESSAGE #" + x + "\n" + sentence.trim() + "\n\n");
////			           UI.getCurrent().push();
//			           
////			           PushdemoUI.mainLayout.addComponent(new Label(sentence.trim()));//jsonDecoder.getMessage(sentence.trim())));	
//			        		   //jsonDecoder.getKeyValues(sentence.trim())));
//			           x++;
//					}
//		            catch (SocketTimeoutException e) {
//		                // timeout exception.
//		                System.out.println("Timeout reached!!! " + e);
//		                serverSocket.close();
//		            }
//				}
//
//		} catch (SocketException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//   }
	
	
}





		           
