package com.andcreations.ae.studio.plugins.ui.main.view;

import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.KeyStroke;

import com.andcreations.ae.studio.plugins.ui.common.UIKeys;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class ViewUtil {
    /** */
    private static BundleResources res = new BundleResources(ViewUtil.class);      
    
    /** */
    public static void closeOthers(View view) {
        List<View> siblings = 
            MainFrame.get().getViewManager().getSiblingViews(view);
        for (View sibling:siblings) {
            if (sibling != view) {
                MainFrame.get().getViewManager().closeView(sibling);
            }
        }
    }
    
    /** */
    public static void buildCloseOthersButton(final View view,
        ViewButton button) {
    //
        KeyStroke keyStroke = KeyStroke.getKeyStroke(
            KeyEvent.VK_O,UIKeys.menuKeyMask());                
        button.setText(res.getStr("close.others.text",
            UIKeys.keyStrokeToString(keyStroke)));
        button.setTooltip(res.getStr("close.others.tooltip"));
        button.setIcon(Icons.getIcon(DefaultIcons.CLOSE_VIEWS));
        button.setAccelerator(keyStroke);        
        button.addViewButtonListener(new ViewButtonListener() {
            /** */
            @Override
            public void actionPerformed(ViewButton button) {
                closeOthers(view);
            }
        });        
    }
}