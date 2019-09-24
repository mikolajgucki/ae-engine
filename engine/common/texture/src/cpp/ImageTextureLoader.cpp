#include <memory>
#include "ImageTextureLoader.h"

using namespace std;
using namespace ae::image;

namespace ae {
    
namespace texture {
   
/** */
Texture *ImageTextureLoader::loadTexture(const std::string &id) {
// load image
    Image *image = imageLoader->loadImage(id);
    if (image == (Image *)0) {
        setError(imageLoader->getError());
        return (Texture *)0;
    }
    
// create texture
    Texture *texture = textureFactory->createFromImage(id,image);
    if (texture == (Texture *)0) {
        delete image;
        ostringstream err;
        err << "Failed to create texture from image " << id << ": " <<
            textureFactory->getError();
        setError(err.str());
        return (Texture *)0;
    }
    
// delete the image
    delete image;
    
    return texture;
}
    
} // namespace

} // namespace