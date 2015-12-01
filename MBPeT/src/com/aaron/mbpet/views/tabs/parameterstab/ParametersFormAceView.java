package com.aaron.mbpet.views.tabs.parameterstab;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.aceeditor.AceEditor;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.DbUtils;
import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.domain.TRT;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.services.ParametersUtils;
import com.aaron.mbpet.services.DemoDataGenerator.SaveObject2Database;
import com.aaron.mbpet.ui.ConfirmDeleteModelWindow;
import com.aaron.mbpet.ui.ConfirmDeleteTRTWindow;
import com.aaron.mbpet.views.MainView;
import com.aaron.mbpet.views.models.ModelEditor;
import com.aaron.mbpet.views.parameters.ParametersAceEditorLayoutWITHOUTFORM;
import com.aaron.mbpet.views.parameters.ParametersEditor;
import com.aaron.mbpet.views.parameters.ParametersForm;
import com.aaron.mbpet.views.parameters.TRTEditor;
import com.aaron.mbpet.views.parameters.TRTForm;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
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
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Grid.SingleSelectionModel;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
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

public class ParametersFormAceView extends HorizontalSplitPanel implements Component, Button.ClickListener {

	private JPAContainer<Parameters> parameterscontainer;
	private JPAContainer<TRT> trtcontainer;
	private TestSession currsession;
	private Parameters currentparams;
	private BeanItem<Parameters> beanItem;
	private TRT selectedTRT;

	FieldGroup binder = new FieldGroup();
	private ParametersForm parametersForm;
	private TRTForm TRTForm;
	
	public Grid grid;
//	private Table sessionsTable;
	private Button saveButton;
	private Button enableAceButton;
	private Button newTRTButton;
	private Button editTRTButton;
	private Button deleteTRTButton;
	
    // Ace Editor elements
	AceEditor editor;	// = new AceEditor();
	private Table trtTable;
	public static ParametersAceEditorLayout editorLayout;
	
	public ParametersFormAceView(TestSession currsession){
		setSizeFull();
		setSplitPosition(45, Unit.PERCENTAGE);
//		setSpacing(true);
//		setMargin(true);
		
		
		this.currsession = currsession;
		this.parameterscontainer = ((MbpetUI) UI.getCurrent()).getParameterscontainer();
		this.trtcontainer = ((MbpetUI) UI.getCurrent()).getTrtcontainer();
		this.currentparams = parameterscontainer.getItem(currsession.getParameters().getId()).getEntity();
		this.editor = new AceEditor();
		
		setFirstComponent(buildLeftSide());
		setSecondComponent(buildRightSide());
	
		
	}
	
	private Component buildRightSide() {
		VerticalLayout rightlayout = new VerticalLayout();
		rightlayout.setSizeFull();
		
		editorLayout = new ParametersAceEditorLayout(editor, "python", beanItem, this);
		editorLayout.toggleEditorFields(true);
		editorLayout.setWidth("97%");
		
		rightlayout.addComponent(editorLayout);
		rightlayout.setComponentAlignment(editorLayout, Alignment.TOP_CENTER);
		
		return rightlayout;	// editorLayout;
	}
	

	private Component buildLeftSide() {
		VerticalLayout layout = new VerticalLayout();
		
		// table
//		layout.addComponent(buildParametersTable());
//		VerticalLayout tablelayout = new VerticalLayout();
//		tablelayout.setHeight("20%");
//		tablelayout.addComponent(TablePersonDisplay());
		
//		currentparams = new Parameters();
//		currentparams.setIp("blank.com");
//		currentparams.setTest_duration(10);
//		currentparams.setTarget_response_times(new Address());
		
		// header
        HorizontalLayout header = new HorizontalLayout();
        header.setWidth("97%");
        header.setSpacing(true);
//        header.setMargin(new MarginInfo(true, false, true, false));
//        header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        
        Label section = new Label("Edit Parameters");
        section.addStyleName("h3");
//        section.addStyleName("colored");
        
		saveButton = new Button("Save", this);
		saveButton.addStyleName("tiny");
		saveButton.addStyleName("primary");
		saveButton.setIcon(FontAwesome.SAVE);
//		saveButton.setEnabled(false);
		
		enableAceButton = new Button("Code Editor");
		enableAceButton.addStyleName("tiny");
		enableAceButton.setIcon(FontAwesome.PENCIL);
//		saveButton.setEnabled(false);
		enableAceButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
//				editorLayout.toggleEditorFields(true);
			}
		});
		
		header.addComponents(section, saveButton);		//enableAceButton
		header.setComponentAlignment(section, Alignment.MIDDLE_LEFT);
		header.setComponentAlignment(saveButton, Alignment.MIDDLE_RIGHT);
