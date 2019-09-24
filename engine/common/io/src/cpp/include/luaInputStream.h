#ifndef AE_IO_LUA_INPUT_STREAM_H
#define AE_IO_LUA_INPUT_STREAM_H

#include "lua.hpp"
#include "InputStream.h"

namespace ae {
    
namespace io {
    
namespace lua {

/**
 * \brief Wraps an input stream so that it can be used as Lua user type.
 */
struct InputStreamType {
    /** */
    InputStream *input;
};
typedef struct InputStreamType InputStreamType;
   
/**
 * \brief Creates and pushes an input stream user type.
 * \param L The Lua state.
 * \param input The input stream to wrap in the user type.
 */
void luaPushInputStreamType(lua_State *L,InputStream *input);

/**
 * \brief Checks wheter the object at given stack index is a user data of
 *     the input stream type. Raises error if the object is not of the type.
 * \param index The Lua stack index.
 * \return The user data of the input stream type.  
 */
InputStreamType *checkInputStreamType(lua_State *L,int index = 1);

/**
 * \brief Loads the input stream library.
 * \param L The Lua state.
 */
void loadInputStreamLib(lua_State *L);

} // namespace
    
} // namespace
    
} // namepsace
    
#endif // AE_IO_LUA_INPUT_STREAM_H