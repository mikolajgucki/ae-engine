#ifndef AE_IO_INPUT_STREAM_H
#define AE_IO_INPUT_STREAM_H

#include "Error.h"

namespace ae {
  
namespace io {
    
/**
 * \brief Binary abstract input stream.
 */
class InputStream:public Error {
public:
    /** */
    enum {
        /** */
        ERROR = -2,
        
        /** */
        NO_DATA = -1
    };
    
    /**
     * \brief Constructs an InputStream.
     */
    InputStream():Error() {
    }
    
    /** */
    virtual ~InputStream() {
    }
    
    /**
     * \brief Opens the stream.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> on error.
     */
    virtual bool open() = 0;    
    
    /**
     * \brief Reads a byte from the stream.
     * \param value The read byte or <code>NO_DATA</code> if there is nothing
     *     to read.
     * \return <code>true</code> if the read has been successful,
     *     sets error and return <code>false</code> otherwise.
     */
    virtual bool read(int &value) = 0;
    
    /**
     * \brief Reads a number of bytes.
     * \param data The pointer to the output data.
     * \param size The number of bytes to read.
     * \return The number of read bytes or <code>ERROR</code> on error.
     */
    virtual int read(void *data,size_t size);
    
    /**
     * \brief Closes the stream.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> on error.
     */
    virtual bool close() = 0;    
};

} // namespace
    
} // namespace

#endif // AE_IO_INPUT_STREAM_H