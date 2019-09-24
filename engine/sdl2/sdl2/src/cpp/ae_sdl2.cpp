#ifdef AE_ANDROID
#include <jni.h>
#endif

#include <vector>

#include "SDL.h"
#include "SDL_log.h"
#include "SDL_mutex.h"
#include "SDL_opengles2.h"
#include "SDL_system.h"

#include "ae_platform.h"
#include "Logger.h"
#include "NullLogger.h"

#ifdef AE_IOS
#include "NSLogger.h"
#else
#include "SDLLogger.h"
#endif
#include "Log.h"
#include "ae_lock.h"
#include "ae_lock_sdl2.h"
#include "SDL_helper.h"
#ifndef AE_ANDROID
#include "SDLAudio.h"
#endif
#include "SDLSleepFunc.h"
#include "SDLGetTicksFunc.h"
#include "SDLEngineMutexFactory.h"
#include "Sound.h"
#include "UnhashInputStreamProvider.h"
#include "RWopsInputStreamProvider.h"
#include "RWopsOutputStreamProvider.h"
#include "RWopsAssets.h"
#include "RWopsStorage.h"
#include "RWopsFileSystem.h"
#include "PNGImageLoader.h"
#include "Engine.h"
#include "LuaEnginePlugin.h"
#include "LuaEngine.h"
#include "LuaTouchHandler.h"
#include "LuaKeyHandler.h"
#include "LuaEngineCfgOS.h"
#include "FPSCounter.h"
#include "FPSMax.h"
#include "SDLLuaEngineListener.h"
#include "LuaExtraLib.h"

#ifdef AE_ANDROID
#include "AndroidAudio.h"
#include "AndroidLuaExtraLib.h"
#include "ae_sdl2_android.h"
#endif

using namespace std;
using namespace ae;
using namespace ae::sdl;
using namespace ae::io;
using namespace ae::engine;
using namespace ae::image;
using namespace ae::texture;
using namespace ae::audio;
using namespace ae::util;

#ifdef AE_ANDROID
using namespace ae::android;
#endif

/// The log tag.
static const char *const logTag = "ae.sdl";

/// The debug mode flag.
static bool debugFlag = false;

/// The logger.
static Logger *logger = (Logger *)0;

/// The number of milliseconds to sleep on update while paused not to
/// consume CPU and drain battery.
static const int pauseSleepMilliseconds = 100;

/// The window.
static SDL_Window *window = (SDL_Window *)0;

/// The window width.
static int windowWidth;

/// The window height.
static int windowHeight;

/// The GL context.
static SDL_GLContext glContext = 0;

#ifdef AE_ANDROID
/// The SDL mutex to pause the loop.
static SDL_mutex *pauseLoopMutex = (SDL_mutex *)0;
#endif

/// The SDL mutex to synchronize app events and engine ticks.
static SDL_mutex *engineTickMutex = (SDL_mutex *)0;

/// The SDL mutex to synchronize application termination.
static SDL_mutex *engineQuitMutex = (SDL_mutex *)0;

/// Indicates if the engine loop execution is paused.
static bool pauseLoop;

/// Indicates the loop is paused (as result of an app event sent by OS).
static bool loopPauseAppEvent;

/// Indicates if to quit the loop. After set to true
/// no GL calls can be made (or game will crash).
static bool quitLoop;

#ifdef AE_ANDROID
/// Indicates if the loop is stopped.
static bool loopStopped;
#endif

/// Indicates if the application is shutting down.
static bool shuttingDown;

/// The Lua source files provider.
static UnhashInputStreamProvider *luaSrcProvider =
    (UnhashInputStreamProvider *)0; 

/// The image provider.
static RWopsInputStreamProvider *imageProvider =
    (RWopsInputStreamProvider *)0;

/// The PNG image loader.
static PNGImageLoader *imageLoader = (PNGImageLoader *)0;

/// The texture image provider.
static RWopsInputStreamProvider *textureImageProvider =
    (RWopsInputStreamProvider *)0;

/// The texture PNG image loader.
static PNGImageLoader *textureImageLoader = (PNGImageLoader *)0;

/// The AndEngine.
static Engine *aeEngine = (Engine *)0;

/// The sleep function.
static SDLSleepFunc *sleepFunc = (SDLSleepFunc *)0;

/// The get-ticks functions
static SDLGetTicksFunc *getTicksFunc = (SDLGetTicksFunc *)0;

/// The mutex factory.
static EngineMutexFactory *mutexFactory = (EngineMutexFactory *)0;

/// The global lock
static EngineMutex *globalLock = (EngineMutex *)0;

/// The Lua engine.
static LuaEngine *luaEngine = (LuaEngine *)0;

/// The Lua engine touch handler.
static LuaTouchHandler *luaTouchHandler = (LuaTouchHandler *)0;

/// The Lua engine key handler.
static LuaKeyHandler *luaKeyHandler = (LuaKeyHandler *)0;

/// The assets.
static RWopsAssets *assets = (RWopsAssets *)0;

