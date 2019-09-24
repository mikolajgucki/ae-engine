#include "ae_platform.h"
#include "DesktopPluginGLES2.h"

#ifdef AE_WIN32
extern "C" {
#include "GL/glew.h"
}
#endif

namespace ae {

namespace gles2 {

/// The log tag.
static const char *const logTag = "gles2"; 
    
/** */
DesktopPluginGLES2::~DesktopPluginGLES2() {
}
    
/** */
bool DesktopPluginGLES2::init() {
// create Lua engine plugin
    gles2luaEnginePlugin = new GLES2LuaEnginePlugin();

#ifdef AE_WIN32
    getLog()->trace(logTag,"Initializing GLEW");
    GLenum glewError = glewInit();
    if (glewError != GLEW_OK) {
        setError((const char *)glewGetErrorString(glewError));
        return false;
    }
#endif
    
    return true;
}

/** */
bool DesktopPluginGLES2::addLuaEnginePlugins() {
    getLuaEngine()->addPlugin(gles2luaEnginePlugin);
    return true;
}

} // namespace

} // namespace