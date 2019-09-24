#ifndef AE_RUNNABLE_QUEUE_H
#define AE_RUNNABLE_QUEUE_H

#include <vector>
#include "Runnable.h"

namespace ae {
  
/**
 * \brief Holds runnables and runs then in the order in which they were added.
 */
class RunnableQueue:public Runnable {
    /** */
    std::vector<Runnable *> runnables;
    
    /**
     * \brief Deletets runnables in a vector.
     * \param toDelete The runnables to delete.
     */
    void deleteRunnables(std::vector<Runnable *> toDelete);
    
public:
    /** */
    RunnableQueue():Runnable(),runnables() {
    }
    
    /** */
    virtual ~RunnableQueue() {
        deleteRunnables(runnables);
    }
    
    /**
     * \brief Adds a runnable to the queue.
     * \param runnable The runnable.
     */
    void add(Runnable *runnable);
    
    /** */
    virtual bool run();
};
    
} // namespace

#endif // AE_RUNNABLE_QUEUE_H