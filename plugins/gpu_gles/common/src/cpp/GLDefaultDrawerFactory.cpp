#include <memory>

#include "GLDefaultDrawer.h"
#include "GLDefaultDrawerFactory.h"

using namespace std;
using namespace ae::gpu;

namespace ae {

namespace gles {

/** */    
DefaultDrawer *GLDefaultDrawerFactory::createDefaultDrawer(
    DefaultDrawerFeatures features,int capacity) {
// shader
    GLSLProgram *glslProgram = defaultDrawerShader.getGLSLProgram(features);
    if (glslProgram == (GLSLProgram *)0) {
        setError(defaultDrawerShader.getError());
        return (DefaultDrawer *)0;;
    }

// drawer
    GLDefaultDrawer *drawer =
        new (nothrow) GLDefaultDrawer(features,capacity,glslProgram);
    if (drawer == (GLDefaultDrawer *)0) {
        delete glslProgram;
        setNoMemoryError();
        return (DefaultDrawer *)0;
    }
    if (drawer->chkError()) {
        setError(drawer->getError());
        delete drawer;
        return (DefaultDrawer *)0;
    }
    
    return drawer;
}
    
} // namespace

} // namespace