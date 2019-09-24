package com.andcreations.ae.studio.plugins.text.editor;

import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.Required;
import com.andcreations.ae.studio.plugin.RequiredPlugins;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id = {
    "com.andcreations.ae.studio.plugins.ui.icons",
    "com.andcreations.ae.studio.plugins.ui.main",
    "com.andcreations.ae.studio.plugins.ui.common",
    "com.andcreations.ae.studio.plugins.project.files"
})
@RequiredPlugins(id = {
    "com.andcreations.ae.studio.plugins.file"
})
public class TextEditorPlugin extends Plugin<TextEditorState> {
    /** */
    @Required(id = "com.andcreations.ae.studio.plugins.problems")
    private Object problemsPluginObject;
    
    /** */
    private TextEditorState state;
    
    /** */
    @Override
    public void start() {
        TextEditor.get().init(getState());
    }
    
    /** */
    @Override
    public boolean canStop() {
    	TextEditorStopDialog dialog = new TextEditorStopDialog();
    	return dialog.canStop();
    }
    
    /** */
    @Override
    public void stop() {
        TextEditor.get().stop();
    }
    
    /** */
    @Override
    public TextEditorState getState() {
        if (state == null) {
            state = new TextEditorState();
        }
        return state;
    }
    
    /** */
    @Override
    public void setState(TextEditorState state) {
        this.state = state;
    }
}