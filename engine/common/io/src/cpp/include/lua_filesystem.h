#ifndef AE_IO_LUA_FILE_SYSTEM_H
#define AE_IO_LUA_FILE_SYSTEM_H

#include "lua.hpp"
#include "FileSystem.h"

namespace ae {
    
namespace io {

namespace lua {
    
/**
 * \brief Loads the file system library.
 * \param L The Lua state.
 * \param fileSystem The file system.
 */
void loadFileSystemLib(lua_State *L,FileSystem *fileSystem);
    
} // namespace
    
} // namespace
    
} // namespace

#endif //  AE_IO_LUA_FILE_SYSTEM_H