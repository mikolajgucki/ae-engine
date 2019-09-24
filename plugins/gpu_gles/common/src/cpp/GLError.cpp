#include "GLError.h"

namespace ae {
    
namespace gles {
 
/** */
const char *GLError::getGLErrorStr(GLenum error) {
    if (error == GL_NO_ERROR) {
        return "GL_NO_ERROR";
    }
    if (error == GL_INVALID_ENUM) {
        return "GL error: GL_INVALID_ENUM";
    }
    if (error == GL_INVALID_VALUE) {
        return "GL error: GL_INVALID_VALUE";
    }
    if (error == GL_INVALID_ENUM) {
        return "GL error: GL_INVALID_ENUM";
    }
    if (error == GL_INVALID_OPERATION) {
        return "GL error: GL_INVALID_OPERATION";
    }
#ifndef AE_USE_GLEW
    if (error == GL_INVALID_FRAMEBUFFER_OPERATION) {
        return "GL error: GL_INVALID_FRAMEBUFFER_OPERATION";
    }
#endif
    if (error == GL_OUT_OF_MEMORY) {
        return "GL error: GL_OUT_OF_MEMORY";
    }
    
    return "Unknown GL error";
}
 
/** */
bool GLError::chkGLError() {
    GLenum error = glGetError();
    if (error == GL_NO_ERROR) {
        return false;
    }
    
    setError(getGLErrorStr(error));
    return true;
}

} // namespace

} // namespace