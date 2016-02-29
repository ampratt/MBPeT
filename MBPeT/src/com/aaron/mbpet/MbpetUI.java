package com.aaron.mbpet;

import java.io.FileNotFoundException;
import java.util.logging.Level;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aaron.mbpet.domain.Adapter;
import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.domain.TRT;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.domain.User;
import com.aaron.mbpet.services.DemoDataGenerator;
import com.aaron.mbpet.services.PushLabelUpdater;
import com.aaron.mbpet.services.PushMasterUpdater;
import com.aaron.mbpet.services.UDPServer;
import com.aaron.mbpet.ui.MasterTerminalWindow;
import com.aaron.mbpet.views.LoginView;
import com.aaron.mbpet.views.MainView;
import com.aaron.mbpet.views.RegistrationView;
import com.aaron.mbpet.views.sessions.SessionViewer;
import com.aaron.mbpet.views.tabs.MonitoringTab;
import com.aaron.mbpet.views.tabs.TabLayout;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Item;
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
public class MbpetUI extends UI implements PushLabelUpdater, PushMasterUpdater	{		//, PushMasterUpdater {

	public static final String PERSISTENCE_UNIT = "mbpet";
    public User sessionuser;
//    public static Item sessionUserItem;
	public JPAContainer<User> persons = JPAContainerFactory.make(User.class,MbpetUI.PERSISTENCE_UNIT);
	public JPAContainer<TestCase> testcases = JPAContainerFactory.make(TestCase.class,MbpetUI.PERSISTENCE_UNIT);
	public JPAContainer<TestSession> sessions = JPAContainerFactory.make(TestSession.class,MbpetUI.PERSISTENCE_UNIT); 
	public JPAContainer<Parameters> parameterscontainer = JPAContainerFactory.make(Parameters.class,MbpetUI.PERSISTENCE_UNIT); 
	public JPAContainer<Adapter> adapters = JPAContainerFactory.make(Adapter.class,MbpetUI.PERSISTENCE_UNIT);
	public JPAContainer<Model> models = JPAContainerFactory.make(Model.class,MbpetUI.PERSISTENCE_UNIT);
	public JPAContainer<TRT> trtcontainer = JPAContainerFactory.make(TRT.class,MbpetUI.PERSISTENCE_UNIT);
    
    
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = MbpetUI.class, widgetset = "com.aaron.mbpet.widgetset.MbpetWidgetset")
	public static class Servlet extends VaadinServlet {
	}

