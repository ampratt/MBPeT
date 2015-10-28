package com.aaron.mbpet.views.tabs.parameterstab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.aceeditor.AceEditor;

import com.aaron.mbpet.domain.DbUtils;
import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.domain.TRT;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.services.DemoDataGenerator.SaveObject2Database;
import com.aaron.mbpet.ui.ConfirmDeleteModelWindow;
import com.aaron.mbpet.views.MBPeTMenu;
import com.aaron.mbpet.views.models.ModelEditor;
import com.aaron.mbpet.views.parameters.ParametersAceEditorLayoutWITHOUTFORM;
import com.aaron.mbpet.views.parameters.ParametersForm;
import com.aaron.mbpet.views.parameters.TRTEditor;
import com.aaron.mbpet.views.parameters.TRTForm;
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

	private JPAContainer<Parameters> parameters;
	private JPAContainer<TRT> trtcontainer;
	private TestSession currsession;
	private Parameters currentparams;
	private BeanItem<Parameters> beanItem;
	private TRT selectedTRT;

	FieldGroup binder = new FieldGroup();
	private ParametersForm parametersForm;
	private TRTForm TRTForm;
	
	public Grid grid;
	private Table sessionsTable;
	private Button saveButton;
	private Button newTRTButton;
	private Button editTRTButton;
	private Button deleteTRTButton;
	
    // Ace Editor elements
	AceEditor editor;	// = new AceEditor();
	public static ParametersAceEditorLayout editorLayout;
	
	public ParametersFormAceView(TestSession currsession){
		setSizeFull();
		setSplitPosition(40, Unit.PERCENTAGE);
//		setSpacing(true);
//		setMargin(true);
		
		
		this.currsession = currsession;
		this.parameters = MBPeTMenu.parameters;
		this.trtcontainer = MBPeTMenu.trtcontainer;
		this.currentparams = parameters.getItem(currsession.getParameters().getId()).getEntity();
		this.editor = new AceEditor();
		
		setFirstComponent(buildLeftSide());
		setSecondComponent(buildRightSide());
	
		
	}
	

	private Component buildLeftSide() {
		VerticalLayout layout = new VerticalLayout();
		
		// table
		layout.addComponent(buildParametersTable());
//		VerticalLayout tablelayout = new VerticalLayout();
//		tablelayout.setHeight("20%");
//		tablelayout.addComponent(TablePersonDisplay());
		
		// form
		layout.addComponent(buildParametersFormView());	//GeneratedLayoutForm()
//		VerticalLayout tablayout = new VerticalLayout();
//		tablayout.setHeight("80%");
//		tablayout.addComponent(TabDisplay());
		
//		addComponent(tablelayout);
//		addComponent(tablayout);
//		setExpandRatio(tablelayout, 1);
//		setExpandRatio(tablayout, 2);	
		
		return layout;
	}
	
	
	private Component buildRightSide() {
		VerticalLayout rightlayout = new VerticalLayout();
		rightlayout.setSizeFull();
		
		editorLayout = new ParametersAceEditorLayout(editor, "python");
		editorLayout.toggleEditorFields(true);
		
		rightlayout.addComponent(editorLayout);
		
		return rightlayout;	// editorLayout;
	}

	
	
	private Component buildParametersFormView() {
		final VerticalLayout layout = new VerticalLayout();
//		layout.setMargin(true);

		
//		layout.addComponent(new Label("<h4>Edit parameters</h4>", ContentMode.HTML));
		
//		currentparams = new Parameters();
//		currentparams.setIp("blank.com");
//		currentparams.setTest_duration(10);
//		currentparams.setTarget_response_times(new Address());
		
		// CREATE FIELDS MANUALLY
		parametersForm = new ParametersForm();
		layout.addComponent(parametersForm);
		
		layout.addComponent(buildGridButtons());
		layout.addComponent(buildResponseGrid());

//		TRTForm = new TRTForm();
//		layout.addComponent(TRTForm);
		
		TRT trt1 = new TRT();
			trt1.setAction("search_on_google");
			trt1.setAverage(0.5);
			trt1.setMax(1);
		List<TRT> responseTimes = new ArrayList<TRT>();
		responseTimes.add(trt1);
		currentparams.setTarget_response_times(responseTimes);
		
		binder = new FieldGroup();
		beanItem = new BeanItem<Parameters>(currentparams);		// takes item as argument
//		beanItem.addNestedProperty("address.street");	// Address info is not person but address to which person is linked
		beanItem.addNestedProperty("target_response_times");
		
		binder.setItemDataSource(beanItem); 	// link the data model to binder
		
		binder.bindMemberFields(parametersForm);	// link the layout to binder	
//		binder.bindMemberFields(TRTForm);	// link to layout	
		
		
//		binder = new FieldGroup();
//		beanItem = new BeanItem<Parameters>(currentparams);		// takes item as argument
////		item.addNestedProperty("address.street");	// Address info is not person but address to which person is linked
////		item.addNestedProperty("address.zip");
////		item.addNestedProperty("address.city");
//		
//		binder.setItemDataSource(beanItem); 	// link the data model to binder
//		binder.bindMemberFields(form);	// link the layout to binder		
////		form.setEnabled(false);
		
		saveButton = new Button("Save");
		saveButton.addStyleName("small");
		saveButton.addStyleName("primary");
//		saveButton.setEnabled(false);
		saveButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					parametersForm.enableValidationMessages();
					TRTForm.toggleValidationMessages(true);
					binder.commit();
				} catch (CommitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		layout.addComponent(saveButton);

		
		return layout;
	}
	

	private Component buildGridButtons() {
		// buttons for TRT grid
		HorizontalLayout h = new HorizontalLayout();
		h.setWidth("100%");
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
	    h.setComponentAlignment(newTRTButton, Alignment.MIDDLE_LEFT);
	    h.setComponentAlignment(editTRTButton, Alignment.MIDDLE_LEFT);
	    h.setComponentAlignment(deleteTRTButton, Alignment.MIDDLE_LEFT);
//		    h.setExpandRatio(label, 2);
	    h.setExpandRatio(deleteTRTButton, 1);
		    
		return h;
	}


	private Component buildResponseGrid() {
		
		// Create the Grid
		grid = new Grid(trtcontainer);
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.setWidth("95%");
		grid.setHeightByRows(4);
		grid.addStyleName("tiny");
		// Define the columns
		grid.removeColumn("id");
		grid.removeColumn("parentparameter");
		grid.setColumnOrder("action", "average", "max");
//		grid.addColumn("Action", String.class);
//		grid.addColumn("Average", double.class);
//		grid.addColumn("Max", int.class);
		
        // set selection model
        SingleSelectionModel selection =
            (SingleSelectionModel) grid.getSelectionModel();
        
		// Handle selection changes
		grid.addSelectionListener(new SelectionListener() {

			@Override
			public void select(SelectionEvent event) {
				editTRTButton.setEnabled(true);
				deleteTRTButton.setEnabled(true);
				
			    // Get selection from the selection model
			    Object selected = ((SingleSelectionModel)
			        grid.getSelectionModel()).getSelectedRow();
			    System.out.println("SELECTED: " + selected);
			    
			    // Get selection from the selection model
//			    Object selected = event.getSelected();		//((SingleSelectionModel) grid.getSelectionModel()).getSelectedRow();
			    
			    if (selected != null) {
		        	selectedTRT = trtcontainer.getItem(selected).getEntity();
			        Notification.show("Selected " + selectedTRT.getAction() );
//			            grid.getContainerDataSource().getItem(selected).getItemProperty("action")
			    } else {
					editTRTButton.setEnabled(false);
					deleteTRTButton.setEnabled(false);			    	
			    }
				
			}
		});

		return grid;
	}


	private Component buildParametersTable() {
	    // Table
		sessionsTable = new Table();
		setFilterBySession();
		sessionsTable.setContainerDataSource(parameters);	//(userSessionsContainer);
		sessionsTable.setWidth("100%");
		sessionsTable.setPageLength(2);	//setHeight("150px");
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
		    	parametersForm.setEnabled(true);
		    	
		        Item item = sessionsTable.getItem(event.getItemId());
		        currentparams = parameters.getItem(item.getItemProperty("id").getValue()).getEntity();
		        beanItem = new BeanItem<Parameters>(currentparams);
		        
		        Notification.show("Event Item Id: " + event.getItemId().toString() +
		        				"\nTable Item: " + item.getItemProperty("id") + item.getItemProperty("ip"), Type.TRAY_NOTIFICATION);

		        binder.setItemDataSource(beanItem);	//(item);
				binder.bindMemberFields(parametersForm);	// link to layout	
//				binder.bindMemberFields(addressForm);	// link to layout	
//				binder.bindMemberFields(skillsForm);
		    }
		});	

		return sessionsTable;
	}

	

	
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == newTRTButton) {
//	        	TestSession newestsession = parentcase.getSessions().get(parentcase.getSessions().size()-1);	//slist.get(slist.size()-1);
		        UI.getCurrent().addWindow(new TRTEditor(currentparams, grid) ); 
//		        		ModelEditor(sessions.getItem(newestsession.getId()).getEntity(),
//		        											parentcase, 
//		        											true));	//testcases.getItem(parent).getEntity()
 
		} else if (event.getButton() == editTRTButton) {
			
	        UI.getCurrent().addWindow(new TRTEditor(selectedTRT.getId(), currentparams, grid) ); 

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

		} else if (event.getButton() == saveButton) {
			
		}
		
	}
	


	
	
    private void setFilterBySession() {
    	parameters.removeAllContainerFilters();
//    	Equal ownerfilter = new Equal("parentcase", getTestCaseByTitle());//  ("parentcase", getTestCaseByTitle(), true, false);
    	Equal casefilter = new Equal("ownersession", currsession);//  ("parentcase", getTestCaseByTitle(), true, false);
    	
    	parameters.addContainerFilter(casefilter);
    }
	
    private void filterTRTByParameters() {
    	trtcontainer.removeAllContainerFilters();
//    	Equal ownerfilter = new Equal("parentcase", getTestCaseByTitle());//  ("parentcase", getTestCaseByTitle(), true, false);
    	Equal paramfilter = new Equal("parentparameter", currentparams);//  ("parentcase", getTestCaseByTitle(), true, false);
    	
    	trtcontainer.addContainerFilter(paramfilter);
    }
	
    private void updateFilters() {
//    	parameters.removeAllContainerFilters();
//    	
//    	setFilterByTestCase();
////    	SimpleStringFilter filter = new SimpleStringFilter("title", modelsFilter, true, false);
//    	parameters.addContainerFilter(filter);
    }



}
