package com.andcreations.ae.studio.plugins.project.explorer;

import com.andcreations.ae.studio.plugins.ui.icons.IconSource;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.icons.ResourceIconSource;

/**
 * @author Mikolaj Gucki
 */
public class ProjectExplorerIcons {
    /** */
    public static final String PROJECT_EXPLORER = "project_explorer";
    
    /** */
    static void init() {
    // source
        IconSource iconSource =
            new ResourceIconSource(ProjectExplorerIcons.class,"resources/");       
        Icons.addIconSource(iconSource);
        
    // colorize
        Icons.colorize(PROJECT_EXPLORER,Icons.orange());
    }
}