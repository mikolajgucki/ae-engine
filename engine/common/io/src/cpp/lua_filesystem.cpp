/*
-- @module filesystem
-- @group IO
-- @brief Allows to read and write files in the file system.
*/
#include "lua_common.h"
#include "luaInputStream.h"
#include "luaOutputStream.h"
#include "lua_filesystem.h"

using namespace ae::lua;

namespace ae {
    
namespace io {

namespace lua {

/// The library name.
static const char *fileSystemName = "filesystem";

/// The name of the Lua global with the file system.
static const char *fileSystemGlobalFileSystem = "ae_filesystem";

/**
 * \brief Gets the file system from the Lua state.
 * \param L The Lua state.
 * \return The file system.
 */
static FileSystem *getFileSystem(lua_State *L) {
    lua_getglobal(L,fileSystemGlobalFileSystem);
    FileSystem *fileSystem = (FileSystem *)lua_touserdata(L,-1);
    lua_pop(L,1);
    return fileSystem;    
}

/*
-- @name .createFile
-- @func
-- @brief Creates a file it does not exist.
-- @param filename The file name.
*/
static int fileSystemCreateFile(lua_State *L) {
    FileSystem *fileSystem = getFileSystem(L);
    const char *filename = luaL_checkstring(L,1);
    
    if (fileSystem->createFile(filename) == false) {
        luaPushError(L,fileSystem->getError().c_str());
        return 0;        
    }
    
    return 0;
}

/*
-- @name .getInputStream
-- @func
-- @brief Gets the input stream reading from a file.
-- @param filename The file name.
-- @return The input stream object (see ()[InputStream]).
*/
static int fileSystemGetInputStream(lua_State *L) {
    FileSystem *fileSystem = getFileSystem(L);
    const char *filename = luaL_checkstring(L,1);
    
    InputStream *input = fileSystem->getInputStream(filename);
    if (input == (InputStream *)0) {
        luaPushError(L,fileSystem->getError().c_str());
        return 0;        
    }
    
    luaPushInputStreamType(L,input);
    return 1;
}

/*
-- @name .getOutputStream
-- @func
-- @brief Gets the output stream writing to a file.
-- @param filename The file name.
-- @return The output stream object (see ()[OutputStream]).
*/
static int fileSystemGetOutputStream(lua_State *L) {
    FileSystem *fileSystem = getFileSystem(L);
    const char *filename = luaL_checkstring(L,1);
    
    OutputStream *output = fileSystem->getOutputStream(filename);
    if (output == (OutputStream *)0) {
        luaPushError(L,fileSystem->getError().c_str());
        return 0;
    }
    
    luaPushOutputStreamType(L,output);
    return 1;
}

/** */
static const struct luaL_Reg fileSystemFuncs[] = {
    {"createFile",fileSystemCreateFile},
    {"getInputStream",fileSystemGetInputStream},
    {"getOutputStream",fileSystemGetOutputStream},
    {0,0}
};

/** */
static int fileSystemRequireFunc(lua_State *L) {
    luaL_newlib(L,fileSystemFuncs);
    return 1;
}

/** */
void loadFileSystemLib(lua_State *L,FileSystem *fileSystem) {
// global with the file system
    lua_pushlightuserdata(L,fileSystem);
    lua_setglobal(L,fileSystemGlobalFileSystem);

// load the library
    luaL_requiref(L,fileSystemName,fileSystemRequireFunc,1);
    lua_pop(L,1);
}

} // namespace
    
} // namespace
    
} // namespace    