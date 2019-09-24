package com.andcreations.ae.studio.plugins.builders;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.andcreations.ae.studio.plugins.project.ProjectMenu;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.common.UIKeys;
import com.andcreations.ae.studio.plugins.ui.main.StatusBar;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class Builders {
    /** */
    private static Builders instance;
    
    /** */
    private BundleResources res = new BundleResources(Builders.class);       
    
    /** */
    private List<Builder> builders = new ArrayList<>();
    
    /** */
    private BuildersStatus status;
    
    /** */
    private BuilderDialog builderDialog;    
    
    /** */
    public void appendBuilder(Builder builder) {
        builders.add(builder);
        updateStatus();
    }
    
    /** */
    public List<Builder> getBuilders() {
        return Collections.unmodifiableList(builders);
    }
    
    /** */
    public Builder getBuilderById(String id) {
        for (Builder builder:builders) {
            if (builder.getId().equals(id) == true) {
                return builder;
            }
        }
        
        return null;
    }
    
    /** */
    void init() {
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {        
                createStatus();
                addRunBuilderMenu();
                createBuilderDialog();
            }
        });
        
    // runnable
        BuilderRunnable.get().init(status);
        BuilderRunnable.get().start();        
    }
    
    /** */
    void finish() {
        BuilderRunnable.get().stop();
    }    
    
    /** */
    private void createStatus() {
        status = new BuildersStatus(new BuildersStatusListener() {
            /** */
            @Override
            public void terminate() {
                BuilderRunnable.get().terminate();
            }
        });
        StatusBar.get().addStatusComponent(status,160);
    }    
       
    /** */
    private void addRunBuilderMenu() {
        JMenuItem item = new JMenuItem(res.getStr("run.builder"));
        item.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_B,UIKeys.menuKeyMask() | KeyEvent.SHIFT_DOWN_MASK));
        item.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                runBuilder();
            }
        });
        
        ProjectMenu.get().add(item);           
    }
    
    /** */
    private void runBuilder() {
        Builder builder = builderDialog.show();
        if (builder != null) {
            postBuild(builder);
        }
    }     
    
    /** */
    private void createBuilderDialog() {
        builderDialog = new BuilderDialog();        
    }     
    
    /** */
    private void updateStatus() {
        // TODO Should consider running builder if any.
        status.setNoAction();
    }
    
    /** */
    boolean hasWarnings() {
        for (Builder builder:builders) {
            if (builder.hasWarnings() == true) {
                return true;
            }
        }        
        return false;
    }
    
    /** */
    boolean hasErrors() {
        for (Builder builder:builders) {
            if (builder.hasErrors() == true) {
                return true;
            }
        }        
        return false;
    }    
    
    /** */
    public void postBuild(Builder builder) {
        BuilderRunnable.get().add(new BuilderBatch(builder));
    }
    
    /** */
    public void postBuild(List<Builder> builders) {
        BuilderRunnable.get().add(new BuilderBatch(builders));
    }    
    
    /** */
    void setTerminable(boolean terminable) {
        status.setTerminable(terminable);
    }
    
    /** */
    public static Builders get() {
        if (instance == null) {
            instance = new Builders();
        }
        
        return instance;
    }
}