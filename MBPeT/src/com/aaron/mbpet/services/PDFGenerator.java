package com.aaron.mbpet.services;

import com.aaron.mbpet.MbpetUI;
import com.vaadin.ui.UI;

public class PDFGenerator {

	String usersBasepath = ((MbpetUI) UI.getCurrent()).getUsersBasepath();	//"C:\\dev\\mbpet\\users\\";
	String mbpetBasepath = ((MbpetUI) UI.getCurrent()).getMbpetBasepath();	//"C:\\dev\\mbpet\\";
	
	
	public PDFGenerator() {

	}
	
	public void createPdfReport(String reportDir){
		System.out.println("received report dir: " + reportDir);
	}

}
