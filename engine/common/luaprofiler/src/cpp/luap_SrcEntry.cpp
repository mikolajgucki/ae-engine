#include <cstring>
#include <cstdlib>
#include "luap_SrcEntry.h"

/** */
luap_SrcEntry *luap_CreateSrcEntry(const char *source) {
// entry
    luap_SrcEntry *entry = (luap_SrcEntry *)malloc(sizeof(luap_SrcEntry));
    if (entry == (luap_SrcEntry *)0) {
        return (luap_SrcEntry *)0;
    }
    
// list
    entry->list = luap_CreateList();
    if (entry->list == (luap_List *)0) {
        free(entry);
        return (luap_SrcEntry *)0;;
    }
    
// source
    entry->source = (char *)malloc(strlen(source) + 1);
    if (entry->source == (char *)0) {
        luap_DeleteList(entry->list);
        free(entry);
        return (luap_SrcEntry *)0;
    }
    strcpy(entry->source,source);
    
    return entry;
}

/** */
void luap_DeleteSrcEntry(luap_SrcEntry *entry) {
    free(entry->source);
    luap_DeleteList(entry->list);
    free(entry);
}