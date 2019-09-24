#ifndef AE_ENGINE_REQUEST_H
#define AE_ENGINE_REQUEST_H

#include "Runnable.h"

namespace ae {
    
namespace engine {
  
/**
 * \brief Engine requests are run between frames (after model update).
 */
class EngineRequest:public Runnable {
    /** Indicates if to remove the request from the engine after it has
        been run. */
    bool remove;
    
protected:
    /**
     * \brief Constructs an EngineRequest removed from the engine after is has
     *     been run.
     */
    EngineRequest():Runnable(),remove(true) {
    }
    
    /**
     * \brief Constructs an EngineRequest.
     * \param remove_ Indicates if to remove the request from the engine
     *     after it has been run.
     */
    EngineRequest(bool remove_):Runnable(),remove(remove_) {
    }
    
public:
    /**
     * \brief Indicates if to remove the request from the engine after it has
     *     been run.
     * \return <code>true</code> if to remove, <code>false</code> otherwise.
     */
    virtual bool doRemoveRequest() const {
        return remove;
    }
};
    
} // namespace
    
} // namespace

#endif // AE_ENGINE_REQUEST_H