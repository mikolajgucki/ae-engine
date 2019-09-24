package com.andcreations.ae.studio.plugins.todo;

import com.andcreations.ae.studio.plugins.ui.icons.IconSource;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.icons.ResourceIconSource;

/**
 * @author Mikolaj Gucki
 */
class ToDoIcons {
    /** */
    public static final String TODO = "todo";
    
    /** */
    static void init() {
    // source
        IconSource iconSource =
            new ResourceIconSource(ToDoIcons.class,"resources/");       
        Icons.addIconSource(iconSource);
        
    // colorize
       Icons.colorize(TODO,Icons.blue());
    }
}