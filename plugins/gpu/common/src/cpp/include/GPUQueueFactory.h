#ifndef AE_GPU_QUEUE_FACTORY_H
#define AE_GPU_QUEUE_FACTORY_H

#include "Error.h"
#include "GPUQueue.h"

namespace ae {
    
namespace gpu {
  
/**
 * \brief Represents a GPU command queue factory.
 */
class GPUQueueFactory:public Error {
public:
    /** */
    GPUQueueFactory():Error() {
    }
    
    /** */
    virtual ~GPUQueueFactory() {
    }
    
    /**
     * \brief Creates a GPU queue.
     * \return The created queue or sets error and returns null if a queue
     *   cannot be created.
     */
    virtual GPUQueue *createGPUQueue() = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_GPU_QUEUE_FACTORY_H