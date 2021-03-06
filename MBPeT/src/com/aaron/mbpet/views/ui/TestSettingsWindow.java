package com.aaron.mbpet.views.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.domain.TRT;
import com.aaron.mbpet.services.GenerateComboBoxContainer;
import com.aaron.mbpet.views.sessions.SessionViewer;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class TestSettingsWindow extends Window implements Button.ClickListener{

	private HorizontalLayout mainlayout;
	private ComboBox slaveSelect;
	public TwinColSelect actionSelect;
	private TextField slaveOptions;
	private TextField masterOptions;
	private Button saveButton;
	JPAContainer<TRT> trtcontainer;
	SessionViewer sessionViewer;
	Parameters params;
	int actions[];
	//	List<TRT> trtList;
	
	public TestSettingsWindow(SessionViewer sessionViewer, Parameters params) {
		super("Configure optional test settings"); // Set window caption
		this.sessionViewer = sessionViewer;
		trtcontainer = ((MbpetUI) UI.getCurrent()).getTrtcontainer();
		this.params = params;
//		this.trtList = trtList;
		
//        parentCase = stripExcess(parentCase);
//        center();
//		setPositionY(event.getClientY() - event.getRelativeY() + 40);
        setResizable(false);
        setClosable(true);
        setModal(true);
        setDraggable(true);
        setCloseShortcut(KeyCode.ESCAPE, null);
//        setHeight("325px");
        
        mainlayout = new HorizontalLayout();
        mainlayout.setMargin(true);
        mainlayout.setSpacing(true);
//        vert.setHeight("400px");
//        vert.setSizeFull();
      
//        Label title = new Label("Configure optional test settings");
//        title.addStyleName(ValoTheme.LABEL_H3);
//        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
//        mainlayout.addComponent(title);

//        Label hr = new Label("<hr>",ContentMode.HTML);
//        hr.addStyleName(ValoTheme.LABEL_NO_MARGIN);
//        vert.addComponent(hr);

        //twin col ACTION select for which charts to display
        filterTRTsByParameter(params);
        actionSelect = new TwinColSelect("Choose individual Actions to monitor in real-time");
        actionSelect.setContainerDataSource(trtcontainer);
        actionSelect.setItemCaptionPropertyId("action");
//        actionSelect.setPropertyDataSource(new Object[] {"action"});
//        for (int i = 0; i < 6; i++) {
//            actionSelect.addItem(i);
//            actionSelect.setItemCaption(i, "Option " + i);
//        }
        actionSelect.setRows(6);
        actionSelect.setNullSelectionAllowed(true);
        actionSelect.setMultiSelect(true);
        actionSelect.setImmediate(true);
//        actionSelect.setLeftColumnCaption("Available Actions");
//        actionSelect.setRightColumnCaption("Selected Actions");
        actionSelect.setWidth("375px");
//        actionSelect.setHeight("200px");
//        actionSelect.addStyleName("tiny");
//        actionSelect.addValueChangeListener(new ValueChangeListener() {
//			@Override
//			public void valueChange(ValueChangeEvent event) {
//				Notification.show("Value changed:",
//		                String.valueOf(event.getProperty().getValue()),
//		                Type.TRAY_NOTIFICATION);
//			}
//		});
        
     // Preselect a few items by creating a set
//        List<String> trtlist = new ArrayList<String>();
        if (!(sessionViewer.getActionSelectObject() == null)){
//        	for (int id : sessionViewer.getActionCharts()){
//        		trtlist.add(trtcontainer.getItem(id).getEntity().getAction());
//        	}        	
        	actionSelect.setValue(sessionViewer.getActionSelectObject());	//(new HashSet<String>(trtlist));	//("Venus", "Earth", "Mars")));
        }
        mainlayout.addComponent(actionSelect);
//        vert.addComponent(hr);

        
        // MASTER AND SLAVE OPTIONS
        VerticalLayout rightVert = new VerticalLayout();
        rightVert.setMargin(new MarginInfo(false, false, true, true));
        rightVert.setSpacing(true);
        mainlayout.addComponent(rightVert);
        
        //slave
        HorizontalLayout hl = new HorizontalLayout();
        hl.setMargin(false);
        hl.setSpacing(true);
        hl.setWidth("97%");
        
		slaveOptions = new TextField();
		slaveOptions.addStyleName("tiny");
		slaveOptions.setImmediate(true);
		slaveOptions.setInputPrompt("e.g. -r 20");
		slaveOptions.setCaption("Optional slave arguments");
		if (!sessionViewer.getSlaveOptions().equals("")){
			slaveOptions.setValue(sessionViewer.getSlaveOptions());
		}
		
        slaveSelect = new ComboBox("No. Slaves");	//, modeList);
        slaveSelect.setContainerDataSource(new GenerateComboBoxContainer().generateContainer(100));
        slaveSelect.setWidth(5, Unit.EM);
        slaveSelect.addStyleName("tiny");
        slaveSelect.setFilteringMode(FilteringMode.CONTAINS);
        slaveSelect.setImmediate(true);
        slaveSelect.setNullSelectionAllowed(false);
//        slaveSelect.select(slaveSelect.getItemIds().iterator().next());
        slaveSelect.select(sessionViewer.getNumSlaves());
        slaveSelect.focus();
//        slaveSelect.addValueChangeListener(new ValueChangeListener() {
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
////                Notification.show(slaveSelect.getValue().toString() + " slaves selected");
//                //event.getProperty().getValue().toString()
//            }
//        });
		
		hl.addComponents(slaveOptions, slaveSelect);
		rightVert.addComponent(hl);

		
		//master and save button
        hl = new HorizontalLayout();
        hl.setMargin(new MarginInfo(true, false, false, false));
        hl.setSpacing(true);
        hl.setWidth("97%");
        
		masterOptions = new TextField();
		masterOptions.addStyleName("tiny");
		masterOptions.setImmediate(true);
		masterOptions.setInputPrompt("e.g. -u RESOURCE_UTILIZATION_THRESHOLD");
		masterOptions.setCaption("Optional master arguments");
		if (!sessionViewer.getMasterOptions().equals("")){
			masterOptions.setValue(sessionViewer.getMasterOptions());
		}
		
		saveButton = new Button("Save", this);
		saveButton.addStyleName("tiny");
		saveButton.addStyleName("primary");
		saveButton.setClickShortcut(KeyCode.ENTER, null);
//		saveButton.addStyleName("blue-start");
//		saveButton.addStyleName("friendly");
//		saveButton.setIcon(FontAwesome.PLAY);
//		saveButton.setDescription("Run Test Session");
		
		hl.addComponents(masterOptions, saveButton);
		hl.setComponentAlignment(saveButton, Alignment.BOTTOM_RIGHT);
		hl.setExpandRatio(saveButton, 1);
		rightVert.addComponent(hl);
		
        setContent(mainlayout);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == saveButton){
			sessionViewer.setNumSlaves((Integer)slaveSelect.getValue());
			sessionViewer.setSlaveOptions(slaveOptions.getValue());
			sessionViewer.setMasterOptions(masterOptions.getValue());
			
			sessionViewer.setActionSelectObject(actionSelect.getValue());
			String idString = String.valueOf(actionSelect.getValue());
			idString = idString.substring(1, idString.length()-1);	// now: '245,256,245'
			
		    String[] items = idString.split(",");
			//		    List<String> idList = new ArrayList<String>(Arrays.asList(items));
			//		    System.out.println(idList);
		    List<Integer> intIDList = new ArrayList<Integer>();
		    if (!idString.equals("")){
		    	for(int i = 0 ; i<items.length ; i++) {
		    		String nospace = items[i].replaceAll("\\s","");
		    		intIDList.add(Integer.parseInt(nospace));
		    	}		    	
		    }
		    sessionViewer.setINDActionIDsToMonitor(intIDList);
//			System.out.println("int list is now:" + intIDList);
//		    for(int i : intIDList) {
//				System.out.println("each list entry:" + i);
//		    }
		                 
//		    int[] intArray = new int[items.length];
//		    for(int i = 0; i < items.length; i++) {
//		        intArray[i] = Integer.parseInt(items[i]);
//		    }	
//			List<?> obj = Arrays.asList(actionSelect.getValue());		
//			Set set = new HashSet();	
////			Object act = actionSelect.getValue();
//			List<?> obj2 = Arrays.asList(obj.get(0));
//			List list = new ArrayList<>(Arrays.asList(actionSelect.getValue()));
//			for (Object str : obj2) {
//
//				System.out.println("for-each loop: " + str);
//			}
//			Set mySet2 = new HashSet<>(Arrays.asList(actionSelect.getValue()));
//			for (Object inner : mySet2) {
//				System.out.println("for-each loop: " + inner);
//			}
//			sessionViewer.setActionCharts(actionSelect.getValue());	//List<Object> actionsSelected 

//			Notification.show("Actions selected:", intIDList + " " + intIDList.getClass().getName() + " - " +
//	                String.valueOf(actionSelect.getValue() + " " + actionSelect.getValue().getClass().getName()),
//	                Type.TRAY_NOTIFICATION);
			this.close();
		}
		
	}

	public void filterTRTsByParameter(Parameters p){
		trtcontainer.removeAllContainerFilters();
//    	Equal ownerfilter = new Equal("parentcase", getTestCaseByTitle());//  ("parentcase", getTestCaseByTitle(), true, false);
    	Equal trtfilter = new Equal("parentparameter", p);//  ("parentcase", getTestCaseByTitle(), true, false);
    	
    	trtcontainer.addContainerFilter(trtfilter);
	}
	
}