/// The storage directory.
static const char *storageDir = (const char *)0;

/// The storage.
static RWopsStorage *storage = (RWopsStorage *)0;

/// The file system.
static RWopsFileSystem *fileSystem = (RWopsFileSystem *)0;

#ifndef AE_ANDROID
/// The SDL audio.
static SDLAudio *sdlAudio = (SDLAudio *)0;
#else
/// The Android audio.
static AndroidAudio *androidAudio = (AndroidAudio *)0;
#endif

/// The Lua engine listener.
static SDLLuaEngineListener *luaEngineListener = (SDLLuaEngineListener *)0;

/// The FPS counter.
static FPSCounter fpsCounter;

/// The total draw time in milliseconds since the last FPS log.
static long drawTime;

/// The total update time in milliseconds since the last FPS log.
static long updateTime;

/// The total request process time in milliseconds since the last FPS log.
static long requestsTime;

/// The maximum FPS.
static FPSMax fpsMax(60);

/// The extra libraries loaded on start.
static vector<LuaExtraLib *> luaExtraLibs;

/// The Lua engine plugins.
static vector<LuaEnginePlugin *> luaEnginePlugins;

/// The time recorded in the the previous frame. Used to calculate time delta.
static Uint32 then;

/** */
static void deleteGLContext() {
    Log::info(logTag,"Deleting GL context");    
    if (glContext != 0) {
        SDL_GL_DeleteContext(glContext);
        glContext = 0;
    }
}

/** */
static void destroyWindow() {
    Log::info(logTag,"Destroying window");
    if (window != (SDL_Window *)0) {
        SDL_DestroyWindow(window);
        window = (SDL_Window *)0;
    }
}

/** */
static void destroySDL() {
    Log::info(logTag,"Destroying SDL");
    deleteGLContext();
    destroyWindow();
}

/** */
static void destoryEngine() {
    Log::info(logTag,"Destroying engine");
    
    if (luaEngineListener != (SDLLuaEngineListener *)0) {
        delete luaEngineListener;
    }
    if (luaKeyHandler != (LuaKeyHandler *)0) {
        delete luaKeyHandler;
    }
    if (luaTouchHandler != (LuaTouchHandler *)0) {
        delete luaTouchHandler;
    }    
    if (luaEngine != (LuaEngine *)0) {
        delete luaEngine;
    }    
    if (aeEngine != (Engine *)0) {
        delete aeEngine;
    }
    if (globalLock != (EngineMutex *)0) {
        delete globalLock;
        aeSetGlobalLock((EngineMutex *)0);
    }
    if (mutexFactory != (EngineMutexFactory *)0) {
        delete mutexFactory;
    }
    if (getTicksFunc != (SDLGetTicksFunc *)0) {
        delete getTicksFunc;
    }
    if (sleepFunc != (SDLSleepFunc *)0) {
        delete sleepFunc;
    }
    
#ifndef AE_ANDROID
    if (sdlAudio != (SDLAudio *)0) {
        delete sdlAudio;
        sdlAudio = (SDLAudio *)0;
    }
#else
    if (androidAudio != (AndroidAudio *)0) {
        delete androidAudio;
        androidAudio = (AndroidAudio *)0;
    }
#endif

    if (fileSystem != (RWopsFileSystem *)0) {
        delete fileSystem;
    }
    if (storage != (RWopsStorage *)0) {
        delete storage;
    }
    if (assets != (RWopsAssets *)0) {
        delete assets;
    }
    if (textureImageLoader != (PNGImageLoader *)0) {
        delete textureImageLoader;
    }    
    if (textureImageProvider != (RWopsInputStreamProvider *)0) {
        delete textureImageProvider;
    }
    if (imageLoader != (PNGImageLoader *)0) {
        delete imageLoader;
    }
    if (imageProvider != (RWopsInputStreamProvider *)0) {
        delete imageProvider;
    }
    if (luaSrcProvider != (UnhashInputStreamProvider *)0) {
        delete luaSrcProvider;
    }
    if (engineTickMutex != (SDL_mutex *)0) {
        SDL_DestroyMutex(engineTickMutex);
        engineTickMutex = (SDL_mutex *)0;
    }    
#ifdef AE_ANDROID
    if (pauseLoopMutex != (SDL_mutex *)0) {
        SDL_DestroyMutex(pauseLoopMutex);
        pauseLoopMutex = (SDL_mutex *)0;
    }    
#endif

// The libraries in luaExtraLibs and plugins in luaEnginePlugins are kept so
// that they can be reused when the engine is restarted. This can happen if
// the Android activity is recreated (onDestroy() call followed by onCreate()
// call).
}

/** */
static void logError(const char *msg) {
    Log::error(logTag,msg);    
}

/**
 * \brief Quits the application.
 * \param callSDLQuit Indicates if to call SQL_Quit().
 */
