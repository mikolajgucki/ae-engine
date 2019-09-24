#ifndef AE_GLES_TEXTURE_FACTORY_H
#define AE_GLES_TEXTURE_FACTORY_H

#include "TextureFactory.h"

namespace ae {
    
namespace gles2 {
 
/**
 * \brief The OpenGL ES implementation of the texture factory.
 */
class GLESTextureFactory:public ::ae::texture::TextureFactory {
public:
    /** */
    GLESTextureFactory():TextureFactory() {
    }
    
    /** */
    virtual ~GLESTextureFactory() {
    }
    
    /** */
    virtual ::ae::texture::Texture *createFromImage(const std::string &id,
        const ::ae::image::Image *image);
    
    /** */
    virtual bool deleteTexture(::ae::texture::Texture *texture);
};
    
} // namespace

} // namespace

#endif // AE_GLES_TEXTURE_FACTORY_H