package com.andcreations.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tools.ant.types.DirSet;
import org.apache.tools.ant.types.resources.FileResource;

/**
 * @author Mikolaj Gucki
 */
public class DirSetHelper {
	/**
	 * Gets the files for a directory set.
	 * 
	 * @param dirSet The directory set.
	 * @return The directories contained in the file set.
	 */
	public static File[] getDirs(DirSet dirSet) {
		List<File> dirs = new ArrayList<File>();
		
		Iterator<?> iterator = dirSet.iterator();
		while (iterator.hasNext() == true) {
			FileResource res = (FileResource)iterator.next();
			dirs.add(res.getFile());
		}
		
		return dirs.toArray(new File[]{});
	}
}