static void quit(bool callSDLQuit) {
// let only one thread do the job or it will crash
    if (engineQuitMutex != (SDL_mutex *)0) {
        if (SDL_LockMutex(engineQuitMutex) != 0) {
            logError(SDL_GetError());
            return;
        }
    }
    
// This might be called from another thread than the loop is running.
// Let the loop quit.
    quitLoop = true;
    
// if already shutting down
    if (shuttingDown == true) {
        return;
    }
    shuttingDown = true;
    Log::trace(logTag,"Shutting down"); 
        
    destoryEngine();
    destroySDL();
    
    if (engineQuitMutex != (SDL_mutex *)0) {    
        if (SDL_UnlockMutex(engineQuitMutex) != 0) {
            logError(SDL_GetError());
            return;
        }
    }

    if (callSDLQuit == true) {
        SDL_Quit();
    }
}

/**
 * \brief Called on fatal error.  This function does not return.
 * \param msg The error message.
 */
static void fatalError(const char *msg) {
    logError(msg);
#ifdef AE_ANDROID    
    throwAEException(msg);
#endif
    quit(true);
}

/**
 * \brief Called on no memory.  This function does not return.
 */
static void noMemoryFatalError() {
    fatalError("Out of memory");
}

/** */
static void checkSDLFatalError() {
    const char *error = getSDLError();
    if (error != (const char *)0) {
        fatalError(error);
    }
}

/** Forward declaration. */
static int handleAppEvents(void *data,SDL_Event *event);

/** */
static void initGLAttributes() {
// TODO Set proper GL attributes.
    SDL_GL_SetAttribute(SDL_GL_RED_SIZE,8);
    SDL_GL_SetAttribute(SDL_GL_GREEN_SIZE,8);
    SDL_GL_SetAttribute(SDL_GL_BLUE_SIZE,8);
    
    SDL_GL_SetAttribute(SDL_GL_ALPHA_SIZE,0);
    SDL_GL_SetAttribute(SDL_GL_DOUBLEBUFFER,1);
    SDL_GL_SetAttribute(SDL_GL_BUFFER_SIZE,0);
    SDL_GL_SetAttribute(SDL_GL_DEPTH_SIZE,0); // was 16
    SDL_GL_SetAttribute(SDL_GL_STENCIL_SIZE,0);
    SDL_GL_SetAttribute(SDL_GL_ACCUM_RED_SIZE,0);
    SDL_GL_SetAttribute(SDL_GL_ACCUM_GREEN_SIZE,0);
    SDL_GL_SetAttribute(SDL_GL_ACCUM_BLUE_SIZE,0);
    SDL_GL_SetAttribute(SDL_GL_ACCUM_ALPHA_SIZE,0);
    SDL_GL_SetAttribute(SDL_GL_STEREO,0);
    SDL_GL_SetAttribute(SDL_GL_MULTISAMPLEBUFFERS,0);
    SDL_GL_SetAttribute(SDL_GL_MULTISAMPLESAMPLES,0);

// GLES 2.0
    SDL_GL_SetAttribute(SDL_GL_CONTEXT_MAJOR_VERSION,2);
    SDL_GL_SetAttribute(SDL_GL_CONTEXT_PROFILE_MASK,SDL_GL_CONTEXT_PROFILE_ES);
    
// check error
    checkSDLFatalError();
}

/** */
static void createGLContext() {
// delete if exists
    deleteGLContext();
    
// create
    Log::info(logTag,"Creating GL context");
    glContext = SDL_GL_CreateContext(window);
    if (glContext == 0) {
        fatalError(SDL_GetError());
    }
}

/** */
static void logGLAttribute(SDL_GLattr attr,const char *name) {
    int value;
    SDL_GL_GetAttribute(attr,&value);
    
    ostringstream msg;
    msg << "  " << name << " = " << value;    
    Log::trace(logTag,msg.str());
}

/** */
static void logGLAttributes() {
    Log::trace(logTag,"GL attributes:");
    logGLAttribute(SDL_GL_RED_SIZE,"SDL_GL_RED_SIZE");
    logGLAttribute(SDL_GL_GREEN_SIZE,"SDL_GL_GREEN_SIZE");
    logGLAttribute(SDL_GL_BLUE_SIZE,"SDL_GL_BLUE_SIZE");

    logGLAttribute(SDL_GL_ALPHA_SIZE,"SDL_GL_ALPHA_SIZE");
    logGLAttribute(SDL_GL_DOUBLEBUFFER,"SDL_GL_DOUBLEBUFFER");
    logGLAttribute(SDL_GL_BUFFER_SIZE,"SDL_GL_BUFFER_SIZE");
    logGLAttribute(SDL_GL_DEPTH_SIZE,"SDL_GL_DEPTH_SIZE");
    logGLAttribute(SDL_GL_STENCIL_SIZE,"SDL_GL_STENCIL_SIZE");
    logGLAttribute(SDL_GL_ACCUM_RED_SIZE,"SDL_GL_ACCUM_RED_SIZE");
    logGLAttribute(SDL_GL_ACCUM_GREEN_SIZE,"SDL_GL_ACCUM_GREEN_SIZE");
    logGLAttribute(SDL_GL_ACCUM_BLUE_SIZE,"SDL_GL_ACCUM_BLUE_SIZE");
    logGLAttribute(SDL_GL_ACCUM_ALPHA_SIZE,"SDL_GL_ACCUM_ALPHA_SIZE");
    logGLAttribute(SDL_GL_STEREO,"SDL_GL_STEREO");
    logGLAttribute(SDL_GL_MULTISAMPLEBUFFERS,"SDL_GL_MULTISAMPLEBUFFERS");
    logGLAttribute(SDL_GL_MULTISAMPLESAMPLES,"SDL_GL_MULTISAMPLESAMPLES");
    
    logGLAttribute(SDL_GL_CONTEXT_MAJOR_VERSION,"SDL_GL_CONTEXT_MAJOR_VERSION");
    logGLAttribute(SDL_GL_CONTEXT_MINOR_VERSION,"SDL_GL_CONTEXT_MINOR_VERSION");
}

