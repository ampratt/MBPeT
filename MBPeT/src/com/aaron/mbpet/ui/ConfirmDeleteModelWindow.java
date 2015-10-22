package com.aaron.mbpet.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.views.MBPeTMenu;
import com.aaron.mbpet.views.MainView;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Item;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

// Define a sub-window by inheritance
public class ConfirmDeleteModelWindow extends Window {
	private static final long serialVersionUID = 5408254248079275265L;

	Model model;
	JPAContainer<TestCase> testcases;
	JPAContainer<TestSession> sessions;
	JPAContainer<Model> models;
	
	boolean navtohomepage = false;
	
	public ConfirmDeleteModelWindow(Model model, String message, boolean navtohomepage) {
        super("Heads Up!");
        center();
        setResizable(false);
        setClosable(false);
        setModal(true);
        
        this.navtohomepage = navtohomepage;

        this.testcases = MBPeTMenu.getTestcases();
        this.sessions = MBPeTMenu.getTestsessions();
        this.models = MBPeTMenu.models;
        this.model = model;

        setContent(buildWindowContent(message));
	}
	
        private Component buildWindowContent(String message) {
        	// Some basic content for the window
            VerticalLayout vc = new VerticalLayout();
            vc.addComponent(new Label(message, ContentMode.HTML));
            vc.setMargin(true);
            vc.setSpacing(true);
//            setContent(vc);
          

            
            // confirm deletion from menu
            Button delete = new Button("Delete");
            delete.addStyleName("danger");
//            submit.setClickShortcut(KeyCode.ENTER);
            delete.addClickListener(new ClickListener() {
				private static final long serialVersionUID = -7778774800816407833L;

				public void buttonClick(ClickEvent event) {

	        		// for notification
	        		String deleteditem = model.getTitle();
	        		
	                                            
	                // 1. remove child from Case and Session's list of Models
	        		TestCase parentcase = testcases.getItem(model.getParentsut().getId()).getEntity();
	        		TestSession parentsession = sessions.getItem(model.getParentsession().getId()).getEntity();

	                parentcase.removeModel(model);
	                parentsession.removeModel(model);
	                
	                // 3. delete model from container
	                models.removeItem(model.getId());
	                
//	                menutree.select(parentid);
	                //navigate to parent
	                if (navtohomepage == true) {
	                	getUI().getNavigator().navigateTo(MainView.NAME + "/" + parentcase.getTitle() + "-sut=" + parentcase.getId());	                	
	                }
	                confirmNotification(deleteditem);
		        	
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
        
    	
		private void confirmNotification(String deletedItem) {
	        // welcome notification
	        Notification notification = new Notification(deletedItem, Type.TRAY_NOTIFICATION);
	        notification
	        		.setDescription("<span>was deleted.</span>");
	        notification.setHtmlContentAllowed(true);
	        notification.setStyleName("dark small");	//tray  closable login-help
	        notification.setPosition(Position.BOTTOM_RIGHT);
	        notification.setDelayMsec(5000);
	        notification.show(Page.getCurrent());
		}

}
