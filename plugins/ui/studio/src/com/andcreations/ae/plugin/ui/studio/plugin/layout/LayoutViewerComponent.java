package com.andcreations.ae.plugin.ui.studio.plugin.layout;

import java.util.List;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import java.io.File;

import com.andcreations.resources.BundleResources;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.common.UIColors;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class LayoutViewerComponent extends JPanel {
    /** */
    private BundleResources res =
        new BundleResources(LayoutViewerComponent.class);  
    
    /** */
    private LayoutViewerComponentListener listener;
    
    /** */
    private JLabel errorLabel;
    
    /** */
    private LayoutComponent layoutComponent;
    
    /** */
    private JLabel sizeLabel;
    
    /** */
    private JButton addToLayoutsButton;
    
    /** */
    private File addToLayoutsFile;
    
    /** */
    private int margin;
    
    /** */
    private int rootWidth;
    
    /** */
    private int rootHeight;
    
    /** */
    LayoutViewerComponent(LayoutViewerComponentListener listener) {
        this.listener = listener;
        create();
    }
    
    /** */
    private void create() {
    // margin, border size)
        margin = (int)(getFont().getSize() / 2);
        int borderSize = margin / 2;
        
    // border, layout
        setBorder(BorderFactory.createEmptyBorder(
            borderSize,borderSize,borderSize,borderSize));        
        setLayout(new BorderLayout());

    // add to layouts button
        addToLayoutsButton = new JButton("");
        addToLayoutsButton.addActionListener(new ActionListener() {
            /** */
            @Override
            public void actionPerformed(ActionEvent event) {
                listener.addToLayouts(addToLayoutsFile);
            }
        });
        
    // error
        errorLabel = new JLabel();
        errorLabel.setForeground(UIColors.error());
        errorLabel.setIcon(Icons.getIcon(DefaultIcons.ERROR));
        add(errorLabel,BorderLayout.NORTH);
        
    // layout component
        layoutComponent = new LayoutComponent(margin,
            new LayoutComponentListener() {
                /** */
                @Override
                public void componentsHighlighted(
                    List<ComponentLuaResult> components) {
                //
                    highlighted(components);
                }
            });
        add(layoutComponent,BorderLayout.CENTER);
        
    // aspect
        sizeLabel = new JLabel("",SwingConstants.CENTER);
        add(sizeLabel,BorderLayout.SOUTH);
        
    // ancestor listener
        addAncestorListener(new AncestorListener() {
            /** */                
            @Override
            public void ancestorAdded(AncestorEvent event) {
                listener.layoutViewerComponentShown();
            }
            
            /** */
            @Override
            public void ancestorMoved(AncestorEvent event) {
            }
            
            /** */
            @Override
            public void ancestorRemoved(AncestorEvent event) {
            }    
        });
        
    // layout component listener
        layoutComponent.addComponentListener(new ComponentAdapter() {
            /** */
            @Override
            public void componentResized(ComponentEvent event) {
                listener.layoutViewerComponentResized();
            }
        });
        
    // nothing initially
        clear();
    }

    /** */
    private void setSizeLabel() {
        sizeLabel.setText(res.getStr("size",Integer.toString(rootWidth),
            Integer.toString(rootHeight)));
    }
    
    /** */
    private void highlighted(List<ComponentLuaResult> components) {
        if (components.size() == 0) {
            setSizeLabel();
            return;
        }
        
        StringBuilder str = new StringBuilder();
        for (ComponentLuaResult component:components) {
            if (str.length() > 0) {
                str.append(", ");
            }
            str.append(component.getId());
        }
        sizeLabel.setText(str.toString());                
    }
    
    /** */
    void setError(String msg,String tooltip) {
        errorLabel.setText(msg);
        errorLabel.setToolTipText(tooltip);
        
        clear();
        add(errorLabel,BorderLayout.NORTH);
        errorLabel.setVisible(true);
    }
    
    /** */
    void setRoot(ComponentLuaResult root,int rootWidth,int rootHeight) {
        layoutComponent.setRoot(root,rootWidth,rootHeight);
        this.rootWidth = rootWidth;
        this.rootHeight = rootHeight;
        setSizeLabel();
        
        clear();
        layoutComponent.setVisible(true);
        sizeLabel.setVisible(true);
    }
    
    /** */
    void clear() {
        remove(addToLayoutsButton);
        addToLayoutsButton.setVisible(false);
        
        remove(errorLabel);
        errorLabel.setVisible(false);
        
        layoutComponent.setVisible(false);
        sizeLabel.setVisible(false);
    }
    
    /** */
    void tryAddToLayouts(File file) {
        addToLayoutsFile = file;
        
        clear();
        addToLayoutsButton.setText(res.getStr("add.to.layouts",
            addToLayoutsFile.getName()));
        add(addToLayoutsButton,BorderLayout.NORTH);
        addToLayoutsButton.setVisible(true);
    }
}