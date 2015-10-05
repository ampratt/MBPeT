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
package com.aaron.mbpet.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.transaction.*;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.TestCase;
import com.aaron.mbpet.domain.TestSession;
import com.aaron.mbpet.domain.User;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;


public class DemoDataGenerator {

	final static String[] fnames = { "Peter", "Alice", "Joshua", "Mike",
			"Olivia", "Nina", "Alex", "Rita", "Dan", "Umberto", "Henrik",
			"Rene", "Lisa", "Marge" };
	final static String[] lnames = { "Smith", "Gordon", "Simpson", "Brown",
			"Clavel", "Simons", "Verne", "Scott", "Allison", "Gates",
			"Rowling", "Barks", "Ross", "Schneider", "Tate" };
	
	static JPAContainer<User> persons = JPAContainerFactory.make(User.class,
    		MbpetUI.PERSISTENCE_UNIT);
	static JPAContainer<TestCase> testcases = JPAContainerFactory.make(TestCase.class,
    		MbpetUI.PERSISTENCE_UNIT);

	public static void create() {

		EntityManager em = Persistence
			.createEntityManagerFactory("mbpet")
			.createEntityManager();	
		
		// Let's have some data created with pure JPA
		// USERS
		em.getTransaction().begin();
		try {
//			em.createNativeQuery("DROP Testsession").executeUpdate();
//			em.createNativeQuery("DROP Testcase").executeUpdate();
//			em.createNativeQuery("DROP User").executeUpdate();
			em.createNativeQuery("DELETE FROM Model").executeUpdate();
			em.createNativeQuery("DELETE FROM Testsession").executeUpdate();
			em.createNativeQuery("DELETE FROM Testcase").executeUpdate();
			em.createNativeQuery("DELETE FROM User").executeUpdate();
		} catch (SecurityException | IllegalStateException e) {
		    e.printStackTrace();
		}	
		User user = new User("Jim", "Halpert", "jim.halpert", "passw0rd");
		User u2 = new User("Pam", "Halpert", "pam.halpert", "passw0rd");
		User u3 = new User("Dwight", "Schrute", "dwight.schrute", "passw0rd");
		em.persist(user);
		em.persist(u2);
		em.persist(u3);
		em.getTransaction().commit();
		
		
		// TEST CASES
		em.getTransaction().begin();
//		try {
//			em.createNativeQuery("DELETE FROM Testcase").executeUpdate();
//		} catch (SecurityException | IllegalStateException e) {
//		    e.printStackTrace();
//		}	
//		Object userid = persons.firstItemId();//  .getItemIds();
//		User user = persons.getItem(userid).getEntity();
//		User u2 = persons.getItem(persons.getIdByIndex(1)).getEntity();
//		User u3 = persons.getItem(persons.getIdByIndex(2)).getEntity();

		TestCase tcdashboard = new TestCase("gen dashboard", "dash decription", user);
		TestCase tcportal = new TestCase("gen portal", "portal decription", user);
		TestCase tc3 = new TestCase("gen talkpanel", "talkpanel decription", user);
		tcdashboard.setOwner(user);
		tcportal.setOwner(user);
		tc3.setOwner(user);

		TestCase tc4 = new TestCase("u2 talkpanel", "talkpanel decription", u2);
		TestCase tc5 = new TestCase("u2 portal", "talkpanel decription", u2);
		tc4.setOwner(u2);
		tc5.setOwner(u2);

		TestCase tc6 = new TestCase("u3 talkpanel", "talkpanel decription", u3);
		TestCase tc7 = new TestCase("u3 portal", "talkpanel decription", u3);
		tc6.setOwner(u3);
		tc7.setOwner(u3);

//		emjpa.persist(new TestCase("gen dashboard", "dash decription", user)); 
//		emjpa.persist(new TestCase("gen portal", "portal decription", user)); 
//		emjpa.persist(new TestCase("gen talkpanel", "talkpanel decription", user)); 
		em.persist(tcdashboard);
		em.persist(tcportal);
		em.persist(tc3);
		em.persist(tc4);
		em.persist(tc5);
		em.persist(tc6);
		em.persist(tc7);
		
		em.getTransaction().commit();
		
		em.getTransaction().begin();
		em.refresh(user);
		em.refresh(u2);
		em.refresh(u3);
		em.getTransaction().commit();
		
		
		// TEST SESSIONS
		em.getTransaction().begin();
//		try {
//			em.createNativeQuery("DELETE FROM Testsession").executeUpdate();
//		} catch (SecurityException | IllegalStateException e) {
//		    e.printStackTrace();
//		}	
//		Object caseid = testcases.firstItemId();//  .getItemIds();
//		Object lastcaseid = testcases.lastItemId();
//		TestCase testcase = testcases.getItem(caseid).getEntity();
//		TestCase lasttestcase = testcases.getItem(lastcaseid).getEntity();
		
		TestSession sess1 = new TestSession("dashboard session 1");
		TestSession sess2 = new TestSession("dashboard session 2");
		TestSession sess3 = new TestSession("dashboard session 3");
		TestSession sess4 = new TestSession("dashboard session 4");
		
		TestSession portal1 = new TestSession("portal session 1");
		TestSession portal2 = new TestSession("portal session 2");
		TestSession portal3 = new TestSession("portal session 3");
		TestSession portal4 = new TestSession("portal session 4");

		sess1.setParentcase(tcdashboard);
		sess2.setParentcase(tcdashboard);
		sess3.setParentcase(tcdashboard);
		sess4.setParentcase(tcdashboard);
		portal1.setParentcase(tcportal);
		portal2.setParentcase(tcportal);
		portal3.setParentcase(tcportal);
		portal4.setParentcase(tcportal);

		em.persist(sess1);
		em.persist(sess2);
		em.persist(sess3);
		em.persist(sess4);
		em.persist(portal1);
		em.persist(portal2);
		em.persist(portal3);
		em.persist(portal4);
		
//		emjpa.persist(new TestSession("dashboard session 1", testcase)); 
//		emjpa.persist(new TestSession("dashboard session 2", testcase)); 
//		emjpa.persist(new TestSession("dashboard session 3", testcase)); 
//
//		emjpa.persist(new TestSession("talkpanel session 1", lasttestcase)); 
//		emjpa.persist(new TestSession("talkpanel session 2", lasttestcase)); 
//		emjpa.persist(new TestSession("talkpanel session 3", lasttestcase)); 
		
		em.getTransaction().commit();
		
		em.getTransaction().begin();
		em.refresh(tcdashboard);
		em.refresh(tcportal);
//		List<TestSession> list = new ArrayList<TestSession>();
//		list.add(sess1);
//		list.add(sess2);
//		list.add(sess3);
//		tc1.setSessions(list);
//		emjpa.persist(tc1);
		em.getTransaction().commit();
		
		
		em.getTransaction().begin();		
		TestSession sess5 = new TestSession("dashboard session 5");
		TestSession sess6 = new TestSession("dashboard session 6");
		TestSession sess7 = new TestSession("dashboard session 7");
		sess5.setParentcase(tcdashboard);
		sess6.setParentcase(tcdashboard);
		sess7.setParentcase(tcdashboard);
		em.persist(sess5);	
		em.persist(sess6);	
		em.persist(sess7);	
		em.getTransaction().commit();
		
		em.getTransaction().begin();
		em.refresh(tcdashboard);
		em.getTransaction().commit();
		
		
		// MODELS
		em.getTransaction().begin();
		
		Model m1 = new Model("passive user", sess1, tcdashboard);
		Model m2 = new Model("active user", sess1, tcdashboard);
		Model m3 = new Model("aggressive user", sess1, tcdashboard);
		Model m4 = new Model("nonexistent user", sess1, tcdashboard);

		Model m21 = new Model("passive user", sess2, tcdashboard);
		Model m22 = new Model("active user", sess2, tcdashboard);
		Model m23 = new Model("aggressive user", sess2, tcdashboard);
		
		Model m31 = new Model("nonexistent user", portal1, tcportal);
		Model m32 = new Model("you get the point user", portal1, tcportal);
		em.persist(m1);
		em.persist(m2);
		em.persist(m3);
		em.persist(m4);
		
		em.persist(m21);
		em.persist(m22);
		em.persist(m23);
		
		em.persist(m31);
		em.persist(m32);
		em.getTransaction().commit();

		em.getTransaction().begin();
		em.refresh(sess1);
		em.refresh(sess2);
		em.refresh(portal1);
		em.refresh(tcdashboard);
		em.refresh(tcportal);
		em.getTransaction().commit();
		
//		EntityManager em = Persistence
//				.createEntityManagerFactory("MBPeT")
//				.createEntityManager();		
//		
//		em.getTransaction().begin();
//		Random r = new Random(0);
//		for (String o : officeNames) {
//			Department geoGroup = new Department();
//			geoGroup.setName(o);
//			for (String g : groupsNames) {
//				Department group = new Department();
//				group.setName(g);
//				Set<Person> gPersons = new HashSet<Person>();
//				
//				int amount = r.nextInt(15) + 1;
//				for (int i = 0; i < amount; i++) {
//					Person p = new Person();
//					p.setFirstName(fnames[r.nextInt(fnames.length)]);
//					p.setLastName(lnames[r.nextInt(lnames.length)]);
//					p.setCity(cities[r.nextInt(cities.length)]);
//					p.setPhoneNumber("+358 02 555 " + r.nextInt(10) + r.nextInt(10)
//							+ r.nextInt(10) + r.nextInt(10));
//					int n = r.nextInt(100000);
//					if (n < 10000) {
//						n += 10000;
//					}
//					p.setZipCode("" + n);
//					p.setStreet(streets[r.nextInt(streets.length)]);
//					p.setDepartment(group);
//					gPersons.add(p);
//					em.persist(p);
//				}
//				group.setParent(geoGroup);
//				group.setPersons(gPersons);
//				em.persist(group);
//			}
//			em.persist(geoGroup);
//		}
//
//		em.getTransaction().commit();
	}

}
