#ifndef AE_GLSL_PROGRAM_H
#define AE_GLSL_PROGRAM_H

#include <string>

#include "ae_GLES.h"
#include "GLError.h"
#include "GLSLShader.h"

namespace ae {
    
namespace gles {
 
/**
 * \brief Represents a GLSL program.
 */
class GLSLProgram:public GLError {
    /** */
    enum {
        /** Indicates no GL program. */
        NO_GL_PROGRAM = 0
    };
    
    /// The GL identifier of the program.
    GLuint glProgram;
    
public:
    /**
     * \brief Constructs a GLSLProgram.
     */
    GLSLProgram():GLError(),glProgram(NO_GL_PROGRAM) {
    }
    
    /** */
    ~GLSLProgram() {
        deleteProgram();
    }
    
    /**
     * \brief Creates the program.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> otherwise.     
     */
    bool create();
    
    /**
     * \brief Attaches a shader to the program.
     * \param shader The shader to attach.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> otherwise.     
     */
    bool attachShader(GLSLShader *shader);
    
    /**
     * \brief Links the program.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> otherwise.     
     */
    bool link();
    
    /**
     * \brief Creates, attaches shaders and links a program.
     * \param vertShader The vertex shader.
     * \param fragShader The fragment shader.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> otherwise.     
     */
    bool build(GLSLShader *vertShader,GLSLShader *fragShader);
    
    /**
     * \brief Uses the program.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> otherwise.     
     */
    bool use();
    
    /**
     * \brief Gets the location of a uniform variable.
     * \param name The variable name.
     * \param glIndex The output for the variable index.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> otherwise.     
     */
    bool getUniformLocation(const GLchar *name,GLint *glIndex);
    
    /**
     * \brief Gets the location of an attribute variable.
     * \param name The variable name.
     * \param glIndex The output for the variable index.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> otherwise.     
     */
    bool getAttribLocation(const GLchar *name,GLint *glIndex);
    
    /**
     * \brief Deletes the program.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> otherwise.
     */
    bool deleteProgram();    
};
    
} // namespace
    
} // namespace

#endif // AE_GLSL_PROGRAM_H