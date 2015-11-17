package com.aaron.mbpet;

import java.io.FileNotFoundException;
import java.util.logging.Level;

import javax.servlet.annotation.WebServlet;

import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.services.DemoDataGenerator;
import com.aaron.mbpet.services.PushLabelUpdater;
import com.aaron.mbpet.services.UDPServer;
import com.aaron.mbpet.views.LoginView;
import com.aaron.mbpet.views.MainView;
import com.aaron.mbpet.views.RegistrationView;
import com.aaron.mbpet.views.tabs.MonitoringTab;
import com.aaron.mbpet.views.tabs.TabLayout;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ClientConnector;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.ConnectorTracker;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

@JavaScript({//"http://cdn.alloyui.com/3.0.1/aui/aui-min.js",
//	"js/alloy-ui-master/.*",
	"js/alloy-ui-master/cdn.alloyui.com_3.0.1_aui_aui-min.js",
//	"js/cdn.alloyui.com_2.5.0_aui_aui-min.js"
//	"js/aui-diagram-builder/js/aui-diagram-builder.js",
//	"js/aui-diagram-builder/js/aui-diagram-builder-connector.js",
//	"js/aui-diagram-builder/js/aui-diagram-node.js",
//	"js/aui-diagram-builder/js/aui-diagram-node-condition.js",
//	"js/aui-diagram-builder/js/aui-diagram-node-end.js",
//	"js/aui-diagram-builder/js/aui-diagram-node-fork.js",
//	"js/aui-diagram-builder/js/aui-diagram-node-join.js",
//	"js/aui-diagram-builder/js/aui-diagram-node-manager-base.js",
//	"js/aui-diagram-builder/js/aui-diagram-node-start.js",
//	"js/aui-diagram-builder/js/aui-diagram-node-state.js",
//	"js/aui-diagram-builder/js/aui-diagram-node-task.js",
	})
@StyleSheet("http://cdn.alloyui.com/3.0.1/aui-css/css/bootstrap.min.css")
@SuppressWarnings("serial")
@Theme("mbpet")
@Push	//(PushMode.MANUAL)
public class MbpetUI extends UI implements PushLabelUpdater {

	public static final String PERSISTENCE_UNIT = "mbpet";
	MonitoringTab montab = TabLayout.getMonitoringTab();
	
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = true, ui = MbpetUI.class, widgetset = "com.aaron.mbpet.widgetset.MbpetWidgetset")
	public static class Servlet extends VaadinServlet {
	}

//    static {
//        DemoDataGenerator.create();
//    }
    
	@Override
    protected void init(VaadinRequest request) {
        addDetachListener(new DetachListener() { 

			@Override
            public void detach(DetachEvent event) {
                releaseResources();
            } 
        });
        
        getPage().setTitle("MBPeT");
        
        //testing dummy data
        try {
			DemoDataGenerator.create();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
		//
        // Create a new instance of the navigator. The navigator will attach
        // itself automatically to this view.
        //
        new Navigator(this, this);	//navigator = 
        
        // Create and register the views
        getNavigator().addView("", new LoginView());
        getNavigator().addView(MainView.NAME, new MainView());
        getNavigator().addView(RegistrationView.NAME, new RegistrationView());
//        navigator.navigateTo("");
        
        //
        // view change handler to ensure the user is always redirected
        // to the login view if the user is not logged in.
        //
        getNavigator().addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {

                // Check if a user has logged in
                boolean isLoggedIn = getSession().getAttribute("user") != null;
                boolean isLoginView = event.getNewView() instanceof LoginView;
                boolean isRegistrationView = event.getNewView() instanceof RegistrationView;

                if (!isLoggedIn && !isLoginView && !isRegistrationView) {
                    // Redirect to login view always if a user has not yet
                    // logged in
                	System.out.println("ELSE IF-request view was: " + event.getViewName());
                    getNavigator().navigateTo(LoginView.NAME);
                    return false;

                } else if (isLoggedIn && (isLoginView || isRegistrationView)) {
                    // If someone tries to access to login view while logged in,
                    // then cancel
                    return false;
                }    
//                 else if (isRegistrationView) {
//                	System.out.println("IF-request view was: " + event.getViewName() + " - " + isRegistrationView);
//                	System.out.println("IF-isLoggedIn?: " + isLoggedIn);
//                	System.out.println("IF-!isLoggedIn?: " + !isLoggedIn);
//                	
//                	return true;
//                }

                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {

            } 
        });
        
//        getNavigator().navigateTo("login");
    }
	
    private void releaseResources() {
        // Redirect this page immediately
        getPage().setLocation("/");	//MBPeT
        
        // Close the session
        getSession().close();
        
        // Close the session
//        UI.getCurrent().getSession().close();
//        UI.getCurrent().getSession().getService().closeSession(VaadinSession.getCurrent());
//        UI.getCurrent().close();
//        
//        // Redirect this page immediately
//        UI.getCurrent().getPage().setLocation(
//        			VaadinServlet.getCurrent().getServletContext().getContextPath());	//"/"
    }
	
	  private ConnectorTracker tracker;
	  public static JPAContainer<TestCase> testcases;

	  @Override
	  public ConnectorTracker getConnectorTracker() {
	    if (this.tracker == null) {
	      this.tracker =  new ConnectorTracker(this) {
			private static final long serialVersionUID = -2456104393122612400L;

			@Override
	        public void registerConnector(ClientConnector connector) {
	          try {
	            super.registerConnector(connector);
	          } catch (RuntimeException e) {
	            getLogger().log(Level.SEVERE, "FAILED CONNECTOR: {0}", connector.getClass().getSimpleName());
	            System.out.println("FAILED CONNECTOR: {" + connector.getConnectorId() + "} " 
	            					+ connector.getClass().getSimpleName());
	            throw e;
	          }
	        }

	      };
	    }

	    return tracker;
	  }

	  
		@Override
		public void printnewestMessage(final String string) {
		    access(new Runnable() {
		        @Override
		        public void run() {
		        	//update the UI
		        	montab.addNewMessageComponent(string);
		        	
//		        	mainview.addNewMessageComponent(string);
		        }
		    });
		}
	    
	    
}