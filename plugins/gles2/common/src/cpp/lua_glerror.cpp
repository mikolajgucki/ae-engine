#include "lua_common.h"
#include "GLESError.h"
#include "lua_glerror.h"

using namespace ae::lua;

namespace ae {

namespace gles2 {
    
namespace lua {
 
/** */
bool luaChkGLError(lua_State *L) {
    GLenum error = glGetError();
    if (error == GL_NO_ERROR) {
        return false;
    }
    
    luaPushError(L,GLESError::getGLErrorStr(error));
    return true;
}
    
} // namespace
    
} // namespace
    
} // namespace
