#include "ae_gles.h"
#include "GLGPUMiscFuncs.h"

namespace ae {

namespace gles {

/** */
bool GLGPUMiscFuncs::clear() {
    glClearColor(0,0,0,1);
    glClear(GL_COLOR_BUFFER_BIT);
    
    return true;
}
    
} // namespace

} // namespace