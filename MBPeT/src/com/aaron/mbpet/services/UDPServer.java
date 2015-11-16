package com.aaron.mbpet.services;

import java.io.*;
import java.net.*;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

/**
 * a simple UDP Server which opens upa a port
 * - See more at: https://systembash.com/a-simple-java-udp-server-and-udp-client/#sthash.KQxQnEoa.dpuf
 */
public class UDPServer {
	
//   public static void main(String args[]) throws Exception {
	public UDPServer() {
	
		try {
			JsonDecoderMbpet jsonDecoder = new JsonDecoderMbpet();
			
			// create datagram socket at port num
			DatagramSocket serverSocket = new DatagramSocket(9999);

			byte[] receiveData;
			byte[] sendData;	// = new byte[5120];
			
			int x=1;
			serverSocket.setSoTimeout(40000);   // set the timeout in millisecounds.  
			while(true) {
				try {
				   receiveData = new byte[5120];
				// create space for received datagram
		    	   DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		    	   // receive datagram
		    	   System.out.println("Waiting for datagram packet...");
		    	   serverSocket.receive(receivePacket);
		    	   
		    	   String sentence = new String(receivePacket.getData());
		    	   
		    	   // get ip address, port # of sender
		    	   InetAddress IPAddress = receivePacket.getAddress();
		    	   int port = receivePacket.getPort();
		    	   
		    	   System.out.println("== RECEIVED message from client ==");
		    	   System.out.println("From: " + IPAddress + ":" + port);
		           System.out.println("ORIGINAL message: \n" + sentence.trim() + "\n");	//new JsonDecoderMbpet(sentence.trim()));// + );
//		           System.out.println("\nMessage: \n" + jsonDecoder.getMessage(sentence.trim()));	//new JsonDecoderMbpet(sentence.trim()));// + );
		//           JsonDecoderMbpet jsonDecoder = new JsonDecoderMbpet(sentence);
		           
		           // update monitoring tab fields
//		           PushdemoUI.addNewMessageComponent("MESSAGE #" + x + "\n" + sentence.trim() + "\n\n");
//		           TabLayout.getMonitoringTab().updateFields(jsonDecoder.getKeyValues(sentence.trim()));

		           UI.getCurrent().push();
//		           PushdemoUI.mainLayout.addComponent(new Label(sentence.trim()));//jsonDecoder.getMessage(sentence.trim())));	
		        		   //jsonDecoder.getKeyValues(sentence.trim())));
		           x++;
				}
	            catch (SocketTimeoutException e) {
	                // timeout exception.
	                System.out.println("Timeout reached!!! " + e);
	                serverSocket.close();
	            }
			}

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   	
   }
}





		           
