/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package restest.datamodel;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;



/**
 *
 * @author Pizano
 */
public class Response
{
    public Set<Entry<String, List<String>>> responseHeaders;
    public String responseBody;
    public int responseCode;
    
    private boolean failed = false;
    
    public void failed()
    {
        this.failed = true;
    }
}
