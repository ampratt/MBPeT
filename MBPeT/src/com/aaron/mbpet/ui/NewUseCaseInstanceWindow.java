package com.aaron.mbpet.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aaron.mbpet.views.MainView;
import com.vaadin.data.Item;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

// Define a sub-window by inheritance
public class NewUseCaseInstanceWindow extends Window {
	/**
	 * 
	 */
	private static final long serialVersionUID = 898541303993774111L;

	public NewUseCaseInstanceWindow(Tree tree) {
        super("Create new Instance of this Test Case"); // Set window caption
        center();
        setResizable(false);
        setClosable(false);
        setModal(true);

        setContent(buildWindowContent(tree, "New Instance"));
	}
	
	
	public NewUseCaseInstanceWindow(Tree tree, String parentCase) {
        super("Create new Instance of " + stripExcess(parentCase)); // Set window caption
        parentCase = stripExcess(parentCase);
        center();
        setResizable(false);
        setClosable(true);
        setModal(true);

        setContent(buildWindowContent(tree, parentCase));
	}
	

		private Component buildWindowContent(Tree tree, String parentCase) {
        	// Some basic content for the window
            VerticalLayout vc = new VerticalLayout();
            vc.addComponent(new Label("Fill in details for this use case of " + parentCase));
            vc.setMargin(true);
            setContent(vc);
          
            // form
            vc.addComponent(buildCreationForm(tree, parentCase));      
            
            return vc;
        }

		private Component buildCreationForm(final Tree tree, final String parentCase) {
			FormLayout form = new FormLayout();
//		        form.addStyleName("outlined");
	        form.setSizeFull();
	        form.setSpacing(true);
	        form.setMargin(new MarginInfo(true, true, false, true));
	 
	        // textfield for name
	        final TextField name = new TextField("Instance Name");
	        name.setWidth(100.0f, Unit.PERCENTAGE);
	        name.setImmediate(true);
	        name.focus();
	        name.setValidationVisible(false);
	        name.setRequired(true);
	        name.setRequiredError("Please give a name for this instance");
//	        name.addValidator(new NullValidator("Cannot be empty", false));
	        
	        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	        Date date = new Date();
	        name.setInputPrompt(dateFormat.format(date));
	        form.addComponent(name);
	        

	        
            // confirm creation to tree
            Button submit = new Button("Create");
            submit.addStyleName("primary");
            submit.setClickShortcut(KeyCode.ENTER);
            submit.addClickListener(new ClickListener() {
                public void buttonClick(ClickEvent event) {
                	name.setValidationVisible(true);                		
                	if (name.getValue() == null
                			|| name.getValue().isEmpty()) {
                	} else {
//	                	Tree tree = MBPeTMenu.tree;
	
	                	// get parent from tree
	                	System.out.println(tree.getItem(parentCase));
	                	
	                    // Create new item
	                    final Object[] itemId = new Object[] {name.getValue()};
	                    String instance = (String) itemId[0];
	                    tree.addItem(instance);
	                    tree.setChildrenAllowed(instance, false);
	//	                    final Object itemId = tree.addItem();
	//	                    tree.setParent(itemId, target);
	
	                    // Set the name for this item (we use it as item caption)
	                    final Item item = tree.getItem(itemId);
	//	                    final Property name = item
	//	                            .getItemProperty(ExampleUtil.hw_PROPERTY_NAME);
	//	                    name.setValue("New Item");
	
	                    // Allow children for the target item, and expand it
	                    tree.setChildrenAllowed(parentCase, true);
	                    tree.setParent(instance, parentCase);
	                    tree.expandItem(parentCase);
	    	            
	    	            // select newly created item and navigate to it
	                    tree.select(instance);
						getUI()
			         		.getNavigator()
			         			.navigateTo(MainView.NAME + "/" + 
			     							parentCase + "/" + instance);
	                    close(); // Close the sub-window
                	}
                }

            });
            
            
            // cancel changes and close window
            Button cancel = new Button("Cancel");
//	            addStyleName("danger");
            cancel.addClickListener(new ClickListener() {
                public void buttonClick(ClickEvent event) {
                    close(); // Close the sub-window
                }

            });

            HorizontalLayout buttons = new HorizontalLayout();
            buttons.setWidth("100%");
            buttons.setMargin(new MarginInfo(true, false, false, false));
            buttons.addComponent(submit);
            buttons.addComponent(cancel);
            buttons.setComponentAlignment(submit, Alignment.MIDDLE_LEFT);
            buttons.setComponentAlignment(cancel, Alignment.MIDDLE_RIGHT);
            
            form.addComponent(buttons);
            
//		        final TextField child2 = new TextField("Child 2", "");
//		        child2.setWidth(100.0f, Unit.PERCENTAGE);
//		        form.addComponent(child2);
//		        form.addComponent(new CheckBox("Child 3"));
//		        form.addComponent(new Button("Child 4"));
            
			return form;
		}

		
		
        private static String stripExcess(String input) {
        	String formatted = input;
        	if (input.contains("/")) {
        		formatted = input.substring(0, input.indexOf("/"));        		
        	}
        	System.out.println("FORMATTED: " + formatted);
        	
        	return formatted;
		}
	        
        

}