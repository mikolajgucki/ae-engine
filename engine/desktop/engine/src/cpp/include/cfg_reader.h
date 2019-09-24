#ifndef AE_DESKTOP_ENGINE_CFG_READER_H
#define AE_DESKTOP_ENGINE_CFG_READER_H

#include <map>
#include <string>
#include "DesktopEngineCfg.h"

namespace ae {
    
namespace engine {
    
namespace desktop {
    
/**
 * \brief Reads and parses the configuration.
 * \param extraProps The additional properties which override the properties
 *   read from the file.
 * \return The read configuration.
 */
DesktopEngineCfg *readCfg(std::map<std::string,std::string> extraProps);
    
} // namespace
    
} // namespave
    
} // namespace

#endif // AE_DESKTOP_ENGINE_CFG_READER_H