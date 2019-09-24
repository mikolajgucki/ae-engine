#ifndef AE_GL_GPU_QUEUE_H
#define AE_GL_GPU_QUEUE_H

#include <vector>
#include "GLGPUQueueCommand.h"
#include "GPUQueue.h"

namespace ae {

namespace gles {
  
/**
 * \brief The OpenGL ES implementation of the GPU command queue.
 */
class GLGPUQueue:public ::ae::gpu::GPUQueue {
    /// The commands in the queue.
    std::vector<GLGPUQueueCommand *> commands;
    
public:
    /** */
    GLGPUQueue():GPUQueue(),commands() {
    }
    
    /** */
    virtual ~GLGPUQueue() {
    }
    
    /**
     * \brief Adds a command to the queue. The command will be deleted when
     *   executed and removed from the queue.
     * \param command The command.
     */
    void add(GLGPUQueueCommand *command);
    
    /** */
    virtual bool flush();
};
    
} // namespace

} // namespace

#endif // AE_GL_GPU_QUEUE_H