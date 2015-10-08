package com.aaron.mbpet.components.tabs;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.views.MBPeTMenu;
import com.aaron.mbpet.views.sessions.ParametersTableFormView;
import com.vaadin.addon.jpacontainer.JPAContainer;
//import org.vaadin.aceeditor.AceEditor;
//import org.vaadin.aceeditor.AceMode;
//import org.vaadin.aceeditor.SuggestionExtension;
//import org.vaadin.aceeditor.AceEditor.SelectionChangeEvent;
//import org.vaadin.aceeditor.AceEditor.SelectionChangeListener;
//import org.vaadin.diagrambuilder.DiagramBuilder;
//
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

public class SettingsTab extends VerticalLayout {

//	AceEditor editor = new AceEditor();
	Label editor = new Label();
	private Table sessionsTable;
	private JPAContainer<Parameters> parameters;
	private TestSession currsession;
	
	public SettingsTab(TestSession currsession) {
		setMargin(true);
		setSpacing(true);
	
		this.parameters = MBPeTMenu.parameters;
		this.currsession = currsession;
		
	    addComponent(new Label("<h3><i>edit test parameters</i></h3>", ContentMode.HTML));	//layout.
		
		ParametersTableFormView paramTableFormView = new ParametersTableFormView(currsession);
		addComponent(paramTableFormView);
		
//	    addComponent(buildParametersTable());
//		addComponent(buildParametersFormView());
//	    addComponent(button());

	}

	
	private Component buildParametersTable() {
	    // Table
		sessionsTable = new Table();
		sessionsTable.setContainerDataSource(parameters);	//(userSessionsContainer);
		setFilterByTestCase();
		sessionsTable.setWidth("100%");
		sessionsTable.setHeight("200px");
		sessionsTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
//		sessionsTable.addStyleName(ValoTheme.TABLE_NO_HEADER);
		sessionsTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		sessionsTable.addStyleName(ValoTheme.TABLE_SMALL);
//		sessionsTable.setSizeFull();
		
		sessionsTable.setSelectable(true);
        sessionsTable.setImmediate(true);
        sessionsTable.setVisibleColumns("id", "ip", "test_duration", "TargetResponseTime", "monitoring_interval", "mean_user_think_time", "standard_deviation");
//        sessionsTable.setColumnHeaders(new String[] {"Model"});
//        modelsTable.setColumnExpandRatio("title", 1);
		
		// add generated column
        sessionsTable.addGeneratedColumn("Owner Session", new ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// Get the value in the first column
//                int modelId = (Integer) source
//                    .getContainerProperty(itemId, "id").getValue();

                // get title of parent session
                TestSession session = (TestSession) source.getContainerProperty(itemId, "ownersession").getValue();
                Label label = new Label(session.getTitle());
                
				return label;
			}
		});


		return sessionsTable;
	}


//	private Component buildParametersFormView() {
//		final VerticalLayout layout = new VerticalLayout();
////		layout.setMargin(true);
////		setContent(layout);
//		
//		layout.addComponent(new Label("<h2>Edit parameters</h2>", 
//				ContentMode.HTML));
//		
//		Person person = new Person();
//		person.setFirstname("John");
//		person.setLastname("Smith");
//		person.setEmail("john.smith@eample.com");
//		
//		Address address = new Address();
//		address.setStreet("Main street");
//		address.setCity("Turku");
//		address.setZip("123456");
//		person.setAddress(address);
//		
//		// empty bean for testing
////		person = new Person();
////		person.setAddress(new Address());
//		
//		// CREATE FIELDS MANUALLY
//		final PersonForm form = new PersonForm();
//		layout.addComponent(form);
//		
//		final FieldGroup binder = new FieldGroup();
//		BeanItem<Person> item = new BeanItem<Person>(person);		// takes item as argument
//		item.addNestedProperty("address.street");	// Address info is not person but address to which person is linked
//		item.addNestedProperty("address.zip");
//		item.addNestedProperty("address.city");
//		
//		binder.setItemDataSource(item); 	// link the data model to binder
//		
//		binder.bindMemberFields(form);	// link the layout to binder		
//		
//		layout.addComponent(new Button("save", new Button.ClickListener() {
//			@Override
//			public void buttonClick(ClickEvent event) {
//				try {
//					form.enableValidationMessages();
//					binder.commit();
//				} catch (CommitException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			}
//		}));
//		
//		return layout;
//	}
	
	
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
	
    private void setFilterByTestCase() {
    	parameters.removeAllContainerFilters();
//    	Equal ownerfilter = new Equal("parentcase", getTestCaseByTitle());//  ("parentcase", getTestCaseByTitle(), true, false);
    	Equal casefilter = new Equal("ownersession", currsession);//  ("parentcase", getTestCaseByTitle(), true, false);
    	
    	parameters.addContainerFilter(casefilter);
    }
	
	
    private void updateFilters() {
//    	parameters.removeAllContainerFilters();
//    	
//    	setFilterByTestCase();
////    	SimpleStringFilter filter = new SimpleStringFilter("title", modelsFilter, true, false);
//    	parameters.addContainerFilter(filter);
    }
	
	
}
