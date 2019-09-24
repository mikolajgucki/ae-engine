package com.andcreations.ae.studio.plugins.simulator.resources;

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
public class SimulatorResourceSource implements ResourceSource {
    /** */
    private BundleResources res = new BundleResources(
        SimulatorResourceSource.class); 
    
    /** */
    @Override
    public List<Resource> getResources() {
        List<Resource> resources = new ArrayList<>();
        
        File dir = new File(ProjectFiles.get().getProjectDir(),
            AEProject.Simulator.PLUGINS_PATH);
    // plugin configuration files
        File[] files = dir.listFiles();
        if (files != null) {
	        for (File file:files) {
	            resources.add(new Resource(DefaultIcons.PROJECT_FILE,
	                file.getName(),res.getStr("plugin.cfg.file"),file));
	        }
        }
        
        return resources;
    }

    /** */
    @Override
    public void openResource(Resource resource) {
        File file = (File)resource.getUserObject();
        DefaultTextEditor.get().edit(file);
    }
}
