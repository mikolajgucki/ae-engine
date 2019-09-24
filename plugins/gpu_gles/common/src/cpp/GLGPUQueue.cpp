#include "GLGPUQueue.h"

using namespace std;

namespace ae {

namespace gles {

/** */
void GLGPUQueue::add(GLGPUQueueCommand *command) {
    commands.push_back(command);
}
 
/** */
bool GLGPUQueue::flush() {
    vector<GLGPUQueueCommand *>::iterator itr;
// for each command
    for (itr = commands.begin(); itr != commands.end();) {
        GLGPUQueueCommand *command = *itr;
        
    // remove from the queue
        itr = commands.erase(itr);
        
    // execute
        if (command->execute() == false) {
            setError(command->getError());
            delete command;
            return false;
        }
        
    // delete
        delete command;
    }
    
    return true;
}

} // namespace

} // namespace