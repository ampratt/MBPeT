package com.aaron.mbpet.views.parameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aaron.mbpet.data.DemoDataGenerator.SaveObject2Database;
import com.aaron.mbpet.domain.DbUtils;
import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.views.MBPeTMenu;
import com.sun.jndi.cosnaming.IiopUrl.Address;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.ReverseConverter;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.themes.ValoTheme;

public class ParametersTableFormView extends VerticalLayout implements Component {

	FieldGroup binder = new FieldGroup();
	private Table sessionsTable;
	private JPAContainer<Parameters> parameters;
	private Parameters currentparams;
	private ParametersForm form;
	private BeanItem<Parameters> beanItem;
	private TestSession currsession;
	
	
	public ParametersTableFormView(TestSession currsession){
		this.parameters = MBPeTMenu.parameters;
		this.currsession = currsession;
				
		// table
		addComponent(buildParametersTable());
//		VerticalLayout tablelayout = new VerticalLayout();
//		tablelayout.setHeight("20%");
//		tablelayout.addComponent(TablePersonDisplay());
		
		// form
		addComponent(GeneratedLayoutForm());	//(buildParametersFormView());
//		VerticalLayout tablayout = new VerticalLayout();
//		tablayout.setHeight("80%");
//		tablayout.addComponent(TabDisplay());
		
//		addComponent(tablelayout);
//		addComponent(tablayout);
//		setExpandRatio(tablelayout, 1);
//		setExpandRatio(tablayout, 2);	
		
	}
	
	private Component buildParametersTable() {
	    // Table
		sessionsTable = new Table();
		sessionsTable.setContainerDataSource(parameters);	//(userSessionsContainer);
		setFilterByTestCase();
		sessionsTable.setWidth("100%");
		sessionsTable.setHeight("150px");
		sessionsTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
//		sessionsTable.addStyleName(ValoTheme.TABLE_NO_HEADER);
		sessionsTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		sessionsTable.addStyleName(ValoTheme.TABLE_SMALL);
//		sessionsTable.setSizeFull();
		
		sessionsTable.setSelectable(true);
        sessionsTable.setImmediate(true);
        sessionsTable.setVisibleColumns("id", "ip", "test_duration","monitoring_interval", "mean_user_think_time", "standard_deviation");
        sessionsTable.setBuffered(true);
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

		sessionsTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
		    @Override
		    public void itemClick(ItemClickEvent event) {
		    	form.setEnabled(true);
		    	
		        Item item = sessionsTable.getItem(event.getItemId());
		        currentparams = parameters.getItem(item.getItemProperty("id").getValue()).getEntity();
		        beanItem = new BeanItem<Parameters>(currentparams);
		        
		        Notification.show("Event Item Id: " + event.getItemId().toString() +
		        				"\nTable Item: " + item.getItemProperty("id") + item.getItemProperty("ip"), Type.TRAY_NOTIFICATION);

		        binder.setItemDataSource(beanItem);	//(item);
				binder.bindMemberFields(form);	// link to layout	
//				binder.bindMemberFields(addressForm);	// link to layout	
//				binder.bindMemberFields(skillsForm);
		    }
		});	

		return sessionsTable;
	}

	
	
	private Component GeneratedLayoutForm() {
		final VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);
		
		
//		layout.addComponent(new Label("<h4>Add Test Session</h4>", ContentMode.HTML));
	
		// set parent Test Case manually without a field
//		if (editmode == false && (clonemode==false)) {
//			testsession.setParentcase(parentCase);			
//		}
		

		currentparams = parameters.getItem(parameters.getIdByIndex(0)).getEntity();
//		currentparams.setIp("blank.com");
//		currentparams.setTest_duration(10);
			
		// empty bean for testing
//		person = new Person();
//		person.setAddress(new Address());
		
		// CREATE FIELDS MANUALLY
//		form = new ParametersForm();
//		layout.addComponent(form);
		
		
		
		
		binder = new FieldGroup();
		beanItem = new BeanItem<Parameters>(currentparams);	
