#include <memory>

#include "SDL_rwops.h"
#include "RWopsStorage.h"

using namespace std;

namespace ae {

namespace io {
    
/** */
void RWopsStorage::create() {
// input stream provider
    inputStreamProvider = new (nothrow) RWopsInputStreamProvider(dir);
    if (inputStreamProvider == (RWopsInputStreamProvider *)0) {
        setNoMemoryError();
        return;
    }
    
// output stream provider
    outputStreamProvider = new (nothrow) RWopsOutputStreamProvider(dir);
    if (outputStreamProvider == (RWopsOutputStreamProvider *)0) {
        setNoMemoryError();
        return;
    }
}

/** */
RWopsStorage::~RWopsStorage() {
    if (inputStreamProvider != (RWopsInputStreamProvider *)0) {
        delete inputStreamProvider;
    }
    
    if (outputStreamProvider != (RWopsOutputStreamProvider *)0) {
        delete outputStreamProvider;
    }    
}

/** */
const string RWopsStorage::getPath(const std::string &filename) {
    const char separator = '/';
    
    string path;
    path.append(dir);
    path.append(&separator,1);
    path.append(filename);
    
    return path;    
}

/** */
bool RWopsStorage::createFile(const std::string &filename) {
    const string path = getPath(filename);
    
// create
    SDL_RWops *rw = SDL_RWFromFile(path.c_str(),"a+");
    if (rw == (SDL_RWops *)0) {
        setError(SDL_GetError());
        SDL_ClearError();
        return false;
    }    
    
// close
    if (SDL_RWclose(rw) < 0) {
        setError(SDL_GetError());
        SDL_ClearError();
        return false;
    }    
    
    return true;
}
 
/** */
InputStream *RWopsStorage::getInputStream(const std::string &filename) {
    InputStream *stream = inputStreamProvider->getInputStream(filename);
    if (stream == (InputStream *)0) {
        setError(inputStreamProvider->getError());
        return (InputStream *)0;
    }
    
    return stream;    
}

/** */
OutputStream *RWopsStorage::getOutputStream(const std::string &filename) {
    OutputStream *stream = outputStreamProvider->getOutputStream(filename);
    if (stream == (OutputStream *)0) {
        setError(outputStreamProvider->getError());
        return (OutputStream *)0;
    }
    
    return stream;
}

} // namespace
    
} // namespace