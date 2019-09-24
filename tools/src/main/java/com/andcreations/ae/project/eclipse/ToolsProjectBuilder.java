package com.andcreations.ae.project.eclipse;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import com.andcreations.ae.dist.AEDistFiles;
import com.andcreations.ae.project.AEProject;
import com.andcreations.eclipse.JavaProjectBuilder;
import com.andcreations.eclipse.JavaProjectCfg;
import com.andcreations.eclipse.LinkedFolder;
import com.andcreations.io.DirScanner;

/**
 * Builds an Eclipse project for project tools. 
 * 
 * @author Mikolaj Gucki
 */
public class ToolsProjectBuilder {
    /** */
    private ToolsProjectBuilderCfg cfg;
    
    /** */
    public ToolsProjectBuilder(ToolsProjectBuilderCfg cfg) {
        this.cfg = cfg;
    }
    
    /** */
    private String getAbsPath(String path) {
    	return path.replace("\\","/");
    }
    
    /** */
    private String getAbsPath(File file) {
        return getAbsPath(file.getAbsolutePath());
    }
    
    /** */
    private String[] getJavaLibs(File dir) {
    	DirScanner scanner = new DirScanner(dir,false);
    	String[] libs = scanner.build(new FileFilter() {
	          /** */
	          @Override
	          public boolean accept(File file) {
	              return file.isFile() && file.getName().endsWith(".jar") ||
            		  file.isDirectory();
	          }
	      });
    	return libs;
    }
    
    /** */
    private void addJavaLibs(JavaProjectCfg javaCfg,File libsDir,
        String libsPath) {
    //
        String[] libs = getJavaLibs(libsDir);
        for (String lib:libs) {
    		javaCfg.addLib(libsPath + "/" + getAbsPath(lib));
        }
    }
    
    /** */
    private void addLibsDir(JavaProjectCfg javaCfg,File libsDir,
		String libsPath) {
	//
    	try {
    		libsDir = libsDir.getCanonicalFile();
    	} catch (IOException exception) {
    		// Simply won't be canonical
    	}
        javaCfg.addLinkedResource(new LinkedFolder(
    		libsPath,getAbsPath(libsDir)));
        addJavaLibs(javaCfg,libsDir,libsPath);    	
    }

    /** */
    private void addAntLibs(JavaProjectCfg javaCfg) {
        File antLibsDir = new File(cfg.getAntHome(),"lib");
        addLibsDir(javaCfg,antLibsDir,"libs/ant");
    }
    
    /** */
    private void addAEDistLibs(JavaProjectCfg javaCfg) {
        File aeLibsDir = new File(cfg.getAEDistDir(),AEDistFiles.LIBS_PATH);
        addLibsDir(javaCfg,aeLibsDir,"libs/ae");
    }
    
    /** */
    private void addToolsLibs(JavaProjectCfg javaCfg) {
        File toolsLibsDir = new File(cfg.getProjectDir(),
    		AEProject.TOOLS_LIBS_PATH);
        addLibsDir(javaCfg,toolsLibsDir,"libs/tools");    	
    }
    
    /** */
    public void createProject(File dstDir) throws IOException {
        String eclipseProjectName = cfg.getProjectName();
        
    // configure
        JavaProjectCfg javaCfg = new JavaProjectCfg(eclipseProjectName);
        javaCfg.addSrcDir("src");
        javaCfg.setOutputDir("build/classes");
        javaCfg.addDir("libs");
        
    // Java sources
        File javaSrc = new File(cfg.getProjectDir(),AEProject.TOOLS_JAVA_SRC);
        javaCfg.addLinkedResource(new LinkedFolder("src",getAbsPath(javaSrc)));

    // libs
        addAntLibs(javaCfg);
        addAEDistLibs(javaCfg);
        addToolsLibs(javaCfg);
        
    // build
        JavaProjectBuilder builder = new JavaProjectBuilder(javaCfg);
        builder.build(new File(dstDir,eclipseProjectName));        
    }
}