/** */
static void initSDL() {
    SDL_LogSetPriority(SDL_LOG_CATEGORY_APPLICATION,SDL_LOG_PRIORITY_VERBOSE);
    Log::info(logTag,"Initializing SDL");
    
// init video
    if (SDL_WasInit(SDL_INIT_VIDEO) != 0) {
        Log::info(logTag,"Quitting video as it was already initialized");
        SDL_QuitSubSystem(SDL_INIT_VIDEO);
    }
    Log::info(logTag,"Initializing video");
    if (SDL_InitSubSystem(SDL_INIT_VIDEO) != 0) {
        fatalError(SDL_GetError());
        return;
    }
    
// init audio
#ifndef AE_ANDROID 
    if (SDL_WasInit(SDL_INIT_AUDIO) != 0) {
        Log::info(logTag,"Quitting audio as it was already initialized");
        SDL_QuitSubSystem(SDL_INIT_AUDIO);
    }
    Log::info(logTag,"Initializing audio");
    if (SDL_InitSubSystem(SDL_INIT_AUDIO) != 0) {
        fatalError(SDL_GetError());
        return;
    }
#endif
    
// GL attributes
    initGLAttributes();
    
// destroy window if exists
    destroyWindow();
    
// window flags
    Uint32 windowFlags = SDL_WINDOW_OPENGL | SDL_WINDOW_BORDERLESS;
#ifdef AE_IOS
    windowFlags |= SDL_WINDOW_ALLOW_HIGHDPI;
#endif
    
    Log::trace(logTag,"Creating window");
// create window
    window = SDL_CreateWindow(
        "AE Engine", // title
        SDL_WINDOWPOS_UNDEFINED,SDL_WINDOWPOS_UNDEFINED, // position
        0,0, // size
        windowFlags); // flags
    if (window == (SDL_Window *)0) {
        fatalError(SDL_GetError());
        return;
    }
    SDL_SetWindowFullscreen(window,SDL_TRUE);

// engine tick mutex
    engineTickMutex = SDL_CreateMutex();
    if (engineTickMutex == (SDL_mutex *)0) {
        fatalError(SDL_GetError());
        return;
    }
    
#ifdef AE_ANDROID    
// pause loop mutex
    pauseLoopMutex = SDL_CreateMutex();
    if (pauseLoopMutex == (SDL_mutex *)0) {
        fatalError(SDL_GetError());
        return;
    }
#endif
    
// engine quit mutex
    engineQuitMutex = SDL_CreateMutex();
    if (engineQuitMutex == (SDL_mutex *)0) {
        fatalError(SDL_GetError());
        return;
    }
    
// event filter
    SDL_SetEventFilter(handleAppEvents,(void *)0);
    
// clear any pending errors
    SDL_ClearError();
}

/**
 * \brief Gets the OS for the Lua engine.
 * \return The OS.
 */
static const LuaEngineCfgOS* getLuaEngineOS() {
#ifdef AE_ANDROID
    return &LuaEngineCfgOS::OS_ANDROID;
#elif defined(AE_IOS)
    return &LuaEngineCfgOS::OS_IOS;
#endif
}

/** */
void aeAddLuaExtraLib(LuaExtraLib *lib) {
    luaExtraLibs.push_back(lib);
}

/** */
void aeAddLuaEnginePlugin(LuaEnginePlugin *plugin) {
    luaEnginePlugins.push_back(plugin);
}

/** */
static void addLuaExtraLibs() {
    vector<LuaExtraLib *>::iterator itr;
    for (itr = luaExtraLibs.begin(); itr != luaExtraLibs.end(); ++itr) {
        luaEngine->addExtraLib(*itr);
    }    
}

/** */
static void addLuaEnginePlugins() {
    vector<LuaEnginePlugin *>::iterator itr;
    for (itr = luaEnginePlugins.begin(); itr != luaEnginePlugins.end(); ++itr) {
        luaEngine->addPlugin(*itr);
    }        
}

/**
 * \brief Initializes the engine.
 */
