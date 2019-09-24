#ifndef AE_DEFAULT_DRAWER_SHADER_H
#define AE_DEFAULT_DRAWER_SHADER_H

#include "Error.h"
#include "DefaultDrawerFeatures.h"
#include "GLSLShader.h"
#include "GLSLProgram.h"

namespace ae {
    
namespace gles {
  
/**
 * \brief Provides shaders for the GL default drawer.
 */
class GLDefaultDrawerShader:public Error {
    /// The GLSL programs corresponding to drawer features.
    GLSLProgram **programs;
    
    /** */
    void create();
    
    /** 
     * \brief Preprocess shader source according to features.
     * \param features The drawer featuers.
     * \param lines The line with the shader source.
     * \return The preprocessed shader source.
     */
    std::string preprocessShaderSrc(::ae::gpu::DefaultDrawerFeatures& features,
        const char *lines[]);
    
    /**
     * \brief Gets the vertex shader source.
     * \param features The drawer featuers.
     * \return The shader source.
     */
    std::string getVertShaderSrc(::ae::gpu::DefaultDrawerFeatures& features);
    
    /**
     * \brief Gets the fragment shader source.
     * \param features The drawer featuers.
     * \return The shader source.
     */
    std::string getFragShaderSrc(::ae::gpu::DefaultDrawerFeatures& features);
    
    /**
     * \brief Creates a vertex shader.
     * \param features The drawer featuers.
     * \return The shader.     
     */
    GLSLShader *createVertShader(::ae::gpu::DefaultDrawerFeatures& features);
    
    /**
     * \brief Creates a fragment shader.
     * \param features The drawer featuers.
     * \return The shader.     
     */
    GLSLShader *createFragShader(::ae::gpu::DefaultDrawerFeatures& features);
    
public:
    /** */
    GLDefaultDrawerShader():Error(),programs((GLSLProgram **)0) {
        create();
    }
    
    /** */
    virtual ~GLDefaultDrawerShader() {
    }
    
    /**
     * \brief Gets the GLSL program which matches default drawer features.
     * \param features The features.
     * \return The GLSL program or sets error and returns null on error.
     */
    GLSLProgram *getGLSLProgram(::ae::gpu::DefaultDrawerFeatures& features);
};
    
} // namespace
    
} // namespace

#endif // AE_DEFAULT_DRAWER_SHADER_H