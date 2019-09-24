#ifndef AE_GPU_QUEUE_H
#define AE_GPU_QUEUE_H

#include "Error.h"

namespace ae {
    
namespace gpu {
  
/**
 * \brief Represents a GPU command queue.
 */
class GPUQueue:public Error {
public:
    /** */
    GPUQueue():Error() {
    }
    
    /** */
    virtual ~GPUQueue() {
    }
    
    /** 
     * \brief Executes all the commands currently in queue and clears the queue.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> on error.
     */
    virtual bool flush() = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_GPU_QUEUE_H