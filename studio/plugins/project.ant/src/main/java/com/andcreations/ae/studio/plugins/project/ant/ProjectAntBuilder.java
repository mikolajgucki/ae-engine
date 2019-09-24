package com.andcreations.ae.studio.plugins.project.ant;

import java.io.File;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.ant.Ant;
import com.andcreations.ae.studio.plugins.builders.AbstractBuilder;

/**
 * @author Mikolaj Gucki
 */
class ProjectAntBuilder extends AbstractBuilder {
    /** */
    private File baseDir;
    
    /** */
    private String targetName;
    
    /** */
    ProjectAntBuilder(String id,String name,ImageIcon icon,String desc,
        File baseDir,String targetName) {
    //
        super(id,name,icon,desc);
        this.baseDir = baseDir;
        this.targetName = targetName;
    }
    
    /** */
    @Override
    public void build() {
        Ant.get().runNoExceptions(baseDir,targetName);
    }
}