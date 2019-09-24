	package com.andcreations.android.project.gradle;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;

import com.andcreations.io.DirScanner;
import com.andcreations.io.FileUtil;
import com.andcreations.resources.BundleResources;
import com.andcreations.velocity.VelocityUtil;

/**
 * Responsible for setting update an Android gradle project.
 * 
 * @author Mikolaj Gucki
 */
public class AndroidGradleProject {
    /** */
    private BundleResources res = new BundleResources(AndroidGradleProject.class); 
    
	/** The directory with the project template. */
	private File templateDir;

	/** The directory with the Android SDK. */
	private File sdkDir;

	/** The directory with the Android NDK. */
	private File ndkDir;
	
	/** The destination directory. */
	private File dstDir;
	
	/** The project configuration. */
	private AndroidGradleProjectCfg cfg;
	
	/** The Velocity context. */
	private Map<String,Object> context;
	
	/** */
	public AndroidGradleProject(File templateDir,File sdkDir,File ndkDir,
		File dstDir,AndroidGradleProjectCfg cfg) {
	//
		this.templateDir = FileUtil.canonical(templateDir);
		this.sdkDir = FileUtil.canonical(sdkDir);
		this.ndkDir = FileUtil.canonical(ndkDir);
		this.dstDir = FileUtil.canonical(dstDir);
		this.cfg = cfg;
	}
	
	/** */
	private void ioError(String resKey) throws IOException {
		throw new IOException(res.getStr(resKey));
	}
	
	/** */
	private String escape(String value) {
		return value.replace("\\","\\\\");
	}
	
	/** */
	private String getCMakeArgsStr() {
		StringBuilder builder = new StringBuilder();
		
		for (String arg:cfg.getCMakeArgs()) {
			if (builder.length() > 0) {
				builder.append(",");
			}
			builder.append('"');
			builder.append(escape(arg));
			builder.append('"');
		}
		
		return builder.toString();
	}
	
	/** */
	private void createContext() throws IOException {
		context = new HashMap<String,Object>();
		
	// SDK directory
		if (sdkDir == null) {
			ioError("missing.sdk.dir");
		}
		if (sdkDir.exists() == false) {
			throw new IOException(res.getStr(
				"sdk.dir.not.found",sdkDir.getAbsolutePath()));
		}
		if (sdkDir.isDirectory() == false) {
			throw new IOException(res.getStr(
				"sdk.dir.not.dir",sdkDir.getAbsolutePath()));
		}
		context.put("sdkDir",escape(sdkDir.getAbsolutePath()));
		
	// NDK directory
		if (cfg.getNativeCode() == true) {
			if (ndkDir == null) {
				ioError("missing.ndk.dir");
			}
			if (ndkDir.exists() == false) {
				throw new IOException(res.getStr(
					"ndk.dir.not.found",ndkDir.getAbsolutePath()));
			}
			if (ndkDir.isDirectory() == false) {
				throw new IOException(res.getStr(
					"ndk.dir.not.dir",ndkDir.getAbsolutePath()));
			}
			context.put("ndkDir",escape(ndkDir.getAbsolutePath()));
		}
		
	// application identifier
		if (cfg.getApplicationId() == null) {
			ioError("missing.application.id");
		}
		context.put("applicationId",cfg.getApplicationId());
		
	// application name
		if (cfg.getApplicationName() == null) {
			ioError("missing.application.name");
		}
		context.put("applicationName",cfg.getApplicationName());
		
	// native code
		context.put("nativeCode",cfg.getNativeCode());
		
	// minimum SDK version
		if (cfg.getMinSdkVersion() == null) {
			ioError("missing.min.sdk.version");
		}
		context.put("minSdkVersion",cfg.getMinSdkVersion());
		
	// target SDK version
		if (cfg.getTargetSdkVersion() == null) {
			ioError("missing.target.sdk.version");
		}
		context.put("targetSdkVersion",cfg.getTargetSdkVersion());
		
	// compile SDK version
		if (cfg.getCompileSdkVersion() == null) {
			ioError("missing.compile.sdk.version");
		}
		context.put("compileSdkVersion",cfg.getCompileSdkVersion());
		
	// package name
		if (cfg.getPackageName() == null) {
			ioError("missing.package.name");
		}
		context.put("packageName",cfg.getPackageName());
		
	// activity name
		if (cfg.getActivityName() == null) {
			ioError("missing.activity.name");
		}
		context.put("activityName",cfg.getActivityName());
		
	// version
		context.put("versionCode",cfg.getVersionCode());
		context.put("versionName",cfg.getVersionName());
		
	// CMake arguments
		if (cfg.getNativeCode() == true && cfg.getCMakeArgs() != null) {
			context.put("cmakeArgs",getCMakeArgsStr());
		}
		
	// dependencies
		context.put("dependencies",cfg.getDependencies());
	}
	
	/** */
	private void copyFile(String path) throws IOException {
		File srcFile = new File(templateDir,path);	
		File dstFile = new File(dstDir,path);
		if (srcFile.isDirectory() == true) {
			dstFile.mkdirs();
			return;
		}
		FileUtils.copyFile(srcFile,dstFile);
	}
	
	/** */
	private void evalFile(String path) throws IOException {
	// source file
		File srcFile = new File(templateDir,path);
		String template = FileUtils.readFileToString(srcFile);
		
	// destination file
		String dstPath = path.substring(0,path.length() - 3);
		File dstFile = new File(dstDir,dstPath);
		
	// evaluate/write
		String src = VelocityUtil.evaluate(template,context);
		FileUtils.write(dstFile,src);
	}
	
	/** */
	public void createProject() throws IOException {
		if (templateDir.exists() == false) {
			throw new IOException(res.getStr("template.dir.not.found",
				templateDir.getAbsolutePath()));
		}
		if (templateDir.isDirectory() == false) {
			throw new IOException(res.getStr("template.dir.not.dir",
				templateDir.getAbsolutePath()));
		}
		
	// context
		createContext();
		
		DirScanner scanner = new DirScanner(templateDir,true);
		String[] paths = scanner.build();
	// for each template file
		for (String path:paths) {
			if (path.endsWith(".vm")) {
				evalFile(path);
			}
			else {
				copyFile(path);
			}
		}
	}
}
