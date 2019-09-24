#ifndef AE_ENGINE_LUA_ENGINE_CFG_H
#define AE_ENGINE_LUA_ENGINE_CFG_H

#include "LuaEngineCfgOS.h"
#include "InputStreamProvider.h"
#include "ImageLoader.h"
#include "TextureFactory.h"
#include "Assets.h"
#include "Storage.h"
#include "FileSystem.h"
#include "Audio.h"
#include "LuaEngineDisplayListener.h"

namespace ae {
    
namespace engine {
  
/**
 * \brief Contains Lua engine configuration.
 */
class LuaEngineCfg {
    /// The OS under which the engine is running.
    const LuaEngineCfgOS *os;
    
    /// Provides Lua source files.
    ::ae::io::InputStreamProvider *luaSrcProvider;
    
    /// The image loader
    ::ae::image::ImageLoader *imageLoader;
    
    /// The texture image loader
    ::ae::image::ImageLoader *textureImageLoader;
    
    /// The texture factory.
    ::ae::texture::TextureFactory *textureFactory;
    
    /// The assets.
    ::ae::io::Assets *assets;
    
    /// The stroage.
    ::ae::io::Storage *storage;
    
    /// The file system.
    ::ae::io::FileSystem *fileSystem;
    
    /// The audio.
    ::ae::audio::Audio *audio;
    
    /// The display listener.
    LuaEngineDisplayListener *displayListener;    
    
public:
    /**
     * \brief Constructs a LuaEngineCfg with empty values.
     */
    LuaEngineCfg():os((const LuaEngineCfgOS *)0),
        luaSrcProvider((::ae::io::InputStreamProvider *)0),
        imageLoader((::ae::image::ImageLoader *)0),
        textureImageLoader((::ae::image::ImageLoader *)0),
        textureFactory((::ae::texture::TextureFactory *)0),
        assets((::ae::io::Assets *)0),storage((::ae::io::Storage *)0),
        fileSystem((::ae::io::FileSystem *)0),audio((::ae::audio::Audio *)0),
        displayListener((LuaEngineDisplayListener *)0) {
    }
    
    /**
     * \brief Sets the operating system under which the engine is running.
     * \param os_ The operating system.
     */
    void setOS(const LuaEngineCfgOS *os_) {
        os = os_;        
    }
    
    /**
     * \brief Gets the operating system under which the engine is running.
     * \return The operating system.
     */
    const LuaEngineCfgOS *getOS() {
        return os;
    }        
    
    /**
     * \brief Sets the Lua source provider.
     * \param luaSrcProvider_ The Lua source provider.
     */
    void setLuaSrcProvider(::ae::io::InputStreamProvider *luaSrcProvider_) {
        luaSrcProvider = luaSrcProvider_;
    }
    
    /**
     * \brief Gets the Lua source provider.
     * \return The Lua source provider.
     */
    ::ae::io::InputStreamProvider *getLuaSrcProvider() {
        return luaSrcProvider;
    }
    
    /**
     * \brief Sets the image loader.
     * \param imageLoader_ The image loader.
     */
    void setImageLoader(::ae::image::ImageLoader *imageLoader_) {
        imageLoader = imageLoader_;
    }
    
    /**
     * \brief Gets the image loader.
     * \return The image loader.
     */
    ::ae::image::ImageLoader *getImageLoader() {
        return imageLoader;
    }
    
    /**
     * \brief Sets the texture image loader.
     * \param textureImageLoader_ The image loader.
     */
    void setTextureImageLoader(::ae::image::ImageLoader *textureImageLoader_) {
        textureImageLoader = textureImageLoader_;
    }
    
    /**
     * \brief Gets the texture image loader.
     * \return The texture image loader.
     */
    ::ae::image::ImageLoader *getTextureImageLoader() {
        return textureImageLoader;
    }
    
    /**
     * \brief Sets the texture factory.
     * \param textureFactory_ The texture factory.
     */
    void setTextureFactory(::ae::texture::TextureFactory *textureFactory_) {
        textureFactory = textureFactory_;
    }
    
    /**
     * \brief Gets the texture factory.
     * \return The texture factory.
     */
    ::ae::texture::TextureFactory *getTextureFactory() {
        return textureFactory;
    }
    
    /**
     * \brief Sets the assets.
     * \param assets_ The assets.
     */
    void setAssets(::ae::io::Assets *assets_) {
        assets = assets_;
    }
    
    /**
     * \brief Gets the assets.
     * \return The assets.
     */
    ::ae::io::Assets *getAssets() {
        return assets;
    }
    
    /**
     * \brief Sets the storage.
     * \param storage_ The storage.
     */
    void setStorage(::ae::io::Storage *storage_) {
        storage = storage_;
    }
    
    /**
     * \brief Gets the storage.
     * \return The storage.    
     */
    ::ae::io::Storage *getStorage() {
        return storage;
    }
    
    /**
     * \brief Sets the file system.
     * \param fileSystem_ The file system.
     */
    void setFileSystem(::ae::io::FileSystem *fileSystem_) {
        fileSystem = fileSystem_;
    }
    
    /**
     * \brief Gets the file system.
     * \return The file system.
     */
    ::ae::io::FileSystem *getFileSystem() {
        return fileSystem;
    }
    
    /**
     * \brief Sets the audio.
     * \param audio_ The audio.
     */
    void setAudio(::ae::audio::Audio *audio_) {
        audio = audio_;
    }
    
    /**
     * \brief Gets the audio.
     * \return The audio.
     */
    ::ae::audio::Audio *getAudio() {
        return audio;
    }
    
    /**
     * \brief Sets the display listener.
     * \param listener The display listener.
     */
    void setLuaEngineDisplayListener(LuaEngineDisplayListener *listener) {
        displayListener = listener;
    }
    
    /**
     * \brief Gets the display listener.
     * \return The display listener.
     */
    LuaEngineDisplayListener *getDisplayListener() {
        return displayListener;
    }    
};
    
} // namespace
    
} // namespace

#endif // AE_ENGINE_LUA_ENGINE_CFG_H