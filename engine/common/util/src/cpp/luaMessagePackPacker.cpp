/*
-- @module MsgPackPacker
-- @group Utility
-- @brief Provides functions to pack a message pack.
-- @full Provides function to unpack a message pack. This module is a C library.
*/
#include <memory>

#include "lua_common.h"
#include "luaOutputStream.h"
#include "MessagePackPacker.h"
#include "luaMessagePackPacker.h"

using namespace std;
using namespace ae::lua;
using namespace ae::io::lua;

namespace ae {
    
namespace util {
    
namespace lua {

/**
 * \brief Wraps the message pack packer so that it can be used as Lua user type.
 */
struct MessagePackPackerType {
    /** */
    MessagePackPacker *packer;
};
typedef struct MessagePackPackerType MessagePackPackerType;
    
/// The name of the Lua user type.
static const char *messagePackPackerName = "MsgPackPacker";
    
/// The name of the Lua metatable of the message pack packer user type.
static const char *messagePackPackerMetatableName = "MsgPackPacker.metatable";
 
/** */
static MessagePackPackerType *checkMessagePackPackerType(lua_State *L) {
    void *data = luaL_checkudata(L,1,messagePackPackerMetatableName);
    luaL_argcheck(L,data != (void *)0,1,"MsgPackPacker expected");
    return (MessagePackPackerType *)data;    
}
    
/*
-- @name .new
-- @func
-- @brief Creates a message pack packer.
-- @return The new message pack packer object.
*/
static int messagePackPackerNew(lua_State *L) {    
// packer
    MessagePackPacker *packer = new (nothrow) MessagePackPacker();
    if (packer == (MessagePackPacker *)0) {
        luaPushNoMemoryError(L);
        return 0;
    }
    
// user data
    MessagePackPackerType *data = (MessagePackPackerType *)lua_newuserdata(
        L,sizeof(MessagePackPackerType));
    data->packer = packer;
    
// metatable
    luaL_getmetatable(L,messagePackPackerMetatableName);
    lua_setmetatable(L,-2);
    
    return 1;
}
 
/*
-- @name :writeBool
-- @func
-- @brief Writes a bool value.
-- @param outputStream The stream to which to write.
-- @param value The bool value.
*/
static int messagePackPackerWriteBool(lua_State *L) {
    MessagePackPackerType *packerData = checkMessagePackPackerType(L);
    OutputStreamType *outputData = checkOutputStreamType(L,2);    
    
    int argValue = lua_toboolean(L,3);    
    bool boolValue = argValue != 0 ? true : false;
    
    if (packerData->packer->writeBool(outputData->output,boolValue) == false) {
        luaPushError(L,packerData->packer->getError().c_str());
        return 0;
    }    
    
    return 0;
}

/*
-- @name :writeNil
-- @func
-- @brief Writes the nil.
-- @param outputStream The stream to which to write.
*/
static int messagePackPackerWriteNil(lua_State *L) {
    MessagePackPackerType *packerData = checkMessagePackPackerType(L);
    OutputStreamType *outputData = checkOutputStreamType(L,2);
    
    if (packerData->packer->writeNil(outputData->output) == false) {
        luaPushError(L,packerData->packer->getError().c_str());
        return 0;
    }
    
    return 0;
}

/*
-- @name :writeInt16
-- @func
-- @brief Writes a 16-bit signed integer.
-- @param outputStream The stream to which to write.
-- @param value The integer value.
*/
static int messagePackPackerWriteInt16(lua_State *L) {
    MessagePackPackerType *packerData = checkMessagePackPackerType(L);
    OutputStreamType *outputData = checkOutputStreamType(L,2);    
    signed int value = (signed int)luaL_checknumber(L,3);
    
    if (packerData->packer->writeInt16(outputData->output,value) == false) {
        luaPushError(L,packerData->packer->getError().c_str());
        return 0;
    }
    
    return 0;
}

/*
-- @name :writeInt32
-- @func
-- @brief Writes a 32-bit signed integer.
-- @param outputStream The stream to which to write.
-- @param value The integer value.
*/
static int messagePackPackerWriteInt32(lua_State *L) {
    MessagePackPackerType *packerData = checkMessagePackPackerType(L);
    OutputStreamType *outputData = checkOutputStreamType(L,2);    
    signed long value = (signed long)luaL_checknumber(L,3);
    
    if (packerData->packer->writeInt32(outputData->output,value) == false) {
        luaPushError(L,packerData->packer->getError().c_str());
        return 0;
    }
    
    return 0;
}

/*
-- @name :writeStr16
-- @func
-- @brief Writes a string (at most 16^2-1 long).
-- @param outputStream The stream to which to write.
-- @param value The string value.
*/
static int messagePackPackerWriteStr16(lua_State *L) {
    MessagePackPackerType *packerData = checkMessagePackPackerType(L);
    OutputStreamType *outputData = checkOutputStreamType(L,2);        
    const char *value = luaL_checkstring(L,3);
    
    if (packerData->packer->writeStr16(outputData->output,value) == false) {
        luaPushError(L,packerData->packer->getError().c_str());
        return 0;
    }
    
    return 0;
}

/*
-- @name :writeMap16
-- @func
-- @brief Writes the beginning of a map (with at most 16^2 key-value pairs).
-- @full Writes the beginning of a map (with at most 16^2 key-value pairs).\
--   The map must be followed by `2*length` elements.
-- @param outputStream The stream to which to write.
-- @param length The number of items in the map.
*/
static int messagePackPackerWriteMap16(lua_State *L) {
    MessagePackPackerType *packerData = checkMessagePackPackerType(L);
    OutputStreamType *outputData = checkOutputStreamType(L,2);        
    unsigned int length = (unsigned int)luaL_checknumber(L,3);
    
    if (packerData->packer->writeMap16(outputData->output,length) == false) {
        luaPushError(L,packerData->packer->getError().c_str());
        return 0;
    }
    
    return 0;
}

/*
-- @name :__gc
-- @func
-- @brief Destorys the packer.
-- @full Destorys the packer. Never call this function directly!
*/
static int messagePackPacker__gc(lua_State *L) {
    MessagePackPackerType *data = checkMessagePackPackerType(L);
    if (data->packer != (MessagePackPacker *)0) {
        delete data->packer;
    }
    
    return 0;
}

// TODO Write __tostring metamethod.

/** The type functions. */
static const struct luaL_Reg messagePackPackerFuncs[] = {
    {"new",messagePackPackerNew},
    {0,0}
};

/** The type methods. */
static const struct luaL_Reg messagePackPackerMethods[] = {
    {"writeNil",messagePackPackerWriteNil},
    {"writeBool",messagePackPackerWriteBool},
    {"writeInt16",messagePackPackerWriteInt16},
    {"writeInt32",messagePackPackerWriteInt32},
    {"writeStr16",messagePackPackerWriteStr16},
    {"writeMap16",messagePackPackerWriteMap16},
    {0,0}
};

/** The type metamethods. */
static const struct luaL_Reg messagePackPackerMetamethods[] = {
    {"__gc",messagePackPacker__gc},
    {0,0}
};    
 
/** */
static int messagePackPackerRequireFunc(lua_State *L) {
    luaLoadUserType(L,messagePackPackerMetatableName,
        messagePackPackerFuncs,messagePackPackerMethods,
        messagePackPackerMetamethods);
    return 1;
}

/** */
void loadMessagePackPackerLib(lua_State *L) {
    luaL_requiref(L,messagePackPackerName,messagePackPackerRequireFunc,1);
    lua_pop(L,1);
}
    
} // namespace
    
} // namespace
    
} // namespace