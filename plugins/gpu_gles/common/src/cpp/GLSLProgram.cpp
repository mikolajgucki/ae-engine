#include "GLSLProgram.h"

using namespace std;

namespace ae {

namespace gles {
    
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
// link...
    glLinkProgram(glProgram);
    if (chkGLError()) {
        return false;
    }
    
// ...and check link status
    GLint linked = GL_FALSE;
    glGetProgramiv(glProgram,GL_LINK_STATUS,&linked);
    
    if (linked == GL_FALSE) {
    // get info length
        GLint infoLogLength;
        glGetProgramiv(glProgram,GL_INFO_LOG_LENGTH,&infoLogLength);
        
    // get info
        char *infoLog = new char[infoLogLength];        
        GLsizei length;
        glGetProgramInfoLog(glProgram,infoLogLength,&length,infoLog);
        
    // set error
        setError(string(infoLog));
        delete infoLog;
        
        return false;        
    }    
    
    return true;
}

/** */
bool GLSLProgram::build(GLSLShader *vertShader,GLSLShader *fragShader) {
    if (create() == false) {
        return false;
    }
    if (attachShader(vertShader) == false) {
        return false;
    }
    if (attachShader(fragShader) == false) {
        return false;
    }
    if (link() == false) {
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
    if ((*glIndex) == -1) {
        ostringstream err;
        err << "Uniform variable " << name << " does not correspond to" <<
            " a uniform variable in the program";
        setError(err.str());
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
    if ((*glIndex) == -1) {
        ostringstream err;
        err << "Attribute variable " << name << " does not correspond to" <<
            " an attribute variable in the program";
        setError(err.str());
        return false;
    }
    
    return true;
}

/** */
bool GLSLProgram::deleteProgram() {
    if (glProgram == NO_GL_PROGRAM) {
        return true;
    }
    
// delete
    glDeleteProgram(glProgram);
    if (chkGLError()) {
        return false;
    }
    
    return true;
}

} // namespace
    
} // namespace
