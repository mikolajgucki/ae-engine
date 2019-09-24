#ifndef AE_LUA_GLES2_H
#define AE_LUA_GLES2_H

#include "lua.hpp"
#include "DLog.h"

namespace ae {
    
namespace gles2 {
    
namespace lua {
    
/**
 * \brief Loads the GLES2 library.
 * \param L The Lua state.
 * \param log_ The log.
 */
void loadGLES2Lib(lua_State *L,DLog *log_);
    
} // namespace
    
} // namespace

} // namespace

#endif // AE_LUA_GLES2_H