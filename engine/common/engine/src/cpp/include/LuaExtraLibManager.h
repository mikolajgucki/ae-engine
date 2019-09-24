#ifndef AE_ENGINE_LUA_EXTRA_LIB_MANAGER_H
#define AE_ENGINE_LUA_EXTRA_LIB_MANAGER_H

#include <vector>
#include "lua.hpp"
#include "Error.h"
#include "LuaExtraLib.h"

namespace ae {
    
namespace engine {

/// Forward declaration.
class LuaEngine;

/**
 * \brief Manager Lua extra libraries.
 */
class LuaExtraLibManager:public Error {
    /// The libraries.
    ::std::vector<LuaExtraLib *> libs;
    
public:    
    /** */
    LuaExtraLibManager():Error() {
    }
    
    /**
     * \brief Adds a library.
     * \param lib The library.
     */
    void addLib(LuaExtraLib *lib);
    
    /**    
     * \brief Loads the extra libraries.
     * \param luaEngine The Lua engine.
     * \return <code>true</code> on success, otherwise sets error and returns
     *   <code>false</code>.     
     */
    bool loadLibs(LuaEngine *luaEngine);
    
    /**
     * \brief Unloads the extra libraries.
     * \return <code>true</code> on success, otherwise sets error and returns
     *   <code>false</code>.     
     */
    bool unloadLibs();
    
    /**
     * \brief Deletes the libraries. They cannot be deleted in the destructor
     *   due to DLL memory handling. If a library is created inside DLL, it has
     *   to be deleted there. Therefore, we cannot delete the libraries when the
     *   manager is deleted.
     */
    void deleteLibs();
};
    
} // namespace
    
} // namespace

#endif // AE_ENGINE_LUA_EXTRA_LIB_MANAGER_H