//		BeanItem<Person> item = new BeanItem<Person>(person);		// takes item as argument
//		item.addNestedProperty("address.street");	// Address info is not person but address to which person is linked
//		item.addNestedProperty("address.zip");
//		item.addNestedProperty("address.city");
		
		binder.setItemDataSource(beanItem); 	// link to data model to binder
		
//		binder.bindMemberFields(form);	// link to layout
	
		// GENERATE FIELDS
		
		for (Object propertyId : beanItem.getItemPropertyIds()) {
			if("ownersession".equals(propertyId)) {
//				field = binder.buildAndBind(currsession.getTitle());
			
			} else if("standard_deviation".equals(propertyId)) {
				TextField field = new TextField("standard_deviation");
//				field.setValue(currentparams.getStandard_deviation().toString());
				field.setConverter(new ReverseConverter(new StringToDoubleConverter()));
				field = (TextField) binder.buildAndBind(propertyId);
				layout.addComponent(field);

			} else if("ramp_list".equals(propertyId)) {
				ArrayList<ArrayList<Integer>> dataListFromDB = 
						(ArrayList<ArrayList<Integer>>) DbUtils.readFromDb((int) beanItem.getItemProperty("id").getValue());
				
				currentparams.setRamp_list(dataListFromDB);
//				field = (TextField) binder.buildAndBind(propertyId);
				TextField field = new TextField("ramp_list");
				field.setValue(dataListFromDB.toString());
				layout.addComponent(field);

			} else if(!"TargetResponseTime".equals(propertyId)) {
//				Map<String, HashMap<String, Double>> responsetimesHashMap = (
//						Map<String, HashMap<String, Double>>) DbUtils.readFromDb((int) beanItem.getItemProperty("id").getValue());
//				
//				currentparams.setTargetResponsTime(responsetimesHashMap);
////				field = (TextField) binder.buildAndBind(propertyId);
//				TextField field = new TextField("TargetResponseTime");
//				field.setValue(responsetimesHashMap);
//				layout.addComponent(field);
				TextField field = (TextField) binder.buildAndBind(propertyId);
				layout.addComponent(field);
			} 
//			else {
//				TextField field = (TextField) binder.buildAndBind(propertyId);
//				layout.addComponent(field);
//			}
		}

		
		// using buildAndBind()
//		titlefield = binder.buildAndBind("title");
//		titlefield.setWidth(18, Unit.EM);
//		titlefield.focus();
//		titlefield.setRequired(true);
////		titlefield.setRequiredError("'Title' cannot be empty.");
////		((TextField) titlefield).setValidationVisible(false);
//		titlefield.addValidator(new BeanValidator(TestSession.class, "title"));
//		((TextField) titlefield).setValidationVisible(true);
////		titlefield.addValidator(new StringLengthValidator("Title must be between 1 and 40 characters", 
////					1, 40, false));
//		((TextField) titlefield).setNullRepresentation("");
////		
//		layout.addComponent(titlefield);
		
		// using bind() to determine what type of field is created yourself...
//		title = new TextField();
//		binder.bind(title, "title");
//		title.setWidth(22, Unit.EM);
//		title.setCaption("Title");
//		title.focus();
//		title.setImmediate(true);
//		title.addValidator(new BeanValidator(TestSession.class, "title"));
////		title.setValidationVisible(false);
//		title.setNullRepresentation("");
//		layout.addComponent(title);
//		
//		binder.setBuffered(true);
//		
//		// button layout
//		HorizontalLayout buttons = new HorizontalLayout();
//		buttons.setWidth("100%");
//		buttons.addStyleName("buttons-margin-top");
//		layout.addComponent(buttons);
//		
//		createButton = new Button("Create", this);
//		if (editmode) createButton.setCaption("Save");
//		createButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
//		createButton.setClickShortcut(KeyCode.ENTER);
//		
//		cancelButton = new Button("Cancel", this);
//		
//		buttons.addComponents(createButton, cancelButton);
//		buttons.setComponentAlignment(createButton, Alignment.MIDDLE_LEFT);
//		buttons.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
		
		return layout;
	}
	
	
	
	private Component buildParametersFormView() {
		final VerticalLayout layout = new VerticalLayout();
//		layout.setMargin(true);
//		setContent(layout);
		
		layout.addComponent(new Label("<h4>Edit parameters</h4>", ContentMode.HTML));
		
		currentparams = new Parameters();
		currentparams.setIp("blank.com");
		currentparams.setTest_duration(10);
			
		// empty bean for testing
//		person = new Person();
//		person.setAddress(new Address());
		
		// CREATE FIELDS MANUALLY
		form = new ParametersForm();
		layout.addComponent(form);
		
		binder = new FieldGroup();
		beanItem = new BeanItem<Parameters>(currentparams);		// takes item as argument
//		item.addNestedProperty("address.street");	// Address info is not person but address to which person is linked
//		item.addNestedProperty("address.zip");
//		item.addNestedProperty("address.city");
		
		binder.setItemDataSource(beanItem); 	// link the data model to binder
		binder.bindMemberFields(form);	// link the layout to binder		
		form.setEnabled(false);
		

		
		layout.addComponent(new Button("save", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					form.enableValidationMessages();
					binder.commit();
				} catch (CommitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}));
		
		return layout;
	}
	

	
