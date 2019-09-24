package com.andcreations.resources;

import java.util.Set;

/**
 * @author Mikolaj Gucki
 */
public abstract class DefaultResources implements StrResources {
    /** 
     * Replaces an argument pattern with the argument value.
     *
     * @param string The string in which the argument is replaced.
     * @param index The index of the argument.
     * @param arg The value of the argument.
     * @return The string with the replaced argument.
     */
    protected static String replace(String string,int index,String arg) {
        String argKey = "{" + index + "}";
        while (true) {
            int indexOf = string.indexOf(argKey);
            if (indexOf == -1) {
                break;
            }
            string = string.substring(0,indexOf) + arg +
                string.substring(indexOf + argKey.length());
        }
        
        return string;
    }

    /** */
    @Override
    public abstract String getStr(String key);

    /** */
    @Override
    public String getStr(String key, String... args) {
        String string = getStr(key);
        
        for (int index = 0; index < args.length; index++) {
            string = replace(string,index,args[index]);
        }
        
        return string;
    }
    
    /** */
    @Override
    public abstract Set<String> getKeys();
}
