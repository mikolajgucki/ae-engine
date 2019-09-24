#include "ae_platform.h"
#include "GPUGLESDesktopPlugin.h"

#ifdef AE_WIN32
extern "C" {
#include "GL/glew.h"
}
#endif

using namespace std;
using namespace ae::gles;

namespace ae {

namespace gpu {
    
namespace gles {

/// The log tag.
static const char *const logTag = "gpu_gles"; 
    
/** */
bool GPUGLESDesktopPlugin::init() {
// create the Lua engine plugin
    glesLuaEnginePlugin = new GPUGLESLuaEnginePlugin();

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
bool GPUGLESDesktopPlugin::addLuaEnginePlugins() {
    getLuaEngine()->addPlugin(glesLuaEnginePlugin);
    return true;
}

} // namespace

} // namespace

} // namespace