#include "RunnableQueue.h"

using namespace std;

namespace ae {
    
/** */
void RunnableQueue::deleteRunnables(vector<Runnable *> toDelete) {
// for each runnable
    vector<Runnable *>::iterator itr;
    for (itr = toDelete.begin(); itr != toDelete.end(); ++itr) {
        Runnable *runnable = (*itr);
        delete runnable;
    }
}

/** */
void RunnableQueue::add(Runnable *runnable) {
    runnables.push_back(runnable);
}

/** */
bool RunnableQueue::run() {
// make a copy so that runnables can be added to the queue from the runnables
// we are about to run
    vector<Runnable *> copy;
    
// for each runnable
    vector<Runnable *>::iterator copyItr;
    for (copyItr = runnables.begin(); copyItr != runnables.end(); ++copyItr) {
        Runnable *runnable = (*copyItr);
        copy.push_back(runnable);
    }
    runnables.clear();

// run all the runnables
    vector<Runnable *>::iterator runItr;
    for (runItr = copy.begin(); runItr != copy.end(); ++runItr) {
        Runnable *runnable = (*runItr);
    // run the runnable
        if (runnable->run() == false) {
            setError(runnable->getError());
            deleteRunnables(copy);
            return false;
        }
    }
    
    deleteRunnables(copy);
    return true;
}
    
} // namespace