//		header.setComponentAlignment(enableAceButton, Alignment.MIDDLE_RIGHT);
		header.setExpandRatio(section, 1);
		
		layout.addComponent(header);
		
		
		// form
		parametersForm = new ParametersForm();
		parametersForm.addStyleName("light");
		parametersForm.setWidth("97%");
		layout.addComponent(parametersForm);
		
		layout.addComponent(buildGridButtons());
		layout.addComponent(buildTRTTable());

//		TRTForm = new TRTForm();
//		layout.addComponent(TRTForm);
		
//		TRT trt1 = new TRT();
//			trt1.setAction("search_on_google");
//			trt1.setAverage(0.5);
//			trt1.setMax(1);
//		List<TRT> responseTimes = new ArrayList<TRT>();
//		responseTimes.add(trt1);
//		currentparams.setTarget_response_times(responseTimes);
		
		binder = new FieldGroup();
		bindFormtoBean(currentparams);
//		beanItem = new BeanItem<Parameters>(currentparams);		// takes item as argument
////		beanItem.addNestedProperty("address.street");	// Address info is not person but address to which person is linked
////		beanItem.addNestedProperty("target_response_times");
//		
//		binder.setItemDataSource(beanItem); 	// link the data model to binder
//		
//		binder.bindMemberFields(parametersForm);	// link the layout to binder	
////		binder.bindMemberFields(TRTForm);	// link to layout	
//		
//		for (Object propertyId : binder.getBoundPropertyIds()) {
//			if ("dstat_mode".equals(propertyId)) {
//				ComboBox combo = (ComboBox) binder.getField(propertyId);
//				System.out.println("property was dstat");
//				if (combo.getValue() == null) {
//					combo.select("None");
//					System.out.println("attempted to set null value to None");
//				}
//			}
//		}
		
