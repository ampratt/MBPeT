package com.aaron.mbpet.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.aaron.mbpet.views.MainView;
import com.vaadin.data.Item;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.shared.ui.label.ContentMode;
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
public class ConfirmDeleteMenuItemWindow extends Window {
	private static final long serialVersionUID = 5408254248079275265L;

	public ConfirmDeleteMenuItemWindow(Tree tree, Object target, String message) {
        super("Heads Up!");
        center();
        setResizable(false);
        setClosable(false);
        setModal(true);

        setContent(buildWindowContent(tree, target, message));
	}
	
        private Component buildWindowContent(final Tree tree, final Object target, String message) {
        	// Some basic content for the window
            VerticalLayout vc = new VerticalLayout();
            vc.addComponent(new Label(message, ContentMode.HTML));
            vc.setMargin(true);
            vc.setSpacing(true);
            setContent(vc);
          
            
            // confirm deletion from menu
            Button delete = new Button("Delete");
            delete.addStyleName("danger");
//            submit.setClickShortcut(KeyCode.ENTER);
            delete.addClickListener(new ClickListener() {
				private static final long serialVersionUID = -7778774800816407833L;

				public void buttonClick(ClickEvent event) {
//                	Tree tree = MBPeTMenu.tree;
                	
                	// delete any child items from parent
                	if (tree.hasChildren(target)) {
                		List<Object> children = new ArrayList<>(tree.getChildren(target));
//                		Collection children = tree.getChildren(target);
                		for (int i=0; i<children.size(); i++) {	//for (Object child : children) {
                			tree.removeItem(children.get(i));
                		}                    		
                	}
                	
                	// delete the parent item and navigate to landing page
                	tree.removeItem(target);
                	getUI()
    	            	.getNavigator()
    	            		.navigateTo(MainView.NAME + "/" + "landingPage");
                	close(); 
                }

            });
            
            
            // cancel changes and close window
            Button cancel = new Button("Cancel");
//            addStyleName("danger");
            cancel.addClickListener(new ClickListener() {
                /**
				 * 
				 */
				private static final long serialVersionUID = -53528761196259133L;

				public void buttonClick(ClickEvent event) {
                    close(); // Close the sub-window
                }

            });

            HorizontalLayout buttons = new HorizontalLayout();
            buttons.setWidth("100%");
            buttons.addComponent(cancel);
            buttons.addComponent(delete);
            buttons.setComponentAlignment(cancel, Alignment.MIDDLE_LEFT);
            buttons.setComponentAlignment(delete, Alignment.MIDDLE_RIGHT);
            
            vc.addComponent(buttons);     
            
            return vc;
        }     

}
