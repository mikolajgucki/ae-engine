package com.andcreations.ae.studio.plugins.file.explorer.tree.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mikolaj Gucki
 */
public class PopupActionRegistry {
    /** */
    private static PopupActionRegistry instance = new PopupActionRegistry();
    
    /** */
    private List<PopupAction> actions = new ArrayList<>();
    
    /** */
    private PopupActionRegistry() {
        addDefaultActions();
    }
    
    /** */
    private void addDefaultActions() {
    	register(new RefreshAction());
    	register(new AddFileAction());
    	register(new AddDirectoryAction());
    	register(new RenameAction());
    	register(new DeleteAction());
    }
    
    /** */
    public void register(PopupAction action) {
    	actions.add(action);
    }
    
    /** */
    public List<PopupAction> getActions() {
        return Collections.unmodifiableList(actions);
    }
    
    /** */
    public static PopupActionRegistry get() {
    	if (instance == null) {
    		instance = new PopupActionRegistry();
    	}
    	
    	return instance;
    }
}
