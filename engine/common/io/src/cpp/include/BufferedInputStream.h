#ifndef AE_IO_BUFFERED_INPUT_STREAM_H
#define AE_IO_BUFFERED_INPUT_STREAM_H

#include "InputStream.h"

namespace ae {
    
namespace io {
    
/**
 * \brief Buffered input stream.
 */
class BufferedInputStream:public InputStream {
    /** */
    enum {
        BUFFER_SIZE = 1024
    };
    
    /// The stream from which to read.
    InputStream *input;

    /// The buffer.
    unsigned char *buffer;
    
    /// The buffer size (amount of read data).
    int size;
    
    /// The buffer offset.
    int offset;
    
    /// The end-of-file indicator.
    bool eof;
    
public:
    /** */
    BufferedInputStream(InputStream *input_):InputStream(),input(input_),
        buffer((unsigned char *)0),size(NO_DATA),offset(NO_DATA),eof(false) {
    //
    }
    
    /** */
    virtual ~BufferedInputStream() {
        if (buffer != (unsigned char *)0) {
            delete buffer;
        }
        delete input;
    }
    
    /** */
    virtual bool open();    
    
    /** */
    virtual bool read(int &value);
    
    /** */
    virtual bool close();
};
    
} // namespace
    
} // namespace

#endif // AE_IO_BUFFERED_INPUT_STREAM_H