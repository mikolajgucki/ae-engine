#ifndef AE_GL_GPU_QUEUE_FACTORY_H
#define AE_GL_GPU_QUEUE_FACTORY_H

#include "GPUQueueFactory.h"

namespace ae {

namespace gles {
  
/**
 * \brief The OpenGL ES implementation of the GPU command queue factory.
 */
class GLGPUQueueFactory:public ::ae::gpu::GPUQueueFactory {
public:
    /** */
    GLGPUQueueFactory():GPUQueueFactory() {
    }
    
    /** */
    virtual ~GLGPUQueueFactory() {
    }
    
    /** */
    virtual ::ae::gpu::GPUQueue *createGPUQueue();
};
    
} // namespace

} // namespace

#endif // AE_GL_GPU_QUEUE_FACTORY_H