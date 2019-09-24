package com.andcreations.ae.launch.image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.andcreations.ae.launch.image.resources.R;
import com.andcreations.io.json.JSON;
import com.andcreations.resources.ResourceLoader;

/**
 * @author Mikolaj Gucki
 */
public class LaunchImageEntryList {
    /** */
    private List<LaunchImageEntry> entries = new ArrayList<>();
    
        /** */
    public List<LaunchImageEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }
    
    /** */
    static LaunchImageEntryList load(String name) throws IOException {
    // load
        String str = ResourceLoader.loadAsString(R.class,name);
           
    // parse
        LaunchImageEntryList list = JSON.<LaunchImageEntryList>read(
            str,LaunchImageEntryList.class);
        return list;
    }    
}