static void initEngine() {
    Log::info(logTag,"Initializing engine");
    
// Lua source provider
    RWopsInputStreamProvider *luaSrcProviderImpl =
        new (nothrow) RWopsInputStreamProvider("lua");
    if (luaSrcProviderImpl == (RWopsInputStreamProvider *)0) {
        noMemoryFatalError();
        return;
    }
    luaSrcProvider =
        new (nothrow) UnhashInputStreamProvider(luaSrcProviderImpl);
    if (luaSrcProvider == (UnhashInputStreamProvider *)0) {
        noMemoryFatalError();
        return;
    }
    
// image loader
    imageProvider = new (nothrow) RWopsInputStreamProvider("");
    if (imageProvider == (RWopsInputStreamProvider *)0) {
        noMemoryFatalError();
        return;
    }
    imageLoader = new (nothrow) PNGImageLoader(imageProvider);
    if (imageLoader == (PNGImageLoader *)0) {
        noMemoryFatalError();
        return;
    }
    
// texture loader
    textureImageProvider = new (nothrow) RWopsInputStreamProvider("textures");
    if (textureImageProvider == (RWopsInputStreamProvider *)0) {
        noMemoryFatalError();
        return;
    }
    textureImageLoader = new (nothrow) PNGImageLoader(textureImageProvider);
    if (textureImageLoader == (PNGImageLoader *)0) {
        noMemoryFatalError();
        return;
    }
    
#ifndef AE_ANDROID
// audio
    sdlAudio = new (nothrow) SDLAudio("sounds","music");
    if (sdlAudio == (SDLAudio *)0) {
        noMemoryFatalError();
        return;
    }
// Audio is initialized as soon as a sound or music is loaded. It's this way
// because on iOS if we start an app and quickly close it, the app is closing
// and if we try to initialize audio, iOS reports an error.
#else
    androidAudio = new (nothrow) AndroidAudio();
    if (androidAudio == (AndroidAudio *)0) {
        noMemoryFatalError();
        return;
    }    
#endif
    
// assets
    assets = new (nothrow) RWopsAssets();
    if (assets == (RWopsAssets *)0) {
        noMemoryFatalError();
        return;
    }
    
// storage directory
#ifdef AE_ANDROID
    storageDir = SDL_AndroidGetInternalStoragePath();
    if (storageDir == (const char *)0) {
        fatalError(SDL_GetError());
        return;
    }
#elif defined(AE_IOS)
    if (storageDir == (const char *)0) {
        fatalError("Storage directory not set. Use aeInitSDL2iOS()");
        return;
    }
#endif
    ostringstream storageDirMsg;
    storageDirMsg << "The storage directory is " << storageDir;
    Log::trace(logTag,storageDirMsg.str());
    
// storage 
    storage = new (nothrow) RWopsStorage(storageDir);
    if (storage == (RWopsStorage *)0) {
        noMemoryFatalError();
        return;
    }
    
// file system
#ifdef AE_ANDROID
    const char *fileSystemDir = "/sdcard";
    fileSystem = new (nothrow) RWopsFileSystem(fileSystemDir);
    if (fileSystem == (RWopsFileSystem *)0) {
        noMemoryFatalError();
        return;
    }    
#endif

// sleep function
    sleepFunc = new (nothrow) SDLSleepFunc();
    if (sleepFunc == (SDLSleepFunc *)0) {
        noMemoryFatalError();
        return;
    }
    
// get-ticks function
    getTicksFunc = new (nothrow) SDLGetTicksFunc();
    if (getTicksFunc == (SDLGetTicksFunc *)0) {
        noMemoryFatalError();
        return;
    }

// mutex factory
    mutexFactory = new (nothrow) SDLEngineMutexFactory();
    if (mutexFactory == (EngineMutexFactory *)0) {
        noMemoryFatalError();
        return;
    }
    
// global lock
    globalLock = mutexFactory->createMutex();
    if (globalLock == (EngineMutex *)0) {
        fatalError(mutexFactory->getError().c_str());
        return;
    }
    aeSetGlobalLock(globalLock);

// engine configuration
    EngineCfg engineCfg(mutexFactory);
    engineCfg.setSleepFunc(sleepFunc);
    engineCfg.setGetTicksFunc(getTicksFunc);
    
// engine
    aeEngine = new (nothrow) Engine(engineCfg);
    if (aeEngine == (Engine *)0) {
        noMemoryFatalError();
        return;
    }
    if (aeEngine->chkError()) {
        fatalError(aeEngine->getError().c_str());
        return;
    }
    
// Lua engine configuration
    LuaEngineCfg luaEngineCfg;
    luaEngineCfg.setOS(getLuaEngineOS());
    luaEngineCfg.setLuaSrcProvider(luaSrcProvider);
    luaEngineCfg.setImageLoader(imageLoader);
    luaEngineCfg.setTextureImageLoader(textureImageLoader);
    luaEngineCfg.setAssets(assets);
    luaEngineCfg.setStorage(storage);
    luaEngineCfg.setFileSystem(fileSystem);
#ifndef AE_ANDROID
    luaEngineCfg.setAudio(sdlAudio);
#else
    luaEngineCfg.setAudio(androidAudio);
#endif
    
// Lua engine
    luaEngine = new (nothrow) LuaEngine(logger,luaEngineCfg,aeEngine);
    if (luaEngine == (LuaEngine *)0) {
        noMemoryFatalError();
        return;
    }
    
// Lua engine listener
    luaEngineListener = new (nothrow) SDLLuaEngineListener();
    if (luaEngineListener == (SDLLuaEngineListener *)0) {
        noMemoryFatalError();
        return;
    }
    luaEngine->setLuaEnginePauseListener(luaEngineListener);
    luaEngine->setLuaEngineQuitListener(luaEngineListener);    

#ifdef AE_ANDROID
    aeAddLuaExtraLib(new AndroidLuaExtraLib());
#endif
    
// extra Lua libraries
    addLuaExtraLibs();
    
// plugins
    addLuaEnginePlugins();
    
// start the Lua engine
    if (luaEngine->start() == false) {
        Log::error(logTag,"Failed to initialize engine due to:");
        ostringstream msg;
        msg << "  " << luaEngine->getError();
        fatalError(msg.str().c_str());
        return;
    }
    
// Lua touch handler
    luaTouchHandler = new (nothrow) LuaTouchHandler(luaEngine->getLuaState());
    if (luaTouchHandler == (LuaTouchHandler *)0) {
        noMemoryFatalError();
        return;
    }
    if (luaTouchHandler->chkError()) {
        fatalError(luaTouchHandler->getError().c_str());
        return;        
    }        
    aeEngine->addRequest(luaTouchHandler);    
    
// Lua key handler
    luaKeyHandler = new (nothrow) LuaKeyHandler(luaEngine->getLuaState());
    if (luaKeyHandler == (LuaKeyHandler *)0) {
        noMemoryFatalError();
        return;
    }
    if (luaKeyHandler->chkError()) {
        fatalError(luaKeyHandler->getError().c_str());
        return;
    }
    aeEngine->addRequest(luaKeyHandler); 
    
    drawTime = 0;
    updateTime = 0;
    requestsTime = 0;
}

