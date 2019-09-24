package com.andcreations.android.project.gradle;

import java.util.Collections;
import java.util.List;

/**
 * The Android gradle project configuration.
 * 
 * @author Mikolaj Gucki
 */
public class AndroidGradleProjectCfg {
	/** The application identifier (build.gradle). */
	private String applicationId;
	
	/** The application name (AndroidManifest.xml). */
	private String applicationName;
	
	/** Indicates if the project contains native source code (build.gradle). */
	private boolean nativeCode;
	
	/** The minimum SDK version (build.gradle, AndroidManifest.xml). */
	private String minSdkVersion;
	
	/** The target SDK version (build.gradle, AndroidManifest.xml). */
	private String targetSdkVersion;
	
	/** The compile SDK version (build.gradle). */
	private String compileSdkVersion;
	
	/** The package name (AndroidManifest.xml). */
	private String packageName;
	
	/** The activity name (AndroidManifest.xml). */
	private String activityName;
	
	/** The version code (build.gradle, AndroidManifest.xml). */
	private String versionCode = "1";
	
	/** The version name (build.gradle, AndroidManifest.xml). */
	private String versionName = "1.0";
	
	/** The CMake arguments. */
	private List<String> cmakeArgs;
	
	/** The dependencies. */
	private List<String> dependencies;
	
	/** */
	public String getApplicationId() {
		if (applicationId == null) {
			return packageName;
		}
		
		return applicationId;
	}

	/** */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	/** */
	public String getApplicationName() {
		return applicationName;
	}

	/** */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	/** */
	public boolean getNativeCode() {
		return nativeCode;
	}

	/** */
	public void setNativeCode(boolean nativeCode) {
		this.nativeCode = nativeCode;
	}

	/** */
	public String getMinSdkVersion() {
		return minSdkVersion;
	}

	/** */
	public void setMinSdkVersion(String minSdkVersion) {
		this.minSdkVersion = minSdkVersion;
	}

	/** */
	public String getTargetSdkVersion() {
		return targetSdkVersion;
	}

	/** */
	public void setTargetSdkVersion(String targetSdkVersion) {
		this.targetSdkVersion = targetSdkVersion;
	}

	/** */
	public String getCompileSdkVersion() {
		return compileSdkVersion;
	}

	/** */
	public void setCompileSdkVersion(String compileSdkVersion) {
		this.compileSdkVersion = compileSdkVersion;
	}

	/** */
	public String getPackageName() {
		return packageName;
	}

	/** */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/** */
	public String getActivityName() {
		return activityName;
	}

	/** */
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	/** */
	public String getVersionCode() {
		return versionCode;
	}

	/** */
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	/** */
	public String getVersionName() {
		return versionName;
	}

	/** */
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	
	/** */
	public List<String> getCMakeArgs() {
		return cmakeArgs;
	}
	
	/** */
	public void setCMakeArgs(List<String> cmakeArgs) {
		this.cmakeArgs = cmakeArgs;
	}

	/** */
	public List<String> getDependencies() {
		if (dependencies == null) {
			return null;
		}
		return Collections.unmodifiableList(dependencies);
	}

	/** */
	public void setDependencies(List<String> dependencies) {
		this.dependencies = dependencies;
	}
}
