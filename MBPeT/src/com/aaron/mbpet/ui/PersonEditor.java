/**
 * Copyright 2009-2013 Oy Vaadin Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aaron.mbpet.ui;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

import com.aaron.mbpet.domain.User;
import com.vaadin.data.Item;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class PersonEditor extends Window implements Button.ClickListener,
        FormFieldFactory {

    private final Item personItem;
    private Form editorForm;
    private Button saveButton;
    private Button cancelButton;

    public PersonEditor(Item personItem, String lableText) {
    	//layouting
    	this.setModal(true);
    	
    	VerticalLayout layout = new VerticalLayout();
    	layout.setMargin(true);  
    	    	
        this.personItem = personItem;
        editorForm = new Form();
        editorForm.setFormFieldFactory(this);
        editorForm.setBuffered(true);
//        editorForm.setImmediate(true);
        editorForm.setItemDataSource(personItem, Arrays.asList("firstname",
                "lastname", "username", "password", "organization"));
        

        //buttons        
        saveButton = new Button("Save", this);
        cancelButton = new Button("Cancel", this);
        saveButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setWidth("100%");
        buttons.addComponents(saveButton, cancelButton);
        buttons.setComponentAlignment(saveButton, Alignment.BOTTOM_LEFT);
        buttons.setComponentAlignment(cancelButton, Alignment.BOTTOM_RIGHT);
        
//        editorForm.getFooter().addComponent(buttons);
//        editorForm.getFooter().addComponent(cancelButton);
        
        layout.addComponent(new Label("<h3>" + lableText + "</h3>", 
				ContentMode.HTML));
        layout.addComponent(editorForm);
        layout.addComponent(buttons);
        
        setSizeUndefined();
        setContent(layout); //editorForm
        setCaption(buildCaption());
    }

    /**
     * @return the caption of the editor window
     */
    private String buildCaption() {
    	if (!(personItem.getItemProperty("firstname").getValue() == null) ) {
    		return String.format("%s %s", personItem.getItemProperty("firstname")
    				.getValue(), personItem.getItemProperty("lastname").getValue());
    	} else {
    		return "";
    	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton() == saveButton) {
            editorForm.commit();
            fireEvent(new EditorSavedEvent(this, personItem));
        } else if (event.getButton() == cancelButton) {
            editorForm.discard();
        }
        close();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.FormFieldFactory#createField(com.vaadin.data.Item,
     * java.lang.Object, com.vaadin.ui.Component)
     */
    @Override
    public Field createField(Item item, Object propertyId, Component uiContext) {
        Field field = DefaultFieldFactory.get().createField(item, propertyId,
                uiContext);
//        if ("department".equals(propertyId)) {
//            field = new DepartmentSelector();
//        } else 
    	if (field instanceof TextField) {
            ((TextField) field).setNullRepresentation("");
//            ((TextField) field).setValidationVisible(false);
        }

        field.addValidator(new BeanValidator(User.class, propertyId.toString()));
		
        return field;
    }

    public void addListener(EditorSavedListener listener) {
        try {
            Method method = EditorSavedListener.class.getDeclaredMethod(
                    "editorSaved", new Class[] { EditorSavedEvent.class });
            addListener(EditorSavedEvent.class, listener, method);
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException(
                    "Internal error, editor saved method not found");
        }
    }

    public void removeListener(EditorSavedListener listener) {
        removeListener(EditorSavedEvent.class, listener);
    }

    public static class EditorSavedEvent extends Component.Event {

        private Item savedItem;

        public EditorSavedEvent(Component source, Item savedItem) {
            super(source);
            this.savedItem = savedItem;
        }

        public Item getSavedItem() {
            return savedItem;
        }
    }

    public interface EditorSavedListener extends Serializable {
        public void editorSaved(EditorSavedEvent event);
    }

}
