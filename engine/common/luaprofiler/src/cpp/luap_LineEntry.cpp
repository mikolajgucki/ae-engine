#include <cstring>
#include <cstdlib>
#include "luap_LineEntry.h"

/** */
luap_LineEntry *luap_CreateLineEntry(int line) {
// entry
    luap_LineEntry *entry = (luap_LineEntry *)malloc(sizeof(luap_LineEntry));
    if (entry == (luap_LineEntry *)0) {
        return (luap_LineEntry *)0;
    }
    
// list
    entry->list = luap_CreateList();
    if (entry->list == (luap_List *)0) {
        free(entry);
        return (luap_LineEntry *)0;
    }    
    
// line
    entry->line = line;
    
    return entry;
}

/** */
void luap_DeleteLineEntry(luap_LineEntry *entry) {
    luap_DeleteList(entry->list);
    free(entry);
}