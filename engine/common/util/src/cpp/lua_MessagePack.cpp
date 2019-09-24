/*
-- @module MsgPack
-- @group Utility
-- @brief Provides functions and values related to the MessagePack
--   specification.
-- @full Provides functions and values related to the MessagePack
--   specification. This module is a C library.
*/
#include "lua_common.h"
#include "MessagePack.h"
#include "lua_MessagePack.h"

using namespace ae::lua;

namespace ae {
    
namespace util {
    
namespace lua {
    
/// The library name.
static const char *messagePackName = "MsgPack";
 
/*
-- @name .getTypesTable
-- @func
-- @brief Gets the table with the message pack types and their values.
-- @return The table with the message pack types and their values.
*/
static int messagePackGetTypesTable(lua_State *L) {
    lua_newtable(L);
    
    luaSetTable(L,"value_nil",(lua_Integer)MessagePack::MSG_PACK_NIL);
    luaSetTable(L,"value_false",(lua_Integer)MessagePack::MSG_PACK_FALSE);
    luaSetTable(L,"value_true",(lua_Integer)MessagePack::MSG_PACK_TRUE);
    luaSetTable(L,"int16",(lua_Integer)MessagePack::MSG_PACK_INT16);
    luaSetTable(L,"int32",(lua_Integer)MessagePack::MSG_PACK_INT32);
    luaSetTable(L,"str16",(lua_Integer)MessagePack::MSG_PACK_STR16);
    luaSetTable(L,"map16",(lua_Integer)MessagePack::MSG_PACK_MAP16);
    
    return 1;
}

/** */
static const struct luaL_Reg messagePackFuncs[] = {
    {"getTypesTable",messagePackGetTypesTable},
    {0,0}
};

/** */
static int messagePackRequireFunc(lua_State *L) {
    luaL_newlib(L,messagePackFuncs);
    return 1;       
}
    
/** */
void loadMessagePackLib(lua_State *L) {
    luaL_requiref(L,messagePackName,messagePackRequireFunc,1);
    lua_pop(L,1);
}
    
} // namespace 
    
} // namespace
    
} // namespace