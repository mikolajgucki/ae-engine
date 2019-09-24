package com.andcreations.ae.studio.plugins.ant;

/**
 * @author Mikolaj Gucki
 */
public class AntPreferencesAdapter implements AntPreferencesListener {
    /**
     * Called when the Ant home has changed.
     *
     * @param oldAndHome The old Ant home.
     * @param newAntHome The new Ant home.
     */
    @Override
    public void antHomeChanged(String oldAntHome,String newAntHome) {
    }
}