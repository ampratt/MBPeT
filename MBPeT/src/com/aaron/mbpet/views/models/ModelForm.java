package com.aaron.mbpet.views.models;

import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.TestSession;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

public class ModelForm extends FormLayout {

	@PropertyId("title")
	private TextField title = new TextField("Title");

	@PropertyId("dotschema")
	private TextField dotschema = new TextField("dot Model");

	
	public ModelForm(){
//		super(2,2);
		this.setMargin(true);
		this.setSpacing(true);
		
		title.addValidator(new BeanValidator(Model.class, "title"));
//		description.addValidator(new BeanValidator(TestCase.class, "description"));
		
		title.focus();
		title.setWidth(18, Unit.EM);//  (100.0f, Unit.PERCENTAGE);	//setWidth("100%");
//		description.setWidth(18, Unit.EM);
//		description.setCaption("Decription (optional)");
//		description.addStyleName("caption-optional");
		
		title.setValidationVisible(false);
		
		title.setNullRepresentation("");
//		description.setNullRepresentation("");

		addComponent(title);	//, 0,0,1,0
//		addComponent(description, 0,1,1,1);
//		addComponent(owner, 0,1,1,1);
	}

	public void disableValidationMessages() {
		title.setValidationVisible(false);

	}
	
	public void enableValidationMessages() {
		title.setValidationVisible(true);
	}
	
	
}
