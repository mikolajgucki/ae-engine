#ifndef AE_ENGINE_CFG_H
#define AE_ENGINE_CFG_H

#include "SleepFunc.h"
#include "GetTicksFunc.h"
#include "EngineMutexFactory.h"

namespace ae {

namespace engine {
  
/**
 * \brief Engine configuration.
 */
class EngineCfg {
    /// The sleep function.
    SleepFunc *sleepFunc; 
    
    /// The get ticks functins.
    GetTicksFunc *getTicksFunc;
    
    /// The mutex factory.
    EngineMutexFactory *mutexFactory;
    
public:
    /** */
    EngineCfg(EngineMutexFactory *mutexFactory_):sleepFunc((SleepFunc *)0),
        getTicksFunc((GetTicksFunc *)0),mutexFactory(mutexFactory_) {
    }
        
    /**
     * \brief Sets the sleep function.
     * \param sleepFunc_ The sleep function.
     */
    void setSleepFunc(SleepFunc *sleepFunc_) {
        sleepFunc = sleepFunc_;
    }
    
    /**
     * \brief Gets the sleep function.
     * \return The sleep function.
     */
    SleepFunc *getSleepFunc() {
        return sleepFunc;
    }
    
    /**
     * \brief Sets the get-ticks function.
     * \param getTicksFunc_ Gets the get-ticks function.
     */
    void setGetTicksFunc(GetTicksFunc *getTicksFunc_) {
        getTicksFunc = getTicksFunc_;
    }
    
    /**
     * \brief Gets the get-ticks function.
     * \return The get-ticks function.
     */
    GetTicksFunc *getGetTicksFunc() {
        return getTicksFunc;
    }
    
    /**
     * \brief Gets the mutex factory.
     * \return The mutex factory.
     */
    EngineMutexFactory *getMutexFactory() {
        return mutexFactory;
    }    
};
    
} // namespace
    
} // namespace

#endif //  AE_ENGINE_CFG_H