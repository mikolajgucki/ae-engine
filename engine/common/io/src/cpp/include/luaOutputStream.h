#ifndef AE_IO_LUA_OUTPUT_STREAM_H
#define AE_IO_LUA_OUTPUT_STREAM_H

#include "lua.hpp"
#include "OutputStream.h"

namespace ae {
    
namespace io {
    
namespace lua {
    
/**
 * \brief Wraps an output stream so that it can be used as Lua user type.
 */     
struct OutputStreamType {
    /** */
    OutputStream *output;
};
typedef struct OutputStreamType OutputStreamType;
    
/**
 * \brief Creates and pushes an output stream user type.
 * \param L The Lua state.
 * \param output The output stream to wrap in the user type.
 */
void luaPushOutputStreamType(lua_State *L,OutputStream *output);

/**
 * \brief Checks wheter the object at given stack index is a user data of
 *     the output stream type. Raises error if the object is not of the type.
 * \param index The Lua stack index.
 * \return The user data of the output stream type. 
 */
OutputStreamType *checkOutputStreamType(lua_State *L,int index = 1);

/**
 * \brief Loads the output stream library.
 * \param L The Lua state.
 */
void loadOutputStreamLib(lua_State *L);

} // namespace
    
} // namespace
    
} // namepsace

#endif // AE_IO_LUA_OUTPUT_STREAM_H