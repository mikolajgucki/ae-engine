#include <cstdio>
#include <cstdlib>
#include <string>
#include <iostream>
#include <sstream>

#include "SDL.h"
#include "SDL_helper.h"
#include "lua_common.h"
#include "Log.h"
#include "ConsoleLogger.h"
#include "DebugLog.h"
#include "Error.h"
#include "LuaGetGlobal.h"
#include "LuaGetField.h"
#include "FileInputStream.h"
#include "FileInputStreamProvider.h"
#include "CachedPNGImageLoader.h"
#include "io_util.h"
#include "System.h"
#include "Mutex.h"
#include "ScopedLock.h"
#include "Model.h"
#include "LuaModel.h"
#include "LuaTouchHandler.h"
#include "LuaKeyHandler.h"
#include "DesktopEngineCfg.h"
#include "cfg_reader.h"
#include "DesktopLuaEngineListener.h"
#include "DesktopEngineListener.h"
#include "DesktopEngineLauncher.h"
#include "DesktopEngineSDL.h"
#include "FPSCounter.h"
#include "Timer.h" 

#include "Screenshot.h"
#include "lua_screenshot.h"
#include "luap.h"
#include "luap_List.h"
#include "luap_Call.h"
#include "lua_profiler.h"
#include "CommConsole.h"
#include "desktop_engine.h"

using namespace std;
using namespace ae;
using namespace ae::sdl;
using namespace ae::lua;
using namespace ae::system;
using namespace ae::util;
using namespace ae::image;
using namespace ae::engine;
using namespace ae::engine::desktop;
using namespace ae::profiler::lua;

