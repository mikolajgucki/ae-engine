package com.andcreations.ae.studio.plugins.file.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class FileListTransferable implements Transferable {
    /** */
    private static final DataFlavor[] FLAVORS = 
        new DataFlavor[]{DataFlavor.javaFileListFlavor};
    
    /** */
    private List<File> files;
    
    /** */
    public FileListTransferable(List<File> files) {
        this.files = files;
    }
    
    /** */
    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return FLAVORS;            
    }

    /** */
    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return DataFlavor.javaFileListFlavor.equals(flavor);
    }

    /** */
    @Override
    public Object getTransferData(DataFlavor flavor)
        throws UnsupportedFlavorException,IOException {
    //
        if (flavor == DataFlavor.javaFileListFlavor) {
            return files;
        }
        throw new UnsupportedFlavorException(flavor); 
    }
}