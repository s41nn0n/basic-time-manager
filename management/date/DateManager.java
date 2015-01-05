/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timemanagementapplication.management.date;

import java.text.SimpleDateFormat;
import java.util.Date;
import timemanagementapplication.management.settings.GlobalSetting;

/**
 *
 * @author f
 */
public class DateManager {
    public static String getCurrentTimeStampString() {
        SimpleDateFormat sdfDate = new SimpleDateFormat(GlobalSetting.DATAFORMAT);//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }
    
    public static Date getCurrentTimeStampDate() {
        SimpleDateFormat sdfDate = new SimpleDateFormat(GlobalSetting.DATAFORMAT);//dd/MM/yyyy
        Date now = new Date();
        return now;
    }
    
//    public static Date getCurrentTimeStampLong() {
////        SimpleDateFormat sdfDate = new SimpleDateFormat(GlobalSetting.DATAFORMAT);//dd/MM/yyyy
////        java.util.Date utilDate = affiliate.getDate();
//        return 
//    }
}
