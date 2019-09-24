#include <iostream>
#include <sstream>
#include <algorithm>

#include "ae_defs.h"
#include "Log.h"
#include "lua_log.h"
#include "lua_common.h"
#include "LuaGetField.h"

#include "InputStreamContentReader.h"

#include "luaVec3.h"
#include "luaMat4.h"
#include "luaImage.h"
#include "luaTexture.h"
#include "lua_audio.h"
#include "luaSound.h"
#include "luaMusic.h"
#include "luaInputStream.h"
#include "luaOutputStream.h"
#include "lua_assets.h"
#include "lua_storage.h"
#include "lua_filesystem.h"
#include "lua_MessagePack.h"
#include "luaMessagePackPacker.h"
#include "luaMessagePackUnpacker.h"

#include "lua_ae.h"
#include "LuaEngine.h"

using namespace std;
using namespace ae::lua;
using namespace ae::io;
using namespace ae::image;

namespace ae {
    
namespace engine {
 
/**
 * \brief Restarts the given Lua engine.
 */
class LuaEngineRestartRequest:public EngineRequest {
    /// The Lua engine to restart.
    LuaEngine *luaEngine;
        
public:
    /**
     * \brief Constructs a LuaEngineRestartRequest.
     * \param luaEngine_ The Lua engine to restart.
     */
    LuaEngineRestartRequest(LuaEngine *luaEngine_):luaEngine(luaEngine_) {
    }
    
    /** */
    virtual ~LuaEngineRestartRequest() {
    }
    
