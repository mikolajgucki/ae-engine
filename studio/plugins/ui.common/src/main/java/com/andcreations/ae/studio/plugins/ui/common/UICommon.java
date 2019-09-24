package com.andcreations.ae.studio.plugins.ui.common;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import com.andcreations.ae.studio.log.Log;

/**
 * @author Mikolaj Gucki
 */
public class UICommon {
    /** */
    public static void invoke(Runnable runnable) {
    // already on the dispatcher thread?
        if (SwingUtilities.isEventDispatchThread() == true) {
            runnable.run();
            return;
        }   
        
        SwingUtilities.invokeLater(runnable);
    }

    /** */
    public static void invokeAndWait(Runnable runnable) {
    // already on the dispatcher thread?
        if (SwingUtilities.isEventDispatchThread() == true) {
            runnable.run();
            return;
        }        
        
        try {
            SwingUtilities.invokeAndWait(runnable);
        } catch (InvocationTargetException exception) {
            Log.error(exception.toString(),exception);
        } catch (InterruptedException exception) {
            Log.error(exception.toString(),exception);
        }
    }
}
