package com.aaron.mbpet.components.tabs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.servlet.annotation.WebServlet;

import org.vaadin.aceeditor.AceEditor;

import com.aaron.mbpet.components.aceeditor.ParametersAceEditorLayout;
import com.aaron.mbpet.components.aceeditor.AceEditorLayoutDirectory;
import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.views.MBPeTMenu;
import com.aaron.mbpet.views.parameters.ParametersTableFormView;
import com.aaron.mbpet.views.sessions.SessionViewer;
import com.vaadin.addon.jpacontainer.JPAContainer;
import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceMode;
import org.vaadin.aceeditor.SuggestionExtension;
import org.vaadin.aceeditor.AceEditor.SelectionChangeEvent;
import org.vaadin.aceeditor.AceEditor.SelectionChangeListener;
//import org.vaadin.diagrambuilder.DiagramBuilder;
//import com.vaadin.tests.themes.valo.mycomponents.MySuggester;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.themes.ValoTheme;

public class ParametersTab extends VerticalLayout {

	AceEditor editor;
//	private TestSession currsession;
	
	public ParametersTab() {		//TestSession currsession
		setSizeFull();
		setMargin(true);
		setSpacing(true);
	
//		this.currsession = SessionViewer.currsession;
		
//	    addComponent(new Label("<h3><i>Edit test parameters</i></h3>", ContentMode.HTML));	//layout.
		
		editor = new AceEditor();
		addComponent(new ParametersAceEditorLayout(editor, "python"));		//currsession
//		editor.setValue(newFieldValue)
		
//	    addComponent(buildParametersTable());
//		addComponent(buildParametersFormView());
//	    addComponent(button());

	}
	
	
}
