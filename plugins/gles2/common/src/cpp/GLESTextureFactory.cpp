#include <memory>
#include "GLESTexture.h"
#include "GLESTextureFactory.h"

using namespace std;
using namespace ae::image;
using namespace ae::texture;

namespace ae {
    
namespace gles2 {
    
/** */
Texture *GLESTextureFactory::createFromImage(const string &id,
    const Image *image) {
// create
    GLESTexture *texture = new (nothrow) GLESTexture(id);
    if (texture == (GLESTexture *)0) {
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
bool GLESTextureFactory::deleteTexture(Texture *texture) {
// delete the texture
    GLESTexture *glesTexture = (GLESTexture *)texture;
    if (glesTexture->deleteTexture() == false) {
        setError(texture->getError());
        return false;
    }
    delete texture;
    
    return true;
}
    
} // namespace

} // namespace