#ifndef AE_DESKTOP_ENGINE_CFG_H
#define AE_DESKTOP_ENGINE_CFG_H

#include <string>
#include <vector>
#include <map>

#include "Error.h"

namespace ae {
    
namespace engine {
    
namespace desktop {
  
/**
 * \brief Contains the desktop engine configuration.
 */
class DesktopEngineCfg:public Error {
    /// The input properties.
    std::map<std::string,std::string> props;
    
    /// The extra variable for replacement.
    std::map<std::string,std::string> extraVars;
    
    /// The path to the AE distribution directory.
    std::string aeDistDir;
    
    /// The OS under which the app is running
    std::string os;
    
    /// The window width.
    int winWidth;
    
    /// The window height.
    int winHeight;
    
    /// Indicates if audio is enabled. 
    bool audio;
    
    /// The audio volume.
    int volume;
    
    /// Indicates if to start the Lua profiler. 
    bool luaProfiler;
    
    /// The plugins to load.
    std::vector<std::string> plugins;
    
    /**
     * \brief Checks if a property is given.
     * \param name The property name.
     * \return <code>true</code> if the property is given, <code>false</code>
     *   otherwise.
     */
    bool hasProp(const std::string& name);
    
    /**
     * \brief Gets a property value.
     * \param name The property name.
     * \param value The property value.
     * \return <code>true</code> if the property is given, <code>false</code>
     *   otherwise.     
     */
    bool getPropValue(const std::string& name,std::string &value);
    
    /**
     * \brief Validates the OS property value.
     * \param value The property value.
     * \return <code>true</code> if the value is valid,
     *   <code>false</code> otherwise.
     */
    bool validateOSProp(const std::string &value);
    
    /**
     * \brief Parses the window size.
     * \param winSize The window size.
     * \return <code>true</code> if the window size is valid,
     *   <code>false</code> otherwise.     
     */
    bool parseWinSize(const std::string &winSize);
    
    /**
     * \brief Parses a boolean.
     * \param propValue The property value.
     * \return <code>true</code> if the property value is valid,
     *   <code>false</code> otherwise.
     */
    bool parseBoolean(const std::string &propValue,bool &result);
    
    /**
     * \brief Adds the extra variables to be replaced in properties.
     */
    void addExtraVars();
    
    /**
     * \brief Creates the configuration from properties.
     */
    void create();
    
public:
    /// The log tag.
    static const char *const logTag;
    
    /**
     * \brief Constructs a DesktopEngineCfg.
     * \param props_ The properties from which to get the configuration.
     */
    DesktopEngineCfg(std::map<std::string,std::string> &props_):props(props_),
        extraVars(),aeDistDir(),os(),winWidth(-1),winHeight(-1),audio(false),
        volume(100),luaProfiler(false) {
    //
        create();
    }
    
    /**
     * \brief Gets the AE distribution directory.
     * \return The AE distribution directory.
     */
    const std::string getAEDistDir() const {
        return aeDistDir;
    }
    
    /**
     * \brief Gets the directory with plugins.
     * \return The plugins directory.
     */
    const std::string getPluginsDir();    
    
    /** 
     * \brief Gets the application name.
     * \return The application name.
     */
    const std::string getAppName();
    
    /**
     * \brief Gets the directories with assets.
     * \return The asset directories.
     */
    const std::vector<std::string> getAssetDirs();
    
    /**
     * \brief Gets the directories with Lua sources.
     * \return The Lua source directories.
     */
    const std::vector<std::string> getLuaSrcDirs();
    
    /**
     * \brief Gets the directories with textures.
     * \return The texture directories.
     */
    const std::vector<std::string> getTextureDirs();
    
    /**
     * \brief Gets the OS under which the app is running.
     * \return The OS.
     */
    const std::string getOS() const {
        return os;
    }
    
    /**
     * \brief Checks if the window size is defined.
     * \return <code>true</code> if defined, <code>false</code> otherwise.
     */
    bool hasWinSize() const {
        return winWidth != -1 && winHeight != -1;
    }
    
    /**
     * \brief Gets the window width.
     * \return The window width.
     */
    int getWinWidth() const {
        return winWidth;
    }
    
    /**
     * \brief Gets the window height.
     * \return The window height.
     */
    int getWinHeight() const {
        return winHeight;
    }
    
    /**
     * \brief Indicates if audio is enabled.
     * \return <code>true</code> if enabled, <code>false</code> otherwise.
     */
    bool getAudio() const {
        return audio;
    }
    
    /**
     * \brief Gets the audio volume.
     * \return The audio volume in percent.
     */
    int getVolume() const {
        return volume;
    }
    
    /**
     * \brief Indicates if to run the Lua profiler.
     * \return <code>true</code> if run, <code>false</code> otherwise.
     */
    bool getLuaProfiler() const {
        return luaProfiler;
    }
    
    /**
     * \brief Gets the plugins to load.
     * \return The plugins.
     */
    const std::vector<std::string> getPlugins() const {
        return plugins;
    }
};
    
} // namespace
    
} // namespace
    
} // namespace

#endif // AE_DESKTOP_ENGINE_CFG_H 