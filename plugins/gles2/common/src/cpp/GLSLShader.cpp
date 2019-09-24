#include "GLSLShader.h"

using namespace std;

namespace ae {

namespace gles2 {
    
/** */
bool GLSLShader::create() {
    glShader = glCreateShader(shaderType);
    if (glShader == 0) {
        if (chkGLError()) {
            return false;
        }
        
        setError("Failed to create GLSL shader");
        return false;
    }
    
    return true;
}
    
/** */
bool GLSLShader::compile(const GLchar *src) {
    glShaderSource(glShader,1,&src,0);
    if (chkGLError()) {
        return false;
    }
    
// compile...
    glCompileShader(glShader);
    
// ...and check status
    GLint compileStatus;
    glGetShaderiv(glShader,GL_COMPILE_STATUS,&compileStatus);
    
    if (compileStatus == GL_FALSE) {
    // get info length
        GLint infoLogLength;
        glGetShaderiv(glShader,GL_INFO_LOG_LENGTH,&infoLogLength);
        
    // get info
        char *infoLog = new char[infoLogLength];        
        GLsizei length;
        glGetShaderInfoLog(glShader,infoLogLength,&length,infoLog);
        
    // set error
        setError(string(infoLog));
        delete[] infoLog;
        
        return false;
    }
    
    return true;
}

/** */
bool GLSLShader::deleteShader() {
    if (glShader == NO_SHADER) {
        return true;
    }
    
    glDeleteShader(glShader);
    if (chkGLError()) {
        return false;
    }    
    
    return true;
}

} // namespace

} // namespace