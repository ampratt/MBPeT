package com.aaron.mbpet.ui;

import com.aaron.mbpet.services.FileSystemUtils;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class SpinnerLoginWindow extends Window {

	VerticalLayout layout;
	private ProgressBar spinner;
	private Label spinLabel;
	
	public SpinnerLoginWindow(String username) {
//	    setCaption("MBPeT Master Output");
	    center();
	//      addStyleName("reportwindow");
	    setResizable(false);
	    setClosable(false);
//	    setCloseShortcut(KeyCode.ESCAPE, null);
//	    setHeight(30, Unit.EM);	//(40.0f, Unit.PERCENTAGE);
//	    setWidth(30, Unit.EM);
//	    setPosition(1, 1);

	    layout = new VerticalLayout();
	    layout.setMargin(true);

        buildContent();
  	
	  	setContent(layout);
	  	
//	  	copyFiles(username);
	}

	private void copyFiles(String username) {
    	FileSystemUtils fileUtils = new FileSystemUtils();
    	fileUtils.copyMasterToUserDir(username); 		
	}

	private void buildContent() {
		spinner = new ProgressBar();
        spinner.setIndeterminate(true);
        spinner.setVisible(true);
//        spinner.addStyleName("small");
//        spinner.setCaption("initiating slaves..");
        
        spinLabel = new Label("copying MBPeT files to local directory");
//        spinLabel.addStyleName("tiny");
        
        layout.addComponents(spinner, spinLabel);
        layout.setComponentAlignment(spinner, Alignment.MIDDLE_CENTER);		
        layout.setComponentAlignment(spinLabel, Alignment.MIDDLE_CENTER);
        layout.setExpandRatio(spinLabel, 1);
	}
}