    /** */
    virtual bool run() {
        if (luaEngine->restart() == false) {
            setError(luaEngine->getError());
            return false;
        }
        
        return true;
    }
};    
    
/// The name of the source loaded on start
const char *const LuaEngine::ROOT_LUA_SRC = "root.lua";
    
/// The log tag.
const char *const LuaEngine::logTag = "lua.engine";

/** */
LuaEngine::LuaEngine(Logger *logger_,LuaEngineCfg cfg_,Engine *engine_):
    Error(),logger(logger_),L((lua_State *)0),cfg(cfg_),
    luaLoader((LuaLoader *)0),imageUtil((ImageUtil *)0),luaPCall(),
    engine(engine_),luaModels(),restartRequest((EngineRequest *)0),
    restartListener((LuaEngineRestartListener *)0),
    pauseListener((LuaEnginePauseListener *)0),
    quitListener((LuaEngineQuitListener *)0),
    width(-1),height(-1),luaGC(),extraLibManager(),enginePluginManager() {
//
    create();
}

/** */
void LuaEngine::create() {
// Lua loader
    luaLoader = new (nothrow) LuaLoader();
    if (luaLoader == (LuaLoader *)0) {
        setNoMemoryError();
        return;
    }
    if (luaLoader->chkError()) {
        setError(luaLoader->getError());
        return;
    }
    
// image utility
    imageUtil = new (nothrow) ImageUtil();
    if (imageUtil == (ImageUtil *)0) {
        setNoMemoryError();
        return;
    }
    
// restart request
    restartRequest = new (nothrow) LuaEngineRestartRequest(this);
    if (restartRequest == (LuaEngineRestartRequest *)0) {
        setNoMemoryError();
        return;
    }    
}

/** */
LuaEngine::~LuaEngine() {
    if (restartRequest != (LuaEngineRestartRequest *)0) {
        delete restartRequest;
    }
    if (imageUtil != (ImageUtil *)0) {
        delete imageUtil;
    }
    if (luaLoader != (LuaLoader *)0) {
        delete luaLoader;
    }
}

/** */
void LuaEngine::addExtraLib(LuaExtraLib *lib) {
    extraLibManager.addLib(lib);
}
    
/** */
void LuaEngine::deleteExtraLibs() {
    extraLibManager.deleteLibs();
}

/** */
void LuaEngine::addPlugin(LuaEnginePlugin *plugin) {
    enginePluginManager.addPlugin(plugin);
}

/** */
LuaEnginePlugin *LuaEngine::getPlugin(const string& name) {
    return enginePluginManager.getPlugin(name);
}

/** */
void LuaEngine::deletePlugins() {
    enginePluginManager.deletePlugins();
}

/** */
bool LuaEngine::loadDefaultLibs() {
    Log::info(logTag,"Loading default Lua libraries:");    
    
    // TODO Load only the relevant libs 
    Log::info(logTag,"  - default");
    luaL_openlibs(L);
    
    Log::info(logTag,"  - log");
    ::ae::lua::loadLogLib(L);
    
    Log::info(logTag,"  - Vec3");
    ::ae::math::lua::loadVec3Lib(L);
    
    Log::info(logTag,"  - Mat4");
    ::ae::math::lua::loadMat4Lib(L);

    if (cfg.getImageLoader() != (::ae::image::ImageLoader *)0) {
        Log::info(logTag,"  - Image");
        ::ae::image::lua::loadImageLib(L,cfg.getImageLoader(),imageUtil);
    }
    else {
        Log::warning(logTag," - skipping Image (no image loader set)");
    }
    
    if (cfg.getTextureImageLoader() != (::ae::image::ImageLoader *)0 &&
        cfg.getTextureFactory() != (::ae::texture::TextureFactory *)0) {
    //
        Log::info(logTag,"  - Texture");
        if (::ae::texture::lua::loadTextureLib(L,cfg.getTextureImageLoader(),
            cfg.getTextureFactory(),this) == false) {
        //
            return false;
        }
    }
    else {
        Log::warning(logTag,"  - skipping Texture (no texture image loader or"
            " texture factory set)");
    }
    
    Log::info(logTag,"  - InputStream");
    ::ae::io::lua::loadInputStreamLib(L);
    
    Log::info(logTag,"  - OutputStream");
    ::ae::io::lua::loadOutputStreamLib(L);
    
    // assets
    if (cfg.getAssets() != (Assets *)0) {    
        Log::info(logTag,"  - assets");
        ::ae::io::lua::loadAssetsLib(L,cfg.getAssets());
    }
    else {
        Log::warning(logTag,"  - skipping assets (none set)");
    }
    
    // storage
    if (cfg.getStorage() != (Storage *)0) {
        Log::info(logTag,"  - storage");
        ::ae::io::lua::loadStorageLib(L,cfg.getStorage());
    }
    else {
        Log::warning(logTag,"  - skipping storage (none set)");
    }
    
    // file system
    if (cfg.getFileSystem() != (FileSystem *)0) {
        Log::info(logTag,"  - filesystem");
        ::ae::io::lua::loadFileSystemLib(L,cfg.getFileSystem());
    }
    else {
        Log::warning(logTag,"  - skipping file system (none set)");
    }
    
    // audio
    if (cfg.getAudio() != (ae::audio::Audio *)0) {
        Log::info(logTag,"  - audio");
        ::ae::audio::lua::loadAudioLib(L,cfg.getAudio());
        ::ae::audio::lua::loadSoundLib(L,cfg.getAudio());
        ::ae::audio::lua::loadMusicLib(L,cfg.getAudio());
    }
    else {
        Log::warning(logTag,"  - skipping Sound (no audio set)");
        Log::warning(logTag,"  - skipping Music (no audio set)");
    }
    
    Log::info(logTag,"  - MessagePack");
    ::ae::util::lua::loadMessagePackLib(L);
    
    Log::info(logTag,"  - MessagePackPacker");
    ::ae::util::lua::loadMessagePackPackerLib(L);
    
    Log::info(logTag,"  - MessagePackUnpacker");
    ::ae::util::lua::loadMessagePackUnpackerLib(L);    
    
    Log::info(logTag,"  - ae");
    ::ae::engine::lua::loadAELib(this);
    
    return true;
}

/** */
void LuaEngine::unloadDefaultLibs() {
    Log::info(logTag,"Unloading default Lua libraries:");    
    
// texture
    Log::info(logTag,"  - Texture");
    ::ae::texture::lua::unloadTextureLib(L);
}

/** */
bool LuaEngine::aeInitRun() {
// get the function
    LuaGetField getRunFunc(L,"ae","init","run");
    if (getRunFunc.run() == false) {
        setError(getRunFunc.getError());
        return false;
    }
    
// call the function
    int callResult;
    if (luaPCall.tryCall(L,0,0,callResult) == false) {
        setError(luaPCall.getError());
        return false;
    }
    
    return true;    
}

/** */
bool LuaEngine::start() {
    Log::trace(logTag,"LuaEngine::start()");
    
// create Lua state
    L = luaL_newstate();

// garbage collector stuff
    luaGC.setLuaState(L);   

// initialize plugins
    if (enginePluginManager.initPlugins(logger,L,this) == false) {
        setError(enginePluginManager.getError());
        return false;
    }
    if (enginePluginManager.configureLuaEngine(&cfg) == false) {
        setError(enginePluginManager.getError());
        return false;
    }
    
// default libraries
    if (loadDefaultLibs() == false) {
        return false;
    }
    
// load the root source
    if (loadSource(ROOT_LUA_SRC) == false) {
        return false;
    }
    
// start plugins
    if (enginePluginManager.startPlugins() == false) {
        setError(enginePluginManager.getError());
        return false;
    }
    
// load extra libraries
    if (extraLibManager.loadLibs(this) == false) {
        setError(extraLibManager.getError());
        return false;
    }
    
// run Lua ae.init.run()
    if (aeInitRun() == false) {
        return false;
    }
    
    return true;
}

/** */
bool LuaEngine::stop() {
// in case start() hasn't been yet called
    if (L == (lua_State *)0) {
        Log::trace(logTag,"LuaEngine::stop() called without prior call of " 
            "LuaEngine::start(). Nothing to do.");
        return true;
    }
    Log::trace(logTag,"LuaEngine::stop()");
    
// push error handler to get traceback on error
    luaPCall.pushErrorHandler(L);      
    
// get the function
    LuaGetField getTerminatingFunc(L,"ae","terminating");
    if (getTerminatingFunc.run() == false) {
        setError(getTerminatingFunc.getError());
        return false;
    }
    
// call the function
    int callResult;
    if (luaPCall.tryCall(L,0,0,callResult) == false) {
        setError(luaPCall.getError());
        return false;
    }
    
// unload extra libraries
    if (extraLibManager.unloadLibs() == false) {
        setError(extraLibManager.getError());
        return false;
    }
    
// remove the models
    vector<LuaModel *>::iterator itr;
    for (itr = luaModels.begin(); itr != luaModels.end(); ++itr) {
        engine->removeModel(*itr);
    }
    luaModels.clear();
    
// close Lua
    lua_close(L);
    
// stop Lua engine plugins
    if (enginePluginManager.stopPlugins() == false) {
        setError(enginePluginManager.getError());
        return false;
    }
    
    L = (lua_State *)0;
    return true;
}

/** */
bool LuaEngine::pausing() {
// push error handler to get traceback on error
    luaPCall.pushErrorHandler(L);      
    
// get the function
    LuaGetField getPausingFunc(L,"ae","pausing");
    if (getPausingFunc.run() == false) {
        setError(getPausingFunc.getError());
        return false;
    }
    
// call the function
    int callResult;
    if (luaPCall.tryCall(L,0,0,callResult) == false) {
        setError(luaPCall.getError());
        return false;
    }
    
    return true;
}

/** */
bool LuaEngine::resuming() {
// push error handler to get traceback on error
    luaPCall.pushErrorHandler(L);
    
// get the function
    LuaGetField getResumingFunc(L,"ae","resuming");
    if (getResumingFunc.run() == false) {
        setError(getResumingFunc.getError());
        return false;
    }
    
// call the function
    int callResult;
    if (luaPCall.tryCall(L,0,0,callResult) == false) {
        setError(luaPCall.getError());
        return false;
    }
    
    return true;
}

/** */
void LuaEngine::postRestart() {
    engine->addRequest(restartRequest);
}

/** */
bool LuaEngine::restart() {
    if (restartListener != (LuaEngineRestartListener *)0) {
        restartListener->luaEngineRestarting(this);        
    }
    
    stop();
    if (start() == false) {
        if (restartListener != (LuaEngineRestartListener *)0) {
            restartListener->luaEngineRestartFailed(this);        
        }
        return false;
    }
    if (displayResized(width,height) == false) {
        if (restartListener != (LuaEngineRestartListener *)0) {
            restartListener->luaEngineRestartFailed(this);        
        }
        return false;
    }
    
    if (restartListener != (LuaEngineRestartListener *)0) {
        restartListener->luaEngineRestarted(this);        
    }
    
    return true;
}

#ifdef AE_ANDROID
static unsigned long currentTimeMs()
{
    struct timeval tv;
    gettimeofday(&tv,NULL);
    return ((tv.tv_sec * 1000) + (tv.tv_usec / 1000));
}
#endif

/** */
bool LuaEngine::loadSource(const string &name) {
// log
    ostringstream msg;
    msg << "Loading Lua from " << name;
    Log::debug(logTag,msg.str());
       
#ifdef AE_ANDROID
    unsigned long startTime = currentTimeMs();
#endif
    
// open
    InputStream *stream = cfg.getLuaSrcProvider()->getInputStream(name);
    if (stream == (InputStream *)0) {
        ostringstream msg;
        msg << "Lua " << name << " not found";
        setError(msg.str());
        return false;
    }    
    if (stream->open() == false) {
        ostringstream msg;
        msg << "Failed to open Lua " << name << ": " <<
            stream->getError();
        setError(msg.str());
        delete stream;
        return false;
    }
    
// read
    if (luaLoader->load(L,stream,name) == false) {
        setError(luaLoader->getError());
        stream->close();
        delete stream;
        return false;        
    }
    
// close
    if (stream->close() == false) {
        ostringstream msg;
        msg << "Failed to load Lua from " << name << " (" <<
            stream->getError() << ")";
        setError(msg.str());
        return false;
    }
    delete stream;

#ifdef AE_ANDROID
    unsigned long endTime = currentTimeMs();
    ostringstream timeMsg;
    timeMsg << "Lua source " << name << " loaded in " <<
        (endTime - startTime) << " ms";
    Log::trace(logTag,timeMsg.str());
#endif    
    
    return true;
}    

/** */
bool LuaEngine::displayResized(int width_,int height_) {
    width = width_;
    height = height_;
    
// notify the listener
    if (cfg.getDisplayListener() != (LuaEngineDisplayListener *)0) {
        cfg.getDisplayListener()->luaEngineDisplayResized(width,height);
    }
    
// push error handler to get traceback on error
    luaPCall.pushErrorHandler(L);      
    
// get the function
    LuaGetField getResizeFunc(L,"ae","display","resize");
    if (getResizeFunc.run() == false) {
        setError(getResizeFunc.getError());
        return false;
    }
    
// push arguments
    lua_pushnumber(L,width);
    lua_pushnumber(L,height);
    
// call the function
    int callResult;
    if (luaPCall.tryCall(L,2,0,callResult) == false) {
        setError(luaPCall.getError());
        return false;
    }

    return true;
}

/** */
void LuaEngine::addLuaModel(LuaModel *luaModel) {
    luaModels.push_back(luaModel);
}

/** */
void LuaEngine::removeLuaModel(LuaModel *luaModel) {
    vector<LuaModel *>::iterator itr =
        find(luaModels.begin(),luaModels.end(),luaModel);
    if (itr != luaModels.end()) {
        luaModels.erase(itr);
    }
}

/** */
void LuaEngine::getUsedMemory(double &usedMemory) {
    luaGC.getUsedMemory(usedMemory);
}

/** */
bool LuaEngine::logUsedMemory() {
// get memory
    double usedMemory;
    getUsedMemory(usedMemory);
    
// log
    ostringstream msg;
    msg << "Lua used memory " << (int)usedMemory << " Kbytes";
    Log::info(logTag,msg.str());
    
    return true;
}

} // namespace    
    
} // namespace