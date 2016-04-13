package com.aaron.mbpet.views.tabs.monitoringtab;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class LabelDataPair extends HorizontalLayout {

	String actionname;
	Label name;
	Label value;
	
	public LabelDataPair(String labelname){
        setWidth("100%");
        //layout.setSpacing(true);
        actionname = labelname;
        name = new Label(shortenLabelName(labelname));	//("Aggregated");
        name.addStyleName("small");
        
        value = new Label("-");
        value.setSizeUndefined();
        value.addStyleName("bold");
        value.addStyleName("small");
        
        addComponents(name, value);
        setComponentAlignment(name, Alignment.MIDDLE_LEFT);
        setComponentAlignment(value, Alignment.MIDDLE_RIGHT);
	}

	public void setDataValue(String labelvalue){
		value.setValue(labelvalue);
	}
	public String getNameUnShortened(){
		return actionname;	//name.getValue();
	}
//	public String getName(){
//		return name.getValue();
//	}	
	
	private String shortenLabelName(String labelname) {
		if (labelname.length() >= 20){
			return (new StringBuilder(labelname.substring(0, 20) + "..)")).toString();
		} else return labelname;
	}
}
