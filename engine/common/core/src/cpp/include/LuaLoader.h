#ifndef AE_CORE_LUA_LOADER_H
#define AE_CORE_LUA_LOADER_H

#include "lua.hpp"

#include "Error.h"
#include "InputStream.h"
#include "LuaPCall.h"

namespace ae {

namespace lua {
 
/**
 * \brief Loads Lua files from an input stream. The file can be text or binary.
 */
class LuaLoader:public Error {
    /// The size of the buffer to read.
    static size_t bufferSize;
    
    /// The read buffer.
    char *buffer;
    
    /// The Lua protected call.
    LuaPCall luaPCall;
    
    /**
     * \brief Creates the buffer.
     */
    void createBuffer();
    
public:
    /**
     * Constructs a LuaLoader.
     */
    LuaLoader():Error(),buffer((char *)0),luaPCall() {
        createBuffer();
    }
    
    /** */
    ~LuaLoader();
    
    /**
     * \brief Loads Lua either text or binary source.
     * \param L The Lua state.
     * \param input The stream from which to read the source.
     * \param chunkname The chunk name.
     * \return <code>true</code> if the source has been sucessfully loaded,
     *     <code>false</code> otherwise.
     */
    bool load(lua_State *L,ae::io::InputStream *input,
        const std::string &chunkname);
};
    
} // namespace
    
} // namespace

#endif // AE_CORE_LUA_LOADER_H