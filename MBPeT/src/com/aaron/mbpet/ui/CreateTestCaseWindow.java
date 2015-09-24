package com.aaron.mbpet.ui;

import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.User;
import com.aaron.mbpet.views.MainView;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
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
public class CreateTestCaseWindow extends Window {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5370960944210111329L;

	private Tree tree;
	private JPAContainer<TestCase> testcases;
	
	public CreateTestCaseWindow(Tree tree, JPAContainer<TestCase> container) {
        super("Create a new Test Case"); // Set window caption
        center();
        setResizable(false);
        setClosable(false);
        setModal(true);

        this.tree = tree;
        this.testcases = container;
        
        setContent(buildWindowContent());
	}
	
        private Component buildWindowContent() {
        	// Some basic content for the window
            VerticalLayout vc = new VerticalLayout();
            setContent(vc);
            vc.addStyleName("subwindow-margin");
            vc.setMargin(true);
            vc.setSpacing(true);

            //label
            vc.addComponent(new Label("Fill in details for this Test Case"));
          
            // form
            vc.addComponent(buildCreationForm());
            
            return vc;
        }

		private Component buildCreationForm() {
			FormLayout form = new FormLayout();
//	        form.addStyleName("outlined");
	        form.setSizeFull();
	        form.setMargin(new MarginInfo(true, true, false, true));
	        form.setSpacing(true);
	 
	        final TextField title = new TextField("Title");
	        title.setWidth(100.0f, Unit.PERCENTAGE);
	        title.addStyleName("hide-required-asterisk");
	        title.setImmediate(true);
	        title.focus();
	        title.setValidationVisible(false);
	        title.setRequired(true);
	        title.setRequiredError("Please give a name for this Test Case");
//	        title.addValidator(new NullValidator("Cannot be empty", false));
	        form.addComponent(title);
	        

	        
            // confirm creation to tree
            Button submit = new Button("Create");
            submit.addStyleName("primary");
            submit.setClickShortcut(KeyCode.ENTER);
            submit.addClickListener(new ClickListener() {
                public void buttonClick(ClickEvent event) {
                	title.setValidationVisible(true);
//                	if (title.getValue() == null
//                			|| title.getValue().isEmpty()) {
//                	} else {
                	if (!(title.getValue() == null
                			|| title.getValue().isEmpty())) {

	                    // Create new item, set as parent, allow children (= leaf node)
	                    final Object[] itemId = new Object[] {title.getValue()};
	                    String treeItem = (String) itemId[0];
//	                    Tree tree = MBPeTMenu.tree;
	                    tree.addItem(treeItem);
	                    tree.setChildrenAllowed(treeItem, true);
	//                    final Object itemId = tree.addItem();
	//                    tree.setParent(itemId, target);
	
	                    // Set the name for this item (we use it as item caption)
	                    final Item item = tree.getItem(itemId);
	//                    final Property name = item
	//                            .getItemProperty(ExampleUtil.hw_PROPERTY_NAME);
	//                    name.setValue("New Item");
	
	                    // Allow children for the target item, and expand it
	//                    tree.setChildrenAllowed(target, true);
	//                    tree.expandItem(target);
	                    
	    	            // select newly created item and navigate to it
	                    tree.select(treeItem);
						getUI()
			         		.getNavigator()
			         			.navigateTo(MainView.NAME + "/" + 
			         					treeItem);
						close(); // Close the sub-window
                	}
                }

            });
            
            
            // cancel changes and close window
            Button cancel = new Button("Cancel");
//            addStyleName("danger");
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
            
//	        final TextField child2 = new TextField("Child 2", "");
//	        child2.setWidth(100.0f, Unit.PERCENTAGE);
//	        form.addComponent(child2);
//	        form.addComponent(new CheckBox("Child 3"));
//	        form.addComponent(new Button("Child 4"));
            
			return form;
		}

		
        
        

}