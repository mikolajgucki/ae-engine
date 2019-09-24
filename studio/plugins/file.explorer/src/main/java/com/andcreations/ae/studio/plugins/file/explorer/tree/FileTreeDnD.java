package com.andcreations.ae.studio.plugins.file.explorer.tree;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.TreePath;

import com.andcreations.ae.studio.plugins.file.dnd.FileListTransferable;
import com.andcreations.ae.studio.plugins.file.transfer.FileTransfer;
import com.andcreations.ae.studio.plugins.ui.common.dnd.TransferData;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTree;
import com.andcreations.io.FileUtil;

/**
 * @author Mikolaj Gucki
 */
class FileTreeDnD {
    /** */
    private LabelTree tree;
    
    /** */
    FileTreeDnD(LabelTree tree) {
        this.tree = tree;
    }
    
    /** */
    private boolean canTransfer(FileTreeNode targetNode,List<File> files) {
        File targetFile = targetNode.getFileNode().getFile();
        
    // cannot move a directory to one of its descendants
        for (File file:files) {
            if (FileUtil.isAncestor(file,targetFile) == true) {
                return false;
            }
        }
        
        return true;
    }
    
    /** */    
    private void move(FileTreeNode targetNode,List<File> files) {
        FileTransfer.get().move(files,targetNode.getFileNode().getFile());
    }
    
    /** */    
    private void copy(FileTreeNode targetNode,List<File> files) {
        FileTransfer.get().copy(files,targetNode.getFileNode().getFile());
    }
    
    /** */
    boolean dragOver(FileTreeNode node,DropTargetDragEvent event) {
        if (node.getFileNode().isDirectory() == false) {
            return false;
        }
        
    // check the files
        List<File> files = TransferData.getFileListTransferData(
            event.getTransferable());
        if (files == null || files.isEmpty() == true) {
            return false;
        }
        
    // copy
        if (event.getDropAction() == DnDConstants.ACTION_COPY) {
            if (canTransfer(node,files) == true) {
                event.acceptDrag(DnDConstants.ACTION_COPY);
                return true;
            }
        }
    // move
        else if (event.getDropAction() == DnDConstants.ACTION_MOVE) {
            if (canTransfer(node,files) == true) {
                event.acceptDrag(DnDConstants.ACTION_MOVE);
                return true;
            }
        }
        
        return false;       
    }
    
    /** */
    boolean drop(FileTreeNode node,DropTargetDropEvent event) {
        if (TransferData.hasFileListTranserData(
            event.getTransferable()) == false) {
        //
            event.rejectDrop();
            return false;
        }
        event.acceptDrop(event.getDropAction());
        
        List<File> files = TransferData.getFileListTransferData(
            event.getTransferable());
        if (files != null && files.isEmpty() == false) {
            if (event.getDropAction() == DnDConstants.ACTION_MOVE) {
                move(node,files);
            }
            else if (event.getDropAction() == DnDConstants.ACTION_COPY) {
                copy(node,files);
            }
        }
        
        return true;
    }    
    
    /** */
    boolean dragGestureRecognized(DragGestureEvent event) {
        TreePath[] paths = tree.getSelectionPaths();
    // if no path is selected
        if (paths == null) {
            return false;
        }      
        
        List<File> files = new ArrayList<>();
    // for each selected node
        for (TreePath path:paths) {
            Object component = path.getLastPathComponent(); 
            FileTreeNode node = (FileTreeNode)component;
            files.add(node.getFileNode().getFile());
        }
        
    // drag source listener
        DragSourceAdapter adapter = new DragSourceAdapter() {
            /** */
            @Override
            public void dragOver(DragSourceDragEvent event) {
                int action = event.getDropAction();
                if (action == DnDConstants.ACTION_MOVE) {
                    event.getDragSourceContext().setCursor(
                        DragSource.DefaultMoveDrop);
                }
                else if (action == DnDConstants.ACTION_COPY) {
                    event.getDragSourceContext().setCursor(
                        DragSource.DefaultCopyDrop);
                }
            }
            
            /** */
            @Override
            public void dragExit(DragSourceEvent event) {
                event.getDragSourceContext().setCursor(
                    DragSource.DefaultMoveNoDrop);
            }
        };
            
    // start drag
        event.startDrag(DragSource.DefaultCopyDrop,
            new FileListTransferable(files),adapter);
        
        return true;
    }
}