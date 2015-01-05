/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timemanagementapplication;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window.Type;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.scene.layout.StackPane;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import timemanagementapplication.management.database.DatabaseManager;
import timemanagementapplication.management.date.DateManager;
import timemanagementapplication.management.settings.GlobalSetting;

/**
 *
 * @author f
 */
public class TimeManagementApplication extends JApplet {
    
    private static final int JFXPANEL_WIDTH_INT = 300;
    private static final int JFXPANEL_HEIGHT_INT = 250;
    private static JFXPanel fxContainer;
    
    private static String whereAmI = "";
    
    private DatabaseManager databaseManager = new DatabaseManager();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            
            
            
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception e) {
                }
                
                JFrame frame = new JFrame("Test");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                frame.h
                
                frame.setType(javax.swing.JFrame.Type.UTILITY);
                frame.setUndecorated(true);
                
//                frame.setFocusableWindowState(false);
                frame.setFocusable(false);
                frame.setAlwaysOnTop(true);
                
                JApplet applet = new TimeManagementApplication();
                applet.init();
                
                frame.setContentPane(applet.getContentPane());
                frame.pack();
                
                frame.setSize(GlobalSetting.FORM_WIDTH, GlobalSetting.FORM_HEIGHT);
//                frame.setLocationRelativeTo(null);
                
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
                Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
                int x = (int) rect.getMaxX() - frame.getWidth();
                int y = (int) rect.getMaxY() - frame.getHeight();
                
                Logger.getLogger(TimeManagementApplication.class.getName()).log(Level.INFO, "Setting Height and Width:\n\tHeight: " + y + "\n\tWidth: "+ x);

                frame.setVisible(true);

                applet.start();
            }
        });
        
    }
    
    @Override
    public void init() {
        fxContainer = new JFXPanel();
        fxContainer.setPreferredSize(new Dimension(JFXPANEL_WIDTH_INT, JFXPANEL_HEIGHT_INT));
        add(fxContainer, BorderLayout.CENTER);
        // create JavaFX scene
        Platform.runLater(new Runnable() {
            
            @Override
            public void run() {
                createScene();
            }
        });
    }
    
    private void createScene() {
//        Button btn = new Button();
//        Button
//        btn.setText("Say 'Hello World'");
//        btn.setOnAction(new EventHandler<ActionEvent>() {
//            
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println("Hello World!");
//            }
//        });
        TextArea txtArea = new TextArea("");
//        txtArea.scroll
        txtArea.setWrapText(true);
        txtArea.setScrollLeft(0);
        txtArea.setScrollTop(0);
//        txtArea.setBackground(Color.red);
        
        txtArea.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent keyEvent) {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                    txtArea.clear();
                }
            }
            
        });
        
        txtArea.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent keyEvent) {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                    String text = txtArea.getText();
                    System.out.println(DateManager.getCurrentTimeStampString() + " - " + text);
//                    Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, null, "Inserting - " + DateManager.getCurrentTimeStamp() + " - " + text);
                    switch(text.charAt(0)){
                        case '~':  drawReport();
                            break;
                        case '§':  databaseManager.deleteAll();
                            break;
                        case '☺':  System.exit(0);
                            break;
                        default: databaseManager.insertITEM(text);
                            break;
                    } 
                }
            }
            
        });
        
        StackPane root = new StackPane();
//        root.getChildren().add(btn);
        root.getChildren().add(txtArea);

        fxContainer.setScene(new Scene(root));
    }
    
    private void drawReport(){
        Logger.getLogger(TimeManagementApplication.class.getName()).log(Level.INFO, "Drawing Report");

        try {
            databaseManager.insertItemHidden(":DREW REPORT:");
            
            File file = new File("Report-" + DateManager.getCurrentTimeStampString().replaceAll("( |:)", "-") + ".report");
            

            if (!file.exists()) {
                Logger.getLogger(TimeManagementApplication.class.getName()).log(Level.INFO, "Creating " + file.getName());
                file.createNewFile();
            }
            
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            Logger.getLogger(TimeManagementApplication.class.getName()).log(Level.INFO, "-- " + file.getAbsolutePath());
            
            ResultSet rs = databaseManager.getReport();
        
            while (rs.next()) {
                bw.write(rs.getString("DATE_CREATED") + " - " + rs.getString("TEXT") + "\n");
            }
            bw.close();
            
            
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().edit(file);
            } else {
                // dunno, up to you to handle this
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(TimeManagementApplication.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(TimeManagementApplication.class.getName()).log(Level.SEVERE, null, ex);

        }
        Logger.getLogger(TimeManagementApplication.class.getName()).log(Level.INFO, "Report Done");
        
    }
}
