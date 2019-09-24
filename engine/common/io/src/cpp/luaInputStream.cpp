/*
-- @module InputStream
-- @group IO
-- @brief Represents a binary input stream.
*/
#include <string>

#include "lua_common.h"
#include "luaInputStream.h"

using namespace std;
using namespace ae::lua;

namespace ae {
    
namespace io {
    
namespace lua {
 
/// The name of the Lua user type.
static const char *inputStreamName = "InputStream";
    
/// The name of the Lua metatable of the input stream user type.
static const char *inputStreamMetatableName = "InputStream.metatable";

/// The initial capacity of a string in the read all function.
static const int readAllInitCapacity = 1024;

/// The size of the buffer while reading everyting from an asset.
static const int readAllBufferSize = 1024;
    
/** */
void luaPushInputStreamType(lua_State *L,InputStream *input) {
// user type
    InputStreamType *data = 
        (InputStreamType *)lua_newuserdata(L,sizeof(InputStreamType));
    data->input = input;
    
// metatable
    luaL_getmetatable(L,inputStreamMetatableName);
    lua_setmetatable(L,-2);
}

/** */
InputStreamType *checkInputStreamType(lua_State *L,int index) {
    void *data = luaL_checkudata(L,index,inputStreamMetatableName);
    luaL_argcheck(L,data != (void *)0,1,"InputStream expected");
    return (InputStreamType *)data;
}

/*
-- @name :open
-- @func
-- @brief Opens the input stream.
*/
static int inputStreamOpen(lua_State *L) {
    InputStreamType *data = checkInputStreamType(L);
    
    if (data->input->open() == false) {
        luaPushError(L,data->input->getError().c_str());
        return 0;        
    }
    
    return 0;
}

/*
-- @name :readAll
-- @func
-- @brief Reads the file contents into a string.
*/
static int inputStreamReadAll(lua_State *L) {
    InputStreamType *data = checkInputStreamType(L);
    string str;
    str.reserve(readAllInitCapacity);
    char buffer[readAllBufferSize];
    
    InputStream *input = data->input;
    while (true) {        
    // read buffer
        int readSize = input->read(buffer,(size_t)readAllBufferSize);
        if (readSize == InputStream::ERROR) {
            luaPushError(L,input->getError().c_str());
            return 0;              
        }
        
    // append
        for (int index = 0; index < readSize; index++) {
            str.push_back(buffer[index]);
        }
        
        if (readSize < readAllBufferSize) {
            break;
        }
    }
    
    lua_pushstring(L,str.c_str());
    return 1;
}

/*
-- @name :close
-- @func
-- @brief Closes the input stream.
*/
static int inputStreamClose(lua_State *L) {
    InputStreamType *data = checkInputStreamType(L);
    
    if (data->input->close() == false) {
        luaPushError(L,data->input->getError().c_str());
        return 0;        
    }
    
    return 0;
}

/*
-- @name :__gc
-- @func
-- @brief Closes the input stream if open.
-- @full Closes the input stream if open. Never call this function directly!
*/
static int inputStream__gc(lua_State *L) {
    InputStreamType *data = checkInputStreamType(L);
    
    if (data->input->close() == false) {
        luaPushError(L,data->input->getError().c_str());
        return 0;        
    }
    
    return 0;
}

// TODO Write __tostring metamethod.

/** The type functions. */
static const struct luaL_Reg inputStreamFuncs[] = {
    {0,0}
};


/** The type methods. */
static const struct luaL_Reg inputStreamMethods[] = {
    {"open",inputStreamOpen},
    {"readAll",inputStreamReadAll},
    {"close",inputStreamClose},
    {0,0}
};

/** The type metamethods. */
static const struct luaL_Reg inputStreamMetamethods[] = {
    {"__gc",inputStream__gc},
    {0,0}
};

/** */
static int inputStreamRequireFunc(lua_State *L) {
    luaLoadUserType(L,inputStreamMetatableName,
        inputStreamFuncs,inputStreamMethods,inputStreamMetamethods);
    return 1;
}

/** */
void loadInputStreamLib(lua_State *L) {
    luaL_requiref(L,inputStreamName,inputStreamRequireFunc,1);
    lua_pop(L,1);
}
    
} // namespace
    
} // namespace
    
} // namepsace