#ifndef AE_GLES_TEXTURE_H
#define AE_GLES_TEXTURE_H

#include "Image.h"
#include "Texture.h"
#include "GLESError.h"

namespace ae {

namespace gles2 {
    
/**
 * \brief The GLES texture implementation.
 */
class GLESTexture:public ::ae::texture::Texture {
    /** */
    enum {
        NO_GL_NAME = 0
    };
    
    /// The OpenGL name of the texture
    GLuint glName;
    
    /// The GLES error.
    GLESError glesError;
    
    /** */
    bool chkGLError();
    
public:
    /** */
    GLESTexture(const std::string &id):Texture(id),glName(NO_GL_NAME),
        glesError() {
    }
    
    /** */
    virtual ~GLESTexture() {
        if (glName != NO_GL_NAME) {
            deleteTexture();
        }
    }
    
    /**
     * \brief Creates the texture from an image.
     * \param image The image.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> otherwise.
     */     
    bool create(const ::ae::image::Image *image);
    
    /**
     * \brief Gets the texture GL name.
     * \return The GL name.
     */
    int getGLName() const {
        return glName;
    }    
    
    /** */
    virtual bool bind();
    
    /** */
    bool deleteTexture();
};
    
} // namespace
    
} // namespace

#endif // AE_GLES_TEXTURE_H