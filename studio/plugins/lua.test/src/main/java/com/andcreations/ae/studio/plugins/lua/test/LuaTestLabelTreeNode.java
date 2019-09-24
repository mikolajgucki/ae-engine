package com.andcreations.ae.studio.plugins.lua.test;

import java.io.File;

import javax.swing.ImageIcon;

import com.andcreations.ae.studio.plugins.lua.explorer.GoToLuaItem;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeNode;
import com.andcreations.ae.studio.plugins.ui.common.tree.LabelTreeNodeAdapter;

/**
 * @author Mikolaj Gucki
 */
@SuppressWarnings("serial")
class LuaTestLabelTreeNode extends LabelTreeNode {
    /** */
    static final int NO_LINE = -1;
    
    /** */
    private File file;
    
    /** */
    // TODO Decide what to with the field.
    @SuppressWarnings("unused")
    private int line = NO_LINE;
    
    /** */
    LuaTestLabelTreeNode(ImageIcon icon) {
        super(icon,null,null);
        create();
    }
    
    /** */
    LuaTestLabelTreeNode(String value) {
        super(value);
        create();
    }
    
    /** */
    LuaTestLabelTreeNode(ImageIcon icon,String value,String htmlValue) {
        super(icon,value,htmlValue);
        create();
    }
    
    /** */
    private void create() {
        addLabelTreeNodeListener(new LabelTreeNodeAdapter() {
            /** */
            @Override
            public void labelTreeNodeDoubleClicked(LabelTreeNode node) {
                if (file != null) {
                    String luaFuncName = getValue();
                    GoToLuaItem.get().goToLuaFunc(file,luaFuncName);
                }
                /*
                if (file != null) {
                    if (line == NO_LINE) {
                        LuaFile.edit(file);
                    }
                    else {
                        LuaFile.edit(file,line);
                    }
                }
                */
            }                
        });          
    }
    
    /** */
    void setFile(File file) {
        this.file = file;
    }
    
    /** */
    void setLine(int line) {
        this.line = line;
    }
}