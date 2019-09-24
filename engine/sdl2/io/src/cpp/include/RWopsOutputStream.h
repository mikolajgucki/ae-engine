#ifndef AE_SDL2_IO_RW_OPS_OUTPUT_STREAM_H
#define AE_SDL2_IO_RW_OPS_OUTPUT_STREAM_H

#include <string>
#include "SDL_rwops.h"

#include "OutputStream.h"

namespace ae {
    
namespace io {
    
/**
 * \brief Output stream which writes to SDL RWops.
 */
class RWopsOutputStream:public OutputStream {
    /// The name of the file to open.
    std::string filename;
    
    /// The SDL read/write object.
    SDL_RWops *rw;
    
public:
    /**
     * \brief Constructs a RWopsOutputStream.
     * @param filename_ The file name.
     */
    RWopsOutputStream(const std::string &filename_):OutputStream(),
        filename(filename_),rw((SDL_RWops *)0) {
    }
    
    /** */
    virtual ~RWopsOutputStream() {
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

#endif // AE_SDL2_IO_RW_OPS_OUTPUT_STREAM_H