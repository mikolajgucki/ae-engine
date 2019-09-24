package com.andcreations.android.project.gradle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;

import com.andcreations.ant.AntSingleValue;

/**
 * An Ant type which holds a list of Gradle dependencies.
 * 
 * @author Mikolaj Gucki
 */
public class GradleDependenciesAntType extends DataType {
	/** */
	private List<AntSingleValue> dependencies;
	
	/** */
	public AntSingleValue createDep() {
        if (isReference()) {
            throw noChildrenAllowed();
        }		
		if (dependencies == null) {
			dependencies = new ArrayList<>();
		}
		
		AntSingleValue value = new AntSingleValue();
		dependencies.add(value);
		
		return value;
	}
	
	@Override
	public void setRefid(Reference ref) {
		System.out.println("setRefId " + ref.getReferencedObject());
		super.setRefid(ref);
	}
	
	/** */
	public List<AntSingleValue> getDeps() {
		if (isReference()) {
			return ((GradleDependenciesAntType)getCheckedRef()).getDeps();
		}
		if (dependencies == null) {
			return null;
		}
		return Collections.unmodifiableList(dependencies);
	}
}