//	private Component TabDisplay(){
////		final BasicDetailsForm basicDetailsForm = new BasicDetailsForm();
////		final AddressForm addressForm = new AddressForm();
////		final SkillsForm skillsForm = new SkillsForm();
//		ParametersForm paramForm = new ParametersForm();
//		
//		// tabs
//		tabs = new TabSheet();
//		tabs.addTab(basicDetailsForm, "Basic Details");
//		tabs.addTab(addressForm, "Address info");
//		tabs.addTab(skillsForm, "Skills info");
//
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
//		Skill skill1 = new Skill();
//		skill1.setSkillName("simply awesome");
//		List<Skill> skills = new ArrayList<Skill>();
//		skills.add(skill1);
//		person.setSkills(skills);
//		
////		binder = new FieldGroup();
//		BeanItem<Person> item = new BeanItem<Person>(person);		// takes item as argument
//		item.addNestedProperty("address.street");	// Address info is not person but address to which person is linked
//		item.addNestedProperty("address.zip");
//		item.addNestedProperty("address.city");
//		item.addNestedProperty("skills");
//		
//		binder.setItemDataSource(item); 	// link the data model to binder
//		
//		binder.bindMemberFields(basicDetailsForm);	// link the layout to binder	
//		binder.bindMemberFields(addressForm);	// link to layout	
//		binder.bindMemberFields(skillsForm);	// link to layout	
//		
//		
//		table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
//		    @Override
//		    public void itemClick(ItemClickEvent event) {
//		        Item item = table.getItem(event.getItemId());
//		        binder.setItemDataSource(item);
//				binder.bindMemberFields(basicDetailsForm);	// link to layout	
//				binder.bindMemberFields(addressForm);	// link to layout	
//				binder.bindMemberFields(skillsForm);
//		    }
//		});
//		
//		
//		return tabs;
//	}
	
	
//	private Component TablePersonDisplay() {
////		final VerticalLayout layout = new VerticalLayout();
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
//
//		BeanItemContainer<Person> peopleBeans = 
//			    new BeanItemContainer<Person>(Person.class); 
//			     
//			// Add some beans to it 
//		peopleBeans.addBean(person);
//		peopleBeans.addBean(new Person(123, "first", "last", "email", "title")); 
////			peopleBeans.addBean(new Person("Chickpea",    686.0)); 
////			peopleBeans.addBean(new Person("Lentil",      1477.0)); 
////			peopleBeans.addBean(new Person("Common bean", 129.0)); 
////			peopleBeans.addBean(new Person("Soybean",     1866.0)); 
//		
//		table = new Table();
//		table.setHeight(10, Unit.EM);
//		table.setWidth("100%");
////		table.setSizeFull();
//		table.setSelectable(true);
//        table.setImmediate(true);
//		table.setContainerDataSource(peopleBeans);
//		table.setVisibleColumns("firstname", "lastname", "email", "title");
//		
////		layout.addComponent(table);		
//		
//		return table;
//		
//	}
	
	
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
