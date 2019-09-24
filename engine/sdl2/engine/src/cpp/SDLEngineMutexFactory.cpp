#include <memory>
#include "SDLEngineMutex.h"
#include "SDLEngineMutexFactory.h"

using namespace std;

namespace ae {
    
namespace engine {
      
/** */
EngineMutex* SDLEngineMutexFactory::createMutex() {
    SDLEngineMutex *sdlEngineMutex = new (nothrow) SDLEngineMutex();
    if (sdlEngineMutex == (SDLEngineMutex *)0) {
        setError("Failed to create SDL engine mutex. No memory.");
        return (EngineMutex *)0;
    }
    
    if (sdlEngineMutex->chkError()) {
        setError(sdlEngineMutex->getError());
        delete sdlEngineMutex;
        return (EngineMutex *)0;
    }
    
    return sdlEngineMutex;
}
    
} // namespace
    
} // namespace