package com.aaron.mbpet.components.diagrambuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceMode;
import org.vaadin.aceeditor.AceEditor.SelectionChangeEvent;
import org.vaadin.aceeditor.AceEditor.SelectionChangeListener;
import org.vaadin.diagrambuilder.Connector;
import org.vaadin.diagrambuilder.DiagramBuilder;
import org.vaadin.diagrambuilder.DiagramStateEvent;
import org.vaadin.diagrambuilder.Node;
import org.vaadin.diagrambuilder.Transition;

import com.aaron.mbpet.components.aceeditor.AceEditorLayoutDirectory;
import com.google.gwt.thirdparty.guava.common.io.Files;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;


public class DiagramAceLayout extends VerticalLayout {

	// Diagram elements
    final VerticalLayout layout = new VerticalLayout();
    final TextField outFileField = new TextField();
    final TextField inFileField = new TextField();
    final TextField diagramTitleField = new TextField();
    DiagramBuilder diagramBuilder; 
    Button aceLoaderButton = new Button("Load dot from editor");
    Button resetDiagramButton = new Button("Reset Diagram Builder");
    Button saveDiagramButton = new Button("Save to .dot file");
    Button loadDiagramButton = new Button("Load .dot file");
    Button renameNodesButton = new Button("Rename Nodes");
    
    List<String> nName = new ArrayList<String>();
    List<String> nType = new ArrayList<String>();
    List<int[]> nXy = new ArrayList<int[]>();
    List<Transition> nTransitions = new ArrayList<Transition>();
    List<Connector> nConnectorName = new ArrayList<Connector>();
    DBuilderUtils diagramUtils = new DBuilderUtils();
    
    // Ace Editor elements
//	AceEditor editor;// = new AceEditor();
    final TextField aceOutFileField = new TextField();
    final TextField aceInFileField = new TextField();
    ComboBox modeBox;
    List<String> modeList;
    
    
	public DiagramAceLayout(AceEditor editor) {
		setSizeFull();
		setMargin(true);
		setSpacing(true);

		addComponent(new Label("<h3>Diagram Ace Combo example</h3>", ContentMode.HTML));

		// AceEditor
		editor = new AceEditor();
		initAceLayout(editor);	//addComponent(new AceEditorLayoutDirectory(editor));
//		initAceLayout(editor);	
		
		// Diagram Builder
		addComponent(new DBuilderLayout());
//		initDiagramLayout(editor);
		
	}

	public DiagramAceLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * Ace Editor Components
	 */
	private void initAceLayout(final AceEditor editor) {

        String[] modes = {"dot", "python", "gv"};
        modeList = Arrays.asList(modes);
        modeBox = new ComboBox("Select source code style", modeList);
//        modeBox.setContainerDataSource(modes);
//        modeBox.setWidth(100.0f, Unit.PERCENTAGE);
        modeBox.addStyleName("tiny");
        modeBox.setInputPrompt("No style selected");
        modeBox.setFilteringMode(FilteringMode.CONTAINS);
        modeBox.setImmediate(true);
        modeBox.setNullSelectionAllowed(false);
        modeBox.setValue(modeList.get(0));        
        modeBox.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Notification.show("mode changed to: " + event.getProperty().getValue().toString());
                setEditorMode(event.getProperty().getValue().toString(), editor);
            }
        });
        //layout.
        addComponent(modeBox);
        
        // Horizontal Layout
        HorizontalLayout h = new HorizontalLayout();
        addComponent(h);
        VerticalLayout v = new VerticalLayout();
        h.setWidth("100%");
        h.setSpacing(true);
        h.addComponent(buildAceEditor(editor));
        h.addComponent(v);
        h.setExpandRatio(editor, 2);
        h.setExpandRatio(v, 1);
		
		aceOutFileField.setCaption("Give file name");
		aceOutFileField.setWidth("20em");
        aceOutFileField.setInputPrompt("C:/dev/output/ace-editor-output.dot");
        aceOutFileField.setValue("C:/dev/output/ace-editor-output.dot");
        //layout.
        v.addComponent(aceOutFileField);

		Button saveButton = new Button("Save");
		saveButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				String s = editor.getValue();
//				Label label = new Label(s);
				//layout.addComponent(label);
				//testing purposes
				Notification.show(s, Type.WARNING_MESSAGE);
				saveToFile(s, aceOutFileField.getValue());
			}
		});
		//layout.
		v.addComponent(saveButton);
		
		aceInFileField.setCaption("Give file name");
		aceInFileField.setWidth("20em");
		aceInFileField.setInputPrompt("C:/dev/output/ace-editor-output.dot");
		aceInFileField.setValue("C:/dev/output/ace-editor-output.dot");
        //layout.
		v.addComponent(aceInFileField);
        
		Button loadButton = new Button("Load");
		loadButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				// load file to editor
				editor.setValue( loadFile(aceInFileField.getValue()) );
				
				// set code style mode to match file type
