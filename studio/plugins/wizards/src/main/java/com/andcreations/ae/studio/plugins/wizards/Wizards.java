package com.andcreations.ae.studio.plugins.wizards;

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
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class Wizards {
    /** */
    private static Wizards instance;
    
    /** */
    private BundleResources res = new BundleResources(Wizards.class);     
    
    /** */
    private List<Wizard> wizards = new ArrayList<>();
    
    /** */
    private WizardDialog wizardDialog;
    
    /** */
    void init() {
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {        
                addRunWizardMenu();
                createWizardDialog();
            }
        });        
    }
    
    /** */
    public void addWizard(Wizard wizard) {
        wizards.add(wizard);
    }
    
    /** */
    List<Wizard> getWizards() {
        return Collections.unmodifiableList(wizards);
    }
    
    /** */
    private void addRunWizardMenu() {
        JMenuItem item = new JMenuItem(res.getStr("run.wizard"));
        item.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_W,UIKeys.menuKeyMask() | KeyEvent.SHIFT_DOWN_MASK));
        item.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                runWizard();
            }
        });
        
        ProjectMenu.get().add(item);           
    }    
    
    /** */
    private void runWizard() {
        Wizard wizard = wizardDialog.show();
        if (wizard != null) {
            wizard.runWizard();
        }
    }
    
    /** */
    private void createWizardDialog() {
        wizardDialog = new WizardDialog();
    }
    
    /** */
    public static Wizards get() {
        if (instance == null) {
            instance = new Wizards();
        }
        
        return instance;
    }
}