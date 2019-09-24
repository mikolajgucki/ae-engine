#ifndef AE_BASE16_OUTPUT_STREAM_H
#define AE_BASE16_OUTPUT_STREAM_H

#include "Base16.h"
#include "OutputStream.h"

namespace ae {
    
namespace io {
  
/**
 * \brief Encodes an output stream using Base16 encoding.
 */
class Base16OutputStream:public OutputStream {
    /// The Base16 encoder.
    Base16 base16;
    
    /// The wrapped output stream.
    OutputStream *output;
    
public:
    /** */
    Base16OutputStream(OutputStream *output_):OutputStream(),base16(),
        output(output_) {
    }
    
    /** */
    virtual ~Base16OutputStream() {
    }
    
    /** */
    virtual bool open();
    
    /** */
    virtual bool write(int value);
    
    /** */
    virtual bool close();
};
    
} // namespace
    
} // namespace

#endif // AE_BASE16_OUTPUT_STREAM_H