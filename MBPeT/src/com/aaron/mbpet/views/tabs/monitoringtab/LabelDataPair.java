package com.aaron.mbpet.views.tabs.monitoringtab;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class LabelDataPair extends HorizontalLayout {

	Label name;
	Label value;
	
	public LabelDataPair(String labelname){
        setWidth("100%");
        //layout.setSpacing(true);

        name = new Label(labelname);	//("Aggregated");
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
	public String getName(){
		return name.getValue();
	}
	
}
