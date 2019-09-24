#ifndef AE_SDL2_IO_RW_OPS_INPUT_STREAM_H
#define AE_SDL2_IO_RW_OPS_INPUT_STREAM_H

#include <string>
#include "SDL_rwops.h"

#include "InputStream.h"

namespace ae {
    
namespace io {
  
/**
 * \brief Input stream which reads from SDL RWops.
 */
class RWopsInputStream:public InputStream {
    /// The name of the file to open,.
    std::string filename;   
    
    /// The SDL read/write object.
    SDL_RWops *rw;
    
public:
    /**
     * \brief Constructs a RWopsInputStream.
     * \param filename_ The file name.
     */
    RWopsInputStream(const std::string &filename_):InputStream(),
        filename(filename_),rw((SDL_RWops *)0) {
    }
    
    /** */
    virtual ~RWopsInputStream() {
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

#endif // AE_SDL2_IO_RW_OPS_INPUT_STREAM_H