#ifndef AE_RUNNABLE_H
#define AE_RUNNABLE_H

#include "Error.h"

namespace ae {

/**
 * \brief Represents a method which can be passed and executed.
 */
class Runnable:public Error {
public:
    /** */
    virtual ~Runnable() {
    }
    
    /**
     * \brief Runs the method.
     * \return <code>false</code> if the execution fails (sets error),
     *     <code>true</code> otherwise.
     */
    virtual bool run() = 0;
};
    
} // namespace

#endif // AE_RUNNABLE_H