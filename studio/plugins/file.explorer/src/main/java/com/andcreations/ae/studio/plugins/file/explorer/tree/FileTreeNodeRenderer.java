package com.andcreations.ae.studio.plugins.file.explorer.tree;

import com.andcreations.io.FileNode;

/**
 * @author Mikolaj Gucki
 */
public interface FileTreeNodeRenderer {
    /** */
    FileTreeNode createFileTreeNode(FileNode fileNode);
}