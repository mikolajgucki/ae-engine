#ifndef AE_GLSL_SHADER_H
#define AE_GLSL_SHADER_H

#include <string>

#include "ae_GLES.h"
#include "GLError.h"

namespace ae {

namespace gles {
  
/**
 * \brief Represents a GLSL shader.
 */
class GLSLShader:public GLError {
    /** */
    enum {
        /** Indicates no shader. */
        NO_SHADER
    };
    
    /// The shader type.
    GLenum shaderType;
    
    /// The GL shader identifier.
    GLuint glShader;
    
public:
    /**
     * \brief Constructs a GLSLShader.
     * \param shaderType_ The shader type.
     */
    GLSLShader(GLenum shaderType_):GLError(),shaderType(shaderType_),
        glShader(NO_SHADER) {
    }
    
    /** */
    ~GLSLShader() {
        deleteShader();
    }
    
    /**
     * \brief Gets the GL identifier of the shader.
     * \return The GL identifier of the shader.
     */
    GLuint getGLShader() const {
        return glShader;
    }    
    
    /**
     * \brief Creates the shader.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> otherwise.
     */
    bool create();
    
    /**
     * \brief Compiles the shader.
     * \param src The shader source.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> otherwise.
     */
    bool compile(const GLchar *src);
    
    /**
     * \brief Deletes the shader.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> otherwise.
     */
    bool deleteShader();
};
    
} // namespace

} // namespace

#endif // AE_GLSL_SHADER_H