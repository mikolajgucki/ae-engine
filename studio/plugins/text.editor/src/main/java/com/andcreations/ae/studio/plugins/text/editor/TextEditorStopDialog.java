package com.andcreations.ae.studio.plugins.text.editor;

import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
public class TextEditorStopDialog {
    /** */
    private BundleResources res =
		new BundleResources(TextEditorStopDialog.class);  	
	
	/** */
	boolean canStop() {
		if (TextEditor.get().hasDirtyEditors() == false) {
			return true;
		}
		
		return CommonDialogs.yesNo(res.getStr("title"),res.getStr("message"));
	}
}
