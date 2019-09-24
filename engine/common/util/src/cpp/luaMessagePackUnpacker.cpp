/*
-- @module MsgPackUnpacker
-- @group Utility
-- @brief Provides function to unpack a message pack.
-- @full Provides function to unpack a message pack. This module is a C library.
*/
#include <memory>

#include "lua_common.h"
#include "MessagePackUnpacker.h"
#include "luaInputStream.h"
#include "luaMessagePackUnpacker.h"

using namespace std;
using namespace ae::lua;
using namespace ae::io::lua;

namespace ae {
    
namespace util {
    
namespace lua {
    
/**
 * \brief Wraps the message pack unpacker so that it can be used as Lua user
 *   type.
 */
struct MessagePackUnpackerType {
    /** */
    MessagePackUnpacker *unpacker;
};
typedef struct MessagePackUnpackerType MessagePackUnpackerType;
    
/// The name of the Lua user type.
static const char *messagePackUnpackerName = "MsgPackUnpacker";

/// The name of the Lua metatable of the message pack unpacker user type.
static const char *messagePackUnpackerMetatableName =
    "MsgPackUnpacker.metatable";
 
/** */
static MessagePackUnpackerType *checkMessagePackUnpackerType(lua_State *L) {
    void *data = luaL_checkudata(L,1,messagePackUnpackerMetatableName);
    luaL_argcheck(L,data != (void *)0,1,"MsgPackUnpacker expected");
    return (MessagePackUnpackerType *)data; 
}

/*
-- @name .new
-- @func
-- @brief Creates a message pack unpacker.
-- @return The new message pack unpacker object.
*/
static int messagePackUnpackerNew(lua_State *L) {
// unpacker
    MessagePackUnpacker *unpacker = new (nothrow) MessagePackUnpacker();
    if (unpacker == (MessagePackUnpacker *)0) {
        luaPushNoMemoryError(L);
        return 0;
    }
    
// user data
    MessagePackUnpackerType *data = (MessagePackUnpackerType *)lua_newuserdata(
        L,sizeof(MessagePackUnpackerType));
    data->unpacker = unpacker;
    
// metatable
    luaL_getmetatable(L,messagePackUnpackerMetatableName);
    lua_setmetatable(L,-2);

    return 1;
}

/*
-- @name :readType
-- @func
-- @brief Reads the type of next element.
-- @param inputStream The input stream from which to read.
-- @return The read type or -1 if end of the file has been reached.
*/
static int messagePackUnpackerReadType(lua_State *L) {
    MessagePackUnpackerType *unpackerData = checkMessagePackUnpackerType(L);
    InputStreamType *inputData = checkInputStreamType(L,2);
    
    int type;
    if (unpackerData->unpacker->readType(inputData->input,type) == false) {
        luaPushError(L,unpackerData->unpacker->getError().c_str());
        return 0;        
    }
    
    if (type == MessagePackUnpacker::END_OF_FILE) {
        lua_pushnumber(L,-1);
        return 1;
    }
    
    lua_pushnumber(L,type);
    return 1;
}

/*
-- @name :readInt16
-- @func
-- @brief Reads the next 16-bit signed integer.
-- @param inputStream The input stream from which to read.
-- @return The read value.
*/
static int messagePackUnpackerReadInt16(lua_State *L) {
    MessagePackUnpackerType *unpackerData = checkMessagePackUnpackerType(L);
    InputStreamType *inputData = checkInputStreamType(L,2);
    
    int value;
    if (unpackerData->unpacker->readInt16(inputData->input,value) == false) {
        luaPushError(L,unpackerData->unpacker->getError().c_str());
        return 0;        
    }
    
    lua_pushnumber(L,value);
    return 1;
}

/*
-- @name :readInt32
-- @func
-- @brief Reads the next 32-bit signed integer.
-- @param inputStream The input stream from which to read.
-- @return The read value.
*/
static int messagePackUnpackerReadInt32(lua_State *L) {
    MessagePackUnpackerType *unpackerData = checkMessagePackUnpackerType(L);
    InputStreamType *inputData = checkInputStreamType(L,2);
    
    long value;
    if (unpackerData->unpacker->readInt32(inputData->input,value) == false) {
        luaPushError(L,unpackerData->unpacker->getError().c_str());
        return 0;        
    }
    
    lua_pushnumber(L,value);
    return 1;
}

/*
-- @name :readStr16
-- @func
-- @brief Reads the next 16-bit string.
-- @param inputStream The input stream from which to read.
-- @return The read value.
*/
static int messagePackUnpackerReadStr16(lua_State *L) {
    MessagePackUnpackerType *unpackerData = checkMessagePackUnpackerType(L);
    InputStreamType *inputData = checkInputStreamType(L,2);
    
    string value;
    if (unpackerData->unpacker->readStr16(inputData->input,value) == false) {
        luaPushError(L,unpackerData->unpacker->getError().c_str());
        return 0;        
    }
    
    lua_pushstring(L,value.c_str());
    return 1;
}

/*
-- @name :readMap16
-- @func
-- @brief Reads the next length of a 16-bit map.
-- @param inputStream The input stream from which to read.
-- @return The read length.
*/
static int messagePackUnpackerReadMap16(lua_State *L) {
    MessagePackUnpackerType *unpackerData = checkMessagePackUnpackerType(L);
    InputStreamType *inputData = checkInputStreamType(L,2);
    
    unsigned int value;
    if (unpackerData->unpacker->readMap16(inputData->input,value) == false) {
        luaPushError(L,unpackerData->unpacker->getError().c_str());
        return 0;        
    }
    
    lua_pushnumber(L,value);
    return 1;
}

/*
-- @name :__gc
-- @func
-- @brief Destorys the unpacker.
-- @full Destorys the unpacker. Never call this function directly! 
*/
static int messagePackUnpacker__gc(lua_State *L) {
    MessagePackUnpackerType *data = checkMessagePackUnpackerType(L);
    if (data->unpacker != (MessagePackUnpacker *)0) {
        delete data->unpacker;
    }
    
    return 0;
}

// TODO Write __tostring metamethod.

/** The type functions. */
static const struct luaL_Reg messagePackUnpackerFuncs[] = {
    {"new",messagePackUnpackerNew},
    {0,0}
};


/** The type methods. */
static const struct luaL_Reg messagePackUnpackerMethods[] = {
    {"readType",messagePackUnpackerReadType},
    {"readInt16",messagePackUnpackerReadInt16},
    {"readInt32",messagePackUnpackerReadInt32},
    {"readStr16",messagePackUnpackerReadStr16},
    {"readMap16",messagePackUnpackerReadMap16},
    {0,0}
};

/** The type metamethods. */
static const struct luaL_Reg messagePackUnpackerMetamethods[] = {
    {"__gc",messagePackUnpacker__gc},
    {0,0}
};

/** */
static int messagePackUnpackerRequireFunc(lua_State *L) {
    luaLoadUserType(L,messagePackUnpackerMetatableName,
        messagePackUnpackerFuncs,messagePackUnpackerMethods,
        messagePackUnpackerMetamethods);
    return 1;
}

/** */
void loadMessagePackUnpackerLib(lua_State *L) {
    luaL_requiref(L,messagePackUnpackerName,messagePackUnpackerRequireFunc,1);
    lua_pop(L,1);
}
    
} // namespace
    
} // namespace
    
} // namespace