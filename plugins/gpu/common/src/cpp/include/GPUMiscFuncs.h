#ifndef AE_GPU_MISC_FUNCS_H
#define AE_GPU_MISC_FUNCS_H

#include "Error.h"

namespace ae {
    
namespace gpu {
    
/**
 * \brief The superclass for miscellaneous functions implementations.
 */
class GPUMiscFuncs:public Error {
public:
    /** */
    GPUMiscFuncs():Error() {
    }
    
    /** */
    ~GPUMiscFuncs() {
    }
    
    /**
     * \brief Clears the display.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> on error.     
     */
    virtual bool clear() = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_GPU_MISC_FUNCS_H