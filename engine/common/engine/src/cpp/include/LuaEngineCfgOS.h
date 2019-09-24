#ifndef AE_ENGINE_LUA_ENGINE_CFG_OS_H
#define AE_ENGINE_LUA_ENGINE_CFG_OS_H

namespace ae {

namespace engine {
  
/**
 * \brief Defines an operating system under which the engine can run.
 */
class LuaEngineCfgOS {
    /** The OS name. */
    const char *name;
    
    /**
     * \brief Constructs a LuaEngineCfgOS.
     * \param name_ The OS name.     
     */
    LuaEngineCfgOS(const char *name_):name(name_) {
    }
    
public:
    /**
     * \brief Gets the OS name.
     * \return The OS name.
     */
    const char *getName() const {
        return name;
    }
    
    /** Android. */
    static const LuaEngineCfgOS OS_ANDROID; 
    
    /** iOS. */
    static const LuaEngineCfgOS OS_IOS; 
    
    /**
     * \brief Gets OS by name.
     * \param name The OS name.
     * \return The OS of the name or null if there is no such OS.
     */
    static const LuaEngineCfgOS *getOSByName(const char *name);
};
    
} // namespace
    
} // namespace

#endif // AE_ENGINE_LUA_ENGINE_CFG_OS_H