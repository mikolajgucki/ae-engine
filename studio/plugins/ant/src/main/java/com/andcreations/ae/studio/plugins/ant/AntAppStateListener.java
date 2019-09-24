package com.andcreations.ae.studio.plugins.ant;

/**
 * @author Mikolaj Gucki
 */
interface AntAppStateListener {
    /**
     * Called when the Ant home has changed.
     *
     * @param oldAndHome The old Ant home.
     * @param newAntHome The new Ant home.
     */
    void antHomeChanged(String oldAntHome,String newAntHome);
}