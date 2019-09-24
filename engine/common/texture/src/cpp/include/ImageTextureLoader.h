#ifndef AE_IMAGE_TEXTURE_LOADER_H
#define AE_IMAGE_TEXTURE_LOADER_H

#include <string>

#include "ImageLoader.h"
#include "Texture.h"
#include "TextureFactory.h"
#include "TextureLoader.h"

namespace ae {
  
namespace texture {
    
/**
 * \brief Loads textures from images.
 */
class ImageTextureLoader:public TextureLoader {
    /** Loads the images of the textures. */
    ::ae::image::ImageLoader *imageLoader;
    
    /** The texture factory. */
    TextureFactory *textureFactory;
    
public:
    /**
     * \brief Constructs an ImageTextureLoader.
     * \param imageLoader_ The loader which loades images for the textures.
     */
    ImageTextureLoader(ae::image::ImageLoader *imageLoader_,
        TextureFactory *textureFactory_):TextureLoader(),
        imageLoader(imageLoader_),textureFactory(textureFactory_) {
    }
    
    /** */
    virtual ~ImageTextureLoader() {
    }        
    
    /**
     * \brief Loads a texture of given identifier.
     * \param id The texture identifier.
     * \return The loaded texture.
     * \throw ImageLoadException if the texture cannot be loaded.
     */
    virtual Texture *loadTexture(const std::string &id);
};

} // namespace

} // namespace

#endif // AE_IMAGE_TEXTURE_LOADER_H