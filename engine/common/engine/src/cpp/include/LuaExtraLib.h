#ifndef AE_ENGINE_LUA_EXTRA_LIB_H
#define AE_ENGINE_LUA_EXTRA_LIB_H

#include "DLog.h"
#include "Error.h"

namespace ae {
    
namespace engine {

/// Forward declaration.
class LuaEngine;
    
/**
 * \brief Represents an extra library loaded on demand.
 */
class LuaExtraLib:public Error {
    /// The log.
    DLog *log;
    
public:
    /** */
    LuaExtraLib():Error() {
    }
    
    /** */
    virtual ~LuaExtraLib() {
    }
    
    /**
     * \brief Sets the log.
     * \param log_ The log.
     */
    void setLog(DLog *log_) {
        log = log_;
    }
    
    /**
     * \brief Gets the log.
     * \return The log.
     */
    DLog *getLog() {
        return log;
    }
    
    /**
     * \brief Gets the library name.
     * \return The library name.
     */
    virtual const char *getName() = 0;
    
    /**
     * \brief Loads the extra library.
     * \param luaEngine The Lua engine.
     * \return <code>true</code> on success, otherwise sets error and returns
     *   <code>false</code>. 
     */
    virtual bool loadExtraLib(LuaEngine *luaEngine) = 0;
    
    /**
     * \brief Unloads the extra library.
     * \return <code>true</code> on success, otherwise sets error and returns
     *   <code>false</code>. 
     */
    virtual bool unloadExtraLib() = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_ENGINE_LUA_EXTRA_LIB_H