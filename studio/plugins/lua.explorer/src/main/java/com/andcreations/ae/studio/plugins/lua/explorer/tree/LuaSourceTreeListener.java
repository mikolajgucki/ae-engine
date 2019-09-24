package com.andcreations.ae.studio.plugins.lua.explorer.tree;

import com.andcreations.io.FileNode;

/**
 * @author Mikolaj Gucki
 */
public interface LuaSourceTreeListener {
	/**
	 * Called when a file node has been double clicked.
	 * 
	 * @param fileNode The file node.
	 */
	void fileNodeDoubleClicked(FileNode fileNode);
}
