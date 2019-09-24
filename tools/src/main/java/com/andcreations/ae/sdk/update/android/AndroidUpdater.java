package com.andcreations.ae.sdk.update.android;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.andcreations.ae.appicon.AppIcon;
import com.andcreations.ae.image.Image;
import com.andcreations.ae.project.AEProjectIcon;
import com.andcreations.ae.project.AEProjectProperties;
import com.andcreations.ae.sdk.update.FileCopyStrategy;
import com.andcreations.ae.sdk.update.FileSetList;
import com.andcreations.ae.sdk.update.SDKUpdateException;
import com.andcreations.ae.sdk.update.SDKUpdater;
import com.andcreations.io.DirScanner;

/**
 * @author Mikolaj Gucki
 */
public class AndroidUpdater extends SDKUpdater {
    /** */
    private static final String ANDROID_PROJECT_ASSETS_PATH = "src/main/assets";
    
    /** */
    private static final String ANDROID_PROJECT_LIBS_PATH = "libs";
    
    /** */
    private static final String ANDROID_LIBS_PATH = "android/libs";
    
    /** */
    private AndroidUpdateCfg cfg;
    
    /** */
    public AndroidUpdater(AndroidUpdateCfg cfg) {
        super(cfg);
        this.cfg = cfg;
        create();
    }
    
    /** */
    private void create() {
        setPluginsProperty(AEProjectProperties.ANDROID_PLUGINS);
    }
    
    /** */
    private File getAndroidProjectFile(String path) {
        return new File(cfg.getAndroidProjectDir(),path);
    }
    
    /** */
    private void updateLibs() throws IOException,SDKUpdateException {
        String[] plugins = getPlugins();
        if (plugins == null) {
            return;
        }
        
        FileSetList libsFiles = new FileSetList();
        libsFiles.add(getPluginsFileSets(plugins,ANDROID_LIBS_PATH));
        libsFiles.add(getAEDistFileSetList(ANDROID_LIBS_PATH));
        
        File libsDir = getAndroidProjectFile(ANDROID_PROJECT_LIBS_PATH);
        updateDir(libsFiles,libsDir,FileCopyStrategy.INSTANCE);
        deleteUnknownFiles(libsFiles,libsDir);
    }
    
    /** */
    private void updateIcons() throws IOException {
        File iconFile = AEProjectIcon.getAndroidIconFile(cfg.getProjectDir());
        if (iconFile == null) {
            return;
        }
        Image icon = Image.load(iconFile);
        
        AppIcon appIcon = AppIcon.android();     
        DirScanner scanner = new DirScanner(getAndroidProjectFile("res"));
    // for file in the resources directory
        for (String path:scanner.build()) {
            Image image = appIcon.createByFilename(icon,path);
            if (image != null) {
                File dstFile = scanner.getFile(path);
                if (FileUtils.isFileNewer(iconFile,dstFile) == true) {
                    notifyUpdatingIcon(dstFile);
                    image.savePNG(dstFile);
                }
            }
        }
    }
    
    /** */
    @Override
    public void update() throws IOException,SDKUpdateException {
        init();
        logPlugins();
        checkPluginsExist();
        updateAssets(getAndroidProjectFile(ANDROID_PROJECT_ASSETS_PATH));        
        updateLibs();
        updateIcons();
    }
    
    /** */
    @Override
    public void clean() throws IOException,SDKUpdateException {
    // assets
        File assetsDir = getAndroidProjectFile(ANDROID_PROJECT_ASSETS_PATH);
        if (assetsDir.exists() == true) {
            notifyDeleting(assetsDir);
            FileUtils.forceDelete(assetsDir);
        }
        
    // libs
        File libsDir = getAndroidProjectFile(ANDROID_PROJECT_LIBS_PATH);
        if (libsDir.exists() == true) {
            notifyDeleting(libsDir);
            FileUtils.forceDelete(libsDir);
        }
    }
}