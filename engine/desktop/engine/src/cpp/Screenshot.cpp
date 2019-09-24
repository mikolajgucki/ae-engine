#include "ae_platform.h"
#ifdef AE_MACOSX
#include <OpenGL/gl.h>
#else
#include <GL/gl.h>
#endif
#include "screenshot.h"

namespace ae {
    
namespace engine {
    
namespace desktop {
    
/** */
void Screenshot::setSize(int width_,int height_) {
    if (width == width_ && height == height_) {
        return;
    }
    
// delete old data
    if (data != (char *)0) {
        delete[] data;
        data = (char *)0;
    }

// size
    width = width_;
    height = height_;
    
// RGB data
    data = new char[width * height * 3];
}
    
/** */
bool Screenshot::take(const std::string& filename) {
// read pixels
    glReadPixels(0,0,width,height,GL_RGB,GL_UNSIGNED_BYTE,data);
    
// write
    pngWriter.write(filename,width,height,data,true);
    if (pngWriter.chkError() == true) {
        setError(pngWriter);
        return false;
    }
    
    return true;
}
    
} // namespace

} // namespace
    
} // namespace