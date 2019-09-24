package com.andcreations.ae.studio.plugins.text.editor;

import java.io.File;
import java.io.IOException;

import com.andcreations.ae.studio.plugins.project.files.ProjectFiles;

/**
 * The configuration data passed while opening a file for edit.
 *
 * @author Mikolaj Gucki
 */
public class EditorCfg {
    /** */
    static final int NO_VALUE = -1;
    
    /** */
    static final String PROJECT_FILE_FLAG = "project.file";
    
    /** The editor syntax. */
    private EditorSyntax syntax;
    
    /** The path to the file to edit. */
    private String filePath;
    
    /** The path relative to the project directory. */
    private String projectPath;
    
    /** The icon name. */
    private String iconName;
    
    /** The line number. */
    private int line = NO_VALUE;
    
    /** The start line number. */
    private int startLine = NO_VALUE;
    
    /** The end line number. */
    private int endLine = NO_VALUE;
    
    /** The offset in the line. */
    private int lineOffset = NO_VALUE;
    
    /**
     * Constructs a {@link EditorCfg}.
     *
     * @param syntax The editor syntax.
     * @param filePath The path to the file to edit.
     * @param iconName The icon name.
     */
    public EditorCfg(EditorSyntax syntax,String filePath,String iconName) {
        this.syntax = syntax;
        this.iconName = iconName;
        this.filePath = filePath;
        init();
    }
    
    /** */
    void init() {        
    // check if it's a project file
        File file = getFile();
        if (ProjectFiles.get().isProjectFile(file) == true) {
            projectPath = ProjectFiles.get().getRelativePath(file);
            filePath = file.getAbsolutePath();
        }
    }
    
    /** */
    String getId() {
        return EditorCfgId.toId(this);
    }
    
    /** */
    static EditorCfg fromId(String id) {
        return EditorCfgId.fromId(id);
    }
    
    /**
     * Gets the editor syntax.
     *
     * @return The editor syntax.
     */
    public EditorSyntax getEditorSyntax() {
        return syntax;
    }
    
    /**
     * Gets the path relative to the project directory or null if the file
     * is not a child of the project directory.
     *
     * @return The project path.
     */
    public String getProjectPath() {
        return projectPath;
    }
    
    /**
     * Gets the path to the file to edit.
     *
     * @return The file path.
     */
    public String getFilePath() {
        return filePath;
    }
    
    /**
     * Gets the icon name.
     *
     * @return The icon name.
     */
    public String getIconName() {
        return iconName;
    }
    
    /**
     * Sets the line number (starting from 1).
     *
     * @param line The line number.
     */
    public void setLine(int line) {
        if (startLine != NO_VALUE || endLine != NO_VALUE) {
            throw new IllegalStateException(
                "Either line or line range can be set");
        }
        this.line = line;
    }
    
    /**
     * Gets the line number (starting from 1).
     *
     * @return The line number.
     */
    public int getLine() {
        return line;
    }
    
    /**
     * Sets the line range (starting from 1).
     *
     * @param startLine The start line number.
     * @param endLine The end line number.
     */
    public void setLineRange(int startLine,int endLine) {
        if (line != NO_VALUE) {
            throw new IllegalStateException(
                "Either line or line range can be set");
        }
        
        this.startLine = startLine;
        this.endLine = endLine;
    }
    
    /**
     * Gets the start line number (starting from 1).
     *
     * @return The start line number.
     */
    public int getStartLine() {
        return startLine;
    }    
    
    /**
     * Gets the end line number (starting from 1).
     *
     * @return The end line number.
     */
    public int getEndLine() {
        return endLine;
    }    
    
    /**
     * Sets the line offset.
     *
     * @param lineOffset The line offset.
     */
    public void setLineOffset(int lineOffset) {
        this.lineOffset = lineOffset;
    }
    
    /**
     * Gets the line offset.
     *
     * @return The line offset.
     */
    public int getLineOffset() {
        return lineOffset != NO_VALUE ? lineOffset : 0;
    }
    
    /** */
    boolean hasLine() {
        return line != NO_VALUE;
    }
    
    /** */
    boolean hasLineRange() {
        return startLine != NO_VALUE && endLine != NO_VALUE;
    }
    
    /** */
    File getFile() {
    // project file
        if (projectPath != null) {
            return ProjectFiles.get().getFileFromRelativePath(projectPath);
        }
        
    // other file
        File file = new File(filePath);
        try {
            file = file.getCanonicalFile();
        } catch (IOException exception) {
        }
        return file;
    }
}