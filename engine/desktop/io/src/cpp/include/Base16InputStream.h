#ifndef AE_BASE16_INPUT_STREAM_H
#define AE_BASE16_INPUT_STREAM_H

#include "Base16.h"
#include "InputStream.h"

namespace ae {
    
namespace io {
  
/**
 * \brief An input stream decoding using Base16 encoding.
 */
class Base16InputStream:public InputStream {
    /// The Base16 encoder.
    Base16 base16;
    
    /// The wrapped input stream.
    InputStream *input;
    
public:
    /** */
    Base16InputStream(InputStream *input_):InputStream(),base16(),
        input(input_) {
    }
    
    /** */
    virtual ~Base16InputStream() {
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

#endif // AE_BASE16_INPUT_STREAM_H