package com.aaron.mbpet.views.tabs.parameterstab;

import org.vaadin.aceeditor.AceEditor;

import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.views.sessions.SessionViewer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class ParametersTab extends Panel {

	AceEditor editor;
//	private TestSession currsession;
	
	public ParametersTab(TestSession currsession) {		//TestSession currsession
//		setSizeFull();
//		setMargin(true);
//		setSpacing(true);
//		this.currsession = currsession;
		editor = new AceEditor();
		
		setHeight("100%");
		setWidth("100%");
		addStyleName("borderless");
			
		VerticalLayout content = new VerticalLayout();
		content.setMargin(new MarginInfo(false, false, false, false));
//		content.setWidth("100%");
		content.addComponent(new ParametersFormAceView(currsession));

		setContent(content);
		
//		addComponent(new ParametersFormAceView(currsession));

	}
	
	
}
