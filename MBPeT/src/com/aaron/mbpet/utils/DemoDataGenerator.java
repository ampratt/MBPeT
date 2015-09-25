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
package com.aaron.mbpet.utils;

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
			em.createNativeQuery("DELETE FROM User").executeUpdate();
		} catch (SecurityException | IllegalStateException e) {
		    e.printStackTrace();
		}	
		em.persist(new User("Jim", "Halpert", "jim.halpert", "passw0rd"));
		em.persist(new User("Pam", "Halpert", "pam.halpert", "passw0rd"));
		em.persist(new User("Dwight", "Schrute", "dwight.schrute", "passw0rd"));
		em.getTransaction().commit();
		
		
		// TEST CASES
		em.getTransaction().begin();
		try {
			em.createNativeQuery("DELETE FROM Testcase").executeUpdate();
		} catch (SecurityException | IllegalStateException e) {
		    e.printStackTrace();
		}	
		Object userid = persons.firstItemId();//  .getItemIds();
		User user = persons.getItem(userid).getEntity();
		TestCase tc1 = new TestCase("gen dashboard", "dash decription", user);
		TestCase tc2 = new TestCase("gen portal", "portal decription", user);
		TestCase tc3 = new TestCase("gen talkpanel", "talkpanel decription", user);
//		emjpa.persist(new TestCase("gen dashboard", "dash decription", user)); 
//		emjpa.persist(new TestCase("gen portal", "portal decription", user)); 
//		emjpa.persist(new TestCase("gen talkpanel", "talkpanel decription", user)); 
		em.persist(tc1);
		em.persist(tc2);
		em.persist(tc3);
		em.getTransaction().commit();
		
		
		// TEST SESSIONS
		em.getTransaction().begin();
		try {
			em.createNativeQuery("DELETE FROM Testsession").executeUpdate();
		} catch (SecurityException | IllegalStateException e) {
		    e.printStackTrace();
		}	
		Object caseid = testcases.firstItemId();//  .getItemIds();
		Object lastcaseid = testcases.lastItemId();
		TestCase testcase = testcases.getItem(caseid).getEntity();
		TestCase lasttestcase = testcases.getItem(lastcaseid).getEntity();
		
		TestSession sess1 = new TestSession("dashboard session 1");
		TestSession sess2 = new TestSession("dashboard session 2");
		TestSession sess3 = new TestSession("dashboard session 3");
		sess1.setParentcase(testcase);
		sess2.setParentcase(testcase);
		sess3.setParentcase(testcase);
		em.persist(sess1);
		em.persist(sess2);
		em.persist(sess3);
		
//		emjpa.persist(new TestSession("dashboard session 1", testcase)); 
//		emjpa.persist(new TestSession("dashboard session 2", testcase)); 
//		emjpa.persist(new TestSession("dashboard session 3", testcase)); 
//
//		emjpa.persist(new TestSession("talkpanel session 1", lasttestcase)); 
//		emjpa.persist(new TestSession("talkpanel session 2", lasttestcase)); 
//		emjpa.persist(new TestSession("talkpanel session 3", lasttestcase)); 
		
		em.getTransaction().commit();
		
		em.getTransaction().begin();
		em.refresh(tc1);
//		List<TestSession> list = new ArrayList<TestSession>();
//		list.add(sess1);
//		list.add(sess2);
//		list.add(sess3);
//		tc1.setSessions(list);
//		emjpa.persist(tc1);
		em.getTransaction().commit();
		
		
		em.getTransaction().begin();		
		TestSession sess4 = new TestSession("dashboard session 4");
		sess4.setParentcase(tc1);
		em.persist(sess4);	
		em.getTransaction().commit();
		
		em.getTransaction().begin();
		em.refresh(tc1);
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
