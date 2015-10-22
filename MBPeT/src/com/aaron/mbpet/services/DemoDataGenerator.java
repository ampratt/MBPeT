package com.aaron.mbpet.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javassist.bytecode.stackmap.BasicBlock.Catch;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.transaction.*;

import org.apache.commons.io.IOUtils;

import com.aaron.mbpet.MbpetUI;
import com.aaron.mbpet.domain.DbUtils;
import com.aaron.mbpet.domain.Model;
import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.domain.AverageMax;
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

	public static void create() throws FileNotFoundException {

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
			em.createNativeQuery("DELETE FROM Parameters").executeUpdate();
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
		
		Model m1 = new Model("passive_user", sess1, tcdashboard);
		Model m2 = new Model("active_user", sess1, tcdashboard);
		Model m3 = new Model("aggressive_user", sess1, tcdashboard);
		Model m4 = new Model("nonexistent_user", sess1, tcdashboard);

		Model m21 = new Model("passive_user", sess2, tcdashboard);
		Model m22 = new Model("active_user", sess2, tcdashboard);
		Model m23 = new Model("aggressive_user", sess2, tcdashboard);
		
		Model m31 = new Model("nonexistent_user", portal1, tcportal);
		Model m32 = new Model("you_get_the_point_user", portal1, tcportal);
		
//		String dotmodel = "";
//		try{
//			dotmodel = FileUtils.readFileToString(
//					new File("C:/dev/git/alternate/mbpet/MBPeT/WebContent/META-INF/output/dot-output.dot"), 
//					Charset.defaultCharset());
//			System.out.println(dotmodel);
//		} catch(IOException e) {
//			
//		}
		File file = new File("C:/dev/git/alternate/mbpet/MBPeT/WebContent/META-INF/output/dot-output.dot");
		String dotmodel = "";
//		try {
//			dotmodel = new Scanner(file).useDelimiter("\\A").next();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		InputStream is = new FileInputStream(file);
		try {
			dotmodel = IOUtils.toString(is, Charset.defaultCharset());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		m1.setDotschema(dotmodel);
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
		
		
		// PARAMETERS - ResponseTimes
		em.getTransaction().begin();

		Parameters p7 = new Parameters(sess7);
		Parameters p6 = new Parameters(sess6);
		Parameters p5 = new Parameters(sess5);
		Parameters p4 = new Parameters(sess4);
		Parameters p3 = new Parameters(sess3);
		Parameters p2 = new Parameters(sess2);
		Parameters p1 = new Parameters(sess1);

		Parameters p21 = new Parameters(portal1);
		Parameters p22 = new Parameters(portal2);
		Parameters p23 = new Parameters(portal3);
		Parameters p24 = new Parameters(portal4);

//		Parameters p1 = new Parameters("google.com", 30, 3, 3, 0.0, sess7);
//		Parameters p2 = new Parameters("facebook.com", 60, 3, 3, 0.5, sess7);
//		Parameters p3 = new Parameters("twitter.com", 90, 3, 3, 1.5,sess6);
//		Parameters p4 = new Parameters("apple.com", 120, 3, 3, 0.8, sess6);
		em.persist(p1);
		em.persist(p2);
		em.persist(p3);
		em.persist(p4);
		em.persist(p5);
		em.persist(p6);
		em.persist(p7);
		em.persist(p21);
		em.persist(p22);
		em.persist(p23);
		em.persist(p24);
	
		// Response times	
//		HashMap<String, Double> map = new HashMap<String, Double>();
//		map.put("average", 0.5);
//		map.put("max", 1.0);
////		map.put("average", 0.7);
////		map.put("max", 1.2);
////		AverageMax<String, Double> rt1 = new AverageMax<String, Double>(map);
////		Map<String, AverageMax<String, Double>> tr = new HashMap<String, AverageMax<String,Double>>();
//		
//		Map<String, HashMap<String, Double>> tr = new HashMap<String, HashMap<String,Double>>();
////		tr.put("search_on_google(car)", map);
		
		// Iterate all key/value pairs
//		for (Entry<String, Double> entry  : map.entrySet()) {
//			System.out.println(entry.getKey() + " - " + entry.getValue());
//			tr.put("search_on_google(car)", (Map<String, Double>) entry);
//			
//		}
//		tr.put("search_on_google(car)", map);
//		p1.setTargetResponsTime(tr);

		
		//		em.persist(rt1);
//		em.persist(p1);
		em.getTransaction().commit();
		
		
		em.getTransaction().begin();
		em.refresh(sess7);
		em.refresh(sess6);
		em.refresh(sess5);
		em.refresh(sess4);
		em.refresh(sess3);
		em.refresh(sess2);
		em.refresh(sess1);
		em.refresh(portal1);
		em.refresh(portal2);
		em.refresh(portal3);
		em.refresh(portal4);
		em.getTransaction().commit();
		
//		// ramp list
//		int[][] r1 = {{0,0},{250,400}};
//		ArrayList<ArrayList<Integer>> arl = new ArrayList<ArrayList<Integer>>();
//		ArrayList<Integer> internal = new ArrayList<Integer>();
//		internal.addAll(Arrays.asList(0,0));
//		arl.addAll((Collection<? extends ArrayList<Integer>>) Arrays.asList(internal,Arrays.asList(250,400), Arrays.asList(400, 600)));
//		//Long objectId = 
////		SaveObject2Database.commitToDb(arl, p1);
//		DbUtils.commitUpdateToDb(arl, p1, "ramp_list");	
//		
//		// target response times
//		HashMap<String, Double> map = new HashMap<String, Double>();
//		map.put("average", 0.5);
//		map.put("max", 1.0);
////		map.put("average", 0.7);
////		map.put("max", 1.2);
////		AverageMax<String, Double> rt1 = new AverageMax<String, Double>(map);
////		Map<String, AverageMax<String, Double>> tr = new HashMap<String, AverageMax<String,Double>>();
//		Map<String, HashMap<String, Double>> tr = new HashMap<String, HashMap<String,Double>>();
//		tr.put("search_on_google(car)", map);
////		DbUtils.commitUpdateToDb(tr, p1, "TargetResponseTime");	
//
//		
//		// read back from db to confirm
////		List<Object> dataListFromDB = SaveObject2Database.readFromDb(p1.getId());
//		
//		em.refresh(p1);
//		em.refresh(sess7);
//		em.refresh(sess6);
//		em.getTransaction().commit();
		
	}
	
	
//    /** SERIALIZING - This method will help to convert any object into byte array*/            
//    private static byte[] convertObjectToByteArray(Object obj) throws IOException {
//            ByteArrayOutputStream byteos = new ByteArrayOutputStream();
//            ObjectOutputStream objectout = new ObjectOutputStream(byteos);
//            objectout.writeObject(obj);
//            
////            byte[] blob = byteos.toByteArray();
//            
//            return byteos.toByteArray();
//    }
	
	
	
	
	public static class SaveObject2Database {

		private static void commitToDb(ArrayList<ArrayList<Integer>> arraylist, Parameters parameters) {
            Connection connection = null;
            int persistObjectID = -1;
            
            try {
                connection = SaveObject2Database.getConnection();

//                List<Object> listToSaveInDB = new ArrayList<Object>();
//                listToSaveInDB.add(arraylist);
//                
//	        		ArrayList<ArrayList<Integer>> array = new ArrayList<ArrayList<Integer>>();
//	        		ArrayList<Integer> internal = new ArrayList<Integer>();
//	        		internal.addAll(Arrays.asList(0,0));
//	        		array.addAll((Collection<? extends ArrayList<Integer>>) Arrays.asList(internal,Arrays.asList(250,400), Arrays.asList(400, 600)));
//                listToSaveInDB.add(array);

                // commit to db
                //persistObjectID = 
                saveBlob(connection, arraylist, parameters);
                System.out.println(arraylist + " Object is saved sucessfully\n");
            }catch (Exception e) {
                e.printStackTrace();
		    } finally {
		        try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		    }
            
//            return persistObjectID;
		}
		
		private static List<Object> readFromDb(int persistObjectID) {
            byte[] serializedRetrievedArrayObject = null;
            List<Object> dataListFromDB = null;
            Connection connection = null;
            try {
                connection = SaveObject2Database.getConnection();
				// retrieve from db
	            serializedRetrievedArrayObject = getBlob(connection, persistObjectID);
	
	            ObjectInputStream objectInputStream = null;
	            if (serializedRetrievedArrayObject != null)
	                objectInputStream = new ObjectInputStream(
	                                            new ByteArrayInputStream(
	                                            		serializedRetrievedArrayObject));
	
	//            Object retrievingObject = objectInputStream.readObject();
	//            List<Object> dataListFromDB = (List<Object>) retrievingObject;
	            
	            dataListFromDB = (List<Object>) objectInputStream.readObject();
	            System.out.println("Retrieved ArrayList :-> " + dataListFromDB.toString());
	            
	            for (Object object : dataListFromDB) {
	            	System.out.println("Retrieved Data is :-> " + object.toString());
	            }
	
	            System.out.println("Successfully retrieved java Object from Database");
	
		        } catch (Exception e) {
		                    e.printStackTrace();
		        } finally {
		            try {
						connection.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
            
            return dataListFromDB;
		}
           	
		
		
        /** This method will help to get mysql connection from database*/   
        private static Connection getConnection() throws Exception {
                String driver = "com.mysql.jdbc.Driver";
                String url = "jdbc:mysql://localhost:3306/mbpetwampdb";
                String username = "root";
                String password = "";
                Class.forName(driver);
                Connection con = DriverManager.getConnection(url, username, password);
                
                return con;
        }

        /** SERIALIZING - This method will help to convert any object into byte array*/            
        private static byte[] convertObjectToByteArray(Object obj) throws IOException {
                ByteArrayOutputStream byteos = new ByteArrayOutputStream();
                ObjectOutputStream objectout = new ObjectOutputStream(byteos);
                objectout.writeObject(obj);
                
//                byte[] blob = byteos.toByteArray();
                
                return byteos.toByteArray();
        }


        /** This method will help to save java objects into database*/             
         private static void saveBlob(Connection con, Object javaObject2Persist, Parameters parameters) {

                byte[] byteArray = null;
                PreparedStatement preparedStatement = null;
                String SQLQUERY_TO_SAVE_JAVAOBJECT = "Update parameters " +
                											"SET ramp_list = ?, " +
            													"ramp_object_name = ? " +
        														"WHERE id = ?"; 	
                		//"INSERT INTO parameters(ramp_list) VALUES (?)";
                int persistedObjectID = -1;
                try {

                    byteArray = convertObjectToByteArray(javaObject2Persist);
                    preparedStatement = con.prepareStatement(
                                            SQLQUERY_TO_SAVE_JAVAOBJECT,
                                            PreparedStatement.RETURN_GENERATED_KEYS);
                    preparedStatement.setBytes(1, byteArray);
                    preparedStatement.setString(2, javaObject2Persist.getClass().getName());
                    preparedStatement.setInt(3, parameters.getId());
                    preparedStatement.executeUpdate();

                    System.out.println("Query - " + SQLQUERY_TO_SAVE_JAVAOBJECT + 
                                       " is successfully executed for Java object serialization ");

                    //Trying to get the Generated Key
                    ResultSet rs = preparedStatement.getGeneratedKeys();

                    if (rs.next()) {
                        persistedObjectID = rs.getInt(1);
                        System.out.println("Object ID while saving the binary object is -> "
                                           + persistedObjectID);
                    }

                    preparedStatement.close();
                    
                } catch (SQLException e) {
                            e.printStackTrace();
                } catch (Exception e) {
                            e.printStackTrace();
                }
                
//                return persistedObjectID;
        }
         

         
		/** DESERIALIZING - This method will help to read java objects from database*/                
		private static byte[] getBlob(Connection con, int objectId) {
                String SQLQUERY_TO_READ_JAVAOBJECT= "SELECT ramp_list FROM parameters WHERE id = ?;";
                PreparedStatement pstmt = null;
                ResultSet resultSet = null;
                Blob blob = null;
                byte[] serializedBytes = null;

                try {         
                    pstmt = con.prepareStatement(SQLQUERY_TO_READ_JAVAOBJECT);
                    System.out.println("Reading the saved Object from the database where the object Id is: -> " + objectId);
                    pstmt.setInt(1, objectId);

                    resultSet = pstmt.executeQuery();
                    while (resultSet.next()) {
                        blob = resultSet.getBlob(1);
                    }
                    serializedBytes = blob.getBytes(1, (int) (blob.length()));

                } catch (SQLException e) {
                            e.printStackTrace();
                } catch (Exception e) {
                            e.printStackTrace();
                }
                
                return serializedBytes;
        }

		
//		
//		private static void init(String name, Double iq){
//            Connection connection = null;
//            byte[] serializedRetrievedArrayObject = null;
//            try {
//                connection = SaveObject2Database.getConnection();
//
//                List<Object> listToSaveInDB = new ArrayList<Object>();
//                listToSaveInDB.add(new Date());
//                listToSaveInDB.add(new String(name));
//                listToSaveInDB.add(new Double(iq));
//
//                // commit to db
//                long persistObjectID = saveBlob(connection, listToSaveInDB);
//                System.out.println(listToSaveInDB + " Object is saved sucessfully\n");
//
//                // retrieve from db
//                serializedRetrievedArrayObject = getBlob(connection, persistObjectID);
//
//                ObjectInputStream objectInputStream = null;
//                if (serializedRetrievedArrayObject != null)
//                    objectInputStream = new ObjectInputStream(
//                                                new ByteArrayInputStream(
//                                                		serializedRetrievedArrayObject));
//
////                Object retrievingObject = objectInputStream.readObject();
////                List<Object> dataListFromDB = (List<Object>) retrievingObject;
//                
//                List<Object> dataListFromDB = (List<Object>) objectInputStream.readObject();
//                
//                for (Object object : dataListFromDB) {
//                	System.out.println("Retrieved Data is :-> " + object.toString());
//                }
//
//                System.out.println("Successfully retrieved java Object from Database");
//
//            } catch (Exception e) {
//                        e.printStackTrace();
//            } finally {
//                try {
//					connection.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//            }
//		}
//		
//        @SuppressWarnings("unchecked")
//        public void main(String args[]) throws Exception {
//                Connection connection = null;
//                byte[] serializedRetrievedArrayObject = null;
//                try {
//                    connection = getConnection();
//
//                    List<Object> listToSaveInDB = new ArrayList<Object>();
//                    listToSaveInDB.add(new Date());
//                    listToSaveInDB.add(new String("KUMAR GAURAV"));
//                    listToSaveInDB.add(new Integer(55));
//
//                    long persistObjectID = saveBlob(connection, listToSaveInDB);
//                    System.out.println(listToSaveInDB + " Object is saved sucessfully");
//
//                    serializedRetrievedArrayObject = getBlob(connection, persistObjectID);
//
//                    ObjectInputStream objectInputStream = null;
//                    if (serializedRetrievedArrayObject != null)
//                        objectInputStream = new ObjectInputStream(
//                                                    new ByteArrayInputStream(
//                                                    		serializedRetrievedArrayObject));
//
//                    Object retrievingObject = objectInputStream.readObject();
//
//                    List<Object> dataListFromDB = (List<Object>) retrievingObject;
//                    for (Object object : dataListFromDB) {
//                    	System.out.println("Retrieved Data is :-> " + object.toString());
//                    }
//
//                    System.out.println("Successfully retrieved java Object from Database");
//
//                } catch (Exception e) {
//                            e.printStackTrace();
//                } finally {
//                            connection.close();
//                }
//        }
	            
	}
	


}
