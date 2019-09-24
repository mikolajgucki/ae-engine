package com.andcreations.resources;

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Wrapper for resource bundle.
 *
 * @author Mikolaj Gucki
 */
public class Bundle {
    /** The resource bundle. */
    private ResourceBundle bundle;
    
    /**
     * Initializes the resources.
     *
     * @param baseName The base name of the resource bundle, a fully qualified
     * class name.
     */
    public Bundle(String baseName) {
        bundle = ResourceBundle.getBundle(baseName);
    }
        
    /**
     * Gets a string of a specified key.
     *
     * @param key The key for the string.
     * @return The string of the key.
     */
    public String getStr(String key) {
        return bundle.getString(key);
    }
    
    /** 
     * Replaces an argument pattern with the argument value.
     *
     * @param str The string in which the argument is replaced.
     * @param index The index of the argument.
     * @param arg The value of the argument.
     * @return The string with the replaced argument.
     */
    private static String replace(String str,int index,String arg) {
        while (true) {
            int indexOf = str.indexOf("{" + index + "}");
            if (indexOf == -1) {
                break;
            }
            str = str.substring(0,indexOf) + arg + str.substring(indexOf + 3);
        }
    
        return str;
    }
    
    /**
     * Replaces the zeroth argument.
     *
     * @param str The string in which the argument is replaced.
     * @param arg0 The value of the argument.
     * @return The string with replaced argument.
     */
    public String replace(String str,String arg0) {
        return replace(str,0,arg0);
    }
    
    /**
     * Replaces the zeroth argument.
     *
     * @param str The string in which the argument is replaced.
     * @param arg0 The value of the argument.
     * @return The string with replaced argument.
     */
    public String replace(String str,char arg0) {
        return replace(str,0,new String(new char[]{arg0}));
    }
    
    /**
     * Replaces the zeroth and first arguments.
     *
     * @param str The string in which the arguments is replaced.
     * @param arg0 The value of the zeroth argument.
     * @param arg1 The value of the first argument.
     * @return The string with replaced arguments.
     */
    public String replace(String str,String arg0,String arg1) {
        return replace(replace(str,0,arg0),1,arg1);
    }
    
    /**
     * Replaces the zeroth, first and second arguments.
     *
     * @param str The string in which the arguments is replaced.
     * @param arg0 The value of the zeroth argument.
     * @param arg1 The value of the first argument.
     * @param arg2 The value of the second argument.
     * @return The string with replaced arguments.
     */
    public String replace(String str,String arg0,String arg1,String arg2) {
        return replace(replace(replace(str,0,arg0),1,arg1),2,arg2);
    }
    
    /**
     * Gets a string of the specified key replacing the zeroth argument.
     *
     * @param key The key for the string.
     * @param arg0 The value of the zeroth argument.
     * @return The string of the key with the replaced argument.
     */
    public String getString(String key,String arg0) {
        return replace(getStr(key),arg0);
    }
    
    /**
     * Gets a string of the specified key replacing the zeroth argument.
     *
     * @param key The key for the string.
     * @param arg0 The value of the zeroth argument.
     * @return The string of the key with the replaced argument.
     */
    public String getString(String key,char arg0) {
        return replace(getStr(key),arg0);
    }
    
    /**
     * Gets a string of the specified key replacing the zeroth and first argument.
     *
     * @param key The key for the string.
     * @param arg0 The value of the zeroth argument.
     * @param arg1 The value of the first argument.
     * @return The string of the key with the replaced argument.
     */
    public String getString(String key,String arg0,String arg1) {
        return replace(getStr(key),arg0,arg1);
    }
    
    /**
     * Gets a string of the specified key replacing the zeroth, first and second
     * argument.
     *
     * @param key The key for the string.
     * @param arg0 The value of the zeroth argument.
     * @param arg1 The value of the first argument.
     * @param arg2 The value of the second argument.
     * @return The string of the key with the replaced argument.
     */
    public String getString(String key,String arg0,String arg1,String arg2) {
        return replace(getStr(key),arg0,arg1,arg2);
    }
    
    /**
     * Gets all the keys in the bundle.
     * 
     * @return The list of the keys.
     */
    public List<String> getKeys() {
    	Enumeration<String> keyEnum = bundle.getKeys();
    	List<String> keys = new ArrayList<String>();
    	
    	while (keyEnum.hasMoreElements() == true) {
    		keys.add(keyEnum.nextElement());
    	}
    	
    	return keys;
    }
    
    /**
     * Loads an image from a URL.
     *
     * @param parent The parent component requesting the image.
     * @param url The URL from which to load the image.
     * @return The loaded image or <tt>null</tt> if the URL is null.
     */
    public static Image loadImage(Component parent,URL url) {
        if (url == null) {
            return null;
        }
        
        MediaTracker tracker = new MediaTracker(parent);
        Image image = Toolkit.getDefaultToolkit().getImage(url);
        
    // wait for the image to load
        tracker.addImage(image,1);
        try {
            tracker.waitForAll();
        } catch (InterruptedException exception) {
        }
        
        return image;
    }    
}
 