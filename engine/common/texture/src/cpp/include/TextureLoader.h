#ifndef AE_TEXTURE_TEXTURE_LOADER_H
#define AE_TEXTURE_TEXTURE_LOADER_H

#include <string>
#include <exception>

#include "Error.h"
#include "Image.h"
#include "Texture.h"

namespace ae {
  
namespace texture {    
    
/**
 * \brief The superclass for the texture loaders.
 */
class TextureLoader:public Error {
public:
    /**
     * \brief Constructs a TextureLoader.
     */
    TextureLoader():Error() {
    }
    
    /** */
    virtual ~TextureLoader() {
    }
    
    /**
     * \brief Loads a texture of given identifier.
     * \param id The texture identifier.
     * \return The loaded texture.
     */
    virtual Texture *loadTexture(const std::string &id) = 0;
};

} // namespace

} // namespace

#endif // AE_TEXTURE_TEXTURE_LOADER_H
