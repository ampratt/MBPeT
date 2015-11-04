package com.aaron.mbpet.views.parameters;

import com.aaron.mbpet.domain.TRT;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class TRTForm extends FormLayout {

	@PropertyId("action") 		//target_response_times
	public TextField action = new TextField("Action:");

	
	@PropertyId("average")
	private TextField average = new TextField("Average:");

	
	@PropertyId("max")
	private TextField max = new TextField("Max:");
	
	
	public TRTForm(){
		setMargin(new MarginInfo(false, false, false, false));
		
		action.addValidator(new BeanValidator(TRT.class, "action"));
		average.addValidator(new BeanValidator(TRT.class, "average"));
		max.addValidator(new BeanValidator(TRT.class, "max"));
		
		action.setNullRepresentation("");
		average.setNullRepresentation("0.0");
		max.setNullRepresentation("0.0");
		
		toggleValidationMessages(false);
		
		addComponent(action);
		addComponent(average);
		addComponent(max);
		
		for (Component c : this.components) {
			c.addStyleName("small");
			c.setWidth("97%");
		}
		
	}

	public void toggleValidationMessages(boolean b) {
		action.setValidationVisible(b);
		average.setValidationVisible(b);
		max.setValidationVisible(b);
		
	}



}