//				System.out.println(inFileField.getValue());
				String extension = Files.getFileExtension(aceInFileField.getValue());	//FilenameUtils.getExtension(filename);
//				System.out.println("extension is " + extension);
				setEditorMode(extension, editor);
			}
		});
		//layout.
		v.addComponent(loadButton);
		
	}
	
	
	private AceEditor buildAceEditor(AceEditor editor) {
		// Ace Editor
		editor.setValue("Hello world!\nif:\n\tthen \ndo that\n...");
			// use static hosted files for theme, mode, worker
//			editor.setThemePath("/static/ace");
//			editor.setModePath("/static/ace");
//			editor.setWorkerPath("/static/ace");
		editor.setWidth("100%");		
		editor.setReadOnly(false);
		editor.setMode(AceMode.python);
//		editor.setUseWorker(true);
//		editor.setTheme(AceTheme.twilight);	
//		editor.setWordWrap(false);
//		editor.setReadOnly(false);
//		editor.setShowInvisibles(false);
//		System.out.println(editor.getValue());
		
		//layout.
//		addComponent(editor);

		
		// Use worker (if available for the current mode)
		//editor.setUseWorker(true);
		editor.addTextChangeListener(new TextChangeListener() {
		    @Override
		    public void textChange(TextChangeEvent event) {
		        Notification.show("Text: " + event.getText());
		    }
		});
		
		editor.addSelectionChangeListener(new SelectionChangeListener() {
		    @Override
		    public void selectionChanged(SelectionChangeEvent e) {
		        int cursor = e.getSelection().getCursorPosition();
		        //Notification.show("Cursor at: " + cursor);
		    }
		});
		return editor;

//		new SuggestionExtension(new MySuggester()).extend(editor);		
	}
	
	
	
	public void saveToFile(String output, String fileName) {
        // create file
//		String fileName = "C:/dev/output/ace-editor-output.dot";
        File file = new File(fileName);
        PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        writer.println( output );
        writer.close();
	
        //show confirmation to user
        Notification.show("dot file was saved at: " + fileName, Notification.Type.TRAY_NOTIFICATION);;
	}
	
	public void setEditorMode(String mode, AceEditor editor) {
//		String file = filename.replace("C:/", "");
//		file = file.replace("/", "\\");
		if (mode.equals("dot")) {
			editor.setMode(AceMode.dot);
//			modeBox.setValue("dot");
			modeBox.setValue(modeList.get(0));
			System.out.println("Mode changed to dot");
		} else if (mode.equals("gv")){
			editor.setMode(AceMode.dot);
			modeBox.setValue(modeList.get(2));
			System.out.println("Mode changed to dot");
		} else if (mode.equals("py") || mode.equals("python")) {
			editor.setMode(AceMode.python);	
			modeBox.setValue(modeList.get(1));
			System.out.println("Mode changed to python");
		} 
	}
	
	public String loadFile(String filename) {
		
		String input = "";
		BufferedReader br;
		try {
			Scanner sc = new Scanner(new FileReader(filename));
			String line = null;
			while (sc.hasNextLine()) {
				line = sc.nextLine();
				System.out.println(line);
				input = input.concat(line);
				if(sc.hasNextLine()) {
					input = input.concat("\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return input;
	}
	
	
	

	/**
	 * Diagram Builder Components
	 */
    public void initDiagramLayout(final AceEditor editor) {
    	// set main content
        	//final VerticalLayout layout = new VerticalLayout();
//        layout.setMargin(true);
//        layout.setSpacing(true);
//        addComponent(layout);
    	addComponent(aceLoaderButton);
                		
        HorizontalLayout saveLayout = new HorizontalLayout();
        saveLayout.setSpacing(true);
        outFileField.setWidth("15em");
        outFileField.setInputPrompt("C:/dev/output/dot-output.dot");
        outFileField.setValue("C:/dev/output/dot-output.dot");
        saveLayout.addComponent(outFileField);
        saveLayout.addComponent(saveDiagramButton);
		
        HorizontalLayout loadLayout = new HorizontalLayout();
        loadLayout.setSpacing(true);
        inFileField.setWidth("15em");
        inFileField.setInputPrompt("C:/dev/output/dot-output.dot");
        inFileField.setValue("C:/dev/output/dot-output.dot");
        loadLayout.addComponent(inFileField);
        loadLayout.addComponent(loadDiagramButton);
        
        VerticalLayout v = new VerticalLayout();
        v.setSpacing(true);
        v.addComponent(saveLayout);
        v.addComponent(loadLayout);
//        v.setComponentAlignment(saveLayout, Alignment.BOTTOM_RIGHT);

        HorizontalLayout dFunctionsLayout = new HorizontalLayout();
        dFunctionsLayout.setWidth("100%");
        dFunctionsLayout.setSpacing(true);
        diagramTitleField.setWidth("15em");
        diagramTitleField.setCaption("Graph title");
        diagramTitleField.setInputPrompt("default_use_case");
        dFunctionsLayout.addComponent(diagramTitleField);
        dFunctionsLayout.addComponent(renameNodesButton);
        dFunctionsLayout.addComponent(v);
        dFunctionsLayout.setComponentAlignment(diagramTitleField, Alignment.BOTTOM_LEFT);
        dFunctionsLayout.setComponentAlignment(renameNodesButton, Alignment.BOTTOM_LEFT);
        dFunctionsLayout.setComponentAlignment(v, Alignment.BOTTOM_RIGHT);
		
        diagramBuilder = new DiagramBuilder();
        diagramBuilder.setImmediate(true);
        diagramUtils.initDiagram(diagramBuilder);

        //layout.
//        addComponent(new Label("<h1>Diagram Ace Combo example</h1>", ContentMode.HTML));
        addComponents(resetDiagramButton,
//        							saveLayout,
//        							loadLayout, 
        							dFunctionsLayout);
        							//new HorizontalLayout(resetDiagramButton, saveLayout, loadLayout));        
        addComponent(diagramBuilder);
        //initDiagram();

        
        aceLoaderButton.addClickListener(new Button.ClickListener() {
        	@Override
			public void buttonClick(ClickEvent event) {
        		try {
        			if (diagramBuilder != null) {
    					//layout.
        				removeComponent(diagramBuilder); 
    				}
//        	        diagramBuilder = new DiagramBuilder();
        	        diagramUtils.readFromDotSource(diagramBuilder = new DiagramBuilder(), editor.getValue());
        	        diagramTitleField.setValue(diagramUtils.getGraphTitle());
    		        //layout.
        	        addComponent(diagramBuilder);    		        
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e.getClass());
				}
			}
		});
		
        
        
        resetDiagramButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (diagramBuilder != null) {
					//layout.
					removeComponent(diagramBuilder); 
				}
				
		        diagramBuilder = new DiagramBuilder();
		        diagramUtils.initDiagram(diagramBuilder);
    	        diagramTitleField.setValue(diagramUtils.getGraphTitle());
		        //layout.
    	        addComponent(diagramBuilder);
			}
		});
        
        saveDiagramButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                // Using asynchronous API to lazily fetch the current state of the diagram.
                diagramBuilder.getDiagramState(new DiagramBuilder.StateCallback() {
                    @Override
                    public void onStateReceived(DiagramStateEvent event) {
                        // DO SOMETHING with received state information. e.g. parse data for .dot files
                    	//diagramUtils.reportStateBack(event);
                    	if (diagramBuilder != null) {
							//layout.
                    		removeComponent(diagramBuilder); 
						}
                    	//save data to file
                    	List<Node> nodes = event.getNodes();
						diagramUtils.saveToFile(nodes, outFileField.getValue(), diagramTitleField.getValue());
                    	
						// redraw diagram if nodes need to be renamed
//                    	nodes = diagramUtils.renameNodes(nodes);
						diagramUtils.generateDiagramAfterRename(diagramBuilder = new DiagramBuilder(), nodes);
						addComponent(diagramBuilder);
                    }
                });
            }
        });
        
        loadDiagramButton.addClickListener(new Button.ClickListener() {
        	@Override
			public void buttonClick(ClickEvent event) {
        		// call method for parsing .dot data
        		try {
        			if (diagramBuilder != null) {
    					//layout.
        				removeComponent(diagramBuilder); 
    				}
//        	        diagramBuilder = new DiagramBuilder();
        	        diagramUtils.readFromDotSource(diagramBuilder = new DiagramBuilder(), inFileField.getValue());
        	        diagramTitleField.setValue(diagramUtils.getGraphTitle());
    		        //layout.
        	        addComponent(diagramBuilder);    		        
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e.getClass());
				}
			}
		});
        
        
        renameNodesButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                // Using asynchronous API to lazily fetch the current state of the diagram.
                diagramBuilder.getDiagramState(new DiagramBuilder.StateCallback() {
                    @Override
                    public void onStateReceived(DiagramStateEvent event) {
                    	if (diagramBuilder != null) {
							//layout.
                    		removeComponent(diagramBuilder); 
						}
                    	List<Node> nodes = event.getNodes();
                    	nodes = diagramUtils.renameNodes(nodes);	//event, diagramBuilder = new DiagramBuilder()
						diagramUtils.generateDiagramAfterRename(diagramBuilder = new DiagramBuilder(), nodes);
						//layout.
						addComponent(diagramBuilder);
                    }
                });
            }
        });
        
    }
    
    
    
}
