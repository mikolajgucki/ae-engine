#ifndef AE_GL_TEXTURE_H
#define AE_GL_TEXTURE_H

#include "Image.h"
#include "Texture.h"
#include "GLError.h"

namespace ae {

namespace gles {
    
/**
 * \brief The OpenGL ES texture implementation.
 */
class GLTexture:public ::ae::texture::Texture {
    /** */
    enum {
        NO_GL_NAME = 0
    };
    
    /// The OpenGL name of the texture
    GLuint glName;
    
    /// The GLES error.
    GLError glError;
    
    /** */
    bool chkGLError();
    
public:
    /** */
    GLTexture(const std::string &id):Texture(id),glName(NO_GL_NAME),
        glError() {
    }
    
    /** */
    virtual ~GLTexture() {
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
    bool deleteTexture();
};
    
} // namespace
    
} // namespace

#endif // AE_GL_TEXTURE_H