/** */
static void engineTick() {
// check the resume request
    if (luaEngineListener->getResumeRequest() == true) {
        Log::trace(logTag,"Resuming engine loop");
        pauseLoop = false;
    }
    
// check the pause request
    if (luaEngineListener->getPauseRequest() == true) {
        Log::trace(logTag,"Pausing engine loop");
        pauseLoop = true;
    }
    
// calculate time delta
    Uint32 now = SDL_GetTicks();
    long deltaTime = (long)(now - then);
    then = now;    
    
// draw
    long drawStartTime = SDL_GetTicks();
    if (aeEngine->modelDraw() == false) {
        Log::error(logTag,"Failed to draw models due to:");
        ostringstream msg;
        msg << "  " << aeEngine->getError();
        fatalError(msg.str().c_str());        
        return;        
    }
    long updateStartTime = SDL_GetTicks();
    drawTime += updateStartTime - drawStartTime;
    
// update
    if (pauseLoop == false) {    
        bool anyUpdated;
        if (aeEngine->modelUpdate(deltaTime,anyUpdated) == false) {
            Log::error(logTag,"Failed to update models due to:");
            ostringstream msg;
            msg << "  " << aeEngine->getError();
            fatalError(msg.str().c_str());        
            return;        
        }
        updateTime += SDL_GetTicks() - updateStartTime;
    }
    else {
        SDL_Delay(pauseSleepMilliseconds);
    }
    
// process requests
    long requestsStartTime = SDL_GetTicks();
    if (aeEngine->processRequests() == false) {
        Log::error(logTag,"Failed to process engine requests due to:");
        ostringstream msg;
        msg << "  " << aeEngine->getError();
        fatalError(msg.str().c_str());        
        return;        
    }    
    requestsTime += SDL_GetTicks() - requestsStartTime;

// FPS
    if (fpsCounter.tick(deltaTime) == true) {
        int fps = (int)fpsCounter.getFPS();
        
        ostringstream msg;
        msg << "FPS " << fps;
        msg << ", draw/update/requests[ms] " << drawTime << "/";
        if (pauseLoop == false) {
            msg << updateTime << "/" << requestsTime;            
        }
        else {
            msg << "(pause)";
        }       
        Log::trace(logTag,msg.str());        
        
        drawTime = 0;
        updateTime = 0;
        requestsTime = 0;
    }
}

/** */
static void viewResized(int width,int height) {
    windowWidth = width;
    windowHeight = height;
    
// log
    ostringstream msg;
    msg << "View resized to " << width << "x" << height;
    Log::trace(logTag,msg.str());
    
// engine
    aeEngine->displayResized(width,height);
    
// Lua engine
    if (luaEngine->displayResized(width,height) == false) {
        Log::error(logTag,"Failed to handle display resize due to:");
        ostringstream msg;
        msg << "  " << aeEngine->getError();
        fatalError(msg.str().c_str());        
        return;        
    }    
}

