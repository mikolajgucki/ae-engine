#ifndef AE_LUAP_LINE_ENTRY_H
#define AE_LUAP_LINE_ENTRY_H

#include "luap_List.h"

/** */
typedef struct {
    /** */
    int line;
    
    /** */
    luap_List *list;
} luap_LineEntry;

/** */
luap_LineEntry *luap_CreateLineEntry(int line);

/** */
void luap_DeleteLineEntry(luap_LineEntry *entry);

#endif //  AE_LUAP_LINE_ENTRY_H