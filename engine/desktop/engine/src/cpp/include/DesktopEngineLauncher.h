#ifndef AE_DESKTOP_ENGINE_LAUNCHER_H
#define AE_DESKTOP_ENGINE_LAUNCHER_H

#include "Logger.h"
#include "Error.h"
#include "FileInputStreamProvider.h"
#include "FileOutputStreamProvider.h"
#include "FileAssets.h"
#include "FileStorage.h"
#include "FileFileSystem.h"
#include "PNGImageLoader.h"
#include "CachedPNGImageLoader.h"
#include "DesktopSleepFunc.h"
#include "DesktopGetTicksFunc.h"
#include "DummyEngineMutexFactory.h"
#include "DesktopPluginManager.h"
#include "Audio.h"
#include "Engine.h"
#include "LuaEngine.h"
#include "Timer.h"
#include "DesktopEngineCfg.h"

namespace ae {
    
namespace engine {
    
namespace desktop {
  
/**
 * \brief Responsible for launching the desktop engine.
 */
class DesktopEngineLauncher:public Error {
    /// The desktop engine configuration
    DesktopEngineCfg *cfg;
    
    /// The logger.
    Logger *logger;
    
    /// The timer.
    Timer *timer;
    
    /// The Lua source provider.
    ae::io::FileInputStreamProvider *luaSrcProvider;
    
    /// The image input stream provider for the Lua image loader.
    ae::io::FileInputStreamProvider *imageStreamProvider;
    
    /// The PNG image loader for the Lua image loader.
    ae::image::PNGImageLoader *imageLoader;
    
    /// The cached PNG image loader for the Lua image loader.
    ae::image::CachedPNGImageLoader *cachedImageLoader;
    
    /// The image input stream provider for the texture loader.
    ae::io::FileInputStreamProvider *textureImageStreamProvider;
    
    /// The image loader for the texture loader.
    ae::image::PNGImageLoader *textureImageLoader;
    
    /// The cached image loader for the texture loader.
    ae::image::CachedPNGImageLoader *textureCachedImageLoader;    
    
    /// The assets.
    ae::io::FileAssets *assets;
    
    /// The storage.
    ae::io::FileStorage *storage;
    
    /// The file system.
    ae::io::FileFileSystem *fileSystem;
    
    /// The sleep function.
    DesktopSleepFunc *sleepFunc;
    
    /// The get-ticks functions.
    DesktopGetTicksFunc *getTicksFunc;
    
    /// The mutex factory.
    DummyEngineMutexFactory *mutexFactory;
    
    /// The plugin manager.
    DesktopPluginManager *pluginManager;
    
    /// The audio.
    ae::audio::Audio *audio;
    
    /// The Lua engine.
    LuaEngine *luaEngine;
    
    /// The engine.
    Engine *engine;
    
    /**
     * \brief Logs the directories.
     */
    void logDirs();
    
    /**
     * \brief Creates the Lua source provider.
     */
    void createLuaSrcProvider();
    
    /**
     * \brief Creates the plugin manager.
     */
    void createPluginManager();
    
    /**
     * \brief Creates the launcher.
     */
    void create();
    
    /**
     * \brief Destroys the launcher.
     */
    void destroy();
    
    /**
     * \brief Configures the plugins.
     * \return true on success, sets error and returns false otherwise.
     */
    bool configurePlugins();
    
public:
    /**
     * \brief Constructs a DesktopEngineLauncher.
     * \param cfg_ The configuration.
     * \param logger_ The logger.
     */
    DesktopEngineLauncher(DesktopEngineCfg *cfg_,Logger *logger_,Timer *timer_):
        Error(),cfg(cfg_),logger(logger_),timer(timer_),
        luaSrcProvider((::ae::io::FileInputStreamProvider *)0),
        imageStreamProvider((ae::io::FileInputStreamProvider *)0),
        imageLoader((ae::image::PNGImageLoader *)0),
        cachedImageLoader((ae::image::CachedPNGImageLoader *)0),
        textureImageStreamProvider((::ae::io::FileInputStreamProvider *)0),
        textureImageLoader((::ae::image::PNGImageLoader *)0),
        textureCachedImageLoader((::ae::image::CachedPNGImageLoader *)0),        
        assets((ae::io::FileAssets *)0),storage((ae::io::FileStorage *)0),
        fileSystem((ae::io::FileFileSystem *)0),
        sleepFunc((DesktopSleepFunc *)0),getTicksFunc((DesktopGetTicksFunc *)0),
        mutexFactory((DummyEngineMutexFactory *)0),
        pluginManager((DesktopPluginManager *)0),audio((ae::audio::Audio *)0),
        luaEngine((LuaEngine *)0),engine((Engine *)0) {
    //
        create();
    }
    
    /**
     * \brief Destroys the launcher.
     */
    ~DesktopEngineLauncher() {
        destroy();
    }
    
    /**
     * \brief Launches the engine.
     * \return <code"true</code" if the engine has been successfully
     *     launched, otherwise sets error and returns <code"false</code".
     */
    bool launch();
    
    /**
     * \brief Gets the Lua engine.
     * \return The Lua engine.
     */
    LuaEngine *getLuaEngine() const {
        return luaEngine;
    }
    
    /**
     * \brief Gets the engine.
     * \return The engine.
     */
    Engine *getEngine() const {
        return engine;
    }
    
    /**
     * \brief Gets the cached image loader.
     * \return The cached image loader.
     */
    ae::image::CachedPNGImageLoader *getCachedImageLoader() const {
        return cachedImageLoader;
    }    
    
    /**
     * \brief Gets the texture cached image loader.
     * \return The texture cached image loader.
     */
    ae::image::CachedPNGImageLoader *getTextureCachedImageLoader() const {
        return textureCachedImageLoader;
    }    
    
    /**
     * \brief Gets the file system.
     * \return The file system.
     */
    ae::io::FileSystem *getFileSystem() const {
        return fileSystem;
    }
    
    /**
     * \brief Gets the audio.
     * \return The audio.
     */
    ae::audio::Audio *getAudio() const {
        return audio;
    }
};
    
} // namespace

} // namespace

} // namespace

#endif // AE_DESKTOP_ENGINE_LAUNCHER_H