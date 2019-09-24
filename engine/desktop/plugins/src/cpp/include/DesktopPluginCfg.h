#ifndef AE_DESKTOP_PLUGIN_CFG_H
#define AE_DESKTOP_PLUGIN_CFG_H

#include <string>
#include "json/json.h"
#include "Error.h"

namespace ae {

namespace engine {

namespace desktop {
  
/**
 * \brief Wrapper for desktop plugin configuration.
 */
class DesktopPluginCfg:public Error {
    /// The plugin name.
    const std::string pluginName;
    
public:
    /** */
    DesktopPluginCfg(const std::string &pluginName_):Error(),
        pluginName(pluginName_) {
    }
    
    /** */
    virtual ~DesktopPluginCfg() {
    }
    
    /**
     * \brief Reads the JSON configuration.
     * \param value The result value.
     * \return true on success, sets error and returns false on error.
     */
    bool readJSONCfg(Json::Value &value);
};
    
} // namespace

} // namespace

} // namespace

#endif // AE_DESKTOP_PLUGIN_CFG_H