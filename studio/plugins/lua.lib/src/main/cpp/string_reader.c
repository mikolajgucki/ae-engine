#include <stdlib.h>
#include <strings.h>
#include "string_reader.h"

/** */
StringReaderData *createStringReaderData(const char *str) {
    StringReaderData *data = (StringReaderData *)malloc(
        sizeof(StringReaderData));
    if (data == (StringReaderData *)0) {
        return (StringReaderData *)0;
    }
    
    data->str = str;
    data->done = 0;
    
    return data;
}

/** */
const char *stringReader(lua_State *L,void *dataPtr,size_t *size) {
    StringReaderData *data = (StringReaderData *)dataPtr;
    
    if (data->done != 0) {
        free(data);
        (*size) = 0;
        return (const char *)0;
    }    
    data->done = 1;
        
    (*size) = strlen(data->str);
    return data->str;
}