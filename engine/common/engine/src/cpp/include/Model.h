#ifndef AE_MODEL_H
#define AE_MODEL_H

#include "Error.h"

namespace ae {
    
namespace engine {
  
/** 
 * \brief The superclass of all the models.
 */
class Model:public Error {
public:
    /** */
    virtual ~Model() {
    }
    
    /**
     * \brief Draws the model.
     */
    virtual void modelDraw() {
    }
    
    /**
     * \brief Updates the model.
     * \param time The time elapsed since the last frame in milliseconds.
     * \return <code>true</code> if the model state has changed,
     *     <code>false</code> otherwise.
     */
    virtual bool modelUpdate(long time) {
        return false;
    }
};
    
} // namespace
    
} // namespace

#endif // AE_MODEL_H