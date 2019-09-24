#include <cstring>
#include <cstdlib>
#include "luap_call.h"

/** */
luap_Call *luap_CreateCall(const char *type,const char *name,
    const char *source,int linedef) {
// create call
    luap_Call *call = (luap_Call *)malloc(sizeof(luap_Call));
    if (call == (luap_Call *)0) {
        return (luap_Call *)0;
    }
    
// type
    call->type = type;
    
// name
    if (name != (char *)0) {
        call->name = (char *)malloc(strlen(name) + 1);
        if (call->name == (char *)0) {
            free(call);
            return (luap_Call *)0;
        }
        strcpy(call->name,name);
    }
    else {
        call->name = (char *)0;
    }
    
// source
    call->source = (char *)malloc(strlen(source) + 1);
    if (call->source == (char *)0) {
        free(call->name);
        free(call);
        return (luap_Call *)0;
    }
    strcpy(call->source,source);    
    
// other values
    call->linedef = linedef;
    call->hits = 0;
    call->totalTime = 0;
    call->totalMemoryAllocated = 0;
    call->subcallsMemoryAllocated = 0;
    
    return call;
}

/** */
void luap_DeleteCall(luap_Call *call) {
    free(call->source);
    if (call->name != (char *)0) {
        free(call->name);
    }
    free(call);
}