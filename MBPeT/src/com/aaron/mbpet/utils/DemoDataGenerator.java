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

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import com.aaron.mbpet.domain.User;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;


public class DemoDataGenerator {

	final static String[] fnames = { "Peter", "Alice", "Joshua", "Mike",
			"Olivia", "Nina", "Alex", "Rita", "Dan", "Umberto", "Henrik",
			"Rene", "Lisa", "Marge" };
	final static String[] lnames = { "Smith", "Gordon", "Simpson", "Brown",
			"Clavel", "Simons", "Verne", "Scott", "Allison", "Gates",
			"Rowling", "Barks", "Ross", "Schneider", "Tate" };


	public static void create() {

		EntityManager emjpa = Persistence
			.createEntityManagerFactory("mbpet")
			.createEntityManager();	
		
		// Let's have some data created with pure JPA
//		EntityManager emjpa = JPAContainerFactory.
//		    createEntityManagerForPersistenceUnit("MBPeT");
		emjpa.getTransaction().begin();
//		emjpa.createQuery("DELETE FROM Person p").executeUpdate();
		emjpa.persist(new User("Jim", "Halpert", "jim.halpert", "passw0rd"));
		emjpa.persist(new User("Pam", "Halpert", "pam.halpert", "passw0rd"));
		emjpa.persist(new User("Dwight", "Schrute", "dwight.schrute", "passw0rd"));
		emjpa.getTransaction().commit();
		
		
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
