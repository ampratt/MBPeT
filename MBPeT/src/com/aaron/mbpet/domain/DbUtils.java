package com.aaron.mbpet.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aaron.mbpet.data.DemoDataGenerator.SaveObject2Database;
import com.google.gwt.thirdparty.guava.common.collect.ImmutableMap;

public class DbUtils {

	static String[] propIds = new String[] {"settings_file", "ramp_list", "TargetResponseTime"};
	static Map<String, String> jObjectTitleFields = ImmutableMap.of(
							"ramp_list", "ramp_object_name", 
							"TargetResponseTime", "responseTime_object_name");
	
	public static void commitUpdateToDb(Object javaObject, Parameters parameters, String targetDbField) {
        Connection connection = null;
        int persistObjectID = -1;
        
        try {
            connection = getConnection();

//	        		ArrayList<Integer> internal = new ArrayList<Integer>();
//	        		internal.addAll(Arrays.asList(0,0));
//	        		array.addAll((Collection<? extends ArrayList<Integer>>) Arrays.asList(internal,Arrays.asList(250,400), Arrays.asList(400, 600)));
//                listToSaveInDB.add(array);
            
            if (targetDbField.equals(propIds[0])){
            	String settings = (String) javaObject;
            	System.out.println("Jav object: " + settings.getClass() + "\ntarget db field: " + targetDbField);  
            	updateBlob(connection, settings, parameters, targetDbField);
//            	System.out.println(settings + " Object is saved sucessfully\n");  
            	
            } else if (targetDbField.equals(propIds[1])){
            	ArrayList<ArrayList<Integer>> arraylist = (ArrayList<ArrayList<Integer>>) javaObject;
            	System.out.println("Jav object: " + arraylist.toString() + "\ntarget db field: " + targetDbField);  
            	updateBlob(connection, arraylist, parameters, targetDbField);
            	System.out.println(arraylist + " Object is saved sucessfully\n");  
            	
            } else if(targetDbField.equals(propIds[2])){
            	Map<String, HashMap<String, Double>> responsetimesHashMap = 
            				(Map<String, HashMap<String, Double>>) javaObject;
            	System.out.println("Jav object: " + responsetimesHashMap.toString() + "\ntarget db field: " + targetDbField);  
            	updateBlob(connection, responsetimesHashMap, parameters, targetDbField);
            	System.out.println(responsetimesHashMap + " Object is saved sucessfully\n");            	
            } 
            // commit to db
            //persistObjectID = 
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
	
	public static Object readFromDb(int persistObjectID) {	//ArrayList<ArrayList<Integer>>
        byte[] serializedRetrievedArrayObject = null;
        ArrayList<ArrayList<Integer>> dataListFromDB = null;
        Connection connection = null;
        Object javaObject = null;
        try {
            connection = getConnection();
			// retrieve from db
            serializedRetrievedArrayObject = getBlob(connection, persistObjectID);

            ObjectInputStream objectInputStream = null;
            if (serializedRetrievedArrayObject != null)
                objectInputStream = new ObjectInputStream(
                                            new ByteArrayInputStream(
                                            		serializedRetrievedArrayObject));

//            Object retrievingObject = objectInputStream.readObject();
//            List<Object> dataListFromDB = (List<Object>) retrievingObject;
            
            javaObject = objectInputStream.readObject();
//            if (targetDbField.equals(propIds[0])) {
//            	// ramp_list
//            	dataListFromDB = (ArrayList<ArrayList<Integer>>) objectInputStream.readObject();
//            	System.out.println("Retrieved ArrayList :-> " + dataListFromDB.toString());            	
//            	
//            } else if (targetDbField.equals(propIds[1])){
//            	// TargetResponseTime
//            	Map<String, HashMap<String, Double>> responsetimesHashMap = 
//            			(Map<String, HashMap<String, Double>>) objectInputStream.readObject();
//            }
//
//
//            
//            for (Object object : dataListFromDB) {
//            	System.out.println("Retrieved Data is :-> " + object.toString());
//            }

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
        
        return javaObject;	//dataListFromDB;
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
     private static void updateBlob(Connection con, Object javaObject2Persist, Parameters parameters, 
    		 				String targetDbField) {

            byte[] byteArray = null;
            PreparedStatement preparedStatement = null;
            String SQLQUERY_TO_SAVE_JAVAOBJECT = String.format("Update parameters " +
            											"SET %s = ? " +//        													", %s = ? " +
    														"WHERE id = ?", 
    														targetDbField); //, jObjectTitleFields.get(targetDbField)); 	
//            "Update parameters SET ramp_list = ?, ramp_object_name = ? WHERE id = ?"; 
            		//"INSERT INTO parameters(ramp_list) VALUES (?)";
            int persistedObjectID = -1;
            try {

                byteArray = convertObjectToByteArray(javaObject2Persist);
                preparedStatement = con.prepareStatement(
                                        SQLQUERY_TO_SAVE_JAVAOBJECT,
                                        PreparedStatement.RETURN_GENERATED_KEYS);
//                preparedStatement.setString(1, targetDbField);
                preparedStatement.setBytes(1, byteArray);
//                preparedStatement.setString(2, jObjectTitleFields.get(targetDbField));
//                preparedStatement.setString(2, javaObject2Persist.getClass().getName());
                preparedStatement.setInt(2, parameters.getId());
                System.out.println("Prep state:-> " + preparedStatement.toString());
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
            String SQLQUERY_TO_READ_JAVAOBJECT= "SELECT settings_file FROM parameters WHERE id = ?;";	//"SELECT ramp_list FROM parameters WHERE id = ?;";
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
