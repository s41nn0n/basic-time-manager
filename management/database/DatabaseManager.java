/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timemanagementapplication.management.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import timemanagementapplication.management.date.DateManager;
import timemanagementapplication.management.settings.GlobalSetting;

/**
 *
 * @author f
 */
public class DatabaseManager {

    private static ArrayList<String> createTable = new ArrayList<String>();
//    private static String createTableSequence = "CREATE SEQUENCE ITEMS_SEQ AS INT MAXVALUE 999999 CYCLE;";
    
    private static Connection conn = null;
    private static Statement stmt = null;
        
    public DatabaseManager(){
        if (GlobalSetting.DATABASE_DROP_TABLES)
            createTable.add(GlobalSetting.DATABASE_DROP_TABLE);

        createTable.add(GlobalSetting.DATABASE_CREATE_TABLE);
        
        createTable.add(GlobalSetting.DATABASE_CREATE_TABLE_LOGGING );

        createTable.add("CREATE SEQUENCE ITEMS_SEQ AS INT MAXVALUE 999999 CYCLE");
        createTable.add("create TRIGGER ITEMS_TRG \n" +
                        "BEFORE INSERT ON ACTIVATION_KEY_TYPE\n" +
                        "FOR EACH ROW\n" +
                        "BEGIN\n" +
                        "  <<COLUMN_SEQUENCES>>\n" +
                        "  BEGIN\n" +
                        "    IF :NEW.ID IS NULL THEN\n" +
                        "      SELECT ITEMS_SEQ.NEXTVAL INTO :NEW.ID FROM DUAL;\n" +
                        "    END IF;\n" +
                        "  END COLUMN_SEQUENCES;\n" +
                        "END;");

        try {
            conn = createDatabaseConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        insertITEM("Application Started");

    }
    
    private void startConnection() throws SQLException {
        boolean flagStartConnection = false;
        try {
//                Logger.getLogger
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, "startConnection()");
                if (conn == null || conn.equals(null) || conn.isClosed()){
                    Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, "flagStartConnection = true");
                    flagStartConnection = true;
                }
            } catch (Exception e) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, e);
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, "flagStartConnection = true");
                flagStartConnection = true;
            }
        
        if (flagStartConnection) {
            try {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, "Createing the Database");
                String driver = "org.apache.derby.jdbc.EmbeddedDriver";
                Class.forName(driver);
//                String url = GlobalSetting.DATABASE_URL;
//                                String url = "jdbc:derby:times;create=true;user=times;password=P@SSw0rD";

                Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, "Database variables set:\n\tDriver: " + driver + "\n\turl: "+ GlobalSetting.DATABASE_URL);
                try {
                    conn = DriverManager.getConnection(GlobalSetting.DATABASE_URL);
                } catch (Exception e) {
                    System.exit(0);
                }
                    
//                conn = DriverManager.getConnection(GlobalSetting.DATABASE_URL, GlobalSetting.USER, GlobalSetting.PASS);

                Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, "Connected to Database");
                
    //           whereAmI = "DataBase Lodaded";
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /*
     * The method creates a Connection object. Loads the embedded driver,
     * starts and connects to the database using the connection URL.
     */
    private Connection createDatabaseConnection()
            throws SQLException, ClassNotFoundException {
//           whereAmI = "Loading DB";
        startConnection();
        try {
            instanciateStatement();
            try {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, "Checking Tables");
                if (stmt == null)
                    Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, "stmt == null");

                ResultSet results = stmt.executeQuery("select * from ITEMS");
                if ((results.equals(null)) || (GlobalSetting.DATABASE_DROP_TABLES == true)){
                    Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, "results.equals(null)");
                    createTables();
                }
            } catch (SQLSyntaxErrorException sqle) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, "createDatabaseConnection" + sqle.getMessage());
