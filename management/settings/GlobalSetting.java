/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timemanagementapplication.management.settings;

/**
 *
 * @author f
 */
public class GlobalSetting {
    public static boolean Production = false;    
    public static boolean Debugging = true;
    public static boolean DATABASE_DROP_TABLES = false;
//    public static boolean DATABASE_LOGGING_DROP_TABLES = false;


    public static final String USER = "TIMES";
    public static final String PASS = "P@SSw0rD";
    
    public static final String DATABASE_INSERT_INTO_ITEMS = "INSERT INTO ITEMS(DATE_CREATED, TEXT, SHOW) VALUES(?, ?, 1)";      
    public static final String DATABASE_INSERT_HIDDEN_INTO_ITEMS = "INSERT INTO ITEMS(DATE_CREATED, TEXT, SHOW) VALUES(?, ?, 0)";   
    public static final String DATABASE_INSERT_LOGGING = "INSERT INTO LOGGING(DATE_CREATED, TEXT) VALUES(?, ?)";      

    
    public static final String DATABASE_CREATE_TABLE = "CREATE TABLE ITEMS (DATE_CREATED DATE NOT NULL, TEXT VARCHAR(4000) NOT NULL, SHOW INT)";
    public static final String DATABASE_CREATE_TABLE_LOGGING = "CREATE TABLE LOGGING (DATE_CREATED DATE NOT NULL, TEXT VARCHAR(4000) NOT NULL)";

    
    public static final String DATABASE_DROP_TABLE = "DROP TABLE ITEMS";
    
    public static final String DATABASE_SELECT_ITEMS = "SELECT * FROM ITEMS WHERE SHOW = 1";
    public static final String DATABASE_CLEAR_ITEMS = "DELETE FROM ITEMS WHERE 1=1";

    
    public static final String DATABASE_ITEM_SEQ = "SELECT NEXT VALUE FOR ITEMS_SEQ";

    
    public static final String DATABASE_URL = "jdbc:derby:/TIMES;create=true";

    public static final String DATAFORMAT = "yyyy-MM-dd HH:mm:ss";

    public static int FORM_WIDTH = 100;
    public static int FORM_HEIGHT = 20;

}