namespace ae {

namespace engine {

namespace desktop {

/// The log tag.
static const char *const logTag = "desktop.engine";

/// The log tag for the debug log.
static const char *const debugLogTag = "desktop_engine";

/// The number of milliseconds to sleep on update while paused not to
/// consume CPU.
static const int pauseSleepMilliseconds = 100;

/// The system-related methods.
static System sys;

/// The logger.
static Logger *logger = (Logger *)0;

/// The configuration.
static DesktopEngineCfg *cfg = (DesktopEngineCfg *)0;

/// The listener.
static DesktopEngineListener *listener = (DesktopEngineListener *)0;

/// The engine launcher.
static DesktopEngineLauncher *launcher;

/// The SDL wrapper.
static DesktopEngineSDL *sdl = (DesktopEngineSDL *)0;

/// The last system time in the idle function.
static long previousSystemTime;

/// Indicates if the engine is paused (by pressing the pause key not by request
/// from the Lua engine).
static bool paused = false;

/// Indicates if the engine loop execution is paused (by request from the Lua
/// engine).
static bool pausedByRequest = false;

/// Indicates if the Lua engine is restarting.
static bool restarting = false;

/// Indicates if the last draw failed.
static bool drawFailed = false;

/// Indicates if the last update failed.
static bool updateFailed = false;

/// The mutex synchronizing access to the engine.
static Mutex engineMutex;

/// The identifier of the pointer passed to the touch listener.
static const int pointerId = 0;

/// The touch handler.
static LuaTouchHandler *luaTouchHandler = (LuaTouchHandler *)0;

/// The key handler.
static LuaKeyHandler *luaKeyHandler = (LuaKeyHandler *)0;

/// The Lua engine listener.
static DesktopLuaEngineListener *luaEngineListener =
    (DesktopLuaEngineListener *)0;

/// The FPS counter;
static FPSCounter fpsCounter;

/// The total draw time in milliseconds since the last FPS log.
static long drawTime;

/// The total update time in milliseconds since the last FPS log.
static long updateTime;

/// The total request processing time in milliseconds since the last FPS log.
static long requestTime;

/// The timer.
static Timer *timer = (Timer *)0;

/// Indicates if to pause the engine.
static bool pauseFlag = false;

/// Indicates if to resume the engine.
static bool resumeFlag = false;

/// Indicates if to restart the engine.
static bool restartFlag = false;

/// Indicates if to stop the engine.
static bool stopFlag = false;

/// Indicates if stats logging is enabled.
static bool logStatsEnabled = true;

/// The current volume factor (0..1).
static double volumeFactor = 1;

/// Indicates if to initialize the volume in the first frame.
static bool initVolume = true;

/// The mutex synchronizing taking screenshots.
static Mutex screenshotMutex;

/// Indicates if to take a screenshot.
static bool screenshotFlag = false;

// Forward declaration.
static void handleUpdateFailed();

// To be able write custom main()
#undef main

/** */
static void logSection(const string &name) {
    ostringstream msg;
    msg << "-----------8<------------ " << name;
    Log::info(logTag,msg.str());
}

/**
 * \brief Logs the events assigned to keys.
 */
static void logKeyMapping() {
    Log::info(logTag,"Key mapping:");
    Log::info(logTag,"  r      Restart");
    Log::info(logTag,"  s      Screenshot (screenshot.png)");
    Log::info(logTag,"  space  Pause/resume");
    Log::info(logTag,"  q      Quit");
    Log::info(logTag,"Key emulation:");
    Log::info(logTag,"  backspace  back");
    Log::info(logTag,"  home       menu");
}

/**
 * \brief Finalizes the desktop engine.
 */
static void finalize() {
    if (luaEngineListener != (DesktopLuaEngineListener *)0) {
        DebugLog::trace(debugLogTag,"Deleting desktop Lua engine listener");        
        delete luaEngineListener;
    }
    if (luaKeyHandler != (LuaKeyHandler *)0) {
        DebugLog::trace(debugLogTag,"Deleting Lua key handler");
        delete luaKeyHandler;
    }
    if (luaTouchHandler != (LuaTouchHandler *)0) {
        DebugLog::trace(debugLogTag,"Deleting Lua touch handler");        
        delete luaTouchHandler;
    }
    if (launcher != (DesktopEngineLauncher *)0) {
        DebugLog::trace(debugLogTag,"Deleting desktop engine launcher");        
        delete launcher;
    }
    if (timer != (Timer *)0) {
        DebugLog::trace(debugLogTag,"Deleting timer");        
        delete timer;
    }
    if (sdl != (DesktopEngineSDL *)0) {
        DebugLog::trace(debugLogTag,"Deleting desktop engine SDL");        
        delete sdl;
    }
}

/** */
static void windowResized(int width,int height) {
    if (paused || drawFailed) {
        return;
    }
    
// lock engine
    ScopedLock lock(&engineMutex);
    
// engine
    launcher->getEngine()->displayResized(width,height);
        
// Lua engine
    LuaEngine *luaEngine = launcher->getLuaEngine();
    if (luaEngine->displayResized(width,height) == false) {
        ostringstream msg;
        msg << "Resize failed: " << luaEngine->getError();
        Log::error(logTag,msg.str());
        return;
    }
}

/** */
static void drawFrame() {
    if (drawFailed || restarting) {
        return;
    }
    
    Engine *engine = launcher->getEngine();
// draw
    DebugLog::trace(debugLogTag,"Drawing models");
    long drawStartTime = sys.getSystemTime();
    if (engine->modelDraw() == false) {
        Log::error(logTag,"Failed to draw models due to:");
        ostringstream msg;
        msg << "  " << engine->getError();
        Log::error(logTag,msg.str());
        drawFailed = true;
        return;
    } 
    drawTime += sys.getSystemTime() - drawStartTime;
    DebugLog::trace(debugLogTag,"Done drawing models");

// swap
    sdl->swapWindow();
}

/** */
static void updateWindowTitle() {
    int fps = (int)fpsCounter.getFPS();    
    
    ostringstream title;
    title << cfg->getAppName();
    
// FPS
    if (paused) {
        title << " (paused";
    }
    else if (fps <= 1000) {
        title << " (FPS " << fps;
    }
    else {
        title << " (FPS more than 1k"; 
    }
    
// volume
    if (cfg->getAudio() == true) {
        if (volumeFactor > 0) {
            title << ", volume " << ((int)(volumeFactor * 100)) << "%";
        }
        else {
            title << ", muted";
        }
    }
    else {
        title << ", no audio";
    }
    
    title << ")";    
    sdl->setWindowTitle(title.str());  
}


/** */
bool setVolumeFactor(double volumeFactor_,bool notify) {
    if (cfg->getAudio() == false) {
        return true;
    }
    
    if (volumeFactor_ < 0) {
        volumeFactor_ = 0;
    }
    if (volumeFactor_ > 1) {
        volumeFactor_ = 1;
    }
    
    if (volumeFactor == volumeFactor_) {
        return true;
    }
    
    if (launcher->getAudio()->setVolumeFactor(volumeFactor_) == false) {
        return false;
    }
    volumeFactor = volumeFactor_;
    updateWindowTitle();
    
    if (notify == true && listener != (DesktopEngineListener *)0) {
        listener->volumeFactorSet(volumeFactor);
    }    
    
    return true;
}

/** */
static void postScreenshot() {
    ScopedLock lock(&screenshotMutex);
    screenshotFlag = true;       
}

/** */
static void takeScreenshot() {
    ScopedLock lock(&screenshotMutex);
    if (screenshotFlag == true) {
        screenshotFlag = false;        
        Engine *engine = launcher->getEngine();
        
    // check resolution
        int width = engine->getWidth();
        if (width % 4 != 0) {
            CommConsole::print("Cannot take screenshot. "
                "Width is not multiple of 4.\n");
            return;
        }
        int height = engine->getHeight();
        if (height % 4 != 0) {
            CommConsole::print("Cannot take screenshot. "
                "Height is not multiple of 4.\n");
            return;
        }
        
        const string filename = "screenshot.png";
        Screenshot screenshot;        
    // log
        ostringstream takingLog;
        takingLog << "Taking screenshot " << filename << " of size " <<
           width << "x" << height << endl;
        CommConsole::trace(takingLog.str());
        
    // take
        screenshot.setSize(engine->getWidth(),engine->getHeight());
        if (screenshot.take(filename) == false) {
            ostringstream errLog;
            errLog << "Failed to take screenshot: " << screenshot.getError();
            DebugLog::error(debugLogTag,takingLog.str());
            return;
        }
    }    
}

/** */
static void logStats() {
    if (logStatsEnabled == false) {
        return;
    }
    
    int fps = (int)fpsCounter.getFPS();
    ostringstream msg;
    
// FPS
    if (fps <= 1000) {
        msg << "FPS " << fps;
    }
    else {
        msg << "FPS more than 1k"; 
    }
    
// times
    msg << ", draw/update/request[ms] ";
    msg << drawTime << "/" << updateTime << "/" << requestTime;
    
// memory
    double usedMemory;
    launcher->getLuaEngine()->getUsedMemory(usedMemory);
    msg << ", lua.memory[kb] " << (int)usedMemory;
    
// log
    Log::trace(logTag,msg.str());
    
// clear
    drawTime = 0;
    updateTime = 0;
    requestTime = 0;    
}

/** */
static void updateFrame() {
    if (paused || drawFailed || updateFailed) {
        return;
    }
    
// resume
    if (luaEngineListener->getResumeRequest() == true) {
        pausedByRequest = false;
        previousSystemTime = sys.getSystemTime();
    }    
// pause
    if (luaEngineListener->getPauseRequest() == true) {
        pausedByRequest = true;
    }
    
    long systemTime = sys.getSystemTime();
    long time = systemTime - previousSystemTime;
    previousSystemTime = systemTime;
    
// timer
    timer->tick(time);

    Engine *engine = launcher->getEngine();
// update models
    if (pausedByRequest == false && restarting == false) {
        DebugLog::trace(debugLogTag,"Updating models");
        long updateStartTime = sys.getSystemTime();
        bool anyUpdated;
        if (engine->modelUpdate(time,anyUpdated) == false) {
            Log::error(logTag,"Failed to update models due to:");
            ostringstream msg;
            msg << "  " << engine->getError();
            Log::error(logTag,msg.str());
            handleUpdateFailed();        
        }
        updateTime += sys.getSystemTime() - updateStartTime;
        DebugLog::trace(debugLogTag,"Done updating models");
    }
    else {
        sys.sleep(pauseSleepMilliseconds);
    }
    
// process requests
    DebugLog::trace(debugLogTag,"Processing requests");
    long requestStartTime = sys.getSystemTime();
    if (engine->processRequests() == false) {
        Log::error(logTag,"Failed to process engine requests due to:");
        ostringstream msg;
        msg << "  " << engine->getError();
        Log::error(logTag,msg.str());
        handleUpdateFailed();
    }
    requestTime += sys.getSystemTime() - requestStartTime;
    DebugLog::trace(debugLogTag,"Done processing requests");
    
// restart is performed in a request and it must be done at this point
    restarting = false;
    
// FPS counter
    if (fpsCounter.tick(time) == true) {
        updateWindowTitle();
        logStats();
    }
    DebugLog::trace(debugLogTag,"updateFrame() end");
}

/** */
static void engineTick() {
    DebugLog::trace(debugLogTag,"engineTick()");
    
// lock engine
    ScopedLock lock(&engineMutex);
    
// stop
    if (stopFlag == true) {
        return;
    }
    
    drawFrame();
    takeScreenshot();    
    updateFrame();
}

/** */
static void pauseEngine() {
    pauseFlag = false;
    if (paused == true || stopFlag == true) {
        return;
    }
    
    LuaEngine *luaEngine = launcher->getLuaEngine();
    if (luaEngine->pausing() == false) {
        ostringstream msg;
        msg << "Lua engine pause failed: " << luaEngine->getError();
        Log::error(logTag,msg.str());
        return;        
    }
        
    paused = true;
    logSection("pause");
    updateWindowTitle();
    
    if (listener != (DesktopEngineListener *)0) {
        listener->paused();
    }    
}

/** */
static void resumeEngine() {
    resumeFlag = false;
    if (paused == false || stopFlag == true) {
        return;
    }
    
    LuaEngine *luaEngine = launcher->getLuaEngine();
    if (luaEngine->resuming() == false) {
        ostringstream msg;
        msg << "Lua engine resume failed: " << luaEngine->getError();
        Log::error(logTag,msg.str());
        return;        
    }
    
    logSection("resume");
    paused = false;
    previousSystemTime = sys.getSystemTime();
    
    if (listener != (DesktopEngineListener *)0) {
        listener->resumed();
    }     
}

/** */
static void handleUpdateFailed() {
    updateFailed = true;
    pauseEngine();
}

/** */
static void restart() {
// lock engine
    ScopedLock lock(&engineMutex);
    
    if (updateFailed == true || stopFlag == true) {
        return;
    }   

    logSection("restart");
    drawFailed = false;
    updateFailed = false;
    if (paused == true) {
        if (listener != (DesktopEngineListener *)0) {
            listener->resumed();
        }         
        paused = false;
        previousSystemTime = sys.getSystemTime();
    }
    restarting = true;
    restartFlag = false;
    launcher->getLuaEngine()->postRestart();
    
    if (listener != (DesktopEngineListener *)0) {
        listener->restarting();
    }
}

/** */
static void postRestart() {   
// lock engine
    ScopedLock lock(&engineMutex);
    
    if (restarting == true || stopFlag == true) {
        return;
    }
    restartFlag = true;
}

/** */
static void logLuaProfiler() {
    if (cfg->getLuaProfiler() == false) {
        return;
    }
    luap_List *log = luap_Stop(launcher->getLuaEngine()->getLuaState());
    const char *filename = "lua_profile.csv";
    
// log
    ostringstream msg;
    msg << "Saving profiler log in file " << filename;
    Log::trace(logTag,msg.str());
      
// dump to CSV and delete
    dumpLuaProfilerLogToCSV(log,launcher->getFileSystem(),filename);    
    luap_DeleteLog(log);
}

/** */
static void stop() {
// lock engine
    ScopedLock lock(&engineMutex);
    
    logSection("stop");
    logLuaProfiler();
    
// pause
    if (launcher->getLuaEngine()->pausing() == false) {
        ostringstream msg;
        msg << "Lua engine pause failed: " <<
            launcher->getLuaEngine()->getError();
        Log::error(logTag,msg.str());
        return;        
    }
    
// stop
    if (launcher->getLuaEngine()->stop() == false) {
        Log::error(logTag,"Failed to stop the engine due to:");
        ostringstream msg;
        msg << "  " << launcher->getLuaEngine()->getError();
        Log::error(logTag,msg.str());
    }
    launcher->getEngine()->stop();
}

/** */
static void postStop() {
    DebugLog::trace(debugLogTag,"postStop()");
// lock engine
    ScopedLock lock(&engineMutex);
    
    if (listener != (DesktopEngineListener *)0) {
        listener->stopping();
    }
    stopFlag = true;
    DebugLog::trace(debugLogTag,"Stop flag set to true in postStop()");
}

/** */
static void handleWindowEvent(const SDL_Event &event) {
    switch (event.window.event) {
    // close
        case SDL_WINDOWEVENT_CLOSE:
            postStop();
            break;
            
    // resize
        case SDL_WINDOWEVENT_RESIZED:
            windowResized(event.window.data1,event.window.data2);
            break;
    }
}

/** */
static void handleMouseDown(const SDL_Event &event) {
    if (paused || drawFailed || updateFailed) {
        return;
    }        
    
    ScopedLock lock(&engineMutex);    
    luaTouchHandler->touchDown(1,event.button.x,event.button.y);
}

/** */
static void handleMouseMotion(const SDL_Event &event) {
    if (paused || drawFailed || updateFailed) {
        return;
    }    
    
    ScopedLock lock(&engineMutex);    
    luaTouchHandler->touchMove(1,event.button.x,event.button.y);
}

/** */
static void handleMouseUp(const SDL_Event &event) {
    if (paused || drawFailed || updateFailed) {
        return;
    }    
    
    ScopedLock lock(&engineMutex);    
    luaTouchHandler->touchUp(1,event.button.x,event.button.y);
}

/** */
static void handleKeyDown(const SDL_Event &event) {
}

/** */
static void handleKeyUp(const SDL_Event &event) {
    SDL_Keycode keysym = event.key.keysym.sym;
    
// lock engine
    ScopedLock lock(&engineMutex);            
    
// pause
    if (keysym == SDLK_SPACE) {
        if (paused) {
            if (updateFailed == false) {
                resumeEngine();
            }
            else {
                Log::warning(logTag,
                    "Cannot resume if update failed. Restart instead. ");
            }
        }
        else {
            pauseEngine();
        }
        return;
    }
    
// restart
    if (keysym == SDLK_r) {
        postRestart();
        return;
    }
    
// quit
    if (keysym == SDLK_q) {
        postStop();
        return;
    }
    
// backspace (back emulation)
    if (keysym == SDLK_BACKSPACE) {
        luaKeyHandler->keyDownAndUp(Key::BACK);
        return;
    }
    
// enter (menu emulation)
    if (keysym == SDLK_HOME) {
        luaKeyHandler->keyDownAndUp(Key::MENU);
        return;
    }    
    
// volume up
    if (keysym == SDLK_EQUALS) {
        setVolumeFactor(volumeFactor + 0.1);
        return;
    }
    
// volume down
    if (keysym == SDLK_MINUS) {
        setVolumeFactor(volumeFactor - 0.1);
        return;
    }
    
// screenshot
    if (keysym == SDLK_s) {
        postScreenshot();
        return;
    }
}

/** */
static void renderFrame() {
    DebugLog::trace(debugLogTag,"renderFrame()");
    
// lock engine
    ScopedLock lock(&engineMutex);        
    if (stopFlag == true) {
        return;
    }
    
    SDL_Event event;
// events
    while (SDL_PollEvent(&event)) {
        switch (event.type) {
            // window event
            case SDL_WINDOWEVENT:
                handleWindowEvent(event);
                break;
                
            // mouse down
            case SDL_MOUSEBUTTONDOWN:
                handleMouseDown(event);
                break;
                
            // mouse motion
            case SDL_MOUSEMOTION:
                handleMouseMotion(event);
                break;
                
            // mouse up
            case SDL_MOUSEBUTTONUP:
                handleMouseUp(event);
                break;                
                
            // key down
            case SDL_KEYDOWN:
                handleKeyDown(event);
                break;
                
            // key up
            case SDL_KEYUP:
                handleKeyUp(event);
                break;
        }
    }
    
// tick
    engineTick();
    DebugLog::trace(debugLogTag,"engineTick() exit");
    
// volume
    if (initVolume == true) {
        if (setVolumeFactor(cfg->getVolume() / (double)100,false) == true) {
            initVolume = false;
        }
    }
    
    if (listener != (DesktopEngineListener *)0 && stopFlag == false) {
        listener->frameRendered();
    }
}

/** */
static void enterLoop() {
    DebugLog::trace(debugLogTag,"enterLoop()");    
    logSection("loop");
    
    while (true) {
        if (pauseFlag == true) {
            pauseEngine();
        }
        if (resumeFlag == true) {
            resumeEngine();
        }
        if (restartFlag == true) {
            restart();
        }
        if (luaEngineListener->getQuitRequest() == true || stopFlag == true) {
            DebugLog::trace(debugLogTag,"Stopping...");
            stop();
            break;
        }
        renderFrame();
    }
}

/**
 * \brief Launches the desktop engine.
 */
static void launch() {
// SDL
    DebugLog::trace(debugLogTag,"Creating desktop engine SDL");
    sdl = new DesktopEngineSDL(cfg);
    if (sdl->init() == false) {
        Log::error(logTag,sdl->getError());
        exit(-1);
    }
    
// timer
    DebugLog::trace(debugLogTag,"Creating timer");
    timer = new Timer();

// engine mutex
    DebugLog::trace(debugLogTag,"Creating engine mutex");
    if (engineMutex.create() == false) {
        Log::error(logTag,"Failed to launch the engine due to:");
        ostringstream msg;
        msg << "  " << engineMutex.getError();
        Log::error(logTag,msg.str());
        return;
    }
    
// screenshot mutex
    DebugLog::trace(debugLogTag,"Creating screenshot mutex");
    if (screenshotMutex.create() == false) {
        Log::error(logTag,"Failed to launch the engine due to:");
        ostringstream msg;
        msg << "  " << screenshotMutex.getError();
        Log::error(logTag,msg.str());
        return;
    }

// window
    if (sdl->createWindow() == false) {
        Log::error(logTag,sdl->getError());
        return;
    }
    
// launcher
    DebugLog::trace(debugLogTag,"Creating desktop engine launcher");
    launcher = new DesktopEngineLauncher(cfg,logger,timer);
    if (launcher->launch() == false) {
        Log::error(logTag,"Failed to launch the engine due to:");
        ostringstream msg;
        msg << "  " << launcher->getError();
        Log::error(logTag,msg.str());
        return;
    }    

// Lua state and engine created
    if (listener != (DesktopEngineListener *)0) {
        listener->luaStateCreated(launcher->getLuaEngine()->getLuaState());
        listener->engineCreated(launcher->getEngine());
    }
    
// touch handler
    DebugLog::trace(debugLogTag,"Creating Lua touch handler");
    luaTouchHandler = new (nothrow) LuaTouchHandler(
        launcher->getLuaEngine()->getLuaState());
    if (luaTouchHandler == (LuaTouchHandler *)0) {
        Log::error(logTag,"Could not allocate memory");
        return;
    }
    if (luaTouchHandler->chkError()) {
        Log::error(logTag,luaTouchHandler->getError());
        return;
    }
    launcher->getEngine()->addRequest(luaTouchHandler);    
    
// key handler
    DebugLog::trace(debugLogTag,"Creating Lua key handler");
    luaKeyHandler = new (nothrow) LuaKeyHandler(
        launcher->getLuaEngine()->getLuaState());
    if (luaKeyHandler == (LuaKeyHandler *)0) {
        Log::error(logTag,"Could not allocate memory");
        return;
    }
    if (luaKeyHandler->chkError()) {
        Log::error(logTag,luaKeyHandler->getError());
        return;
    }
    launcher->getEngine()->addRequest(luaKeyHandler);    
    
// the cached image loaders to update while restarting the Lua engine
    vector<CachedPNGImageLoader *> imageLoaders;
    imageLoaders.push_back(launcher->getCachedImageLoader());
    imageLoaders.push_back(launcher->getTextureCachedImageLoader());    
    
// Lua engine listener
    DebugLog::trace(debugLogTag,"Creating desktop Lua engine listener");
    luaEngineListener = new (nothrow) DesktopLuaEngineListener(
        imageLoaders,luaTouchHandler,luaKeyHandler,listener);
    if (luaEngineListener == (DesktopLuaEngineListener *)0) {
        Log::error(logTag,"Could not allocate memory");
        return;
    }    
    launcher->getLuaEngine()->setLuaEngineRestartListener(luaEngineListener);
    launcher->getLuaEngine()->setLuaEnginePauseListener(luaEngineListener);
    launcher->getLuaEngine()->setLuaEngineQuitListener(luaEngineListener);
    
// Lua profiler
    if (cfg->getLuaProfiler() == true) {
        luap_Start(launcher->getLuaEngine()->getLuaState());
    }
    
// window size
    int width,height;
    sdl->getWindowSize(width,height);
    windowResized(width,height);
    
// notify about to enter the loop
    if (listener != (DesktopEngineListener *)0) {
        listener->willEnterLoop();
    }    
    
// screenshot
    loadLuaScreenshotFunc(launcher->getLuaEngine()->getLuaState(),
        launcher->getEngine());
    
// enter the loop
    previousSystemTime = sys.getSystemTime();
    enterLoop();
}

/** */
void runDesktopEngine(Logger *logger_,DesktopEngineCfg *cfg_,
    DesktopEngineListener *listener_) {
//
    logger = logger_;
    cfg = cfg_;
    listener = listener_;
    
    logKeyMapping();
    launch();
    finalize();
}             

/** */
void pauseDesktopEngine() {    
    DebugLog::trace(debugLogTag,"pauseDesktopEngine()");    
    pauseFlag = true;
}

/** */
void resumeDesktopEngine() {
    DebugLog::trace(debugLogTag,"resumeDesktopEngine()");    
    resumeFlag = true;
}

/** */
void restartDesktopEngine() {
    DebugLog::trace(debugLogTag,"restartDesktopEngine()");    
    postRestart();
}

/** */
void stopDesktopEngine() {
    DebugLog::trace(debugLogTag,"stopDesktopEngine()");    
    postStop();
}

/** */
void setLogStatsEnabled(bool enabled) {
    logStatsEnabled = enabled;
}

} // namespace

} // namespace

} // namespace

/**
 * Must be outside the namespace to link it properly.
 */
long luap_GetTime() {
    return ae::engine::desktop::sys.getSystemTime();
}

