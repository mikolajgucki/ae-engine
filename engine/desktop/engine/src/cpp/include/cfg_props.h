#ifndef AE_DESKTOP_ENGINE_CFG_PROPS_H
#define AE_DESKTOP_ENGINE_CFG_PROPS_H

#include <map>
#include <string>

namespace ae {
    
namespace engine {
    
namespace desktop {
    
/**
 * \brief Replaces variables in a string with envrionment and extra variables.
 * \param str The string in which to replace.
 * \param extraVar The extra variables.
 */
void replaceEnvVars(std::string &str,
    std::map<std::string,std::string> &extraVars);

} // namespace
    
} // namespave
    
} // namespace

#endif // AE_DESKTOP_ENGINE_CFG_PROPS_H