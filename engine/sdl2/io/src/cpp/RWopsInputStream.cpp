#include "SDL_helper.h"
#include "RWopsInputStream.h"

using namespace ae::sdl;

namespace ae {
    
namespace io {
    
/** */
bool RWopsInputStream::open() {
    rw = SDL_RWFromFile(filename.c_str(),"rb");
    if (rw == (SDL_RWops *)0) {
        setError(SDL_GetError());
        SDL_ClearError();
        return false;
    }
    
    return true;
}

/** */
bool RWopsInputStream::read(int &value) {
// read
    unsigned char ch;
    size_t readSize = SDL_RWread(rw,&ch,1,1);
    
    if (readSize == 0) {
    // check error
        const char *error = getSDLError();
        if (error != (const char *)0) {
            setError(error);
            SDL_ClearError();
            return false;
        }
        
    // end of file
        value = NO_DATA;
        return true;
    }
        
    value = (int)ch;
    return true;
}

/** */
int RWopsInputStream::read(void *data,size_t size) {
    size_t readSize = SDL_RWread(rw,data,1,size);
    if (readSize == 0) {
    // check error
        const char *error = getSDLError();
        if (error != (const char *)0) {
            setError(error);
            SDL_ClearError();
            return ERROR;
        }
        
    // end of file
        return 0;
    }
    
    return (int)readSize;
}

/** */
bool RWopsInputStream::close() {
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