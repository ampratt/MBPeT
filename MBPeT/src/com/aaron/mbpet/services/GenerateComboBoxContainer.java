package com.aaron.mbpet.services;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.Action.Container;

public class GenerateComboBoxContainer {

    static final String CAPTION_PROPERTY = "caption";
    static final String INDEX_PROPERTY = "index";

    public void GenerateComboBoxContainer() {
    	
    }
    
    @SuppressWarnings("unchecked")
	public static IndexedContainer generateContainer(final int size) {
        final IndexedContainer container = new IndexedContainer();	//hierarchical ? new HierarchicalContainer() : new IndexedContainer();
//        final StringGenerator sg = new StringGenerator();
        container.addContainerProperty(CAPTION_PROPERTY, Integer.class, null);
        container.addContainerProperty(INDEX_PROPERTY, Integer.class, null);

        for (int i = 1; i < size + 1; i++) {
            final Item item = container.addItem(i);
            item.getItemProperty(CAPTION_PROPERTY).setValue(i);
//                    sg.nextString(true) + " " + sg.nextString(false));
            item.getItemProperty(INDEX_PROPERTY).setValue(i);
        }


        return container;
    }
}
