#include <memory>

#include "GLTexture.h"
#include "GLTextureFactory.h"

using namespace std;
using namespace ae::image;
using namespace ae::texture;

namespace ae {
    
namespace gles {
    
/** */
Texture *GLTextureFactory::createFromImage(const string &id,
    const Image *image) {
// create
    GLTexture *texture = new (nothrow) GLTexture(id);
    if (texture == (GLTexture *)0) {
        setNoMemoryError();
        return (Texture *)0;
    }
    
// create from image
    if (texture->create(image) == false) {
        setError(texture->getError());
        delete texture;
        return (Texture *)0;     
    }
    
    return texture;
}

/** */
bool GLTextureFactory::deleteTexture(Texture *texture) {
// delete the texture
    GLTexture *glTexture = (GLTexture *)texture;
    if (glTexture->deleteTexture() == false) {
        setError(texture->getError());
        return false;
    }
    delete texture;
    
    return true;
}
    
} // namespace

} // namespace