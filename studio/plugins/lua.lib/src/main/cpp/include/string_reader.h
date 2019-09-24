#ifndef AE_LUA_UTIL_STRING_READER_H
#define AE_LUA_UTIL_STRING_READER_H

#include "lua.h"

struct StringReaderData {
    /** */
    const char *str;
    
    /** */
    char done;
};
typedef struct StringReaderData StringReaderData;

/** */
StringReaderData *createStringReaderData(const char *str);

/** */
const char *stringReader(lua_State *L,void *dataPtr,size_t *size);

#endif // AE_LUA_UTIL_STRING_READER_H
