package com.andcreations.ae.studio.plugins.ui.common.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class TransferData {
    /** */
    public static boolean hasFileListTranserData(
        Transferable transferable) {
    //
        return transferable.isDataFlavorSupported(
            DataFlavor.javaFileListFlavor);
    }
    
    /** */
    @SuppressWarnings("unchecked")
    public static List<File> getFileListTransferData(
        Transferable transferable) {
    //
        try {
            return (List<File>)transferable.getTransferData(
                DataFlavor.javaFileListFlavor);
        } catch (UnsupportedFlavorException exception) {
            return null;
        } catch (IOException exception) {
            return null;
        }
    }      
}