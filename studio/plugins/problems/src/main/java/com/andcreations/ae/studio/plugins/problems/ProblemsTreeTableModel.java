package com.andcreations.ae.studio.plugins.problems;

import java.util.List;

import javax.swing.tree.TreePath;

import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.resources.BundleResources;

/**
 * The problem tree table model.
 *
 * @author Mikolaj Gucki
 */
class ProblemsTreeTableModel extends AbstractTreeTableModel {
    /** */
    private static final int COLUMN_DESCRIPTION = 0;
    
    /** */
    private static final int COLUMN_RESOURCE = 1;
    
    /** */
    private static final int COLUMN_LOCATION = 2;
    
    /** */
    private static final int COLUMN_TYPE = 3;
    
    /** */
    private BundleResources res =
        new BundleResources(ProblemsTreeTableModel.class);    
        
    /** The groups of problems of the same severity. */
    private List<ProblemGroup> groups;
    
    /** */
    private String[] columns;
    
    /** */
    ProblemsTreeTableModel(List<ProblemGroup> groups) {
        super(new Object());
        this.groups = groups;
        create();
    }

    /** */
    private void create() {
        columns = new String[]{
            res.getStr("description"),
            res.getStr("resource"),
            res.getStr("location"),
            res.getStr("type")
        };
    }
    
    /** */
    @Override
    public int getColumnCount() {
        return columns.length;
    }
    
    /** */
    @Override
    public String getColumnName(int column) {
        return columns[column];
    }    
    
    /** */
    @Override
    public boolean isLeaf(Object node) {
        return node instanceof Problem;
    }
    
    /** */    
    @Override
    public Object getValueAt(Object node,int column) {
        if (node instanceof ProblemGroup) {
            ProblemGroup group = (ProblemGroup)node;
            switch (column) {
                case COLUMN_DESCRIPTION:
                    return group.getDescription();
            }
        }
        
        if (node instanceof Problem) {
            Problem problem = (Problem)node;
            switch (column) {
                case COLUMN_DESCRIPTION:
                    return problem.getDescription();
                case COLUMN_RESOURCE:
                    return problem.getResource();
                case COLUMN_LOCATION:
                    return problem.getLocation();
                case COLUMN_TYPE:                    
                    return problem.getTypeDisplayText();
            }
        }
        
        return null;
    }
    
    /** */
    @Override
    public int getIndexOfChild(Object parent,Object child) {
        ProblemGroup group = (ProblemGroup)parent;
        Problem problem = (Problem)child;
        
        return group.indexOf(problem);
    }
    
    /** */
    @Override
    public int getChildCount(Object parent) {
        if (parent instanceof ProblemGroup) {
            ProblemGroup group = (ProblemGroup)parent;
            return group.size();
        }
        
        return groups.size();
    }
    
    /** */
    @Override
    public Object getChild(Object parent,int index) {
        if (parent instanceof ProblemGroup) {
            ProblemGroup group = (ProblemGroup)parent;
            return group.get(index);
        }
        
        return groups.get(index);
    }
    
    /** */
    void problemAdded(final ProblemGroup group,final Problem problem) {
        UICommon.invoke(new Runnable() {
            /** */
            @Override
            public void run() {
                modelSupport.fireChildAdded(
                    new TreePath(new Object[]{getRoot(),group}),
                    group.indexOf(problem),problem);
            }
        });
    }
    
    /** */
    void problemRemoved(final ProblemGroup group,final int index,
        final Problem problem) {
    //
        UICommon.invoke(new Runnable() {
            /** */
            @Override
            public void run() {
                modelSupport.fireChildRemoved(
                    new TreePath(new Object[]{getRoot(),group}),index,problem);
            }
        });
    }
}