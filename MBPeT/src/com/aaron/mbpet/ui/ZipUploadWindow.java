package com.aaron.mbpet.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipFile;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.domain.User;
import com.aaron.mbpet.services.FileSystemUtils;
import com.aaron.mbpet.services.ModelUtils;
import com.aaron.mbpet.services.Unzip;
import com.aaron.mbpet.services.ZippedSessionCreator;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.UploadException;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

public class ZipUploadWindow extends Window {

	Tree tree;
	private ComboBox sutCombobox;
	
	List<Model> mlist;
	
	FileSystemUtils fileUtils = new FileSystemUtils();
	ModelUtils modelUtils = new ModelUtils();
	
	boolean navToCasePage = false;
	
	private IndexedContainer ic;
	private JPAContainer<TestCase> sutcontainer;	// = ((MbpetUI) UI.getCurrent()).getTestcases();
	private BeanItem<TestSession> newSessionItem;
	private User curruser;	
	private TestSession newSession;
	TestCase parentSUT;
	String usersBasePath = ((MbpetUI) UI.getCurrent()).getUsersBasepath();
	String userHomeDirPath;
	String parentSutpath;	// = usersBasePath + curruser.getUsername() + "/" + parentSUT.getTitle() + "/";
	
	/*
	 * Create new Model
	 */
	public ZipUploadWindow(User curruser, Tree tree) {	//boolean navToCasePage,
		super("Upload Zip file with project folder"); // Set window caption
		
		this.tree = tree;
		this.curruser = curruser;
		this.userHomeDirPath = usersBasePath + "/" + curruser.getUsername() + "/";
		
		this.sutcontainer = ((MbpetUI) UI.getCurrent()).getTestcases();	//JPAContainerFactory.make(TestCase.class, MbpetUI.PERSISTENCE_UNIT);	//container;
		newSession = new TestSession();
		this.newSessionItem = new BeanItem<TestSession>(newSession);
      
		init();
	}
	
	private void init() {
		center();
		setResizable(false);
		setClosable(true);
//		setModal(true);
        setSizeUndefined();
        
//		// delete unzipped tmp project folder in uploads
//		FileSystemUtils fileUtils = new FileSystemUtils();
//    	fileUtils.deleteUploadsDirContent(curruser.getUsername());
        
        setContent(buildContent()); //ManualLayoutDesign
//        setCaption(buildCaption());
	}
	
    /**
     * @return the caption of the editor window
     */
    private String buildCaption() {
    		return String.format("Upload Zip file with project folder: %s");	//,	subject.getTitle());
    }
    
    
	private Component buildContent() {
		final VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);
		
		
		// helper for zip format
		Button infohelper = new Button();
		infohelper.setIcon(FontAwesome.QUESTION_CIRCLE);
		infohelper.addStyleName("borderless");
		infohelper.setDescription("See required folder structure format.");
		infohelper.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().addWindow(new ZipInfoHelperWindow());
			}
		});

		
		// UPLOAD
		// Create the upload with a caption and set receiver later
		Upload upload = new Upload("", null);	//null);
		upload.setButtonCaption("Upload File");
		upload.addStyleName("primary");
		final InlineZipFileUploader zipuploader = new InlineZipFileUploader();	//(curruser, parentSUT); 
		upload.setReceiver(zipuploader);
		upload.addListener(zipuploader);
		
//		upload.addStartedListener(new StartedListener() {
//            @Override
//            public void uploadStarted(final StartedEvent event) {
////                if (uploadInfoWindow.getParent() == null) {
////                    UI.getCurrent().addWindow(uploadInfoWindow);
////                }
////                uploadInfoWindow.setClosable(false);
//            }
//        });
		        
//		// Put the upload component in a panel
//		Panel panel = new Panel("Cool Image Storage");
//		panel.addComponent(upload);
//		        
//		// Show uploaded file in this placeholder
//		final Embedded image = new Embedded("Uploaded Image");
//		image.setVisible(false);
//		panel.addComponent(image);

		
		// COMBOBOX SUT
		sutCombobox = new ComboBox("Parent SUT for this project");
