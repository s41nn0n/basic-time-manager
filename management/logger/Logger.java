/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timemanagementapplication.management.logger;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextArea;

/**
 *
 * @author f
 */
public class Logger {
    
    private static JFrame loggerFrame;
    private static JTextArea logging;
    
    
    public Logger() {
        loggerFrame = new JFrame();
        
//        logging = new TextArea();

        loggerFrame.pack();
        loggerFrame.setVisible(true);
    }
    
    public static void setVisible(Boolean flag){
        loggerFrame.setVisible(flag);
    }
    
}