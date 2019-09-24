package com.andcreations.ae.studio.plugins.ui.icons;

import javax.swing.ImageIcon;

/**
 * Icon source.
 * 
 * @author Mikolaj Gucki
 */
public interface IconSource {
	/**
	 * Checks if this icon source has an icon.
	 * 
	 * @param name The icon name.
	 * @return <code>true</code> if has, <code>false</code> otherwise.
	 */
	boolean hasIcon(String name);
	
	/**
	 * Gets an icon.
	 * 
	 * @param name The icon name.
	 * @return The icon or <code>null</code> if there is no such icon.
	 */
	ImageIcon getIcon(String name);
}
