#ifndef AE_LUAP_H
#define AE_LUAP_H

#include "lua.hpp"
#include "luap_List.h"

/** */
int luap_Start(lua_State *L_);

/** */
long luap_GetTime();

/** */
luap_List *luap_Stop(lua_State *L);

/** */
void luap_DeleteLog(luap_List *log);

#endif // AE_LUAP_H