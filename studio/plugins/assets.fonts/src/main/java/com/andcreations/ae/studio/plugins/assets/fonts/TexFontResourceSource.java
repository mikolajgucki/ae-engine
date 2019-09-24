package com.andcreations.ae.studio.plugins.assets.fonts;

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
class TexFontResourceSource implements ResourceSource {
    /** */
    private String fontIconName;
    
    /** */
    private String fontWarningIconName;
    
    /** */
    private String fontErrorIconName;
    
    /** */
    TexFontResourceSource() {
        create();
    }
    
    /** */
    private void create() {
        fontIconName = FontsIcons.FONT;
        
    // warning
        Icons.getIcon(FontsIcons.FONT,DefaultIcons.DECO_WARNING);
        fontWarningIconName = Icons.getIconName(
            FontsIcons.FONT,DefaultIcons.DECO_WARNING);
        
    // error
        Icons.getIcon(FontsIcons.FONT,DefaultIcons.DECO_ERROR);
        fontErrorIconName = Icons.getIconName(
            FontsIcons.FONT,DefaultIcons.DECO_ERROR);
    }
    
    /** */
    @Override
    public List<Resource> getResources() {
        List<Resource> resources = new ArrayList<>();
        
        List<File> files = TexFont.get().getTexFontFiles();
        for (File file:files) {
            String fileName = file.getName();
            String baseName = FilenameUtils.getBaseName(fileName);
            
        // name
            int index = baseName.indexOf('.');
            String name = baseName.substring(0,index);
            
        // icon
            String iconName = fontIconName;
            if (TexFont.get().hasErrors(file) == true) {
                iconName = fontErrorIconName;
            }
            else if (TexFont.get().hasWarnings(file) == true) {
                iconName = fontWarningIconName;
            }
            
            resources.add(new Resource(iconName,name,fileName,file));
        }
        
        return resources;
    }
    
    /** */
    @Override
    public void openResource(Resource resource) {
        File file = (File)resource.getUserObject();
        TexFont.get().edit(file);
    }
}