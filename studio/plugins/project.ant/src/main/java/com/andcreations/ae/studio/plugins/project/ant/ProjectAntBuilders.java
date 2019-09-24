package com.andcreations.ae.studio.plugins.project.ant;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.ant.AntIcons;
import com.andcreations.ae.studio.plugins.builders.Builders;
import com.andcreations.ae.studio.plugins.problems.Problem;
import com.andcreations.ae.studio.plugins.problems.ProblemSeverity;
import com.andcreations.ae.studio.plugins.problems.Problems;
import com.andcreations.ae.studio.plugins.project.files.ProjectFiles;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.ant.AntTargets;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class ProjectAntBuilders {
    /** */
    private static final String AE_STUDIO_TARGET_PREFIX = "ae.studio.";
    
    /** */
    private static ProjectAntBuilders instance;
    
    /** */
    private BundleResources res =
        new BundleResources(ProjectAntBuilders.class);      
    
    /** */
    private Problem targetsProblem;
    
    /** */
    private String getBuilderName(String targetName) {
        String name = targetName.substring(AE_STUDIO_TARGET_PREFIX.length());
        name = name.replace('_',' ');
        return name;
    }
    
    /** */
    private void createBuilder(File baseDir,String targetName,
        String targetDesc) {
    //
        if (targetDesc == null) {
            targetDesc = res.getStr("null.target.desc");
        }
        
        String id = String.format("project_ant_target_%s",targetName);
        String builderName = getBuilderName(targetName);
        
    // create builder
        ProjectAntBuilder builder = new ProjectAntBuilder(id,builderName,
            Icons.getIcon(AntIcons.ANT),targetDesc,baseDir,targetName);
        Builders.get().appendBuilder(builder);
    }
    
    /** */
    void createBuilders() {
        if (targetsProblem != null) {
            Problems.get().removeProblem(targetsProblem);
            targetsProblem = null;
        }
        
    // get targets
        File dir = ProjectFiles.get().getProjectDir();
        Map<String,String> targets = null;
        try {
            AntTargets antTargets = new AntTargets();
            targets = antTargets.listTargets(dir);
        } catch (IOException exception) {
            targetsProblem = Problems.get().add(
                ProjectAntBuilders.class.getName(),ProblemSeverity.WARNING,
                res.getStr("failed.to.get.targets",dir.getAbsolutePath(),
                exception.getMessage()));
            return;
        }
        
    // log
        StringBuilder msg = new StringBuilder();
        msg.append(res.getStr("found.targets",dir.getAbsolutePath()));
        boolean first = true;
        for (String name:targets.keySet()) {
            if (first == false) {
                msg.append("\n");
            }
            first = false;
            msg.append(res.getStr("found.target",name));
        }
        Log.trace(msg.toString());
        
    // AE studio targets
        for (String name:targets.keySet()) {
            if (name.startsWith(AE_STUDIO_TARGET_PREFIX) == true) {
                createBuilder(dir,name,targets.get(name));
            }
        }
    }
    
    /** */
    void deleteBuilders() {
    }
    
    /** */
    public static ProjectAntBuilders get() {
        if (instance == null) {
            instance = new ProjectAntBuilders();
        }
        
        return instance;
    }
}