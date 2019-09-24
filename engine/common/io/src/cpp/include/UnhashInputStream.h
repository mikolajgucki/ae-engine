#ifndef AE_UNHASH_INPUT_STREAM_H
#define AE_UNHASH_INPUT_STREAM_H

#include "InputStream.h"

namespace ae {
    
namespace io {
  
/**
 * \brief Unhashes data from a wrapped input stream.
 */
class UnhashInputStream:public InputStream {
    /// The wrapped input stream.
    InputStream *input;
    
    /// The next value to read when reading.
    int nextValue;
    
    /// End-of-file indicator.
    bool eof;
    
    /** */
    unsigned char unhash(unsigned char input) {
        return ((input & 0x0f) << 4) + ((input & 0xf0) >> 4); 
    }
    
public:
    /** */
    UnhashInputStream(InputStream *input_):InputStream(),input(input_),
        nextValue(NO_DATA),eof(false) {
    }
    
    /** */
    virtual ~UnhashInputStream() {
        delete input;
    }
    
    /** */
    virtual bool open();    
    
    /** */
    virtual bool read(int &value);
    
    /** */
    virtual int read(void *data,size_t size);
    
    /** */
    virtual bool close();        
};
    
} // namespace
    
} // namespace

#endif // AE_UNHASH_INPUT_STREAM_H