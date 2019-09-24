#ifndef AE_GL_TEXTURE_FACTORY_H
#define AE_GL_TEXTURE_FACTORY_H

#include "TextureFactory.h"

namespace ae {
    
namespace gles {
 
/**
 * \brief The OpenGL ES implementation of the texture factory.
 */
class GLTextureFactory:public ::ae::texture::TextureFactory {
public:
    /** */
    GLTextureFactory():TextureFactory() {
    }
    
    /** */
    virtual ~GLTextureFactory() {
    }
    
    /** */
    virtual ::ae::texture::Texture *createFromImage(const std::string &id,
        const ::ae::image::Image *image);
    
    /** */
    virtual bool deleteTexture(::ae::texture::Texture *texture);
};
    
} // namespace

} // namespace

#endif // AE_GL_TEXTURE_FACTORY_H