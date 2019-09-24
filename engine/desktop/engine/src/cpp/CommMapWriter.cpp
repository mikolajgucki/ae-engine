#include <sstream>
#include "ScopedSDLMutexLock.h"
#include "SDL_helper.h"
#include "CommConsole.h"
#include "CommMapWriter.h"

using namespace std;
using namespace ae::util;
using namespace ae::sdl;

namespace ae {
    
namespace engine {

namespace desktop {

/** */
void CommMapWriter::create() {
    mutex = SDL_CreateMutex();
    if (mutex == (SDL_mutex *)0) {
        ostringstream err;
        err << "Failed to create mutex for CommMapWriter: " << getSDLError();
        CommConsole::print(err.str().c_str());
        setError(getSDLError());
        return;
    }
}
    
/** */
bool CommMapWriter::write(MessagePackMap& map) {
    if (mutex == (SDL_mutex *)0) {
        CommConsole::print("Cannot write message pack: mutex not created");
        return false;
    }
    ScopedSDLMutexLock scopedLock(mutex);
    
    if (map.write(output) == false) {
        CommConsole::print("Failed to write message pack");
        return false;
    }
    if (output->flush() == false) {
        CommConsole::print("Failed to flush the output stream");
        return false;
    }
    
    return true;
}
    
} // namespace

} // namespace

} // namespace