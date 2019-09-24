package com.andcreations.ae.sdk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.BuildException;

import com.andcreations.ae.project.AEProjectProperties;
import com.andcreations.ae.project.NoSuchVariableException;
import com.andcreations.ant.AntPath;
import com.andcreations.io.DirScanner;
import com.andcreations.resources.BundleResources;
import com.andcreations.resources.StrResources;

/**
 * The superclass for Ant task which fetch files from an AndEngine project.
 *
 * @author Mikolaj Gucki
 */
public abstract class FetchProjectFilesAntTask extends UpdateProjectAntTask {
    /** The string resources. */
    private StrResources res =
        new BundleResources(FetchProjectFilesAntTask.class);
        
    /** The project property with the directories. */
    private String property;
    
    /** The list of the properties with directories to ignore. */
    private List<String> ignoreProperties = new ArrayList<String>();
    
    /** The files to ignore. */
    private List<String> ignoreFiles = new ArrayList<String>();
    
    /** The path to the destination directory. */
    private AntPath dstDir;

    /**
     * Constructs a {@link FetchProjectFilesAntTask}.
     *
     * @param property The project property with the directories from
     *   which to copy the files.
     */
    protected FetchProjectFilesAntTask(String property) {
        this.property = property;
    }
    
    /**
     * Adds a property with directories to ignore.
     *
     * @param ignoreProperty The ignore property. 
     */
    protected void addIgnoreProperty(String ignoreProperty) {
        ignoreProperties.add(ignoreProperty);
    }
    
    /** */
    public AntPath createDstDir() {
        if (dstDir != null) {
            duplicatedElement("dstdir");
        }
        
        dstDir = new AntPath();
        return dstDir;
    }
    
    /**
     * Makes the destination directory.
     */
    private void makeDstDir() {
        File dir = new File(dstDir.getPath());
        dir.mkdirs();
    }
    
    /**
     * Builds the list of the files to ignore.
     *
     * @param srcDirPath The path to the directory with the files to ignore.
     */
    private void buildIgnoreFileList(String srcDirPath) {
        File srcDir = new File(srcDirPath);
        if (srcDir.isAbsolute() == false) {
            srcDir = getProjectRelativeFile(srcDir);
        }
        if (srcDir.exists() == false) {
            return;
        }
        
    // list the files
        DirScanner dirScanner = new DirScanner(srcDir);
        String[] fileNames = dirScanner.build(); 

        for (String fileName:fileNames) {
            File file = new File(srcDir,fileName);
            if (file.isFile()) {
                try {
                    ignoreFiles.add(file.getCanonicalPath());
                } catch (IOException exception) {
                    throw new BuildException(exception.getMessage());
                }
            }
        }
    }
    
    /**
     * Builds the list of the files to ignore.
     */
    private void buildIgnoreFileList() {
        for (String ignoreProperty:ignoreProperties) {
        // get the value of the ignore property
            String value = getProperty(ignoreProperty);
            try {
                value = AEProjectProperties.replaceEnvVariables(value);
            } catch (NoSuchVariableException exception) {
                throw new BuildException(res.getStr("missing.env.var",
                    exception.getName(),property));
            }
            
        // directories from the property
            String[] dirs = AEProjectProperties.getDirs(value);
            for (String dir:dirs) {
                buildIgnoreFileList(dir);                
            }
        }
    }
    
    /**
     * Copies a file.
     *
     * @param srcFile The source file.
     * @param dstFile The destination file.
     */
    protected void copyFile(File srcFile,File dstFile) {
        try {
            FileUtils.copyFile(srcFile,dstFile);
        } catch (IOException exception) {
            throw new BuildException(res.getStr("file.copy.failed",
                srcFile.getAbsolutePath(),dstFile.getAbsolutePath(),
                exception.getMessage()));
        }        
    }
    
    /**
     * Fetches a file from the project.
     *
     * @param srcFile The source file.
     * @param dstFile The destination file.
     */
    protected abstract void fetchFile(File srcFile,File dstFile);
    
    /**
     * Checks if a file should be ignored while copying.
     *
     * @param file The file.
     * @return <code>true</code> if to ignore, <code>false</code> otherwise.
     */
    private boolean ignore(File file) {
        String canonicalPath;
        try {
            canonicalPath = file.getCanonicalPath();
        } catch (IOException exception) {
            throw new BuildException(exception.getMessage());
        }
        
        for (String ignoreFile:ignoreFiles) {
            if (canonicalPath.equals(ignoreFile)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Copies newer files from a source to a destination directory.
     *
     * @param srcDirPath The path to the source directory.
     * @param dstDirPath The path to the destination directory.
     * @return The number of copied files.
     */
    protected int copyFiles(String srcDirPath,String dstDirPath) {
        File srcDir = new File(srcDirPath);
        if (srcDir.isAbsolute() == false) {
            srcDir = getProjectRelativeFile(srcDir);
        }
        if (srcDir.exists() == false) {
            throw new BuildException(res.getStr(
                "src.dir.not.exists",srcDirPath));
        }
        
    // list the files
        DirScanner dirScanner = new DirScanner(srcDir);
        String[] fileNames = dirScanner.build();        
        
    // log
        String srcFullPath = null;
        try {
            srcFullPath = srcDir.getCanonicalPath();
        } catch (IOException exception) {
            srcFullPath = srcDir.getAbsolutePath();
        }
        log(res.getStr("copying.from",srcFullPath));
        
        int count = 0;
    // copy the files
        for (String fileName:fileNames) {
            File srcFile = new File(srcDir,fileName);
            if (srcFile.isFile() && ignore(srcFile) == false) {
                File dstFile = new File(dstDirPath,fileName);
                
            // copy if the destination file does not exist or the source
            // file is newer
                if (dstFile.exists() == false ||
                    FileUtils.isFileNewer(srcFile,dstFile) == true) {
                //
                    verbose(res.getStr("copying.file",fileName));
                    fetchFile(srcFile,dstFile);
                    count++;
                }
            }
        }       
        
        return count;
    }    
    
    /**
     * Copies all the files from directories given in a project propery.
     *
     * @param property The project property with the directories.
     * @param dstDirPath The path to the destination directory.
     */    
    protected void copyProjectFiles(String property,String dstDirPath) {
        if (getProperties().containsKey(property) == false) {
            return;
        }        
        
    // log
        String dstFullPath = null;
        File dstDir = new File(dstDirPath);
        try {
            dstFullPath = dstDir.getCanonicalPath();
        } catch (IOException exception) {
            dstFullPath = dstDir.getAbsolutePath();
        }
        log(res.getStr("copying.project.files.to",dstFullPath));
        
    // get directories from the property
        String value = getProperty(property);
        try {
            value = AEProjectProperties.replaceEnvVariables(value);
        } catch (NoSuchVariableException exception) {
            throw new BuildException(res.getStr("missing.env.var",
                exception.getName(),property));
        }
        
        int count = 0;
    // copy the files
        String[] dirs = AEProjectProperties.getDirs(value);
        for (String dir:dirs) {
            count = count + copyFiles(dir,dstDirPath);
        }        
        log(res.getStr("copied.files.count",Integer.toString(count)));
    }    
    
    /** */
    @Override
    public void execute() {
        super.execute();
        verifyElementSet(dstDir,"dstdir");
        makeDstDir();
        buildIgnoreFileList();
        copyProjectFiles(property,dstDir.getPath());
    }
}