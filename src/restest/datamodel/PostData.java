/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package restest.datamodel;

import javafx.beans.property.SimpleStringProperty;



/**
 *
 * @author Pizano
 */
public class PostData
{
    final private SimpleStringProperty param = new SimpleStringProperty();
    final private SimpleStringProperty data = new SimpleStringProperty();
    
    
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
        param.set(p);
        data.set(d);
    }
    
    
    /**
     * 
     * @return 
     */
    @Override
    public String toString()
    {
        return param.get() + "=" + data.get();
    }
    
    
    /**
     * 
     * @return 
     */
    public String getParam()
    {
        return param.get();
    }
    
    
    /**
     * 
     * @return 
     */
    public String getData()
    {
        return data.get();
    }
    
    
    /**
     * 
     * @param param 
     */
    public void setParam(String p)
    {
        param.set(p);
    }
    
    
    /**
     * 
     * @param value 
     */
    public void setData(String d)
    {
        data.set(d);
    }
}
