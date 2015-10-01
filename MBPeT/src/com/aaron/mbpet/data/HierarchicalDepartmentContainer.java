package com.aaron.mbpet.data;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.TestCase;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.provider.CachingLocalEntityProvider;

public class HierarchicalDepartmentContainer extends JPAContainer<TestCase> {

    public HierarchicalDepartmentContainer() {
        super(TestCase.class);
        setEntityProvider(new CachingLocalEntityProvider<TestCase>(
        		TestCase.class,
                JPAContainerFactory
                        .createEntityManagerForPersistenceUnit(MbpetUI.PERSISTENCE_UNIT)));
//        setParentProperty("parent");
    }

    @Override
    public boolean areChildrenAllowed(Object itemId) {
        return super.areChildrenAllowed(itemId);
                //&& getItem(itemId).getEntity();	//.isSuperDepartment();
    }
}
