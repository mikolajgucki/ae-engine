#include "Image.h"
#include "ae_GLES.h"
#include "GLTexture.h"

using namespace ae::image;

namespace ae {
    
namespace gles {
 
/** */
bool GLTexture::chkGLError() {
    if (glError.chkGLError() == true) {
        setError(glError.getError());
        return true;
    }
    
    return false;
}

    
/** */
bool GLTexture::create(const Image *image) {
    setSize(image->getWidth(),image->getHeight());
    
// generate name
    glGenTextures(1,&glName);
    if (chkGLError()) {
        return false;
    }
    
// bind    
    glBindTexture(GL_TEXTURE_2D,glName);
    if (chkGLError()) {
        return false;
    }
    
// parameters
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
    if (chkGLError()) {
        return false;
    }    
    
// image
    glTexImage2D(GL_TEXTURE_2D, // target
        0, // level
        GL_RGBA, // internal format
        image->getWidth(),image->getHeight(),
        0, // border
        GL_RGBA, // format
        GL_UNSIGNED_BYTE, // type
        image->getData());
    if (chkGLError()) {
        return false;
    }

    return true;
}

/** */
bool GLTexture::deleteTexture() {
    if (glName == NO_GL_NAME) {
        setError("Texture not created");
        return false;
    }    
    
// delete
    glDeleteTextures(1,&glName);
    if (chkGLError()) {
        return false;
    }
    glName = NO_GL_NAME;    
    
    return true;
}
    
} // namespace
    
} // namespace