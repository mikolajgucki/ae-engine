/*
-- @module OutputStream
-- @group IO
-- @brief Represents a binary output stream.
*/
#include "lua_common.h"
#include "luaOutputStream.h"

using namespace ae::lua;

namespace ae {
    
namespace io {
    
namespace lua {
 
/// The name of the Lua user type.
static const char *outputStreamName = "OutputStream";
    
/// The name of the Lua metatable of the output stream user type.
static const char *outputStreamMetatableName = "OutputStream.metatable";
    
/** */
void luaPushOutputStreamType(lua_State *L,OutputStream *output) {
// user data
    OutputStreamType *data =
        (OutputStreamType *)lua_newuserdata(L,sizeof(OutputStreamType));
    data->output = output;
    
// metatable
    luaL_getmetatable(L,outputStreamMetatableName);
    lua_setmetatable(L,-2);
}

/** */
OutputStreamType *checkOutputStreamType(lua_State *L,int index) {
    void *data = luaL_checkudata(L,index,outputStreamMetatableName);
    luaL_argcheck(L,data != (void *)0,1,"OutputStream expected");
    return (OutputStreamType *)data;
}

/*
-- @name :open
-- @func
-- @brief Opens the output stream.
*/
static int outputStreamOpen(lua_State *L) {
    OutputStreamType *data = checkOutputStreamType(L);
    
    if (data->output->open() == false) {
        luaPushError(L,data->output->getError().c_str());
        return 0;
    }
    
    return 0;
}

/*
-- @name :close
-- @func
-- @brief Closes the output stream.
*/
static int outputStreamClose(lua_State *L) {
    OutputStreamType *data = checkOutputStreamType(L);
    
    if (data->output->close() == false) {
        luaPushError(L,data->output->getError().c_str());
        return 0;
    }
    
    return 0;
}

/*
-- @name :__gc
-- @func
-- @brief Closes the output stream if open.
-- @full Closes the output stream if open. Never call this function directly!
*/
static int outputStream__gc(lua_State *L) {
    OutputStreamType *data = checkOutputStreamType(L);
    
    if (data->output->close() == false) {
        luaPushError(L,data->output->getError().c_str());
        return 0;
    }
    
    return 0;
}

// TODO Write __tostring metamethod.

/** The type functions. */
static const struct luaL_Reg outputStreamFuncs[] = {
    {0,0}
};


/** The type methods. */
static const struct luaL_Reg outputStreamMethods[] = {
    {"open",outputStreamOpen},
    {"close",outputStreamClose},
    {0,0}
};

/** The type metamethods. */
static const struct luaL_Reg outputStreamMetamethods[] = {
    {"__gc",outputStream__gc},
    {0,0}
};

/** */
static int outputStreamRequireFunc(lua_State *L) {
    luaLoadUserType(L,outputStreamMetatableName,
        outputStreamFuncs,outputStreamMethods,outputStreamMetamethods);
    return 1;
}

/** */
void loadOutputStreamLib(lua_State *L) {
    luaL_requiref(L,outputStreamName,outputStreamRequireFunc,1);
    lua_pop(L,1);
}
    
} // namespace
    
} // namespace
    
} // namepsace