#include <memory>
#include "LuaLoader.h"

using namespace std;
using namespace ae::io;

namespace ae {
    
namespace lua {

/**
 * \brief The Lua read function data.
 */
struct LuaReadFuncData {
    /// The stream from which to read the data.
    ae::io::InputStream *input;
    
    /// The read buffer size.
    size_t bufferSize;
    
    /// The read buffer.
    char *buffer;
    
    /// Indidicates if all the data has been read.
    bool done;
    
    /**
     * \brief Constructs a LuaLoaderReadFuncData.
     * \param input_ The input stream from which to read.
     * \param bufferSize_ The read buffer size.
     * \param buffer_ The read buffer.
     */
    LuaReadFuncData(ae::io::InputStream *input_,size_t bufferSize_,
        char *buffer_):input(input_),bufferSize(bufferSize_),
        buffer(buffer_),done(false) {
    //
    }
};    
typedef struct LuaReadFuncData LuaReadFuncData;

/** */
static const char *luaReadFunc(lua_State *L,void *data,size_t *size) {
    LuaReadFuncData *funcData = (LuaReadFuncData *)data;
    
// if already end of file reached
    if (funcData->done == true) {
        (*size) = 0;
        return (const char *)0;
    }
    
    int readSize = funcData->input->read(
        (void *)funcData->buffer,funcData->bufferSize);
    if (readSize == InputStream::ERROR) {
    // TODO Report error if data read fails.
        return (const char *)0;
    }
    
// if there is no more data
    if ((size_t)readSize < funcData->bufferSize) {
        funcData->done = true;
    }
    
    (*size) = (size_t)readSize;
    return funcData->buffer;
}

/// The size of the buffer to read.
size_t LuaLoader::bufferSize = 512;

/** */
void LuaLoader::createBuffer() {
    buffer = new (nothrow) char[bufferSize];
    if (buffer == (const char *)0) {
        setNoMemoryError();
        return;
    }
}

/** */
LuaLoader::~LuaLoader() {
    if (buffer == (const char *)0) {
        delete[] buffer;
    }
}

/** */
bool LuaLoader::load(lua_State *L,ae::io::InputStream *input,
    const std::string &chunkname) {
// load
// TODO Create LuaReadFuncData with no throw.
    LuaReadFuncData *funcData = new LuaReadFuncData(input,bufferSize,buffer);
    int loadResult = lua_load(L,luaReadFunc,funcData,chunkname.c_str(),"tb");
    delete funcData;

// load failed?
    if (loadResult != 0) {
        ostringstream msg;
        msg << "Failed to load Lua source from chunk " << chunkname;
        if (loadResult == LUA_ERRSYNTAX) {
            msg << " (" << lua_tostring(L,-1) << ")";
        }
        if (loadResult == LUA_ERRMEM) {
            msg << " (memory allocation error)";
        }
        
        setError(msg.str());
        return false;
    }
    
    int callResult;
    if (luaPCall.tryCall(L,0,LUA_MULTRET,callResult) == false) {
        setError(luaPCall.getError());
        return false;
    }
    
    return true;
}
    
} // namespace
    
} // namespace