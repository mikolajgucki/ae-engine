package com.andcreations.ae.studio.plugins.file.explorer.tree;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.file.Files;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeNode;
import com.andcreations.ae.studio.plugins.ui.icons.DefaultIcons;
import com.andcreations.ae.studio.plugins.ui.icons.Icons;
import com.andcreations.io.FileNode;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
public class FileTreeNode extends LabelTreeNode {
    /** */
    private String iconName;
    
    /** */
    private FileNode fileNode;
    
    /** */
    private FileTreeNode parentNode;
    
    /** */
    public FileTreeNode(String iconName,String value,FileNode fileNode) {
        super(null,value);
        this.iconName = iconName;
        this.fileNode = fileNode;
        setUserObject(fileNode);
    }
    
    /** */
    public FileTreeNode(String iconName,String value,String htmlValue,
        FileNode fileNode) {
    //
        super(null,value,htmlValue);
        this.iconName = iconName;
        this.fileNode = fileNode;
        setUserObject(fileNode);
    }
    
    /** */
    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
    
    /** */
    @Override
    public ImageIcon getIcon() {
        if (super.getIcon() != null) {
            return super.getIcon();
        }
        
        List<String> iconNames = new ArrayList<>();
        iconNames.add(iconName);
        
    // errors, warnings
        if (hasErrors() == true) {
            iconNames.add(DefaultIcons.DECO_ERROR);
        } if (hasWarnings() == true) {
            iconNames.add(DefaultIcons.DECO_WARNING);
        }
        
        return Icons.getIcon(iconNames);
    }
    
    /** */
    public FileNode getFileNode() {
        return fileNode;
    }
    
    /** */
    public File getFile() {
        return fileNode.getFile();
    }
    
    /** */
    void setParentNode(FileTreeNode parentNode) {
        this.parentNode = parentNode;
    }
    
    /** */
    public FileTreeNode getParentNode() {
        return parentNode;
    }
    
    /** */
    private boolean hasErrors() {
        return Files.get().hasErrors(getFile());
    }
    
    /** */
    private boolean hasWarnings() {
        return Files.get().hasWarnings(getFile());
    }
    
    /** */
    @Override    
    public String toString() {
        return getFile().getAbsolutePath();
    }
}