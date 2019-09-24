package com.andcreations.ae.appicon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.andcreations.ae.appicon.resources.R;
import com.andcreations.io.json.JSON;
import com.andcreations.resources.ResourceLoader;

/**
 * @author Mikolaj Gucki
 */
public class AppIconEntryList {    
    /** */
    private List<AppIconEntry> entries = new ArrayList<>();
    
    /** */
    private String dir;    
    
    /** */
    public List<AppIconEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }
    
    /** */
    public String getDir() {
        return dir;
    }
    
    /** */
    static AppIconEntryList load(String name) throws IOException {
    // load
        String str = ResourceLoader.loadAsString(R.class,name);
           
    // parse
        AppIconEntryList list = JSON.<AppIconEntryList>read(
            str,AppIconEntryList.class);
        return list;
    }
}
