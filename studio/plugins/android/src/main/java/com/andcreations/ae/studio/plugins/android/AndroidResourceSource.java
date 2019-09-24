package com.andcreations.ae.studio.plugins.android;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.andcreations.ae.studio.plugins.resources.Resource;
import com.andcreations.ae.studio.plugins.resources.ResourceSource;
import com.andcreations.ae.studio.plugins.text.editor.DefaultTextEditor;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;

/**
 * @author Mikolaj Gucki
 */
class AndroidResourceSource implements ResourceSource {
    /** */
    private void addAndroidManifest(File dir,List<Resource> resources) {
        final String name = "AndroidManifest.xml";
        File androidManifest = new File(dir,name);
        if (androidManifest.exists() == true) {
            resources.add(new Resource(DefaultIcons.FILE,name,androidManifest));
        }
    }
    
    /** */
    @Override
    public List<Resource> getResources() {
        File dir = AndroidProjectDir.get().getAndroidProjectDir();
        if (AndroidProjectDir.isValid(dir) == false) {
            return null;
        }
        
        List<Resource> resources = new ArrayList<>();
        addAndroidManifest(dir,resources);
        
        return resources; 
    }
    
    /** */
    @Override
    public void openResource(Resource resource) {
        File file = (File)resource.getUserObject();
        DefaultTextEditor.get().edit(file);
    }
}