//		binder = new FieldGroup();
//		beanItem = new BeanItem<Parameters>(currentparams);		// takes item as argument
////		item.addNestedProperty("address.street");	// Address info is not person but address to which person is linked
////		item.addNestedProperty("address.zip");
////		item.addNestedProperty("address.city");
//		
//		binder.setItemDataSource(beanItem); 	// link the data model to binder
//		binder.bindMemberFields(form);	// link the layout to binder		
////		form.setEnabled(false);

		
		return layout;
	}
	

	public void bindFormtoBean(Parameters currparams) {
		this.currentparams = currparams;
		beanItem = new BeanItem<Parameters>(currentparams);		// takes item as argument
//		beanItem.addNestedProperty("address.street");	// Address info is not person but address to which person is linked
//		beanItem.addNestedProperty("target_response_times");
		
		binder.setItemDataSource(beanItem); 	// link the data model to binder
		
		binder.bindMemberFields(parametersForm);	// link the layout to binder	
//		binder.bindMemberFields(TRTForm);	// link to layout	
		
		for (Object propertyId : binder.getBoundPropertyIds()) {
			if ("dstat_mode".equals(propertyId)) {
				ComboBox combo = (ComboBox) binder.getField(propertyId);
				System.out.println("property was dstat");
				if (combo.getValue() == null) {
					combo.select("None");
					System.out.println("attempted to set null value to None");
				}
			}
		}
		
	}

	private Component buildGridButtons() {
		// buttons for TRT grid
		HorizontalLayout h = new HorizontalLayout();
		h.setWidth("97%");
		h.setSpacing(true);
		
		newTRTButton = new Button("", this);
		newTRTButton.setIcon(FontAwesome.PLUS);
		newTRTButton.addStyleName("borderless-colored");
		newTRTButton.addStyleName("small");
		newTRTButton.addStyleName("icon-only");
		newTRTButton.setDescription("Create new action");
		newTRTButton.setEnabled(true);

		editTRTButton = new Button("", this);
		editTRTButton.setIcon(FontAwesome.PENCIL);
		editTRTButton.addStyleName("borderless-colored");
		editTRTButton.addStyleName("small");
		editTRTButton.addStyleName("icon-only");
		editTRTButton.setDescription("Edit action");
		editTRTButton.setEnabled(false);

		deleteTRTButton = new Button("", this);
		deleteTRTButton.setIcon(FontAwesome.TRASH_O);
		deleteTRTButton.addStyleName("borderless-colored");
		deleteTRTButton.addStyleName("small");
		deleteTRTButton.addStyleName("icon-only");
		deleteTRTButton.setDescription("Delete action");
		deleteTRTButton.setEnabled(false);
		
		Label label = new Label("<b>Target Response Times:</b>", ContentMode.HTML);
		label.addStyleName("small");
		label.setWidth(15, Unit.EM);
		
		h.addComponents(label, newTRTButton, editTRTButton, deleteTRTButton);
	    h.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
	    h.setComponentAlignment(newTRTButton, Alignment.MIDDLE_RIGHT);
//	    h.setComponentAlignment(editTRTButton, Alignment.MIDDLE_LEFT);
//	    h.setComponentAlignment(deleteTRTButton, Alignment.MIDDLE_LEFT);
		h.setExpandRatio(label, 1);
//	    h.setExpandRatio(deleteTRTButton, 1);
		    
		return h;
	}


	private Component buildTRTTable() {
		filterTRTByParameters();
		trtTable = new Table();
		trtTable.setContainerDataSource(trtcontainer);
		trtTable.setWidth("97%");
		trtTable.setPageLength(5);
//		modelsTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
//		modelsTable.addStyleName(ValoTheme.TABLE_NO_HEADER);
		trtTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		trtTable.addStyleName(ValoTheme.TABLE_SMALL);
//		modelsTable.addStyleName("background-white");
		
		trtTable.setSelectable(true);
        trtTable.setImmediate(true);
        
        trtTable.setVisibleColumns("action", "average", "max");
        trtTable.setColumnHeaders(new String[] {"Action", "Average", "Max"});
        trtTable.setColumnExpandRatio("action", 3);
        trtTable.setColumnExpandRatio("average", 1);
        trtTable.setColumnExpandRatio("max", 1);
//        trtTable.setColumnWidth("max", 20);
 
        //handle selections
        trtTable.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (trtTable.getValue() != null) {
					editTRTButton.setEnabled(true);
					deleteTRTButton.setEnabled(true);
					
				    // Get selection from the selection model
					selectedTRT = trtcontainer.getItem(event.getProperty().getValue()).getEntity();
				    System.out.println("SELECTED: " + selectedTRT.getAction());
//				    Notification.show("Selected " + selectedTRT.getAction() );
				    
				} else {
					editTRTButton.setEnabled(false);
					deleteTRTButton.setEnabled(false);	
				}
			}
		});
        
//		// Create the Grid
//		grid = new Grid(trtcontainer);
//		grid.setSelectionMode(SelectionMode.SINGLE);
//		grid.setWidth("95%");
//		grid.setHeightByRows(4);
//		grid.addStyleName("tiny");
//		// Define the columns
//		grid.removeColumn("id");
//		grid.removeColumn("parentparameter");
//		grid.setColumnOrder("action", "average", "max");
////		grid.addColumn("Action", String.class);
//
//        // set selection model
//        SingleSelectionModel selection =
//            (SingleSelectionModel) grid.getSelectionModel();
//        
//		// Handle selection changes
//		grid.addSelectionListener(new SelectionListener() {
//			@Override
//			public void select(SelectionEvent event) {
//				editTRTButton.setEnabled(true);
//				deleteTRTButton.setEnabled(true);
//				
//			    // Get selection from the selection model
//			    Object selected = ((SingleSelectionModel)
//			        grid.getSelectionModel()).getSelectedRow();
//			    System.out.println("SELECTED: " + selected);
//			    
//			    // Get selection from the selection model
////			    Object selected = event.getSelected();		//((SingleSelectionModel) grid.getSelectionModel()).getSelectedRow();
//			    
//			    if (selected != null) {
//		        	selectedTRT = trtcontainer.getItem(selected).getEntity();
//			        Notification.show("Selected " + selectedTRT.getAction() );
////			            grid.getContainerDataSource().getItem(selected).getItemProperty("action")
//			    } else {
//					editTRTButton.setEnabled(false);
//					deleteTRTButton.setEnabled(false);			    	
//			    }
//				
//			}
//		});

		return trtTable;
	}


