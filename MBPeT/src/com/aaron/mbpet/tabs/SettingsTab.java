package com.aaron.mbpet.tabs;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;

//import org.vaadin.aceeditor.AceEditor;
//import org.vaadin.aceeditor.AceMode;
//import org.vaadin.aceeditor.SuggestionExtension;
//import org.vaadin.aceeditor.AceEditor.SelectionChangeEvent;
//import org.vaadin.aceeditor.AceEditor.SelectionChangeListener;
//import org.vaadin.diagrambuilder.DiagramBuilder;
//
//import com.vaadin.tests.themes.valo.mycomponents.MySuggester;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

public class SettingsTab extends VerticalLayout {

//	AceEditor editor = new AceEditor();
	Label editor = new Label();
	
	public SettingsTab() {
		setMargin(true);
		setSpacing(true);
	
	    addComponent(new Label("<h3><i>What kind of settings and UI components are needed here?</i></h3>", ContentMode.HTML));	//layout.
//		addComponent(buildAceEditor());
		addComponent(button());

	    //initDiagram();
	}

	private Component buildAceEditor() {
		// Ace Editor
		editor.setValue("Hello world!\nif:\n\tthen \ndo that\n...");
		editor.setWidth("70%");		
		//editor.setWordWrap(false);
		editor.setReadOnly(false);
//		editor.setMode(AceMode.python);			SET THIS

		//editor.setShowInvisibles(false);
		//editor.setTheme(AceTheme.twilight);	

		// Use worker (if available for the current mode)
		//editor.setUseWorker(true);
//		editor.addTextChangeListener(new TextChangeListener() {
//		    @Override
//		    public void textChange(TextChangeEvent event) {
//		        Notification.show("Text: " + event.getText());
//		    }
//		});
//		
//		editor.addSelectionChangeListener(new SelectionChangeListener() {
//		    @Override
//		    public void selectionChanged(SelectionChangeEvent e) {
//		        int cursor = e.getSelection().getCursorPosition();
//		        //Notification.show("Cursor at: " + cursor);
//		    }
//		});
		
		return(editor);

	}
	private Component button(){
		Button button = new Button("Get Current Code");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				String s = editor.getValue();
				Label label = new Label(s);
				//layout.addComponent(label);
				//testing purposes
				Notification.show(editor.getValue(), Type.WARNING_MESSAGE);
			}
		});
		addComponent(button);

//		new SuggestionExtension(new MySuggester()).extend(editor);		
		
		return button;
	}
	
	
}
