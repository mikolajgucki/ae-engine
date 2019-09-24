package com.andcreations.ae.studio.plugins.ui.common;

import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

/**
 * Key event related methods.
 *
 * @author Mikolaj Gucki
 */
public class UIKeys {
    /** */
    public static boolean testModifiers(KeyEvent event,int onmask,
        int offmask) {
    //
        return (event.getModifiers() & (onmask | offmask)) == onmask;
    }
    
    /** */
    public static boolean testModifiersEx(KeyEvent event,int onmask,
        int offmask) {
    //
        return (event.getModifiersEx() & (onmask | offmask)) == onmask;
    }
    
    /** */
    public static boolean testModifiersEx(KeyEvent event,KeyStroke stroke) {
        int modifiers =
            KeyEvent.CTRL_DOWN_MASK |
            KeyEvent.SHIFT_DOWN_MASK |
            KeyEvent.ALT_DOWN_MASK |
            KeyEvent.META_DOWN_MASK;
        return (event.getModifiersEx() & modifiers) ==
            (stroke.getModifiers() & modifiers);
    }    
    
    /** */
    public static boolean isMenuOnly(KeyEvent event) {
        int onmask = menuKeyMask();
        int offmask =
            InputEvent.CTRL_MASK | 
            InputEvent.SHIFT_MASK |
            InputEvent.ALT_MASK |
            InputEvent.META_MASK;
        offmask = offmask & (0xffffffff ^ onmask);
        
        return testModifiers(event,onmask,offmask);
    }

    /** */
    public static boolean isModifierKey(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_ALT:
            case KeyEvent.VK_ALT_GRAPH:
            case KeyEvent.VK_CONTROL:
            case KeyEvent.VK_SHIFT:
            case KeyEvent.VK_META:
                return true;
            default:
                return false;
        }
    }
    
    /** */
    public static String keyStrokeToString(KeyStroke stroke) {
        String result = "";
        
        int modifiers = stroke.getModifiers();
        if (modifiers != 0) {
            result = KeyEvent.getKeyModifiersText( modifiers );
        }

        int keyCode = stroke.getKeyCode();
        if (!isModifierKey(keyCode)) {
            if (modifiers != 0) {
                result += "+";
            }                        
            if (keyCode != 0) {
                result += KeyEvent.getKeyText( keyCode );
            }
            else {
                result += stroke.getKeyChar();
            }
        }

        return result;
    }
    
    /** */
    public static int menuKeyMask() {
        return Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    }
}