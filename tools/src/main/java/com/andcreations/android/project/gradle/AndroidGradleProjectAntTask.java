package com.andcreations.android.project.gradle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Reference;

import com.andcreations.ant.AETask;
import com.andcreations.ant.AntFlag;
import com.andcreations.ant.AntPath;
import com.andcreations.ant.AntSingleValue;

/**
 * Creates Gradle-based Android project.
 * 
 * @author Mikolaj Gucki
 */
public class AndroidGradleProjectAntTask extends AETask {
	/** */
	private static final String CMAKE_ARGS_SEPARATOR = " ";
	
	/** */
	private AntPath templateDir;
	
	/** */
	private AntPath sdkDir;
	
	/** */
	private AntPath ndkDir;
	
	/** */
	private AntSingleValue applicationName;
	
	/** */
	private AntFlag nativeCode;
	
	/** */
	private AntSingleValue minSdkVersion;
	
	/** */
	private AntSingleValue targetSdkVersion;
	
	/** */
	private AntSingleValue compileSdkVersion;
	
	/** */
	private AntSingleValue packageName;
	
	/** */
	private AntSingleValue activityName;
	
	/** */
	private AntSingleValue versionCode;
	
	/** */
	private AntSingleValue versionName;
	
	/** */
	private AntSingleValue cmakeArgs;

	/** */
	private GradleDependenciesAntType deps;
	
	/** */
	private GradleDependenciesAntType depsRef;
	
	/** */
	private AntPath dstDir;
	
	/** */
	public AntPath createTemplateDir() {
		if (templateDir != null) {
			duplicatedElement("templateDir");
		}
		
		templateDir = new AntPath();
		return templateDir;
	}
    
	/** */
	public AntPath createSdkDir() {
		if (sdkDir != null) {
			duplicatedElement("sdkDir");
		}
		
		sdkDir = new AntPath();
		return sdkDir;
	}
    
	/** */
	public AntPath createNdkDir() {
		if (ndkDir != null) {
			duplicatedElement("ndkDir");
		}
		
		ndkDir = new AntPath();
		return ndkDir;
	}
	
    /** */
    public AntSingleValue createApplicationName() {
    	if (applicationName != null) {
    		duplicatedElement("applicationName");
    	}
    	
    	applicationName = new AntSingleValue();
    	return applicationName;
    }
    
    /** */
    public AntFlag createNativeCode() {
    	if (nativeCode != null) {
    		duplicatedElement("nativeCode");
    	}
    	
    	nativeCode = new AntFlag();
    	return nativeCode;
    }
    
    /** */
    public AntSingleValue createMinSdkVersion() {
    	if (minSdkVersion != null) {
    		duplicatedElement("minSdkVersion");
    	}
    	
    	minSdkVersion = new AntSingleValue();
    	return minSdkVersion;
    }
    
    /** */
    public AntSingleValue createTargetSdkVersion() {
    	if (targetSdkVersion != null) {
    		duplicatedElement("targetSdkVersion");
    	}
    	
    	targetSdkVersion = new AntSingleValue();
    	return targetSdkVersion;
    }
    
    /** */
    public AntSingleValue createCompileSdkVersion() {
    	if (compileSdkVersion != null) {
    		duplicatedElement("compileSdkVersion");
    	}
    	
    	compileSdkVersion = new AntSingleValue();
    	return compileSdkVersion;
    }
	
    /** */
    public AntSingleValue createPackageName() {
    	if (packageName != null) {
    		duplicatedElement("packageName");
    	}
    	
    	packageName = new AntSingleValue();
    	return packageName;
    }
    
    /** */
    public AntSingleValue createActivityName() {
    	if (activityName != null) {
    		duplicatedElement("activityName");
    	}
    	
    	activityName = new AntSingleValue();
    	return activityName;
    }
    
    /** */
    public AntSingleValue createVersionCode() {
    	if (versionCode != null) {
    		duplicatedElement("versionCode");
    	}
    	
    	versionCode = new AntSingleValue();
    	return versionCode;
    }
    
    /** */
    public AntSingleValue createVersionName() {
    	if (versionName != null) {
    		duplicatedElement("versionName");
    	}
    	
    	versionName = new AntSingleValue();
    	return versionName;
    }
    
    /** */
    public AntSingleValue createCmakeArgs() {
    	if (cmakeArgs != null) {
    		duplicatedElement("cmakeArgs");
    	}
    	
    	cmakeArgs = new AntSingleValue();
    	return cmakeArgs;
    }
    
    /** */
    public GradleDependenciesAntType createDeps() {
    	if (deps != null) {
    		duplicatedElement("deps");    		
    	}
    	
    	deps = new GradleDependenciesAntType();
    	return deps;
    }
    
    /** */
    public void setDepsref(Reference reference) {
    	depsRef = (GradleDependenciesAntType)reference.getReferencedObject();
    }
    
	/** */
	public AntPath createDstDir() {
		if (dstDir != null) {
			duplicatedElement("dstDir");
		}
		
		dstDir = new AntPath();
		return dstDir;
	}
	
	/** */
	private List<String> getDependencies() {
		if (deps == null && depsRef == null) {
			return null;
		}
		List<String> allDeps = new ArrayList<>();
		
	// dependencies
		if (deps != null && deps.getDeps() != null) {
			for (AntSingleValue value:deps.getDeps()) {
				if (value.getValue() != null) {
					allDeps.add(value.getValue());
				}
			}
		}
		
	// reference dependencies
		if (depsRef != null && depsRef.getDeps() != null) {
			for (AntSingleValue value:depsRef.getDeps()) {
				if (value.getValue() != null) {
					allDeps.add(value.getValue());
				}
			}
		}
		
		return allDeps;
	}
    
	/** */
	private List<String> getCMakeArgs() {
		String[] args = cmakeArgs.getValue().split(CMAKE_ARGS_SEPARATOR);
		return Arrays.asList(args);
	}
	
	/** */
	@Override
	public void execute() throws BuildException {
		verifyElementSet(templateDir,"templateDir");
		verifyElementSet(sdkDir,"sdkDir");
		verifyElementSet(applicationName,"applicationName");
		verifyElementSet(minSdkVersion,"minSdkVersion");
		verifyElementSet(targetSdkVersion,"targetSdkVersion");
		verifyElementSet(compileSdkVersion,"compileSdkVersion");
		verifyElementSet(packageName,"packageName");
		verifyElementSet(activityName,"activityName");
		verifyElementSet(dstDir,"dstDir");
		
	// configure
		AndroidGradleProjectCfg cfg = new AndroidGradleProjectCfg();
		cfg.setApplicationName(applicationName.getValue());
		cfg.setMinSdkVersion(minSdkVersion.getValue());
		cfg.setTargetSdkVersion(targetSdkVersion.getValue());
		cfg.setCompileSdkVersion(compileSdkVersion.getValue());
		cfg.setPackageName(packageName.getValue());
		cfg.setActivityName(activityName.getValue());
		cfg.setNativeCode(nativeCode != null);
		if (cmakeArgs != null) {
			cfg.setCMakeArgs(getCMakeArgs());
		}
		cfg.setDependencies(getDependencies());
		
		File ndkDirFile = null;
		if (ndkDir != null) {
			ndkDirFile = ndkDir.getFile();
		}
	// create
		AndroidGradleProject project = new AndroidGradleProject(
			templateDir.getFile(),sdkDir.getFile(),ndkDirFile,
			dstDir.getFile(),cfg);
		try {
			project.createProject();
		} catch (IOException exception) {
			throw new BuildException(exception.getMessage());
		}
	}
}
