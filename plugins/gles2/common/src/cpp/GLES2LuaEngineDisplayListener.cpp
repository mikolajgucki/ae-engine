#include "ae_GLES2.h"
#include "GLES2LuaEngineDisplayListener.h"

namespace ae {

namespace gles2 {
    
/** */
void GLES2LuaEngineDisplayListener::luaEngineDisplayResized(
    int width,int height) {
//
    glViewport(0,0,width,height);
}
    
} // namespace

} // namespace