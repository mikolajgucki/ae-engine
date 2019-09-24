#ifndef AE_TEXTURE_FACTORY_H
#define AE_TEXTURE_FACTORY_H

#include "Error.h"
#include "Image.h"
#include "Texture.h"

namespace ae {
    
namespace texture {
    
/**
 * \brief The superclass for texture factories.
 */
class TextureFactory:public Error {
public:
    /** */
    TextureFactory():Error() {
    }
    
    /** */
    virtual ~TextureFactory() {
    }
    
    /**
     * \brief Creates a texture from image.
     * \param id The texture identifier.
     * \param image The image.
     * \return The texture, sets error and returns null if the texture cannot
     *   be created.
     */
    virtual Texture *createFromImage(const std::string &id,
        const ::ae::image::Image *image) = 0;
    
    /**
     * \brief Deletes a texture create by this factory.
     * \param texture A texture created by this factory.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> otherwise.
     */
    virtual bool deleteTexture(Texture *texture) = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_TEXTURE_FACTORY_H