package com.andcreations.ae.studio.plugins.ui.icons;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

/**
 * Loads icons from resources.
 * 
 * @author Mikolaj Gucki
 */
public class ResourceIconSource implements IconSource {
	/** */
	private Class<?> clazz;
	
    /** */
    private String prefix;
    
    /** The cached icons. */
    private Map<String,ImageIcon> icons = new HashMap<>();
    
    /** */    
    public ResourceIconSource(Class<?> clazz,String prefix) {
    	this.clazz = clazz;
        this.prefix =
            '/' + clazz.getPackage().getName().replace('.','/') + '/' + prefix;
    }
    
    /** */
    private String getResourceName(String name) {
        return prefix + name + ".png";
    }
    
    /** */
    private URL getResource(String name) {
    	return clazz.getResource(getResourceName(name));
    }
    
    /** */
    @Override
    public boolean hasIcon(String name) {
        if (icons.containsKey(name)) {
            return true;
        }
        
        return getResource(name) != null;
    }

    /** */
    @Override
    public ImageIcon getIcon(String name) {
        if (icons.containsKey(name)) {
            return icons.get(name);
        }
        
        ImageIcon icon = new ImageIcon(getResource(name));
        icons.put(name,icon);
        
        return icon;
    }
    
    /** */
    @Override
    public String toString() {
        return prefix;
    }
}
