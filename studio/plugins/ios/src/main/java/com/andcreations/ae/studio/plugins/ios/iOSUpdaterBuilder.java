package com.andcreations.ae.studio.plugins.ios;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.swing.ImageIcon;

import com.andcreations.ae.ios.AEiOSProject;
import com.andcreations.ae.sdk.update.SDKUpdaterListener;
import com.andcreations.ae.sdk.update.ios.iOSUpdateCfg;
import com.andcreations.ae.sdk.update.ios.iOSUpdater;
import com.andcreations.ae.studio.plugins.ae.dist.AEDist;
import com.andcreations.ae.studio.plugins.builders.AbstractBuilder;
import com.andcreations.ae.studio.plugins.builders.BuilderException;
import com.andcreations.ae.studio.plugins.console.DefaultConsole;
import com.andcreations.ae.studio.plugins.project.files.ProjectFiles;
import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
abstract class iOSUpdaterBuilder extends AbstractBuilder {
    /** */
    private static BundleResources res =
        new BundleResources(iOSUpdaterBuilder.class);
        
    /** */
    private iOSPluginState state;
    
    /** */
    protected iOSUpdaterBuilder(String id,String name,ImageIcon icon,
        String desc,iOSPluginState state) {
    //
        super(id,name,icon,desc);
        this.state = state;
    }
    
    /** */
    protected iOSPluginState getiOSPluginState() {
        return state;
    }
    
    /** */
    private SDKUpdaterListener getSDKUpdaterListener() {
        return new SDKUpdaterListener() {
            /** */
            @Override
            public void log(String msg) {
                DefaultConsole.get().traceln(msg);
            }
            
            /** */
            @Override
            public void copying(File srcFile,File dstFile) {
                DefaultConsole.get().traceln(res.getStr("copying",
                    ProjectFiles.get().getRelativePath(srcFile),
                    ProjectFiles.get().getRelativePath(dstFile)));
            }
            
            /** */
            @Override
            public void deleting(File file) {
                DefaultConsole.get().traceln(res.getStr("deleting",
                    ProjectFiles.get().getRelativePath(file)));
            }
            
            /** */
            @Override
            public void skipping(File srcFile,File dstFile) {
            }
            
            /** */
            @Override
            public void updatingIcon(File file) {
                DefaultConsole.get().traceln(res.getStr("updating.icon",
                    ProjectFiles.get().getRelativePath(file)));
            }
        };
    }
    
    /** */
    protected abstract void runUpdater(iOSUpdater updater)
        throws Exception;
        
    /** */
    protected Properties loadAEBuildProperties(File iosProjectDir) {
        File file = AEiOSProject.getAEBuildPropertiesFile(iosProjectDir);
        if (file.exists() == false || file.isDirectory() == true) {
            return null;
        }
      
        try {
            return AEiOSProject.loadAEBuildProperties(iosProjectDir);
        } catch (IOException exception) {
            CommonDialogs.error(res.getStr("error.title"),
                res.getStr("failed.to.load.ae.build.properties",
                file.getAbsolutePath(),exception.getMessage()));            
            throw new BuilderException();
        }
    }
        
    /** */
    protected void run(File iosProjectDir) {
    // build properties
        Properties aeBuildProperties = loadAEBuildProperties(iosProjectDir);
        if (aeBuildProperties == null) {
            File file = AEiOSProject.getAEBuildPropertiesFile(iosProjectDir);
            DefaultConsole.get().warningln(
                res.getStr("missing.ae.build.properties",
                file.getAbsolutePath()));
        }
        
    // configuration
        iOSUpdateCfg cfg = new iOSUpdateCfg(
            AEDist.get().getAEDistDir(),ProjectFiles.get().getProjectDir(),
            iosProjectDir);
        if (aeBuildProperties != null) {
            cfg.setAppIconSetDir(new File(iosProjectDir,
                AEiOSProject.getAppIconSetDir(aeBuildProperties)));
        }
        iOSUpdater updater = new iOSUpdater(cfg);        
        if (state.getVerbose() == true) {
            updater.setSDKUpdaterListener(getSDKUpdaterListener());
        }
        
    // update
        try {
            runUpdater(updater);
        } catch (Exception exception) {
            exception.printStackTrace();
            CommonDialogs.error(res.getStr("error.title"),
                res.getStr("failed.to.update",exception.getMessage()));
            return;            
        }
    }
    
    /** */
    @Override
    public void build() {
    // directory
        String path = state.getiOSProjectDir();
        if (path == null || path.length() == 0) {
            CommonDialogs.error(res.getStr("error.title"),
                res.getStr("dir.not.set"));
            return;
        }
        File iosProjectDir = iOSProjectDir.fromPath(path);
        
    // validate
        String error = iOSProjectDir.validate(iosProjectDir);
        if (error != null) {
            CommonDialogs.error(res.getStr("error.title"),
                res.getStr("dir.error",error));
            return;
        }
        
    // run iOS updater
        run(iosProjectDir);
    }
}