//    static {
//        try {
//			DemoDataGenerator.create();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
	  private static ThreadLocal<MbpetUI> threadLocal = new ThreadLocal<MbpetUI>();

	
	@Override
    protected void init(VaadinRequest request) {
//	    setInstance(this); // So that we immediately have access to the current application
	    addDetachListener(new DetachListener() { 

			@Override
            public void detach(DetachEvent event) {
//                releaseResources();
            } 
        });
        
        getPage().setTitle("MBPeT");
        
        //testing dummy data
//        try {
//			DemoDataGenerator.create();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

        
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
//        getPage().setLocation("/");	//MBPeT
        
        // Close the session
//        getSession().close();
        
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
		public void updateMonitoringFields(final String[] values, final int numslaves, final String[] slaveresults, final SessionViewer sessionViewer) {	//, final double current
		    access(new Runnable() {
		        @Override
		        public void run() {
		        	//update the UI
		        	MonitoringTab montab = sessionViewer.tabs.getMonitoringTab();
		        	montab.updateFields(values);
		        	montab.updateSlaveMonitoringInfo(numslaves, slaveresults);
		        	montab.updateChart(Integer.parseInt(values[7]));
		        }
		    });
		}
		
		@Override
		public void printNewestMessage(final String message, final SessionViewer sessionViewer) {	//, final double current
		    access(new Runnable() {
		        @Override
		        public void run() {
		        	//update the UI
//		        	MonitoringTab montab = SessionViewer.tabs.getMonitoringTab();	// TabLayout.getMonitoringTab();

		        	sessionViewer.tabs.getMonitoringTab().addNewMessageComponent(message);
		        	
//		        	mainview.addNewMessageComponent(string);
//		        	SessionViewer.progressbar.setValue(new Float(current));
//                    if (current < 1.0)
//                    	SessionViewer.progressstatus.setValue("" +
//                            ((int)(current*100)) + "% done");
//                    else
//                    	SessionViewer.progressstatus.setValue("all done");
		        }
		    });
		}
	    		
		@Override
		public void printFinalMessage(final String message, final int numslaves, final SessionViewer sessionViewer) {
		    access(new Runnable() {
		        @Override
		        public void run() {
		        	//update the UI
		        	MonitoringTab montab = sessionViewer.tabs.getMonitoringTab();
		        	montab.addNewMessageComponent(message);
		        	montab.generateSlaveMonitoringInfo(numslaves, "Disconnected");
		        }
		    });
		}
	    
		@Override
		public void printNextMasterOutput(final String message, final MasterTerminalWindow masterWindow) {	//final SessionViewer sessionViewer ){
		    access(new Runnable() {
		        @Override
		        public void run() {
		        	//update the UI
//		        	MonitoringTab montab = sessionViewer.tabs.getMonitoringTab();
//		        	montab.insertDataToEditor(message);
		        	masterWindow.insertDataToEditor(message);
		        	
//		        	MonitoringTab montab = masterWindow.tabs.getMonitoringTab();
//		        	montab.addNewMessageComponent(message);
//		        	sessionViewer.tabs.getMonitoringTab().addNewMessageComponent(message);
		        	
		        }
		    });
		}
		
		public User getSessionUser() {
			return sessionuser;
		}

		public void setSessionUser(User sessionUser) {
			this.sessionuser = sessionUser;
		}
		
		
		public JPAContainer<TestCase> getTestcases() {
			return testcases;
		}
		public void setTestcases(JPAContainer<TestCase> testcases) {
			this.testcases = testcases;
		}
		
		
		public JPAContainer<TestSession> getTestsessions() {
			return sessions;
		}
		public void setTestsessions(JPAContainer<TestSession> sessions) {
			this.sessions = sessions;
		}

		
		public JPAContainer<Model> getModels() {
			return models;
		}
		public void setModels(JPAContainer<Model> models) {
			this.models = models;
		}
		
		
		public JPAContainer<User> getPersons() {
			return persons;
		}
		public void setPersons(JPAContainer<User> persons) {
			this.persons = persons;
		}

		
		public JPAContainer<Parameters> getParameterscontainer() {
			return parameterscontainer;
		}
		public void setParameterscontainer(JPAContainer<Parameters> parameterscontainer) {
			this.parameterscontainer = parameterscontainer;
		}

		public JPAContainer<Adapter> getAdapterscontainer() {
			return adapters;
		}
		public void setAdapterscontainer(JPAContainer<Adapter> adapterscontainer) {
			this.adapters = adapterscontainer;
		}
		
		public JPAContainer<TRT> getTrtcontainer() {
			return trtcontainer;
		}
		public void setTrtcontainer(JPAContainer<TRT> trtcontainer) {
			this.trtcontainer = trtcontainer;
		}
		
		
//		  // return the current application instance
//		  public static MbpetUI getInstance() {
//		    return threadLocal.get();
//		  }
//
//		  // Set the current application instance
//		  public static void setInstance(MbpetUI application) {
//		    threadLocal.set(application);
//		  }
//
//		  @Override
//		  public void onRequestStart(HttpServletRequest request, HttpServletResponse response) {
//			  MbpetUI.setInstance(this);
//		  }
//
//		  @Override
//		  public void onRequestEnd(HttpServletRequest request, HttpServletResponse response) {
//		    threadLocal.remove();
//		  }
		  
}