#ifndef AE_GL_GPU_QUEUE_COMMAND_H
#define AE_GL_GPU_QUEUE_COMMAND_H

#include "Error.h"

namespace ae {
    
namespace gles {
    
/**
 * \brief The superclass for commands executed in the GL GPU command queue.
 */
class GLGPUQueueCommand:public Error {
public:
    /** */
    GLGPUQueueCommand():Error() {
    }
    
    /** */
    virtual ~GLGPUQueueCommand() {
    }
    
    /**
     * \brief Executes the command.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> on error.
     */
    virtual bool execute() = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_GL_GPU_QUEUE_COMMAND_H