package com.andcreations.ae.studio.plugins.simulator;

import java.io.File;

import com.andcreations.ae.project.AEProject;
import com.andcreations.ae.studio.plugins.project.files.ProjectFiles;
import com.andcreations.ae.studio.plugins.ui.main.CommonDialogs;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class SimulatorStateUtil {
    /** */
    private static final BundleResources res =
        new BundleResources(SimulatorStateUtil.class);
        
    /** */
    static void deleteState() {
        if (CommonDialogs.yesNo(res.getStr("delete.state.title"),
            res.getStr("delele.state.msg")) == false) {
        //
            return;
        }
        
    // the state file
        File projectDir = ProjectFiles.get().getProjectDir();
        File stateFile = new File(projectDir,AEProject.Simulator.STATE_PATH);
        
    // if the state file exists...
        if (stateFile.exists() == true) {
        // ...delete it
            if (stateFile.delete() == false) {
                CommonDialogs.error(res.getStr("delete.state.title"),
                    res.getStr("failed.to.delete.state.file",
                    stateFile.getAbsolutePath()));
            }
        }
    }
}