/** */
static void getWindowSize() {
    int width,height;
#ifdef AE_IOS
    SDL_GL_GetDrawableSize(window,&width,&height);
#else
    SDL_GetWindowSize(window,&width,&height);
#endif
    viewResized(width,height);
}

/** */
static void lockEngineTick() {
// might be already destroyed if fatalError() has already been called
    if (engineTickMutex == (SDL_mutex *)0) {
        return;
    }
    
    if (SDL_LockMutex(engineTickMutex) != 0) {
        fatalError(SDL_GetError());
        return;
    }
}

/** */
static void unlockEngineTick() {
    if (engineTickMutex == (SDL_mutex *)0) {
        return;
    }
    
    if (SDL_UnlockMutex(engineTickMutex) != 0) {
        fatalError(SDL_GetError());
        return;
    }
}

/** */
static void lockPauseLoopMutex() {
#ifdef AE_ANDROID
    if (SDL_LockMutex(pauseLoopMutex) != 0) {
        fatalError(SDL_GetError());
        return;
    }
#endif
}

/** */
static void unlockPauseLoopMutex() {
#ifdef AE_ANDROID
    if (SDL_UnlockMutex(pauseLoopMutex) != 0) {
        fatalError(SDL_GetError());
        return;
    }
#endif
}

/** */
static void handleWindowEvent(const SDL_Event &event) {
    switch (event.window.event) {
    // close
        case SDL_WINDOWEVENT_CLOSE:
            break;
            
    // resize
        case SDL_WINDOWEVENT_RESIZED:
            Log::trace(logTag,"Window resized");
            getWindowSize();
            break;
    }
}

/** */
static void handleFingerDown(const SDL_Event &event) {
    luaTouchHandler->touchDown((int)event.tfinger.fingerId,
        event.tfinger.x * windowWidth,event.tfinger.y * windowHeight);
#ifdef AE_IOS
    //SDL_ClearError();
#endif
}

/** */
static void handleFingerMotion(const SDL_Event &event) {
    luaTouchHandler->touchMove((int)event.tfinger.fingerId,
        event.tfinger.x * windowWidth,event.tfinger.y * windowHeight);
#ifdef AE_IOS
    //SDL_ClearError();
#endif
}

/** */
static void handleFingerUp(const SDL_Event &event) {
    luaTouchHandler->touchUp((int)event.tfinger.fingerId,
        event.tfinger.x * windowWidth,event.tfinger.y * windowHeight);
#ifdef AE_IOS
    //SDL_ClearError();
#endif
}

/** */
static void handleKeyDown(const SDL_Event &event) {
    SDL_Keycode keysym = event.key.keysym.sym;
    const Key *key = mapSDLKey(keysym);
    if (key == (const Key *)0) {
        ostringstream err;
        err << "Key down event of unknown SDL key code " << keysym;
        Log::warning(logTag,err.str());
        return;
    }
    luaKeyHandler->keyDown(*key);
}

/** */
static void handleKeyUp(const SDL_Event &event) {
    SDL_Keycode keysym = event.key.keysym.sym;
    const Key *key = mapSDLKey(keysym);
    if (key == (const Key *)0) {
        ostringstream err;
        err << "Key up event of unknown SDL key code " << keysym;
        Log::warning(logTag,err.str());
        return;
    }
    luaKeyHandler->keyUp(*key);
}

/** */
static void terminating() {
#ifdef AE_ANDROID
// wait for the loop to stop
    quitLoop = true;
    while (loopStopped == false) {
    }
#endif
    
    if (luaEngine->stop() == false) {
        Log::error(logTag,"Failed to stop the engine due to:");
        ostringstream err;
        err << "Failed to stop the engine: " << luaEngine->getError();
        Log::error(logTag,err.str());
    }
    
    aeEngine->stop();
#ifdef AE_ANDROID
    quit(false);
#endif    
}

/** */
static void pausing() {
    if (luaEngine->pausing() == false) {
        ostringstream err;
        err << "Lua engine pause failed: " << luaEngine->getError();
        fatalError(err.str().c_str());
        return;
    }
}

/** */
static void resuming() {
    fpsCounter.reset();
    
    if (luaEngine->resuming() == false) {
        ostringstream err;
        err << "Lua engine resume failed: " << luaEngine->getError();
        fatalError(err.str().c_str());
        return;
    }
}

