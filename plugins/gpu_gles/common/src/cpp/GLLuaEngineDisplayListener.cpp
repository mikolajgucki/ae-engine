#include "ae_GLES.h"
#include "GLLuaEngineDisplayListener.h"

namespace ae {

namespace gles {
    
/** */
void GLLuaEngineDisplayListener::luaEngineDisplayResized(
    int width,int height) {
//
    glViewport(0,0,width,height);
}
    
} // namespace

} // namespace