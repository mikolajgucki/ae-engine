package com.andcreations.ae.studio.plugins.text.editor;

import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.KeyStroke;

import com.andcreations.ae.studio.plugins.ui.common.UIKeys;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.ae.studio.plugins.ui.main.view.View;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButton;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewButtonListener;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCategory;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewDropDown;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewFactory;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewManager;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewUtil;
import com.andcreations.ae.studio.plugins.ui.main.view.dialogs.GoToView;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class TextEditorViewFactory implements ViewFactory {
    /** */
    private static final String FACTORY_ID = TextEditorPlugin.class.getName();
    
    /** */
    private BundleResources res =
        new BundleResources(TextEditorViewFactory.class);      
    
    /** */
    private TextEditorViewFactoryListener listener;
    
    /** */
    private TextEditorState state;
    
    /** */
    private TextEditorCache cache;
    
    /** */
    TextEditorViewFactory(TextEditorViewFactoryListener listener,
        TextEditorState state,TextEditorCache cache) {
    //
        this.listener = listener;
        this.state = state;
        this.cache = cache;
    }
    
    /** */
    @Override
    public String getId() {
        return FACTORY_ID;
    }

    /** */
    private String getTitle(String baseTitle) {
        ViewManager viewManager = MainFrame.get().getViewManager();
        if (viewManager.getViewByTitle(baseTitle) == null) {
            return baseTitle;
        }
        
        int index = 2;
        while (true) {
            String title = String.format("%s (%d)",baseTitle,index);
            if (viewManager.getViewByTitle(title) == null) {
                return title;
            }
            index++;
        }
    }
    
    /** */
    private void addGoToViewButton(final TextEditorViewHandler viewHandler) {
        KeyStroke keyStroke = KeyStroke.getKeyStroke(
            KeyEvent.VK_E,UIKeys.menuKeyMask());        
        ViewButton button = viewHandler.addSecondaryButton();
        button.setText(res.getStr("go.to.view.text",
            UIKeys.keyStrokeToString(keyStroke)));
        button.setTooltip(res.getStr("go.to.view.tooltip"));
        button.setIcon(Icons.getIcon(DefaultIcons.VIEWS));
        button.setAccelerator(keyStroke);        
        button.addViewButtonListener(new ViewButtonListener() {
            /** */
            @Override
            public void actionPerformed(ViewButton button) {
                GoToView.go(viewHandler.getView());
            }
        });         
    }
    
    /** */
    private void addCloseOthersButton(TextEditorViewHandler viewHandler) {
        ViewButton button = viewHandler.addSecondaryButton();
        ViewUtil.buildCloseOthersButton(viewHandler.getView(),button);
    }
    
    /** */
    @Override
    public View createView(String viewId) {        
        EditorCfg cfg = state.getCfg(viewId);
        if (cfg == null) {
            cfg = EditorCfg.fromId(viewId);
            if (cfg == null) {
                return null;
            }
        }
        File file = new File(cfg.getFilePath());
        
    // view
        View view = MainFrame.get().getViewManager().createView(this,viewId);
        view.setTitle(getTitle(file.getName()));
        view.setIcon(Icons.getIcon(cfg.getIconName()));
        view.setCategory(ViewCategory.EDITOR);
        
    // actions, handler
        ViewDropDown secondaryActions = view.addDropDown();
        TextEditorViewHandler viewHandler = new TextEditorViewHandler(
            view,secondaryActions);
    
    // default buttons
        addGoToViewButton(viewHandler);
        addCloseOthersButton(viewHandler);
        
    // component
        EditorComponent component = new EditorComponent(cfg);        
        view.setComponent(component);
        
    // mediator
        EditorMediator editor = new EditorMediator(
            cache,cfg,component,view);
        
    // notify
        listener.textEditorCreated(editor,viewHandler);

    // cache
        cache.put(cfg,view);    
        
        return view;
    }
    
}
