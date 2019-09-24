package com.andcreations.ae.studio.plugins.lua.debug.snippet;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import com.andcreations.ae.studio.plugins.lua.LuaIcons;
import com.andcreations.ae.studio.plugins.lua.debug.LuaDebugIcons;
import com.andcreations.ae.studio.plugins.lua.debug.LuaDebugPluginState;
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
 * Lua snippet allows to run a snippet of code while a game is running.
 * The code in run between frames.
 *
 * @author Mikolaj Gucki
 */
public class LuaSnippet implements LuaSnippetViewComponentListener {
    /** The view identifier. */
    private static final String VIEW_ID =
        "com.andcreations.ae.studio.plugins.lua.debug.snippet"; 
        
    /** */
    private static LuaSnippet instance;
    
    /** */
    private LuaSnippetListener listener;
    
    /** */
    private LuaDebugPluginState state;
    
    /** */
    private BundleResources res = new BundleResources(LuaSnippet.class);     
    
    /** */
    private LuaSnippetViewComponent component;    
    
    /** */
    private ViewButton runButton;
    
    /** */
    private boolean runEnabled = true;
    
    /** */
    private boolean hasSession;
    
    /** */
    public void init(LuaDebugPluginState state,LuaSnippetListener listener) {
        this.state = state;
        this.listener = listener;
        initUI();
    }        
    
    /** */
    private void initUI() {
    // component
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                component = new LuaSnippetViewComponent(LuaSnippet.this);
            }
        });        
        component.setText(state.getLuaSnippet());
        
        String title = res.getStr("view.title");
        ImageIcon icon = Icons.getIcon(LuaIcons.LUA_PROJECT_FILE,
            DefaultIcons.DECO_PLAY);

    // view factory
        SingleViewFactory factory = new SingleViewFactory(
            VIEW_ID,component,title,icon);
        factory.setViewCategory(ViewCategory.EDITOR);
        MainFrame.get().getViewManager().registerViewFactory(factory);
        
    // view provider
        DefaultViewProvider provider = new DefaultViewProvider(
            title,icon,factory);
        MainFrame.get().getViewManager().addViewProvider(provider);        
        
    // run
        factory.addViewDecorator(new ViewDecorator() {
            /** */
            @Override
            public void decorateView(View view) {
                KeyStroke keyStroke = KeyStroke.getKeyStroke(
                    KeyEvent.VK_ENTER,UIKeys.menuKeyMask());                  
                runButton = view.addButton();
                runButton.setEnabled(false);
                runButton.setAccelerator(keyStroke);
                runButton.setIcon(Icons.getIcon(LuaDebugIcons.LUA_SNIPPET_RUN));
                runButton.setDisabledIcon(
                    Icons.getIcon(LuaDebugIcons.LUA_SNIPPET_RUN));
                runButton.setText(res.getStr("run.text",
            		UIKeys.keyStrokeToString(keyStroke)));
                runButton.setTooltip(res.getStr("run.tooltip"));
                runButton.addViewButtonListener(new ViewButtonListener() {
                    /** */
                    @Override
                    public void actionPerformed(ViewButton button) {
                        runLuaSnippet();
                    }
                });                
            }
        });
    }
    
    /** */
    private void updateRunEnabledButtonState() {
        if (runButton != null) {
            runButton.setEnabled(hasSession && runEnabled);
        }
    }
    
    /** */
    public void sessionAttached() {
        hasSession = true;
        updateRunEnabledButtonState();
    }
    
    /** */
    public void sessionDetached() {
        hasSession = false;
        updateRunEnabledButtonState();
    }
    
    /** */
    @Override
    public void textChanged() {
        state.setLuaSnippet(component.getText());
    }
    
    /** */
    private void runLuaSnippet() {
        setRunEnabled(false);
        listener.runLuaSnippet(component.getText());
    }
    
    /** */
    public void setRunEnabled(boolean runEnabled) {
        this.runEnabled = runEnabled;
        updateRunEnabledButtonState();
    }
    
    /** */
    public static LuaSnippet get() {
        if (instance == null) {
            instance = new LuaSnippet();
        }
        
        return instance;
    }
}