package com.andcreations.ae.studio.plugins.ui.common.tree;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Mikolaj Gucki    
 */
@SuppressWarnings("serial")
public class LabelTreeNode extends DefaultMutableTreeNode {
    /** */
    private ImageIcon icon;
    
    /** */
    private String value;
    
    /** */
    private String htmlValue;
    
    /** */
    private String toolTip;
    
    /** */
    private List<LabelTreeNodeListener> listeners = new ArrayList<>();
    
    /** */
    private boolean canHaveChildren;
    
    /**
     * Constructs a {@link LabelTreeNode}.
     *
     * @param icon The icon.
     */
    public LabelTreeNode(String value) {
        this(null,value,value);
    }
    
    /**
     * Constructs a {@link LabelTreeNode}.
     *
     * @param icon The icon.
     * @param value The value.
     */
    public LabelTreeNode(ImageIcon icon,String value) {
        this(icon,value,value);
    }
    
    /**
     * Constructs a {@link LabelTreeNode}.
     *
     * @param icon The icon.
     * @param value The value.
     * @param htmlValue The value in HTML.
     */
    public LabelTreeNode(ImageIcon icon,String value,String htmlValue) {
        this.icon = icon;
        this.value = value;
        this.htmlValue = htmlValue;
    }    
    
    /**
     * Gets the icon.
     *
     * @return The icon.
     */
    public ImageIcon getIcon() {
        return icon;
    }
    
    /**
     * Sets the icon.
     *
     * @param icon The icon.
     */
    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }
    
    /**
     * Gets the value.
     *
     * @return The value.
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Sets the value.
     *
     * @param value The value.
     */
    public void setValue(String value) {
        this.value = value;
    }
    
    /**
     * Gets the value in HTML
     *
     * @return The value in HTML.
     */
    public String getHTMLValue() {
        return htmlValue;
    }
    
    /** 
     * Sets the HTML value.
     *
     * @param htmlValue The HTML value.
     */
    public void setHTMLValue(String htmlValue) {
        this.htmlValue = htmlValue;
    }
    
    /**
     * Gets the tool tip.
     *
     * @return The tool tip.
     */
    public String getToolTip() {
        return toolTip;
    }
    
    /**
     * Sets the tool tip.
     *
     * @param toolTip The tool tip.
     */
    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }
    
    /**
     * Sets the flag which indicates if this node can have children.
     *
     * @param canHaveChildren The flag.
     */
    public void setCanHaveChildren(boolean canHaveChildren) {
        this.canHaveChildren = canHaveChildren;
    }
    
    /**
     * Adds a node listener.
     *
     * @param listener The listener.
     */
    public void addLabelTreeNodeListener(LabelTreeNodeListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }    
    
    /** */
    protected void notifySelected() {
        synchronized (listeners) {
            for (LabelTreeNodeListener listener:listeners) {
                listener.labelTreeNodeSelected(this);
            }
        }
    }
       
    /** */
    protected void notifyDoubleClicked() {
        synchronized (listeners) {
            for (LabelTreeNodeListener listener:listeners) {
                listener.labelTreeNodeDoubleClicked(this);
            }
        }
    }    
    
    /** */
    protected void notifyCollapsed() {
        synchronized (listeners) {
            for (LabelTreeNodeListener listener:listeners) {
                listener.labelTreeNodeCollapsed(this);
            }
        }
    }
    
    /** */
    protected void notifyExpanded() {
        synchronized (listeners) {
            for (LabelTreeNodeListener listener:listeners) {
                listener.labelTreeNodeExpanded(this);
            }
        }
    }
    
    /** */
    @Override
    public boolean isLeaf() {
        if (canHaveChildren == true) {
            return false;
        }
        
        return super.isLeaf();
    }
    
    /** */
    @Override
    public boolean getAllowsChildren() {
        if (canHaveChildren == true) {
            return true;
        }
        
        return super.getAllowsChildren();
    }
    
    /** */
    public List<LabelTreeNode> getChildrenAsList() {
        List<LabelTreeNode> list = new ArrayList<>();
        
        Enumeration<?> children = children();
        while (children.hasMoreElements()) {
            list.add((LabelTreeNode)children.nextElement());
        }
        
        return list;
    }
    
    /** */
    boolean dragOver(DropTargetDragEvent event) {
        synchronized (listeners) {
            for (LabelTreeNodeListener listener:listeners) {
                if (listener.dragOver(this,event) == true) {
                    return true;
                }                    
            }
        }
        
        event.rejectDrag();
        return false;
    }
    
    /** */
    void drop(DropTargetDropEvent event) {
        synchronized (listeners) {
            for (LabelTreeNodeListener listener:listeners) {
                if (listener.drop(this,event) == true) {
                    return;
                }                    
            }
        }
        
        event.rejectDrop();
    }
}