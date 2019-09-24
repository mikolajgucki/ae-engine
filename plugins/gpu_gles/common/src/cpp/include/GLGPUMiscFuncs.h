#ifndef AE_GL_GPU_MISC_FUNCS_H
#define AE_GL_GPU_MISC_FUNCS_H

#include "GPUMiscFuncs.h"

namespace ae {
    
namespace gles {
  
/**
 * \brief The OpenGL ES implementation of the GPU miscellaneous functions.
 */
class GLGPUMiscFuncs:public ::ae::gpu::GPUMiscFuncs {
public:
    /** */
    GLGPUMiscFuncs():GPUMiscFuncs() {
    }
    
    /** */
    virtual ~GLGPUMiscFuncs() {
    }
    
    /** */
    virtual bool clear();
};
    
} // namespace
    
} // namespace

#endif // AE_GL_GPU_MISC_FUNCS_H