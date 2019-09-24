package com.andcreations.ae.studio.plugins.ae.dist;

import java.awt.Frame;
import java.io.File;
import java.util.Map;

import com.andcreations.ae.project.AEProject;
import com.andcreations.ae.studio.plugin.Plugin;
import com.andcreations.ae.studio.plugin.PluginException;
import com.andcreations.ae.studio.plugin.PluginState;
import com.andcreations.ae.studio.plugin.StartAfterAndRequire;
import com.andcreations.ae.studio.plugins.ui.common.MessageDialog;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
@StartAfterAndRequire(id="com.andcreations.ae.studio.plugins.ui.common")
public class AEDistPlugin extends Plugin<PluginState> {
    /** */
    private BundleResources res = new BundleResources(AEDistPlugin.class);
    
    /** The directory with the AE distribution. */
    private File aeDistDir;
    
    /**
     * Gets the AE distribution directory from the environment variables.
     */
    private void getAEDistDirFromEnv() {
        Map<String,String> env = System.getenv();
        
    // variable
        String aeDistEnv = env.get(AEProject.Env.AE_DIST);
        if (aeDistEnv == null || aeDistEnv.trim().isEmpty()) {
            MessageDialog.error((Frame)null,res.getStr("no.ae.dist"),
                res.getStr("no.ae.dist.env",AEProject.Env.AE_DIST));
            System.exit(-1);
        }
        
    // file
        aeDistDir = new File(aeDistEnv);
        if (aeDistDir.exists() == false || aeDistDir.isDirectory() == false) {
            MessageDialog.error((Frame)null,res.getStr("no.ae.dist"),
                res.getStr("no.ae.dist.dir",aeDistEnv));
            System.exit(-1);
        }
        
        AEDist.get().init(aeDistDir);
    }
    
    /** */
    @Override
    public void start() throws PluginException {
        UICommon.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                getAEDistDirFromEnv();
            }
        });                
    }
    
    /**
     * Gets the directory with the AE distribution. 
     *
     * @return The directory with the distribution.
     */
    public File getAEDistDir() {
        return aeDistDir;
    }
}