#include <cstdio>
#include <cstring>
#include <cstdlib>

#include "luap.h"
#include "luap_List.h"
#include "luap_StackItem.h"
#include "luap_Call.h"
#include "luap_SrcEntry.h"
#include "luap_LineEntry.h"

/** */
static luap_List *stack;

/** */
static luap_List *luaCalls;

/** */
static long luap_getUsedMemory(lua_State *L) {
    int kilobytes = lua_gc(L,LUA_GCCOUNT,0);
    int remainder = lua_gc(L,LUA_GCCOUNTB,0);
    
    return kilobytes * 1024 + remainder;
}

/** */
static luap_Call *getLuaCall(lua_Debug *info) {
    const char *type = "Lua";
    const char *source = info->source;
    int linedef = info->linedefined;
    
// for each Lua source
    int isrc;
    for (isrc = 0; isrc < luap_GetListSize(luaCalls); isrc++) {
        luap_SrcEntry *entry = (luap_SrcEntry *)luap_GetListItem(luaCalls,isrc);
            
    // if the same source
        if (strcmp(source,entry->source) == 0) {
        // for each call in this Lua source
            int icall;
            for (icall = 0; icall < luap_GetListSize(entry->list); icall++) {
                luap_Call *call =
                    (luap_Call *)luap_GetListItem(entry->list,icall);
                if (call->linedef == linedef) {
                    return call;
                }
            }
            
        // no such call
            luap_Call *call = luap_CreateCall(type,info->name,source,linedef);
            if (call == (luap_Call *)0) {
                return (luap_Call *)0;
            }
            luap_AddToList(entry->list,call);
            
            return call;
        }
    }
    
// no such source - create entry...
    luap_SrcEntry *entry = luap_CreateSrcEntry(source);
    if (entry == (luap_SrcEntry *)0) {
        return (luap_Call *)0;
    }
    
// ...add a new call to it
    luap_Call *call = luap_CreateCall(type,info->name,source,linedef);
    if (call == (luap_Call *)0) {
        return (luap_Call *)0;
    }
    luap_AddToList(luaCalls,entry);
    luap_AddToList(entry->list,call);
    
    return call;    
}

/** */
static int getTopDebugInfo(lua_State *L,lua_Debug *info) {
    if (lua_getstack(L,0,info) == 0) {
        return 0;
    }
    
    lua_getinfo(L,"Sln",info);
    return 1;
}

/** */
static void callHook(lua_State *L,lua_Debug *ar) {
    lua_Debug info;
    if (getTopDebugInfo(L,&info) == 0) {
        return;
    }
    
// push onto stack
    luap_StackItem *stackItem = luap_CreateStackItem();
    stackItem->startTime = luap_GetTime();
    stackItem->startUsedMemory = luap_getUsedMemory(L);
    luap_AddToList(stack,stackItem);
    
    if (info.what == 0) {
        return;
    }  
    if (info.name == 0) {
        return;
    }
    
// if a Lua call (info.what equals Lua)
    if (info.what[0] == 'L') {
        luap_Call *call = getLuaCall(&info);
        call->hits++;
    }
    
// if a C call (info.what equals C)
    if (info.what[0] == 'C') {
        // TODO
    }    
}   

/** */
static void returnHook(lua_State *L,lua_Debug *ar) {
    lua_Debug info;
    if (getTopDebugInfo(L,&info) == 0) {
        return;
    }
    
// pop from the stack
    luap_StackItem *stackItem =
        (luap_StackItem *)luap_GetListItem(stack,stack->size - 1);
    luap_RemoveLastListItem(stack);
    
// update metrics
    long callTime = luap_GetTime() - stackItem->startTime;
    long memoryAllocated = luap_getUsedMemory(L) - stackItem->startUsedMemory;
    long subcallsMemoryAllocated = stackItem->subcallsMemoryAllocated;
    
// get rid of the item
    free(stackItem);
    
// update the current top stack item
    if (luap_GetListSize(stack) > 1) {
        luap_StackItem *topStackItem =
            (luap_StackItem *)luap_GetListItem(stack,stack->size - 1);
        topStackItem->subcallsMemoryAllocated += memoryAllocated;
    }
    
    if (info.what == 0) {
        return;
    }    
    if (info.name == 0) {
        return;
    }
    
// if a Lua call (info.what equals Lua)
    if (info.what[0] == 'L') {
        luap_Call *call = getLuaCall(&info);
        call->totalTime += callTime;
        call->totalMemoryAllocated += memoryAllocated;
        call->subcallsMemoryAllocated += subcallsMemoryAllocated;
    }
    
// if a C call (info.what equals C)
    if (info.what[0] == 'C') {
        // TODO
    }
}

/** */
static void hook(lua_State *L,lua_Debug *ar) {
    if (ar->event == LUA_HOOKCALL) {
        callHook(L,ar);
    }
    if (ar->event == LUA_HOOKRET) {
        returnHook(L,ar);
    }
}

/** */
int luap_Start(lua_State *L) {
    stack = luap_CreateList();
    if (stack == (luap_List *)0) {
        return 0;
    }
    
    luaCalls = luap_CreateList();
    if (luaCalls == (luap_List *)0) {
        return 0;
    }
    
    lua_sethook(L,hook,LUA_MASKCALL|LUA_MASKRET,0);
    return 1;
}

/** */
static luap_List *createLog() {
    luap_List *log = luap_CreateList();
    if (log == (luap_List *)0) {
        return (luap_List *)0;        
    }
    
// for each Lua source
    int isrc;
    for (isrc = 0; isrc < luap_GetListSize(luaCalls); isrc++) {
        luap_SrcEntry *entry = (luap_SrcEntry *)luap_GetListItem(luaCalls,isrc);
        
    // for each call in this Lua source
        int icall;
        for (icall = 0; icall < luap_GetListSize(entry->list); icall++) {
            luap_Call *call = (luap_Call *)luap_GetListItem(entry->list,icall);
            luap_AddToList(log,call);
        }
    }
    
    return log;
}

/** */
luap_List *luap_Stop(lua_State *L) {
    lua_sethook(L,hook,0,0);    
    
// log
    luap_List *log = createLog();
    
// delete the lists and stack    
    luap_DeleteList(luaCalls);    
    luap_DeleteList(stack);
    
    return log;
}

/** */
void luap_DeleteLog(luap_List *log) {
    int ilog;
    for (ilog = 0; ilog < luap_GetListSize(log); ilog++) {
        luap_Call *call = (luap_Call *)luap_GetListItem(log,ilog);
        luap_DeleteCall(call);
    }
    luap_DeleteList(log);
}