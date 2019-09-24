package com.andcreations.ae.studio.plugins.problems;

/**
 * The interface for problem listeners.
 *
 * @author Mikolaj Gucki
 */
public interface ProblemListener {
    /**
     * Called when a problem has been double-clicked.
     *
     * @param problem The problem.
     */
    void problemDoubleClicked(Problem problem);
}