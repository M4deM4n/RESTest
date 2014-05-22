/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package restest.application;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;



/**
 *
 * @author Pizano
 */
public class RESTApp
{
    public String getDate()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        return dateFormat.format(calendar.getTime());
    }
}
