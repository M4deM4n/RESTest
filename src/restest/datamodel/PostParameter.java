/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package restest.datamodel;

import java.io.Serializable;
import javafx.beans.property.SimpleStringProperty;



/**
 *
 * @author Pizano
 */
public class PostData implements Serializable
{
//    final private SimpleStringProperty param = new SimpleStringProperty();
//    final private SimpleStringProperty data = new SimpleStringProperty();
    private String param;
    private String data;
    
    
    /**
     * 
     */
    public PostData()
    {
        // Do Nothing
    }
    
    
    /**
     * 
     * @param p
     * @param d 
     */
    public PostData(String p, String d)
    {
//        param.set(p);
//        data.set(d);
        
        param = p;
        data = d;
    }
    
    
    /**
     * 
     * @return 
     */
    @Override
    public String toString()
    {
        return param + "=" + data;
    }
    
    
    /**
     * 
     * @return 
     */
    public String getParam()
    {
        return param;
    }
    
    
    /**
     * 
     * @return 
     */
    public String getData()
    {
        return data;
    }
    
    
    /**
     * 
     * @param p
     */
    public void setParam(String p)
    {
        param = p;
    }
    
    
    /**
     * 
     * @param d
     */
    public void setData(String d)
    {
        data = d;
    }
}