package com.aaron.mbpet.services;

import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceTheme;

public class AceUtils {

//	AceEditor editor;
	
	public static void setAceTheme(AceEditor editor, String theme) {	
//		this.editor = editor;
		
		switch (theme) {
		case "ambiance":
			editor.setTheme(AceTheme.ambiance);
            break;
        case "chrome":
			editor.setTheme(AceTheme.chrome);
            break;
        case "clouds":
			editor.setTheme(AceTheme.clouds);
            break;
        case "cobalt":
			editor.setTheme(AceTheme.cobalt);
			break;
        case "dreamweaver":
			editor.setTheme(AceTheme.dreamweaver);
			break;
        case "eclipse":
			editor.setTheme(AceTheme.eclipse);
			break;
        case "github":
			editor.setTheme(AceTheme.github);
			break;
        case "terminal":
			editor.setTheme(AceTheme.terminal);
			break;
        case "twilight":
			editor.setTheme(AceTheme.twilight);
			break;
        case "xcode":
			editor.setTheme(AceTheme.xcode);
			break;
		
        default:
			editor.setTheme(AceTheme.clouds);
		}	
	}
	
//	public void setEditorMode(AceEditor editor, String mode) {
//		//String file = filename.replace("C:/", "");
//		//file = file.replace("/", "\\");
//		if (mode.equals("dot")) {
//			editor.setMode(AceMode.dot);
//			modeBox.setValue(modeList.get(1));
//		//	System.out.println("Mode changed to dot");
//		} else if (mode.equals("gv")){
//			editor.setMode(AceMode.dot);
//			modeBox.setValue(modeList.get(2));
//		//	System.out.println("Mode changed to dot");
//		} else if (mode.equals("py") || mode.equals("python")) {
//			editor.setMode(AceMode.python);	
//			modeBox.setValue(modeList.get(0));
//		//	System.out.println("Mode changed to python");
//		} 
//	}
}
