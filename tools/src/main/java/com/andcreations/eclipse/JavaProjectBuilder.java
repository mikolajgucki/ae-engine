package com.andcreations.eclipse;

import java.io.File;
import java.io.IOException;

/**
 * @author Mikolaj Gucki
 */
public class JavaProjectBuilder extends ProjectBuilder {
    /** The Java project configuration. */
    private JavaProjectCfg javaCfg;
    
    /**
     * Constructs a {@link JavaProjectBuilder}.
     *
     * @param javaCfg The Java project configuration.
     */
    public JavaProjectBuilder(JavaProjectCfg javaCfg) {
        super(javaCfg);
        this.javaCfg = javaCfg;
    }
    
    /**
     * Gets the Java project configuration.
     *
     * @return The configuration.
     */
    public JavaProjectCfg getJavaProjectCfg() {
        return javaCfg;
    }
    
    /**
     * Builds the project.
     *
     * @param dstDir The destination directory.
     */
    public void build(File dstDir) throws IOException {
        super.build(dstDir);
        writeTemplate(dstDir,".project","java_project.vm");
        writeTemplate(dstDir,".classpath","java_classpath.vm");
    }
}