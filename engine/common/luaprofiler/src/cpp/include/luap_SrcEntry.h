#ifndef AE_LUAP_SRC_ENTRY_H
#define AE_LUAP_SRC_ENTRY_H

#include "luap_List.h"

/** */
typedef struct {
    /** */
    char *source;
    
    /** */
    luap_List *list;
} luap_SrcEntry;

/** */
luap_SrcEntry *luap_CreateSrcEntry(const char *source);

/** */
void luap_DeleteSrcEntry(luap_SrcEntry *entry);

#endif //  AE_LUAP_SRC_ENTRY_H