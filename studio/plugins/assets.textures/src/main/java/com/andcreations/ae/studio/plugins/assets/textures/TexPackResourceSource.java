package com.andcreations.ae.studio.plugins.assets.textures;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.andcreations.ae.studio.plugins.resources.Resource;
import com.andcreations.ae.studio.plugins.resources.ResourceSource;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;

/**
 * @author Mikolaj Gucki
 */
class TexPackResourceSource implements ResourceSource {
    /** */
    private String texIconName;
    
    /** */
    private String texWarningIconName;
    
    /** */
    private String texErrorIconName;
    
    /** */
    TexPackResourceSource() {
        create();
    }
    
    /** */
    private void create() {
        texIconName = TexturesIcons.TEXTURE;
        
    // warning
        Icons.getIcon(TexturesIcons.TEXTURE,DefaultIcons.DECO_WARNING);
        texWarningIconName = Icons.getIconName(
            TexturesIcons.TEXTURE,DefaultIcons.DECO_WARNING);
        
    // error
        Icons.getIcon(TexturesIcons.TEXTURE,DefaultIcons.DECO_ERROR);
        texErrorIconName = Icons.getIconName(
            TexturesIcons.TEXTURE,DefaultIcons.DECO_ERROR);        
    }
    
    /** */
    @Override
    public List<Resource> getResources() {
        List<Resource> resources = new ArrayList<>();
        
        List<File> files = TexPack.get().getTexPackFiles();
        for (File file:files) {
            String fileName = file.getName();
            String baseName = FilenameUtils.getBaseName(fileName);
            
        // name
            int index = baseName.indexOf('.');
            String name = baseName.substring(0,index);
            
        // icon
            String iconName = texIconName;
            if (TexPack.get().hasErrors(file) == true) {
                iconName = texErrorIconName;
            }
            else if (TexPack.get().hasWarnings(file) == true) {
                iconName = texWarningIconName;
            }            
            
            resources.add(new Resource(iconName,name,fileName,file));
        }
        
        return resources;
    }
    
    /** */
    @Override
    public void openResource(Resource resource) {
        File file = (File)resource.getUserObject();
        TexPack.get().edit(file);
    }
}