/** */
static void renderFrame(void *callbackParam) {
    Uint32 start = SDL_GetTicks();
    SDL_Event event;
    
    while (SDL_PollEvent(&event)) {
        if (quitLoop == true) {
            break;
        }
        
        switch (event.type) {
            // window event
            case SDL_WINDOWEVENT:
                handleWindowEvent(event);
                break;
                
            // finger down
            case SDL_FINGERDOWN:
                handleFingerDown(event);
                break;
                
            // finger motion
            case SDL_FINGERMOTION:
                handleFingerMotion(event);
                break;
                
            // finger up
            case SDL_FINGERUP:
                handleFingerUp(event);
                break;
                
            // key down
            case SDL_KEYDOWN:
                handleKeyDown(event);
                break;
                
            // key up
            case SDL_KEYUP:
                handleKeyUp(event);
                break;
                
            // quit
            case SDL_QUIT:
                Log::trace(logTag,"Got event SDL_QUIT");
                quitLoop = true;
                break;
        }
        
        if (quitLoop == true) {
            break;
        }
    }
    
// check the quit request
    if (luaEngineListener->getQuitRequest() == true) {
        quitLoop = true;
    }
    
// tick
    lockEngineTick();
    if (quitLoop == false && loopPauseAppEvent == false) {        
        engineTick();       
        SDL_GL_SwapWindow(window);
        
        Uint32 renderTime = SDL_GetTicks() - start;
    // FPS maximum
        int delay = fpsMax.getDelayPeriod((int)renderTime);
        SDL_Delay(delay);
    }
    unlockEngineTick();
}

#ifdef AE_ANDROID
/** */
static void enterLoop() {
    Log::trace(logTag,"Starting loop");
    while (quitLoop == false) {
        lockPauseLoopMutex();
        if (quitLoop == false) {
            renderFrame((void *)0);
        }
        unlockPauseLoopMutex();
    }
    loopStopped = true;
    Log::trace(logTag,"Loop stopped");
}
#endif

/** */
static int handleAppEvents(void *data,SDL_Event *event) {
    switch (event->type) {
        case SDL_APP_TERMINATING:
            Log::trace(logTag,"SDL_APP_TERMINATING");
            terminating();
            return 0;
            
        case SDL_QUIT:
            Log::trace(logTag,"Got event SDL_QUIT in handleAppEvents()");
            quitLoop = true;
            unlockPauseLoopMutex();
            return 1;
            
        case SDL_APP_LOWMEMORY:
            Log::trace(logTag,"SDL_APP_LOWMEMORY");
            return 0;
            
        case SDL_APP_WILLENTERBACKGROUND:
            Log::trace(logTag,"SDL_APP_WILLENTERBACKGROUND");
            loopPauseAppEvent = true;
            lockPauseLoopMutex();
            lockEngineTick();
            pausing();
            unlockEngineTick();
            return 0;
            
        case SDL_APP_DIDENTERBACKGROUND:
            Log::trace(logTag,"SDL_APP_DIDENTERBACKGROUND");
            lockEngineTick();
            glFinish();
            unlockEngineTick();
            return 0;
            
        case SDL_APP_WILLENTERFOREGROUND:
            Log::trace(logTag,"SDL_APP_WILLENTERFOREGROUND");
            return 0;
            
        case SDL_APP_DIDENTERFOREGROUND:
            Log::trace(logTag,"SDL_APP_DIDENTERFOREGROUND");
            then = SDL_GetTicks();
            resuming();
            unlockPauseLoopMutex();
            loopPauseAppEvent = false;
            return 0;
    }
    
    return 1;
}

/** */
void aeInitDebug() {    
    debugFlag = true;
    
// logger
#ifdef AE_IOS
    logger = new NSLogger();
#else
    logger = new SDLLogger();
#endif
}

/** */
bool aeIsDebug() {
    return debugFlag;
}

/** */
void aeSetStorageDir(const char *storageDir_) {
    storageDir = storageDir_;
}

/** */
SDL_Window *aeGetSDLWindow() {
    return window;
}

#ifdef AE_IOS
/** Defined in SDL */
extern "C" {
int SDL_iPhoneSetAnimationCallback(SDL_Window *window,int interval,
    void (*callback)(void *),void *callbackParam);

/** */
void renderFrame(void *);
}
#endif

/** */
void aeInitLogger() {
// if no logger set, then discard all the log messages
    if (logger == (Logger *)0) {
        logger = new NullLogger();
    }
    Log::init(logger);
}

/** */
static void aeInit() {
    then = SDL_GetTicks();
    pauseLoop = false;
    loopPauseAppEvent = false;
    quitLoop = false;
    shuttingDown = false;
    
#ifdef AE_ANDROID
    loopStopped = false;
#endif
}

/** */
int ae_sdl_main(int argc,char *argv[]) {
    aeInitLogger();
    Log::trace(logTag,"Entered ae_sdl_main");
    aeInit();
    initSDL();
    createGLContext();
    logGLAttributes();
    initEngine();
    
// if a fatal error was reporting during initialization, then everything is
// destroyed and we cannot continue
    if (shuttingDown == false) {        
        getWindowSize();
    
#ifdef AE_IOS
        SDL_iPhoneSetAnimationCallback(window,1,renderFrame,0);
#else
        enterLoop();
#endif
    }    

    Log::trace(logTag,"About to return from ae_sdl_main");    
    return 0;
}
