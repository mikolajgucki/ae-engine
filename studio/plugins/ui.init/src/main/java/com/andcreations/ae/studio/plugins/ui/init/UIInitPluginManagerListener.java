package com.andcreations.ae.studio.plugins.ui.init;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugin.manager.PluginDesc;
import com.andcreations.ae.studio.plugin.manager.PluginManagerAdapter;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class UIInitPluginManagerListener extends PluginManagerAdapter {
    /** */
    private BundleResources res =
        new BundleResources(UIInitPluginManagerListener.class);
    
    /** */
    private void fatal(final String title,final String message) {
        Runnable runnable = new Runnable() {
            /** */
            @Override
            public void run() {
                JOptionPane.showMessageDialog(
                    null,message,title,JOptionPane.PLAIN_MESSAGE);
            }
        };
        
        try {
            SwingUtilities.invokeAndWait(runnable);
        } catch (InvocationTargetException exception) {
            Log.error(exception.toString(),exception);
        } catch (InterruptedException exception) {
            Log.error(exception.toString(),exception);
        }
        System.exit(-1);
    }
        
    /** */
    @Override
    public void failedToStartPlugins(Exception exception) {
        fatal(res.getStr("start"),res.getStr("failed.to.start.plugins"));
    }
    
    /** */
    @Override
    public void failedToStartPlugin(PluginDesc plugin,Exception exception) {
        fatal(res.getStr("start"),res.getStr("failed.to.start.plugins",
            plugin.getId()));
    }    
}