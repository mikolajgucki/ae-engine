package com.andcreations.ae.studio.plugins.outline;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.file.EditedFile;
import com.andcreations.ae.studio.plugins.file.EditedFileListener;
import com.andcreations.ae.studio.plugins.file.FileAdapter;
import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.text.editor.EditorMediator;
import com.andcreations.ae.studio.plugins.text.editor.TextEditor;
import com.andcreations.ae.studio.plugins.text.editor.TextEditorAdapter;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.ae.studio.plugins.ui.main.view.DefaultViewProvider;
import com.andcreations.ae.studio.plugins.ui.main.view.SingleViewFactory;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCategory;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class Outline {
    /** The view identifier. */
    private static final String VIEW_ID =
        "com.andcreations.ae.studio.plugins.outline";      
    
    /** The class instance. */
    private static Outline instance;
        
    /** The view component. */
    private OutlineViewComponent component;    
    
    /** */
    private List<OutlineSource> sources = new ArrayList<>();
    
    /** */
    private BundleResources res = new BundleResources(Outline.class);     
    
    /** */
    void init() {
    // edited file listener
        EditedFile.get().addEditedFileListener(new EditedFileListener() {
            /** */
            @Override
            public void editedFileChanged(File file) {
                fileChanged(file);
            }
        });
        
    // file listener
        Files.get().addFileListener(new FileAdapter() {
            /** */
            @Override
            public void fileChanged(File file) {
                Outline.this.fileChanged(file);
            }
        });
        
    // text editor listener
        TextEditor.get().addTextEditorListener(new TextEditorAdapter() {
            /** */
            @Override
            public void textRecentlyChanged(EditorMediator editor) {
                fileChanged(editor.getFile());
            }                 
        });
        
    // UI
        initUI();
    }
    
    /** */
    private void initUI() {
    // component
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                component = new OutlineViewComponent();
            }
        });
        
        String title = res.getStr("view.title");
        ImageIcon icon = Icons.getIcon(OutlineIcons.OUTLINE);
        
    // view factory
        SingleViewFactory factory = new SingleViewFactory(
            VIEW_ID,component,title,icon);
        factory.setViewCategory(ViewCategory.DETAILS);
        MainFrame.get().getViewManager().registerViewFactory(factory);
        
    // view provider
        DefaultViewProvider provider = new DefaultViewProvider(
            title,icon,factory);
        MainFrame.get().getViewManager().addViewProvider(provider);
    }
    
    /** */
    public void addOutlineSource(OutlineSource source) {
        synchronized (sources) {
            sources.add(source);
        }
    }
    
    /** */
    private void fileChanged(File file) {
        synchronized (sources) {
            for (OutlineSource source:sources) {
                OutlineItem root = source.getOutlineRootItem(file);
                if (root != null) {
                    setRootItem(root);
                    return;
                }
            }
        }
        
        clear();
    }
    
    /**
     * Sets the root item displayed in the outline view.
     *
     * @param root The root item.
     */
    private void setRootItem(OutlineItem rootItem) {
        component.setRootItem(rootItem);        
    }
    
    /**
     * Clears the outline.
     */
    public void clear() {
        component.clear();
    }
    
    /**
     * Gets the class instance.
     *
     * @return The class instance.
     */
    public static Outline get() {
        if (instance == null) {
            instance = new Outline();
        }
        
        return instance;
    }    
}