//                                Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, "createDatabaseConnection ERROR", e);
                createTables();
            } catch (Exception e) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, e);
            }
                
        } catch (Exception e) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, e);

        }
            
        
        return conn;
    }
    
    private void createTables() {
        Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, "Create Tables");
        try {
            Iterator<String> it = createTable.iterator();
            while(it.hasNext()){
                String sqlS = it.next();
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, sqlS);
//                System.out.println(sqlS);
                if (stmt == null)
                    Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, "----------------------------------------------NULL");
                try {
                    stmt.execute(sqlS);
                } catch (Exception e) {
                }
            }
            //:TODO: Still need to close the connection
            //stmt.close();
        } catch (Exception e) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private void instanciateStatement(){
        Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, "instanciateStatement() Called");
        boolean flagToCreate = false;
        try {
            if (stmt == null || stmt.equals(null)) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, "stmt.equals(null)");
                flagToCreate = true;
            } else if (stmt.isClosed()){
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, "stmt.isClosed()");
                flagToCreate = true;
            } 
        } catch (Exception e) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, e);
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, "Exception <flagToCreate = true>");
            flagToCreate = true;
        }
        
        if (flagToCreate){
            try {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, "stmt = conn.createStatement()");
                
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, "before -- startConnection();");
                startConnection();
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, "after -- startConnection();");

                stmt = conn.createStatement();
                
                if (stmt == null) 
                    Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, "stmt == null");

                else
                    Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, "stmt != null");

            } catch (Exception e) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, e);
            }
                
        }
    }
    
    public Statement getStatement(){
        instanciateStatement();
        return stmt;
    }
    
    public void insertITEM(String comment){
        try {
            //Needs to check length and split if longer than 4000Chars
//            stmt.executeQuery
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, GlobalSetting.DATABASE_INSERT_INTO_ITEMS);

            PreparedStatement pstmt = conn.prepareStatement(GlobalSetting.DATABASE_INSERT_INTO_ITEMS); 
//            pstmt.setInt(1, selectNextItemSeq());
            pstmt.setDate(1, new java.sql.Date(DateManager.getCurrentTimeStampDate().getTime()));            
            pstmt.setString(2, comment);
            
            
            int count = pstmt.executeUpdate();
            
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, "executeUpdate(): " + count);

            pstmt.close();
            
        } catch (Exception e) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void insertItemHidden(String comment){
        if (comment.length() > 4000) {
            insertItemHidden(comment.substring(0, 3999));
            insertItemHidden(comment.substring(4000, comment.length()-1));
        } else {
            try {
                //Needs to check length and split if longer than 4000Chars
    //            stmt.executeQuery
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, GlobalSetting.DATABASE_INSERT_HIDDEN_INTO_ITEMS);

                PreparedStatement pstmt = conn.prepareStatement(GlobalSetting.DATABASE_INSERT_HIDDEN_INTO_ITEMS); 
    //            pstmt.setInt(1, selectNextItemSeq());
                pstmt.setDate(1, new java.sql.Date(DateManager.getCurrentTimeStampDate().getTime()));            
                pstmt.setString(2, comment);


                int count = pstmt.executeUpdate();

                Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, "executeUpdate(): " + count);

                pstmt.close();

            } catch (Exception e) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, e);
            }
        }

    }
    
    public void Logging(String comment){
        if (comment.length() > 4000) {
            insertItemHidden(comment.substring(0, 3999));
            insertItemHidden(comment.substring(4000, comment.length()-1));
        } else {
            try {
                //Needs to check length and split if longer than 4000Chars
    //            stmt.executeQuery
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, GlobalSetting.DATABASE_INSERT_LOGGING);

                PreparedStatement pstmt = conn.prepareStatement(GlobalSetting.DATABASE_INSERT_LOGGING); 
    //            pstmt.setInt(1, selectNextItemSeq());
                pstmt.setDate(1, new java.sql.Date(DateManager.getCurrentTimeStampDate().getTime()));            
                pstmt.setString(2, comment);


                int count = pstmt.executeUpdate();

                Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, "executeUpdate(): " + count);

                pstmt.close();

            } catch (Exception e) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
    public ResultSet getReport(){
        try {
            //Needs to check length and split if longer than 4000Chars
            //            stmt.executeQuery
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, GlobalSetting.DATABASE_SELECT_ITEMS);

            ResultSet rs = stmt.executeQuery(GlobalSetting.DATABASE_SELECT_ITEMS); 
            return rs;
            
        } catch (Exception e) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
    
    public static void deleteAll(){
        try {
            //Needs to check length and split if longer than 4000Chars
            //            stmt.executeQuery
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, GlobalSetting.DATABASE_CLEAR_ITEMS);

            int rs = stmt.executeUpdate(GlobalSetting.DATABASE_CLEAR_ITEMS); 
            
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, "Items Cleared: " + rs);
        } catch (Exception e) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
