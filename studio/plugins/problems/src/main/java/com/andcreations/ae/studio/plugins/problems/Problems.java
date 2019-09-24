package com.andcreations.ae.studio.plugins.problems;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ae.studio.plugins.ui.main.MainFrame;
import com.andcreations.ae.studio.plugins.ui.main.StatusBar;
import com.andcreations.ae.studio.plugins.ui.main.view.DefaultViewProvider;
import com.andcreations.ae.studio.plugins.ui.main.view.SingleViewFactory;
import com.andcreations.ae.studio.plugins.ui.main.view.ViewCategory;
import com.andcreations.resources.BundleResources;

/**
 * The problems.
 *
 * @author Mikolaj Gucki
 */
public class Problems {
    /** The view identifier. */
    private static final String VIEW_ID =
        "com.andcreations.ae.studio.plugins.problems";    
    
    /** The class instance. */
    private static Problems instance;
    
    /** */
    private BundleResources res =
        new BundleResources(Problems.class);     
        
    /** The table model. */
    private ProblemsTreeTableModel model;
    
    /** The view component. */
    private ProblemsViewComponent component;
    
    /** The status displayed in the status bar. */
    private ProblemsStatus status;
    
    /** The groups of problems of the same severity. */
    private List<ProblemGroup> groups = new ArrayList<>();
    
    /** The view provider. */
    private DefaultViewProvider viewProvider;
    
    /** */
    Problems() {
        create();
    }
    
    /** */
    private void create() {
    // groups
        for (ProblemSeverity severity:ProblemSeverity.values()) {
            groups.add(new ProblemGroup(severity));
        }
    
    // model
        model = new ProblemsTreeTableModel(groups);    
    }        
    
    /** */
    void initUI() {
    // component
        UICommon.invokeAndWait(new Runnable() {
            /** */
            @Override
            public void run() {
                component = new ProblemsViewComponent(model);
            }
        });
        
        final String title = res.getStr("view.title");
        ImageIcon icon = Icons.getIcon(DefaultIcons.PROBLEM);
        
    // view factory
        SingleViewFactory factory = new SingleViewFactory(
            VIEW_ID,component,title,icon);
        factory.setViewCategory(ViewCategory.LOG);
        MainFrame.get().getViewManager().registerViewFactory(factory);
        
    // view provider
        viewProvider = new DefaultViewProvider(title,icon,factory,VIEW_ID);
        MainFrame.get().getViewManager().addViewProvider(viewProvider);
        
    // status
        UICommon.invoke(new Runnable() {
            public void run() {
                status = new ProblemsStatus(viewProvider);
                StatusBar.get().addStatusComponent(status,96);
                updateStatus();
            }
        });
    }    
    
    /** */
    private ProblemGroup getGroup(ProblemSeverity severity) {
        for (ProblemGroup group:groups) {
            if (group.getSeverity() == severity) {
                return group;
            }
        }
        
        return null;
    }
    
    /** */
    public void addProblem(Problem problem) {
        ProblemGroup group = getGroup(problem.getSeverity());
        group.add(problem);
        model.problemAdded(group,problem);
        updateStatus();
    }
    
    /** */
    private void updateStatus() {
    // if not yet initialized
        if (status == null) {
            return;
        }
        
        UICommon.invoke(new Runnable() {
            /** */
            @Override
            public void run() {
                status.update(
                    getGroup(ProblemSeverity.WARNING).size(),
                    getGroup(ProblemSeverity.ERROR).size());
            }
        });
    }
    
    /**
     * Adds a problem.
     *
     * @param sourceId The problem source identifier.
     * @param severity The severity.
     * @param description The description.
     * @param resource The resource in which the problem occurs.
     * @param location The location in the resource.
     * @param type The problem type.
     * @return The problem.
     */
    public Problem add(String sourceId,ProblemSeverity severity,
        String description,String resource,String location,ProblemType type) {
    //
        Problem problem = new Problem(sourceId,severity,description);
        problem.setResource(resource);        
        problem.setLocation(location);
        problem.setType(type);
        addProblem(problem);
        
        updateStatus();
        return problem;
    }    
    
    /**
     * Adds a problem.
     *
     * @param sourceId The problem source identifier.
     * @param severity The severity.
     * @param description The description.
     * @param resource The resource in which the problem occurs.
     * @param location The location in the resource.
     * @return The problem.
     */
    public Problem add(String sourceId,ProblemSeverity severity,
        String description,String resource,String location) {
    //
        return add(sourceId,severity,description,resource,location,null);
    }
    
    /**
     * Adds a problem.
     *
     * @param sourceId The problem source identifier.
     * @param severity The severity.
     * @param description The description.
     * @param resource The resource in which the problem occurs.
     * @return The problem.
     */
    public Problem add(String sourceId,ProblemSeverity severity,
        String description,String resource) {
    //
        return add(sourceId,severity,description,resource,null,null);
    }
    
    /**
     * Adds a problem.
     *
     * @param sourceId The problem source identifier.
     * @param severity The severity.
     * @param description The description.
     * @return The problem.
     */
    public Problem add(String sourceId,ProblemSeverity severity,
        String description) {
    //
        return add(sourceId,severity,description,null,null);
    }

    /** */
    private void removeBySourceId(String sourceId,ProblemGroup group) {
        while (true) {
            Problem problemToRemove = null;
            for (int index = 0; index < group.size(); index++) {
                Problem problem = group.get(index);
                if (problem.getSourceId().equals(sourceId)) {
                    problemToRemove = problem;
                    break;
                }
            }
            
            if (problemToRemove == null) {
                break;
            }
            removeProblem(problemToRemove);
        }
    }
    
    /**
     * Removes all the problems by source identifier.
     *
     * @param sourceId The problem source identifier.
     */
    public void removeBySourceId(String sourceId) {
        for (ProblemGroup group:groups) {
            removeBySourceId(sourceId,group);
        }
        updateStatus();
    }
    
    /** */
    public void removeProblem(Problem problem) {
        ProblemGroup group = getGroup(problem.getSeverity());
        int index = group.remove(problem);
        if (index >= 0) {
            model.problemRemoved(group,index,problem);
            updateStatus();
        }
    }
    
    /** */
    public boolean hasProblems(ProblemSeverity severity,ProblemType type) {
        ProblemGroup group = getGroup(severity);
        for (int index = 0; index < group.size(); index++) {
            Problem problem = group.get(index);
            if (type.equals(problem.getType()) == true) {
                return true;
            }
        }
        return false;
    }
    
    /** */
    public boolean hasErrors(ProblemType type) {
        return hasProblems(ProblemSeverity.ERROR,type);
    }
    
    /** */
    public boolean hasWarnings(ProblemType type) {
        return hasProblems(ProblemSeverity.WARNING,type);
    }
    
    /**
     * Gets the class instance.
     *
     * @return The class instance.
     */
    public static Problems get() {
        if (instance == null) {
            instance = new Problems();
        }
        
        return instance;
    }
}