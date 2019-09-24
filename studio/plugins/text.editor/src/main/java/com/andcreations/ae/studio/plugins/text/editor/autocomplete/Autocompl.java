package com.andcreations.ae.studio.plugins.text.editor.autocomplete;

import javax.swing.Icon;

/**
 * @author Mikolaj Gucki
 */
interface Autocompl {
    /** */
    String getDisplayText();
    
    /** */
    String getDisplayHTML();
    
    /** */
    Icon getIcon();    
    
    /** */    
    String getLowerCaseName();
    
    /** */
    String getLowerCasePrefix();
        
    /** */
    String getLowerCaseFullName();
}