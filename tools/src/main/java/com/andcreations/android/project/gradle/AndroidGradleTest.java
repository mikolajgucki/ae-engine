package com.andcreations.android.project.gradle;

import java.io.File;

public class AndroidGradleTest {
	public static void main(String[] args) throws Exception {
		AndroidGradleProjectCfg cfg = new AndroidGradleProjectCfg();
		cfg.setApplicationName("My App");
		cfg.setMinSdkVersion("16");
		cfg.setTargetSdkVersion("23");
		cfg.setCompileSdkVersion("android-16");
		cfg.setPackageName("com.andcreations.myapp");
		cfg.setActivityName("MyAppActivity");
		cfg.setNativeCode(true);
		//cfg.setCMakeArgs("-DMY_VAR=myvar22");
		
		AndroidGradleProject p = new AndroidGradleProject(
			new File("c:\\andcreations\\ae\\templates\\android\\project"),
			new File("c:\\programs\\android\\sdk"),
			new File("c:\\programs\\android\\sdk\\ndk-bundle"),
			new File("c:\\andcreations\\android_test"),cfg);
		p.createProject();
	}
}
