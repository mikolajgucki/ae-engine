package com.andcreations.ae.studio.plugins.assets.textures;

import java.io.File;

import com.andcreations.ae.studio.plugins.outline.Outline;
import com.andcreations.ae.studio.plugins.text.editor.EditorMediator;
import com.andcreations.ae.studio.plugins.text.editor.TextEditor;
import com.andcreations.ae.studio.plugins.text.editor.TextEditorAdapter;

/**
 * @author Mikolaj Gucki
 */
class TexPackOutline {
    /** */
    TexPackOutline() {
        create();
    }
    
    /** */
    private void create() {
    // text editor listener
        TextEditor.get().addTextEditorListener(new TextEditorAdapter() {
            /** */
            @Override
            public void focusGained(EditorMediator editor) {
                updateOutline(editor);
            }                
            
            /** */
            @Override
            public void fileSaved(EditorMediator editor) {
                updateOutline(editor);
            }                
        });
    }
    
    /** */
    private void updateOutline(EditorMediator editor) {
        File file = editor.getFile();
        if (TexPack.get().isTexPackFile(file) == false) {
            return;
        }
        
        Outline.get().clear();
    }
}