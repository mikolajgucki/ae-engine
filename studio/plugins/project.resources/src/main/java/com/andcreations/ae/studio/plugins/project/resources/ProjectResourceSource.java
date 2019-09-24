package com.andcreations.ae.studio.plugins.project.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.project.AEProject;
import com.andcreations.ae.studio.plugins.project.files.ProjectFiles;
import com.andcreations.ae.studio.plugins.resources.Resource;
import com.andcreations.ae.studio.plugins.resources.ResourceSource;
import com.andcreations.ae.studio.plugins.text.editor.DefaultTextEditor;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.resources.BundleResources;

/**
 * Provides the project resources.
 * 
 * @author Mikolaj Gucki
 */
public class ProjectResourceSource implements ResourceSource {
    /** */
    private static final String AE_PROPERTIES_NAME = AEProject.PROPERTIES_FILE; 
    
    /** */
    private BundleResources res = new BundleResources(
            ProjectResourceSource.class);     
    
    /** */
    @Override
    public List<Resource> getResources() {
        List<Resource> resources = new ArrayList<>();
        
    // ae.properties
        resources.add(new Resource(DefaultIcons.PROJECT_FILE,
            AE_PROPERTIES_NAME,res.getStr("project.cfg.file"),null));
        
        return resources;
    }

    /** */
    @Override
    public void openResource(Resource resource) {
    // ae.properties
        if (AE_PROPERTIES_NAME.equals(resource.getName())) {
            DefaultTextEditor.get().edit(
                new File(ProjectFiles.get().getProjectDir(),
                AEProject.PROPERTIES_FILE));
            return;
        }
    }
}
