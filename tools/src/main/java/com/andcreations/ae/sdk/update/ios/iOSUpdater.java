package com.andcreations.ae.sdk.update.ios;

import java.io.File;
import java.io.FileFilter;
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
public class iOSUpdater extends SDKUpdater {
    /** */
    private static final String IOS_PROJECT_ASSETS_PATH = "ae.assets";
    
    /** */
    private static final String IOS_PROJECT_LIBS_PATH = "ae.libs";
    
    /** */
    private static final String IOS_PROJECT_SRC_PATH = "ae.src";
    
    /** */
    private static final String IOS_SRC_PATH = "ios/src";
    
    /** */
    private static final String IOS_LIBS_PATH = "ios/libs";
    
    /** */
    private static final String IOS_PLUGIN_SRC_PATHS[] = {
        "common/src","ios/src","ios/libs/src"
    };
    
    /** */
    private iOSUpdateCfg cfg;
    
    /** */
    public iOSUpdater(iOSUpdateCfg cfg) {
        super(cfg);
        this.cfg = cfg;
        create();
    }

    /** */
    private void create() {
        setPluginsProperty(AEProjectProperties.IOS_PLUGINS);
    }
    
    /** */
    private File getiOSProjectFile(String path) {
        return new File(cfg.getiOSProjectDir(),path);
    }
    
    /** */
    private void updateLibs() throws IOException,SDKUpdateException {
        String[] plugins = getPlugins();
        if (plugins == null) {
            return;
        }
        
        FileFilter libsFileFilter = new FileFilter() {
            /** */
            @Override
            public boolean accept(File file) {
                if (file.getName().startsWith("src")) {
                    return false;
                }
                return true;
            }
        };        
        
        FileSetList libsFiles = new FileSetList();
        libsFiles.add(getPluginsFileSets(plugins,libsFileFilter,IOS_LIBS_PATH));
        libsFiles.add(getAEDistFileSetList(IOS_LIBS_PATH));
        
        File libsDir = getiOSProjectFile(IOS_PROJECT_LIBS_PATH);
        updateDir(libsFiles,libsDir,FileCopyStrategy.INSTANCE);
        deleteUnknownFiles(libsFiles,libsDir);
    }       
    
    /** */
    private void updateSrc() throws IOException,SDKUpdateException {
        String[] plugins = getPlugins();
        if (plugins == null) {
            return;
        }
        
        FileSetList srcFiles = new FileSetList();
        srcFiles.add(getPluginsFileSets(plugins,IOS_PLUGIN_SRC_PATHS));
        srcFiles.add(getAEDistFileSetList(COMMON_SRC_PATHS));
        srcFiles.add(getAEDistFileSetList(IOS_SRC_PATH));
        
        File srcDir = getiOSProjectFile(IOS_PROJECT_SRC_PATH);
        updateDir(srcFiles,srcDir,FileCopyStrategy.INSTANCE);
        deleteUnknownFiles(srcFiles,srcDir);
    }
    
    /** */
    private void updateIcons() throws IOException {
        if (cfg.getAppIconSetDir() == null) {
            return;
        }
        
        File iconFile = AEProjectIcon.getiOSIconFile(cfg.getProjectDir());
        if (iconFile == null) {
            return;
        }
        Image icon = Image.load(iconFile);
        
        AppIcon appIcon = AppIcon.ios();     
        DirScanner scanner = new DirScanner(cfg.getAppIconSetDir());
    // for each file in the resources directory
        for (String path:scanner.build()) {
            Image image = appIcon.createByFilename(icon,path);
            if (image != null) {
                File dstFile = scanner.getFile(path);
                notifyUpdatingIcon(dstFile);
                image.savePNG(dstFile);
            }
        }        
    }
    
    /** */
    @Override
    public void update() throws IOException,SDKUpdateException {
        init();
        logPlugins();
        checkPluginsExist();
        updateAssets(getiOSProjectFile(IOS_PROJECT_ASSETS_PATH));        
        updateLibs();
        updateSrc();
        updateIcons();
    }
    
    /** */
    @Override
    public void clean() throws IOException,SDKUpdateException {
    // assets
        File assetsDir = getiOSProjectFile(IOS_PROJECT_ASSETS_PATH);
        if (assetsDir.exists() == true) {
            notifyDeleting(assetsDir);
            FileUtils.forceDelete(assetsDir);
        }
        
    // libs
        File libsDir = getiOSProjectFile(IOS_PROJECT_LIBS_PATH);
        if (libsDir.exists() == true) {
            notifyDeleting(libsDir);
            FileUtils.forceDelete(libsDir);        
        }
        
    // src
        File srcDir = getiOSProjectFile(IOS_PROJECT_SRC_PATH);
        if (srcDir.exists() == true) {
            notifyDeleting(srcDir);
            FileUtils.forceDelete(srcDir);        
        }
    }
}