#include "SDL_helper.h"
#include "RWopsOutputStream.h"

using namespace ae::sdl;

namespace ae {
    
namespace io {
 
/** */
bool RWopsOutputStream::open() {
    rw = SDL_RWFromFile(filename.c_str(),"wb+");
    if (rw == (SDL_RWops *)0) {
        setError(SDL_GetError());
        SDL_ClearError();
        return false;
    }
    
    return true;
}

/** */
bool RWopsOutputStream::write(int value) {
// write
    unsigned char ch = (unsigned char)value;
    size_t size = SDL_RWwrite(rw,&ch,1,1);
    
    if (size <= 0) {
    // check error
        const char *error = getSDLError();
        if (error != (const char *)0) {
            setError(error);
            SDL_ClearError();
            return false;
        }
        
        setError("Failed to write to file");
        return false;        
    }
    
    return true;
}

/** */
bool RWopsOutputStream::close() {
    if (rw != (SDL_RWops *)0) {
        if (SDL_RWclose(rw) < 0) {
            setError(SDL_GetError());
            SDL_ClearError();
            return false;
        }
        
        rw = (SDL_RWops *)0;
    }
        
    return true;    
}
    
} // namespace

} // namespace