#include <memory>
#include "GLGPUQueue.h"
#include "GLGPUQueueFactory.h"

using namespace std;
using namespace ae::gpu;

namespace ae {
    
namespace gles {
    
/** */
GPUQueue *GLGPUQueueFactory::createGPUQueue() {
    GPUQueue *queue = new (nothrow) GLGPUQueue();
    if (queue == (GPUQueue *)0) {
        setNoMemoryError();
        return (GPUQueue *)0;
    }
    
    return queue;
}

} // namespace
    
} // namespace