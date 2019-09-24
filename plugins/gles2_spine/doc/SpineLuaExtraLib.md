Spine
====

Android
----
To make Spine available to your Android project:
1. Load the native library `libae_spine.a` in the static block of your activity class.
2. Import the class `com.andcreations.spine.AESpine` which provides the native method `loadLuaLib()` to load the Lua library. Call the method in the `onCreate()` method of your activity class.

Loading Spine would look like:
```java
import com.andcreations.spine.AESpine;

public class GameActivity {
    static {
        System.loadLibrary("ae_spine");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AESpine.loadLuaLib();
    }    
}
```

iOS
----
To make Spine available to your iOS project:
1. Include `SpineLuaExtraLib.h` and `ae_sdl2.h`.
2. Use namespace `ae::spine`.
3. Call `addLuaExtraLib(new SpineLuaExtraLib())` from `SDL_main`.

Loading Spine would look like:
```mm
#include "SpineLuaExtraLib.h"
#include "ae_sdl2.h"

using namespace ae::spine;

/** */
int SDL_main(int argc,char *argv[]) {
    addLuaExtraLib(new SpineLuaExtraLib());
    return ae_sdl_main(argc,argv);
}
```

Common
----
Initialize the plugin from `ae.ready()` by calling `spine.init()`.