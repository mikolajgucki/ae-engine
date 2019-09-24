#ifndef AE_STANDARD_INPUT_STREAM_H
#define AE_STANDARD_INPUT_STREAM_H

#include "InputStream.h"

namespace ae {
    
namespace io {
  
/**
 * \brief An input stream reading from the standard input.
 */
class StandardInputStream:public InputStream {
    /// The class instance.
    static StandardInputStream *instance;
    
public:
    /** */
    StandardInputStream():InputStream() {
    }
    
    /** */
    virtual ~StandardInputStream() {
    }
    
    /** */
    virtual bool open() {
        return true;
    }
    
    /** */
    virtual bool read(int &value);
    
    /** */
    virtual bool close() {
        return true;
    }
    
    /**
     * \brief Gets the instance.
     * \return The instance.
     */
    static StandardInputStream *getInstance();
};
    
} // namespace
    
} // namespace

#endif // AE_STANDARD_INPUT_STREAM_H