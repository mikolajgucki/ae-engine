#ifndef AE_IO_OUTPUT_STREAM_H
#define AE_IO_OUTPUT_STREAM_H

#include "Error.h"

namespace ae {
  
namespace io {

/**
 * \brief Binary abstract output stream.
 */
class OutputStream:public Error {
public:  
    /**
     * \brief Constructs an OutputStream.
     */
    OutputStream():Error() {
    }
    
    /** */
    virtual ~OutputStream() {
    }
    
    /**
     * \brief Opens the stream.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> on error.     
     */
    virtual bool open() = 0;
    
    /**
     * \brief Writes a byte to the stream.
     * \param value The byte to write.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> on error.
     */
    virtual bool write(int value) = 0;    
    
    /**
     * \brief Closes the stream.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> on error.
     */
    virtual bool close() = 0;  

    /**
     * \brief Flushes the pending data.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> on error.
     */
    virtual bool flush() {
        return true;
    }
};
    
} // namespace
    
} // namespace
    
#endif // AE_IO_OUTPUT_STREAM_H