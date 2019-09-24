package com.andcreations.ae.studio.plugins.android;

import com.andcreations.ae.studio.plugins.builders.Builders;

/**
 * @author Mikolaj Gucki
 */
class AndroidBuilders {
    /** */
    static void appendBuilders(AndroidPluginState state) {
        Builders.get().appendBuilder(new AndroidAEProjectUpdateBuilder(state));
        Builders.get().appendBuilder(new AndroidAEProjectCleanBuilder(state));     
        Builders.get().appendBuilder(new AssembleDebugGradleBuilder());
        Builders.get().appendBuilder(new AssembleReleaseGradleBuilder());
        Builders.get().appendBuilder(new CleanGradleBuilder());
        Builders.get().appendBuilder(new InstallDebugADBBuilder());
        Builders.get().appendBuilder(new InstallReleaseADBBuilder());
        Builders.get().appendBuilder(new UninstallADBBuilder());
    }
}