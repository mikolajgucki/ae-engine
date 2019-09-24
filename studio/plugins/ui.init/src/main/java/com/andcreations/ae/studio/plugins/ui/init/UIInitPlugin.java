package com.andcreations.ae.studio.plugins.ui.init;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

import org.pushingpixels.substance.api.skin.SubstanceTwilightLookAndFeel;
import org.pushingpixels.substance.api.skin.TwilightSkin;

import com.andcreations.ae.studio.plugin.Initializer;
import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.Required;
import com.andcreations.ae.studio.plugin.manager.PluginManager;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.util.ExceptionRunnable;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@Initializer
public class UIInitPlugin extends Plugin<UIInitState> {
    /** */
    private BundleResources res = new BundleResources(UIInitPlugin.class);
    
    @Required(id="com.andcreations.ae.studio.plugins.ui.common")
    private Object uiCommonPluginObject;
    
    /** */
    /*
    private static void setUIFont(Font font) {
        Enumeration<?> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if (value != null && value instanceof FontUIResource) {
                UIManager.put(key,font);
            }
        }
    }
    */
    
    /** */
    /*
    private Font loadFontFromResource(String resourceName)
        throws Exception {
    //
        String fullResourceName = '/' +
            UIInitPlugin.class.getPackage().getName().replace('.','/') +
            "/resources/" + resourceName;
        return Font.createFont(Font.TRUETYPE_FONT,
            UIInitPlugin.class.getResourceAsStream(fullResourceName));
    }
    */
    
    /** */
    private void initLookAndFeel() throws Exception {
        SubstanceTwilightLookAndFeel laf =
            new SubstanceTwilightLookAndFeel();
        TwilightSkin skin = new TwilightSkin();
        SubstanceTwilightLookAndFeel.setSkin(skin);
        UIManager.setLookAndFeel(laf);

        /*
        final String fontName = "Vera.ttf";
        Font font = null;
        try {
            font = loadFontFromResource(fontName);
        } catch (Exception exception) {
            Log.error(String.format("Failed to load font %s: %s",
                fontName,exception.getMessage()));
        }
        if (font != null) {
            setUIFont(font.deriveFont((float)11));
        }
        */
        
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);            
    }
    
    /** */
    @Override
    public void start() throws PluginException {
        ExceptionRunnable lafInitializer = new ExceptionRunnable() {
            /** */
            public void runWithException() throws Exception {
                initLookAndFeel();
            }
        };
    // initialize L&F
        UICommon.invokeAndWait(lafInitializer);
        if (lafInitializer.getException() != null) {
            throw new PluginException(res.getStr("failed.to.init.laf",
                lafInitializer.getException().toString()),
                lafInitializer.getException());
        }
        
    // plugin manager listener
        PluginManager.addPluginManagerListener(
            new UIInitPluginManagerListener());
    }
    
    /** */
    @Override
    public UIInitState getState() {
        return new UIInitState();
    }
    
    /** */
    @Override
    public void setState(UIInitState state) {
    }
}