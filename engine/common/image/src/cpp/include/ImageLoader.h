#ifndef AE_IMAGE_LOADER_H
#define AE_IMAGE_LOADER_H

#include <string>

#include "Error.h"
#include "Image.h"

namespace ae {

namespace image {
    
/**
 * \brief The superclass for image loaders.
 */
class ImageLoader:public Error {
public:
    /**
     * \brief Loads the image of given identifier.
     * \param id The image identifier.
     * \return The loaded image or null if error occurs.
     */
    virtual Image *loadImage(const std::string &id) = 0;
};

} // namespace

} // namespace

#endif // AE_IMAGE_LOADER_H
