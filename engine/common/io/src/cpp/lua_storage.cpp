/*
-- @module storage
-- @group IO
-- @brief Allows to read and write files in a storage permanent between
--   application launches.
*/
#include "lua_common.h"
#include "luaInputStream.h"
#include "luaOutputStream.h"
#include "lua_storage.h"

using namespace ae::lua;

namespace ae {
    
namespace io {

namespace lua {
    
/// The library name.
static const char *storageName = "storage";

/// The name of the Lua global with the storage.
static const char *storageGlobalStorage = "ae_storage";
    
/**
 * \brief Gets the storage from the Lua state.
 * \param L The Lua state.
 * \return The storage.
 */
static Storage *getStorage(lua_State *L) {
    lua_getglobal(L,storageGlobalStorage);
    Storage *storage = (Storage *)lua_touserdata(L,-1);
    lua_pop(L,1);
    return storage;    
}

/*
-- @name .createFile
-- @func
-- @brief Creates a file it does not exist.
-- @param filename The file name.
*/
static int storageCreateFile(lua_State *L) {
    Storage *storage = getStorage(L);
    const char *filename = luaL_checkstring(L,1);
    
    if (storage->createFile(filename) == false) {
        luaPushError(L,storage->getError().c_str());
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
static int storageGetInputStream(lua_State *L) {
    Storage *storage = getStorage(L);
    const char *filename = luaL_checkstring(L,1);
    
    InputStream *input = storage->getInputStream(filename);
    if (input == (InputStream *)0) {
        luaPushError(L,storage->getError().c_str());
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
static int storageGetOutputStream(lua_State *L) {
    Storage *storage = getStorage(L);
    const char *filename = luaL_checkstring(L,1);
    
    OutputStream *output = storage->getOutputStream(filename);
    if (output == (OutputStream *)0) {
        luaPushError(L,storage->getError().c_str());
        return 0;
    }
    
    luaPushOutputStreamType(L,output);
    return 1;
}

/** */
static const struct luaL_Reg storageFuncs[] = {
    {"createFile",storageCreateFile},
    {"getInputStream",storageGetInputStream},
    {"getOutputStream",storageGetOutputStream},
    {0,0}
};

/** */
static int storageRequireFunc(lua_State *L) {
    luaL_newlib(L,storageFuncs);
    return 1;
}

/** */
void loadStorageLib(lua_State *L,Storage *storage) {
// global with the storage
    lua_pushlightuserdata(L,storage);
    lua_setglobal(L,storageGlobalStorage);
    
// load the library
    luaL_requiref(L,storageName,storageRequireFunc,1);
    lua_pop(L,1);
}
    
} // namespace
    
} // namespace
    
} // namespace