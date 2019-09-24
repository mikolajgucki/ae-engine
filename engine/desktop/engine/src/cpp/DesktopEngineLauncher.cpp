#include <vector>

#include "Log.h"
#include "PNGImageLoader.h"
#include "ImageTextureLoader.h"
#include "DummyAudio.h"
#include "SDLAudio.h"
#include "DesktopEngineLauncher.h"

using namespace std;
using namespace ae;
using namespace ae::lua;
using namespace ae::io;
using namespace ae::image;
using namespace ae::texture;
using namespace ae::audio;

namespace ae {
    
namespace engine {
    
namespace desktop {

/// The log tag.
static const char *const logTag = "desktop.engine"; 

/** */
void DesktopEngineLauncher::create() {
// image loader
    imageStreamProvider = new FileInputStreamProvider(cfg->getAssetDirs());
    imageLoader = new PNGImageLoader(imageStreamProvider);
    cachedImageLoader = new CachedPNGImageLoader(
        imageLoader,imageStreamProvider);
    
// texture loader
    textureImageStreamProvider = new FileInputStreamProvider(
        cfg->getTextureDirs());
    textureImageLoader = new PNGImageLoader(textureImageStreamProvider);
    textureCachedImageLoader = new CachedPNGImageLoader(
        textureImageLoader,textureImageStreamProvider);    
    
// others
    assets = new FileAssets(cfg->getAssetDirs());
    storage = new FileStorage("simulator/storage");
    fileSystem = new FileFileSystem("simulator/filesystem");
    sleepFunc = new DesktopSleepFunc();
    getTicksFunc = new DesktopGetTicksFunc(); 
    mutexFactory = new DummyEngineMutexFactory();
    
// Lua sources
    createLuaSrcProvider();
}

/** */
void DesktopEngineLauncher::destroy() {
    if (pluginManager != (DesktopPluginManager *)0) {
        delete pluginManager;
    }
    if (luaEngine != (LuaEngine *)0) {
        delete luaEngine;
    }
    if (engine != (Engine *)0) {
        delete engine;
    }
    if (audio != (Audio *)0) {
        delete audio;
    }
    if (mutexFactory != (EngineMutexFactory *)0) {
        delete mutexFactory;
    }
    if (getTicksFunc != (GetTicksFunc *)0) {
        delete getTicksFunc;
    }
    if (sleepFunc != (SleepFunc *)0) {
        delete sleepFunc;
    }
    if (fileSystem != (FileSystem *)0) {
        delete fileSystem;
    }
    if (storage != (Storage *)0) {
        delete storage;
    }
    if (assets != (Assets *)0) {
        delete assets;
    }
    if (textureCachedImageLoader != (ImageLoader *)0) {
        delete textureCachedImageLoader;
    }
    if (textureImageLoader != (ImageLoader *)0) {
        delete textureImageLoader;
    }
    if (textureImageStreamProvider != (InputStreamProvider *)0) {
        delete textureImageStreamProvider;
    }    
    if (cachedImageLoader != (ImageLoader *)0) {
        delete cachedImageLoader;
    }
    if (imageLoader != (ImageLoader *)0) {
        delete imageLoader;
    }
    if (imageStreamProvider != (InputStreamProvider *)0) {
        delete imageStreamProvider;
    }
    if (luaSrcProvider != (InputStreamProvider *)0) {
        delete luaSrcProvider;
    }
}

/** */
void DesktopEngineLauncher::logDirs() {
// log plugins directory
    ostringstream pluginsDir;
    pluginsDir << "Plugins directory is " << cfg->getPluginsDir();
    Log::debug(logTag,pluginsDir.str());
        
// Lua source directories
    vector<string> luaSrcDirs = cfg->getLuaSrcDirs();    
    Log::debug(logTag,"Lua source directories:");
    vector<string>::iterator luaSrcDirsItr = luaSrcDirs.begin();
    for (; luaSrcDirsItr != luaSrcDirs.end(); ++luaSrcDirsItr) {
        ostringstream msg;
        msg << "  " << *luaSrcDirsItr;
        Log::debug(logTag,msg.str());
    }
    
// texture directories
    vector<string> textureDirs = cfg->getTextureDirs();
    Log::debug(logTag,"Texture directories:");
    vector<string>::iterator textureDirsItr = textureDirs.begin();
    for (; textureDirsItr != textureDirs.end(); ++textureDirsItr) {
        ostringstream msg;
        msg << "  " << *textureDirsItr;
        Log::debug(logTag,msg.str());
    }
    
// asset directories
    vector<string> assetDirs = cfg->getAssetDirs();
    Log::debug(logTag,"Asset directories:");
    vector<string>::iterator assetDirsItr = assetDirs.begin();
    for (; assetDirsItr != assetDirs.end(); ++assetDirsItr) {
        ostringstream msg;
        msg << "  " << *assetDirsItr;
        Log::debug(logTag,msg.str());
    }
}

/** */
void DesktopEngineLauncher::createLuaSrcProvider() {
    luaSrcProvider = new FileInputStreamProvider(cfg->getLuaSrcDirs());
}

/** */
void DesktopEngineLauncher::createPluginManager() {
// plugins directories
    vector<string> dirs;
    dirs.push_back(cfg->getPluginsDir());
    
// create manager
    pluginManager = new DesktopPluginManager(dirs,logger,timer,luaEngine);
}

/** */
bool DesktopEngineLauncher::configurePlugins() {
    vector<string> plugins = cfg->getPlugins();
    if (plugins.empty() == true) {
        Log::debug(logTag,"No plugins to configure");
        return true;
    }
    
    Log::debug(logTag,"Initializing plugins:");
    vector<string>::iterator itr;
// for each plugin
    for (itr = plugins.begin(); itr != plugins.end(); ++itr) {
    // log
        ostringstream msg;
        msg << "  " << *itr;
        Log::trace(logTag,msg.str());
        
    // start
        if (pluginManager->initPlugin(*itr) == false) {
            setError(pluginManager->getError());
            return false;
        }
    }
    
    return true;
}

/** */
bool DesktopEngineLauncher::launch() {
    logDirs();
    
// engine configuration
    EngineCfg engineCfg(mutexFactory);
    engineCfg.setSleepFunc(sleepFunc);
    engineCfg.setGetTicksFunc(getTicksFunc);
    
// engine
    engine = new Engine(engineCfg);
    engine->start();    
    
// Lua engine configuration
    LuaEngineCfg luaEngineCfg;
    luaEngineCfg.setOS(LuaEngineCfgOS::getOSByName(cfg->getOS().c_str()));
    luaEngineCfg.setLuaSrcProvider(luaSrcProvider);
    luaEngineCfg.setImageLoader(cachedImageLoader);
    luaEngineCfg.setTextureImageLoader(textureCachedImageLoader);
    luaEngineCfg.setAssets(assets);
    luaEngineCfg.setStorage(storage);
    luaEngineCfg.setFileSystem(fileSystem);
    
// audio
    if (cfg->getAudio() == true) {
        SDLAudio *sdlAudio = new SDLAudio("assets/sounds","assets/music");
        if (sdlAudio->init() == false) {
            setError(sdlAudio->getError());
            return false;
        }
        audio = sdlAudio;
    }
    else {
        audio = new DummyAudio();
    }    
    luaEngineCfg.setAudio(audio);    

// Lua engine
    luaEngine = new LuaEngine(logger,luaEngineCfg,engine);    
    
// initialize plugins
    createPluginManager();
    if (configurePlugins() == false) {
        return false;
    }

// start Lua engine
    if (luaEngine->start() == false) {
        setError(luaEngine->getError());
        return false;
    }
    
    return true;
}
    
} // namespace

} // namespace

} // namespace