//		binder.bind(sessionCombobox, "parentsession");
		sutCombobox.setWidth(22, Unit.EM);
		sutCombobox.setNullSelectionAllowed(false);
		sutCombobox.setImmediate(true);
//		sessionCombobox.setFilteringMode(FilteringMode.CONTAINS);
//		sessionCombobox.addValidator(new BeanValidator(Model.class, "parentsession"));
//		title.setValidationVisible(false);
//		sessionCombobox.setNullRepresentation("");
//		sessionCombobox.setContainerDataSource(sessions);
		updateFilters();

		ic = new IndexedContainer();
		ic.addContainerProperty("id", Integer.class, "");
		ic.addContainerProperty("title", String.class, "");
		
		// set null option
//		String nullitem = "-- none --";
//		sessionCombobox.addItem(nullitem);
//		sessionCombobox.setNullSelectionItemId(nullitem);
		
		//populate combobox
		for (Object id : sutcontainer.getItemIds()) {
			System.out.println("SUT-Parent: " + sutcontainer.getItem(id).getEntity().getTitle() +
					sutcontainer.getItem(id).getEntity().getOwner());
			
			Object itemId = ic.addItem();
			Item item = ic.getItem(itemId);
			item.getItemProperty("id").setValue(sutcontainer.getItem(id).getEntity().getId());
			item.getItemProperty("title").setValue(sutcontainer.getItem(id).getEntity().getTitle());
			
		}
		sutCombobox.setContainerDataSource(ic);
		sutCombobox.setItemCaptionPropertyId("title");
		

		sutCombobox.select(ic.getIdByIndex(ic.size()-1));
		Object capid = sutCombobox.getValue();// ic.getItem(binder.getField("parentsession").getValue());
		Item item = ic.getItem(capid);
		//TODO - this needs to be added
		parentSUT = sutcontainer.getItem(item.getItemProperty("id").getValue()).getEntity();
		parentSutpath = usersBasePath + curruser.getUsername() + "/" + parentSUT.getTitle() + "/";

//		uploader.setParentSUT(parentSUT);
//		currSession.setParentsut(sutcontainer.getItem(item.getItemProperty("id").getValue()).getEntity());			
		
		sutCombobox.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
//				Notification.show("selected: " + event.getProperty().getValue().toString());
//				System.out.println("binder field value: " + binder.getField("parentsession").getValue().toString());
				System.out.println("combobox value: " + sutCombobox.getValue().toString());

//				Item item = ic.getItem(binder.getField("parentsession").getValue());
				Object capid = sutCombobox.getValue();// ic.getItem(binder.getField("parentsession").getValue());
				Item item = ic.getItem(capid);
				System.out.println("sutCombobox prop id(sut): " + item.getItemProperty("id").getValue().toString());
				parentSUT = sutcontainer.getItem(item.getItemProperty("id").getValue()).getEntity();
				System.out.println(parentSUT.getTitle());	// setValue(parentsession);
				//TODO - this needs to be added
				parentSutpath = usersBasePath + curruser.getUsername() + "/" + parentSUT.getTitle() + "/";
//				uploader.setParentSUT(parentSUT);
//				currSession.setParentsut(parentSUT);	
			}
		});
		
		final HorizontalLayout uploadlayout = new HorizontalLayout();
		uploadlayout.setSpacing(false);
		uploadlayout.setMargin(false);
		uploadlayout.addComponents(infohelper, upload);
		uploadlayout.setComponentAlignment(infohelper, Alignment.MIDDLE_RIGHT);
		
		layout.addComponents(sutCombobox, uploadlayout);	//(title);
	