//	private Component buildParametersTable() {
//	    // Table
//		sessionsTable = new Table();
//		setFilterBySession();
//		sessionsTable.setContainerDataSource(parameterscontainer);	//(userSessionsContainer);
//		sessionsTable.setWidth("100%");
//		sessionsTable.setPageLength(2);	//setHeight("150px");
//		sessionsTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
////		sessionsTable.addStyleName(ValoTheme.TABLE_NO_HEADER);
//		sessionsTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
//		sessionsTable.addStyleName(ValoTheme.TABLE_SMALL);
////		sessionsTable.setSizeFull();
//		
//		sessionsTable.setSelectable(true);
//        sessionsTable.setImmediate(true);
//        sessionsTable.setVisibleColumns("id", "ip", "test_duration","monitoring_interval", "mean_user_think_time", "standard_deviation");
//        sessionsTable.setBuffered(true);
////        sessionsTable.setColumnHeaders(new String[] {"Model"});
////        modelsTable.setColumnExpandRatio("title", 1);
//		
//		// add generated column
//        sessionsTable.addGeneratedColumn("Owner Session", new ColumnGenerator() {
//			
//			@Override
//			public Object generateCell(Table source, Object itemId, Object columnId) {
//				// Get the value in the first column
////                int modelId = (Integer) source
////                    .getContainerProperty(itemId, "id").getValue();
//
//                // get title of parent session
//                TestSession session = (TestSession) source.getContainerProperty(itemId, "ownersession").getValue();
//                Label label = new Label(session.getTitle());
//                
//				return label;
//			}
//		});
//
//		sessionsTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
//		    @Override
//		    public void itemClick(ItemClickEvent event) {
//		    	parametersForm.setEnabled(true);
//		    	
//		        Item item = sessionsTable.getItem(event.getItemId());
//		        currentparams = parameterscontainer.getItem(item.getItemProperty("id").getValue()).getEntity();
//		        beanItem = new BeanItem<Parameters>(currentparams);
//		        
//		        Notification.show("Event Item Id: " + event.getItemId().toString() +
//		        				"\nTable Item: " + item.getItemProperty("id") + item.getItemProperty("ip"), Type.TRAY_NOTIFICATION);
//
//		        binder.setItemDataSource(beanItem);	//(item);
//				binder.bindMemberFields(parametersForm);	// link to layout	
////				binder.bindMemberFields(addressForm);	// link to layout	
////				binder.bindMemberFields(skillsForm);
//		    }
//		});	
//
//		return sessionsTable;
//	}

	

	
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == newTRTButton) {
//	        	TestSession newestsession = parentcase.getSessions().get(parentcase.getSessions().size()-1);	//slist.get(slist.size()-1);
		        UI.getCurrent().addWindow(new TRTEditor(new TRT(), currentparams, trtTable) ); 
//		        		ModelEditor(sessions.getItem(newestsession.getId()).getEntity(),
//		        											parentcase, 
//		        											true));	//testcases.getItem(parent).getEntity()
 
		} else if (event.getButton() == editTRTButton) {
			
	        UI.getCurrent().addWindow(new TRTEditor(selectedTRT.getId(), currentparams, trtTable) ); 

//			Model model = models.getItem(modelsTable.getValue()).getEntity();	//.getBean();
//			TestSession parentsession = sessions.getItem(model.getParentsession().getId()).getEntity();
//	        UI.getCurrent().addWindow(new ModelEditor(
//	        			model.getId(),
//	        			parentsession,	//model.getParentsession(),
//	        			parentcase,
//        				modelsTable)
//	        );
		} else if (event.getButton() == deleteTRTButton) {
//			TRT trt = models.getItem(modelsTable.getValue()).getEntity();	//.getBean();
//	        UI.getCurrent().addWindow(new ConfirmDeleteModelWindow(selectedTRT, 
//	        		"Are you sure you want to delete <b>" + selectedTRT.getAction()) + "</b> ?<br /><br />", true));
			UI.getCurrent().addWindow(
					new ConfirmDeleteTRTWindow(selectedTRT, currentparams, 
											"Are you sure you want to delete Action '<b>" +
											selectedTRT.getAction() + "</b>' ?"));
			
		} else if (event.getButton() == saveButton) {
			parametersForm.enableValidationMessages();
			
			// reset trt list so it's up to date before committing
			filterTRTByParameters();
			Collection<Object> idList = trtcontainer.getItemIds();
			List<TRT> trtList = new ArrayList<TRT>();
			for (Object id : idList){
				trtList.add(trtcontainer.getItem(id).getEntity());
//				currentparams.addTRT(trtcontainer.getItem(id).getEntity());
			}
			currentparams.setTarget_response_times(trtList);
			System.out.println("PARAM TRTs before commit:");
			for (TRT trt : currentparams.getTarget_response_times()){
				System.out.println(trt.getAction());

			}
//			TRTForm.toggleValidationMessages(true);
//			binder.commit();
		    try {
				binder.commit();
				
				// 1 UPDATE container
				parameterscontainer.addEntity(beanItem.getBean());
				this.currentparams = parameterscontainer.getItem(beanItem.getBean().getId()).getEntity();
//				System.out.println("Parameters are now: " + currentparams.getId() 
//									+ " " + currentparams.getSettings_file());
				
				// 2 UPDATE parentcase reference
				currsession.setParameters(parameterscontainer.getItem(currentparams.getId()).getEntity());		
//				System.out.println("Session's Params are now: " + currsession.getParameters().getId() + " " + currsession.getParameters().getSettings_file());
				
				currentparams.setTarget_response_times(trtList);
				System.out.println("PARAM TRTs after commit:");
				for (TRT trt : currentparams.getTarget_response_times()){
					System.out.println(trt.getAction());

				}
				// insert form data into settings file
				String settings = ParametersUtils.insertFormDataToAce(currentparams, editorLayout.getEditorValue());
				currentparams.setSettings_file(settings);
				parameterscontainer.addEntity(currentparams);

				// add settings to editor view
				editorLayout.toggleEditorFields(true);
				editorLayout.setEditorValue(settings);
				
				
		        Notification notification = new Notification("Parameters",Type.TRAY_NOTIFICATION);
		        notification.setDescription("were edited");
		        notification.setStyleName("dark small");	//tray  closable login-help
		        notification.setPosition(Position.BOTTOM_RIGHT);
		        notification.setDelayMsec(500);
		        notification.show(Page.getCurrent());
	        
			} catch (CommitException  | InvalidValueException e) {
				e.printStackTrace();
		        Notification notification = new Notification("Heads Up!");
		        notification.setDescription("Some fields had improper values");
		        notification.setStyleName("failure");	//tray  closable login-help
//		        notification.setPosition(Position.BOTTOM_RIGHT);
//		        notification.setDelayMsec(500);
		        notification.show(Page.getCurrent());
			} 
//		    catch (InvalidValueException e) {
//				e.printStackTrace();
//			}
		    
		//	new ParametersEditor(currentparams, beanItem, currsession, binder);
		//	currentparams = parameterscontainer.getItem(currentparams.getId()).getEntity();
		//			parametersForm.setReadOnly(true);
		//			parametersForm.addStyleName("light");
		//			event.getButton().setCaption("Edit");
		//			event.getButton().removeStyleName("primary");
		}
		
	}
	


	
	
    private void setFilterBySession() {
    	parameterscontainer.removeAllContainerFilters();
//    	Equal ownerfilter = new Equal("parentcase", getTestCaseByTitle());//  ("parentcase", getTestCaseByTitle(), true, false);
    	Equal casefilter = new Equal("ownersession", currsession);//  ("parentcase", getTestCaseByTitle(), true, false);
    	
    	parameterscontainer.addContainerFilter(casefilter);
    }
	
    private void filterTRTByParameters() {
    	trtcontainer.removeAllContainerFilters();
//    	Equal ownerfilter = new Equal("parentcase", getTestCaseByTitle());//  ("parentcase", getTestCaseByTitle(), true, false);
    	Equal paramfilter = new Equal("parentparameter", currentparams);//  ("parentcase", getTestCaseByTitle(), true, false);
    	
    	trtcontainer.addContainerFilter(paramfilter);
    }
	



}
