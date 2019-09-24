#ifndef AE_STANDARD_OUTPUT_STREAM_H
#define AE_STANDARD_OUTPUT_STREAM_H

#include "OutputStream.h"

namespace ae {
    
namespace io {
  
/**
 * \brief An output stream writing to the standard output.
 */
class StandardOutputStream:public OutputStream {
    /// The class instance.
    static StandardOutputStream *instance;
    
public:
    /** */
    StandardOutputStream():OutputStream() {
    }
    
    /** */
    virtual ~StandardOutputStream() {
    }
    
    /** */
    virtual bool open() {
        return true;
    }
    
    /** */
    virtual bool write(int value);
    
    /** */
    virtual bool close() {
        return true;
    }
    
    /** */
    virtual bool flush();
    
    /**
     * \brief Gets the instance.
     * \return The instance.
     */
    static StandardOutputStream *getInstance();
};
    
} // namespace
    
} // namespace

#endif // AE_STANDARD_OUTPUT_STREAM_H