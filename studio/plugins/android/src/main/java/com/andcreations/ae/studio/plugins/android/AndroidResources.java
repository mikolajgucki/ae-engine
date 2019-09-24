package com.andcreations.ae.studio.plugins.android;

import com.andcreations.ae.studio.plugins.resources.Resources;

/**
 * @author Mikolaj Gucki
 */
class AndroidResources {
    /** */
    static void init() {
        Resources.get().addSource(new AndroidResourceSource());
    }
}