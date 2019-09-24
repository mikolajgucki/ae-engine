Libraries
====

Lua
----
In order to compile Lua as NDK module the following must be added to the file `luaconf.h`.
```cpp
#ifdef __ANDROID__
#define lua_getlocaledecpoint() '.'
#endif
```
The file `luac.c` must be excluded from compilation.

Uncommented `LUA_USE_C89` in `luaconf.h`.

Added rules `dylib` and `jnilib` to src/Makefile.

The file `lua-5.3.2-ae.zip` is the library with the changes described above.

libpng
----
The PNG library additionally contains `pnglibconf.h` in the root directory of the zip file.

The file `lpng1610-ae.zip` contains the change.

To disable ARM neon optimization add the following to `pnglibconf.h`:
```cpp
#define PNG_ARM_NEON_OPT 0
```

SDL2
----
The header file `SDL_syswm.h` was changed and explicitly added constructor to the structure `SDL_SysWMinfo` and added structure `SDL_uikit` with no-argument constructor. Otherwise the code doesn't compile in C++11.

### SDL2 on Android
There seems to be an issue when `onDestroy()` gets called. Before `onDestroy()` Android calls `onPause()` in which SDL is signaled to pause the loop via `Android_PauseSem` semaphore. In turn, the loop is paused in `Android_PumpEvents()` when the control reaches `SDL_SemWait(Android_ResumeSem)` right after `android_egl_context_backup()`. `Android_PumpEvents()` is called in `SDL_PullEvents()` so the loop is paused when getting events from the SDL queue. `onDestroy()` in SDL calls `Java_org_libsdl_app_SDLActivity_nativeQuit` which increments the `Android_ResumeSem` and should let continue execution in `Android_PumpEvents()`. It does not for unknown reason and the game loop is never resumed. SDL `onDestroy()` tries to join the game loop thread which is locked and `onDestroy()` gets locked as well.

In order to fix the issue, `#define SDL_ANDROID_BLOCK_ON_PAUSE  1` was commented out and pausing the loop is done in `ae_sdl2.cpp` using mutex `pauseLoopMutex`.

### SDL on iOS

Added call `SDL_iPhoneMain()` to `int main(int argc, char **argv)` in `src/video/uikit/SDL_uikitappdelegate.m`. The function `SDL_iPhoneMain()` must be defined in the application which is on top of SDL. This function gives opportunity to register listeners to receive application events.

Added call `SDL_DidFinishLaunchingWithOptions(UIApplication *application)` in `src/video/uikit/SDL_uikitappdelegate.m`. The function `SDL_DidFinishLaunchingWithOptions()` must be defined in the application which in on top of SDL.

### SDL2 on Windows
`SDL_platform.h` must be replaced with https://hg.libsdl.org/SDL/raw-file/e217ed463f25/include/SDL_platform.h in order to compile. Otherwise, `winapifamily.h` missing error will be reported. It's a bug in SDL.

`SDL_VIDEO_RENDER_D3D` must be undefined in `SDL_config.h`.

Install MinGW-w64 and then follow the steps:
1. Run `bash` in the SDL source directory.
2. `configure --disable-render-d3d`
3. `make`
4. `make install`

SDL2 mixer
----
The iOS Xcode project was changed so that it looks for header files (`*.h`) in the directory `SDL2` instead of `SDL`.
The header file `SDL_mixer.h` (inside `libs/osx/SDL2_mixer-2.0.0`) in the result framework was changed to `#include "..."` instead of `#include <SDL/...>`.

The file `SDL2_mixer-2.0.0-ae.zip` contains the change.

### SDL2 mixer on Windows
Install MSys from MinGW and then follow the steps:
1. Build `external/smpeg2-2.0.0`
	1. `export SDL2_CONFIG=/local/bin/sdl2-config`
	2. `./configure --disable-sdltest`
	3. `./make`
	3. `./make install`
2. Build `external/libogg-1.3.1`
	1. `./configure`
	2. `./make`
	3. `./make install`
3. Build `external/libvorbisidec-1.2.1`
	1. `./configure --disable-oggtest`


1. Run `bash` in the SDL mixer source directory.
2. `export SDL2_CONFIG=/local/bin/sdl2-config`
3. `configure --disable-sdltest --enable-music-ogg`
4. `make`
5. `make install`

### SDL2 on OS X

Google Play Services
----
The Google Play Services library was taken from Android SDK and zipped into `libs\google-play-services.zip`.

