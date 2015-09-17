package com.aaron.mbpet.ui;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

import com.aaron.mbpet.domain.User;
import com.aaron.mbpet.views.LoginView;
import com.vaadin.data.Item;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class UserEditor extends Panel implements Button.ClickListener,
        FormFieldFactory {

    private final Item personItem;
    private Form editorForm;
    private Button saveButton;
    private Button cancelButton;

    public UserEditor(Item personItem) {
        this.personItem = personItem;
        editorForm = new Form();
        editorForm.setFormFieldFactory(this);
        editorForm.setBuffered(true);
        editorForm.setImmediate(true);
        editorForm.setItemDataSource(personItem, Arrays.asList("firstname",
                "lastname", "username", "password", "organization"));

        saveButton = new Button("Save", this);
        cancelButton = new Button("Cancel", this);
        saveButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        
        editorForm.getFooter().addComponent(saveButton);
        editorForm.getFooter().addComponent(cancelButton);
        
        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(editorForm);
        
        setSizeUndefined();
//        setCaption(buildCaption());
        setCaption(buildCaption());
        setContent(layout);
    }

    /**
     * @return the caption of the editor window
     */
    private String buildCaption() {
        return String.format("%s %s", personItem.getItemProperty("firstname")
                .getValue(), personItem.getItemProperty("lastname").getValue());
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
            UI.getCurrent().getNavigator().navigateTo(LoginView.NAME);			

        }
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
        }

        field.addValidator(new BeanValidator(User.class, propertyId
                .toString()));

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