//		// button layout
//		HorizontalLayout buttons = new HorizontalLayout();
//		buttons.setWidth("100%");
//		buttons.addStyleName("buttons-margin-top");
//		layout.addComponent(buttons);
//		
//		saveButton = new Button("Create", this);
//		saveButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
//		saveButton.setClickShortcut(KeyCode.ENTER);
//		
//		cancelButton = new Button("Cancel", this);
//		
//		buttons.addComponents(saveButton, cancelButton);
//		buttons.setComponentAlignment(saveButton, Alignment.MIDDLE_LEFT);
//		buttons.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
		
		return layout;
	}
	

		
	    private void updateFilters() {
	    	sutcontainer.removeAllContainerFilters();
	    	
	    	Equal userfilter = new Equal("owner", curruser);//  ("parentcase", getTestCaseByTitle(), true, false);	    	
	    	sutcontainer.addContainerFilter(userfilter);
	    }
	    
	
		// Implement both receiver that saves upload in a file and
		// listener for successful upload
		ZipFile zipfileUploaded = null;
		class InlineZipFileUploader implements Receiver, SucceededListener {
		    public File file;	    
			
			// upload the file to destination of username/selected SUT
		    public OutputStream receiveUpload(String filename, String mimeType) {		    	
		        // Create upload stream
		        FileOutputStream fos = null; // Output stream to write to
		        
		        // get selected SUT
				Object capid = sutCombobox.getValue();// ic.getItem(binder.getField("parentsession").getValue());
				Item item = ic.getItem(capid);
				System.out.println("sutCombobox prop id(sut): " + item.getItemProperty("id").getValue().toString());
				parentSUT = sutcontainer.getItem(item.getItemProperty("id").getValue()).getEntity();
				
		        try {
		            // Open the file (destination uploading to) for writing.
		            file = new File(userHomeDirPath +	//parentSutpath +
		            				"uploads/" +
		            				filename);			//"C:/dev/mbpet/zip/"
//		            zipfileUploaded = new ZipFile(zfile);
		            fos = new FileOutputStream(file);
		        } catch (final java.io.FileNotFoundException e) {
		            Notification.show(
		                    "Oops. Something went wrong", "Sorry about that. Please try again or create the project manually.",
		                    Notification.Type.ERROR_MESSAGE);
		            close();
		            return null;
		        }catch (IOException e) {
					e.printStackTrace();
				}
		        return fos; // Return the output stream to write to
		    }

		    // create the session - together with all the enclosed files
		    public void uploadSucceeded(SucceededEvent event) {
		        // Show the uploaded file in the image viewer
//		    	Notification.show("Zip uploaded. " +
//		    			"Generating the Test Session Configurations from the enclosed files." +
//		    			file, 
//		    			Notification.Type.HUMANIZED_MESSAGE);

		    	// unzip the upload dir
				Unzip unzipper = new Unzip(file);
				File unzippedProjectDir = unzipper.unzipFileIntoDirectory();
				System.out.println("the unzipped test project dir is>" + unzippedProjectDir);

				//CREATE SESSION
				ZippedSessionCreator sessionCreator = new ZippedSessionCreator(unzippedProjectDir, curruser, parentSUT, tree);

				// delete unzipped tmp project folder in uploads
//				unzipper.deleteUnzippedProjectDir(unzippedProjectDir);
				
				confirmNotification("The project <b>" + unzippedProjectDir.getName() + "</b> added to SUT: <b>" + parentSUT.getTitle() + "</b>");
		    	
				// delete unzipped tmp project folder in uploads
				FileSystemUtils fileUtils = new FileSystemUtils();
		    	fileUtils.deleteUploadsDirContent(curruser.getUsername());

				close();
		    }
		};
		
		
		
		private void confirmNotification(String message) {
	        // welcome notification
	        Notification notification = new Notification("Project Uploaded!", Type.TRAY_NOTIFICATION);
	        notification.setDescription("<br/>"+message);
	        notification.setHtmlContentAllowed(true);
	        notification.setStyleName("success");	//tray  closable login-help
	        notification.setPosition(Position.BOTTOM_RIGHT);
	        notification.setDelayMsec(1000);
	        notification.show(Page.getCurrent());
		}

		
		public class ZipInfoHelperWindow extends Window {
			
			public ZipInfoHelperWindow(){
				center();
				setResizable(false);
				setModal(true);
				
				VerticalLayout vl = new VerticalLayout();
				vl.setWidth(15, Unit.EM);
				
				// Serve the image from the theme
				Resource res = new ThemeResource("img/zip-structure.png");

				// Display the image without caption
				Image image = new Image(null, res);
				image.setHeight(150, Unit.PIXELS);
				
				vl.addComponent(new Label("Required folder structure"));
				vl.addComponent(image);
				
				
				setContent(vl);
			}
		}
		

}