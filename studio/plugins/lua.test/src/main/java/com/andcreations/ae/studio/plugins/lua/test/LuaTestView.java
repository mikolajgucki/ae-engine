package com.andcreations.ae.studio.plugins.lua.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.andcreations.ae.studio.plugins.project.ProjectMenu;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.common.UIKeys;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.ae.studio.plugins.ui.main.view.DefaultViewProvider;
import com.andcreations.ae.studio.plugins.ui.main.view.SingleViewFactory;
import com.andcreations.ae.studio.plugins.ui.main.view.View;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButton;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButtonListener;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCategory;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewDecorator;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class LuaTestView implements LuaTestListener {
    /** The view identifier. */
    private static final String VIEW_ID = LuaTestView.class.getName();
    
    /** */
    private BundleResources res = new BundleResources(LuaTestView.class);  
        
    /** */
    private LuaTestViewListener listener;
    
    /** */
    private LuaTestComponent component;
    
    /** */
    private ViewButton runButton;
    
    /** */
    private ViewButton terminateButton;
    
    /** */
    private DefaultViewProvider viewProvider;
    
    /** */
    void init() {
    // component
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                component = new LuaTestComponent();
                addRunLuaTestsMenu();
            }
        });        
        
    // icon
        ImageIcon icon = Icons.getIcon(DefaultIcons.TESTS);
        
    // factory
        SingleViewFactory factory = new SingleViewFactory(
            VIEW_ID,component,res.getStr("view.title"),icon);
        factory.setViewCategory(ViewCategory.NAVIGATOR);
        MainFrame.get().getViewManager().registerViewFactory(factory);
        
    // provider
        viewProvider = new DefaultViewProvider(
            res.getStr("view.title"),icon,factory);
        MainFrame.get().getViewManager().addViewProvider(viewProvider);
        
    // run
        factory.addViewDecorator(new ViewDecorator() {
            /** */
            @Override
            public void decorateView(View view) {
                KeyStroke keyStroke = KeyStroke.getKeyStroke(
                    KeyEvent.VK_ENTER,UIKeys.menuKeyMask());                  
                runButton = view.addButton();
                runButton.setAccelerator(keyStroke);
                runButton.setIcon(Icons.getIcon(DefaultIcons.PLAY));
                runButton.setDisabledIcon(Icons.getIcon(DefaultIcons.PLAY));
                runButton.setText(res.getStr("run"));
                runButton.setTooltip(res.getStr("run"));
                runButton.addViewButtonListener(new ViewButtonListener() {
                    /** */
                    @Override
                    public void actionPerformed(ViewButton button) {
                        runLuaTests();
                    }
                });                  
            }                
        });
        
    // terminate
        factory.addViewDecorator(new ViewDecorator() {
            /** */
            @Override
            public void decorateView(View view) {
                KeyStroke keyStroke = KeyStroke.getKeyStroke(
                    KeyEvent.VK_X,UIKeys.menuKeyMask());                  
                terminateButton = view.addButton();
                terminateButton.setAccelerator(keyStroke);
                terminateButton.setIcon(Icons.getIcon(DefaultIcons.KILL));
                terminateButton.setDisabledIcon(
                    Icons.getIcon(DefaultIcons.KILL));
                terminateButton.setText(res.getStr("terminate"));
                terminateButton.setTooltip(res.getStr("terminate"));
                terminateButton.setEnabled(false);
                terminateButton.addViewButtonListener(new ViewButtonListener() {
                    /** */
                    @Override
                    public void actionPerformed(ViewButton button) {
                        terminateLuaTests();
                    }
                });                  
            }                
        });        
    }
    
    /** */
    void setLuaTestViewListener(LuaTestViewListener listener) {
        this.listener = listener;
    }
    
    /** */
    private void addRunLuaTestsMenu() {
        JMenuItem item = new JMenuItem(res.getStr("run"));
        item.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_T,UIKeys.menuKeyMask() | KeyEvent.SHIFT_DOWN_MASK));
        item.setIcon(Icons.getIcon(DefaultIcons.TESTS));
        item.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                runLuaTests();
            }
        });
        
        ProjectMenu.get().addSeparator();
        ProjectMenu.get().add(item);          
    }
    
    /** */
    private void runLuaTests() {
        LuaTest.get().runAllTests();
    }
    
    /** */
    private void terminateLuaTests() {
        listener.terminateLuaTests();
    }    
    
    /** */
    LuaTestComponent getComponent() {
        return component;
    }
    
    /** */
    @Override
    public void startingLuaTests() {
        viewProvider.showView();
        runButton.setEnabled(false);
        terminateButton.setEnabled(true);
    }
    
    /** */
    @Override
    public void luaTestsFinished() {
        runButton.setEnabled(true);
        terminateButton.setEnabled(false);
    }
}