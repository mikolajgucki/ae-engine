package com.andcreations.ae.studio.plugins.problems;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.view.DefaultViewProvider;
import com.andcreations.resources.BundleResources;

/**
 * Provides info on number of warnings and errors.
 * 
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class ProblemsStatus extends JPanel {
    /** */
    private BundleResources res = new BundleResources(ProblemsStatus.class);         
    
    /** */
    private DefaultViewProvider viewProvider;
    
    /** */
    private JLabel left;
    
    /** */
    private JLabel center;
    
    /** */
    ProblemsStatus(DefaultViewProvider viewProvider) {
        this.viewProvider = viewProvider;
        create();
    }
    
    /** */
    private void create() {
        setLayout(new BorderLayout());
        
    // left
        left = new JLabel();
        left.addMouseListener(new MouseAdapter() {
            /** */
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    goToProblems();
                }
            }
        });
        add(left,BorderLayout.WEST);
        
    // center
        center = new JLabel();
        center.addMouseListener(new MouseAdapter() {
            /** */
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    goToProblems();
                }
            }
        });        
        add(center,BorderLayout.CENTER);
    }
    
    /** */
    private void goToProblems() {
        viewProvider.showView();
    }
    
    /** */
    void update(int warnings,int errors) {
    // no problems
        if (warnings == 0 && errors == 0) {
            left.setIcon(Icons.getIcon(DefaultIcons.OK));
            left.setText(res.getStr("no.problems"));
            left.setVisible(true);
            center.setVisible(false);
            return;
        }
        
    // errors
        if (errors > 0) {
            left.setIcon(Icons.getIcon(DefaultIcons.ERROR));
            left.setText(Integer.toString(errors) + "   ");
            left.setVisible(true);
        }
        else {
            left.setVisible(false);            
        }
                
    // warnings
        if (warnings > 0) {
            center.setIcon(Icons.getIcon(DefaultIcons.WARNING));
            center.setText(Integer.toString(warnings) + "   ");
            center.setVisible(true);
        }
        else {
            center.setVisible(false);
        }        
    }
}
