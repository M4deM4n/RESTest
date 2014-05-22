/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package restest.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



/**
 *
 * @author Pizano
 */
public class PersistantRequest implements Serializable
{
    private String url;
    private List<RequestHeader> headers;
    private List<PostData> postData;
    
    public PersistantRequest()
    {
        headers = new ArrayList<>();
        postData = new ArrayList<>();
    }
    
    public void setHeaders(ObservableList<RequestHeader> h)
    {
        headers.addAll(h);
    }
    
    public void setPostData(ObservableList<PostData> p)
    {
        postData.addAll(p);
    }
    
    public void setURL(String u)
    {
        url = u;
    }
    
    public String getURL()
    {
        return url;
    }
    
    public ObservableList<RequestHeader> getHeaders()
    {
        ObservableList<RequestHeader> r = FXCollections.observableArrayList();
        r.addAll(headers);
        return r;
    }
    
    public List<RequestHeader> getHeaderList()
    {
        return headers;
    }
    
    public ObservableList<PostData> getPostData()
    {
        ObservableList<PostData> r = FXCollections.observableArrayList();
        r.addAll(postData);
        return r;
    }
    
    public List<PostData> getPostDataList()
    {
        return postData;
    }
}
