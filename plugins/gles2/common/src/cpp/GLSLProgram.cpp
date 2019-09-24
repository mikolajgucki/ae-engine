#include "GLSLProgram.h"

using namespace std;

namespace ae {

namespace gles2 {
    
/** */
bool GLSLProgram::create() {
    glProgram = glCreateProgram();
    if (glProgram == 0) {
        if (chkGLError()) {
            return false;
        }
        
        setError("Failed to create GLSL program");
        return false;
    }
    
    return true;
}

/** */
bool GLSLProgram::attachShader(GLSLShader *shader) {
    glAttachShader(glProgram,shader->getGLShader());
    if (chkGLError()) {
        return false;
    }
    
    return true;
}

/** */
bool GLSLProgram::link() {
    glLinkProgram(glProgram);
    if (chkGLError()) {
        return false;
    }
    
    return true;
}

/** */
bool GLSLProgram::use() {
    glUseProgram(glProgram);
    if (chkGLError()) {
        return false;
    }
    
    return true;
}
    
/** */
bool GLSLProgram::getUniformLocation(const GLchar *name,GLint *glIndex) {
    (*glIndex) = glGetUniformLocation(glProgram,name);
    if (chkGLError()) {
        return false;
    }    
    
    return true;
}
    
/** */
bool GLSLProgram::getAttribLocation(const GLchar *name,GLint *glIndex) {
    (*glIndex) = glGetAttribLocation(glProgram,name);
    if (chkGLError()) {
        return false;
    }    
    
    return true;
}

/** */
bool GLSLProgram::deleteProgram() {
    glDeleteProgram(glProgram);
    if (chkGLError()) {
        return false;
    }
    
    return true;
}

} // namespace
    
} // namespace
