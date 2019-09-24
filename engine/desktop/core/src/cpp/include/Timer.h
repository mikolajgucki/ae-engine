#ifndef AE_TIMER_H
#define AE_TIMER_H

#include <iostream>
#include <vector>
#include "TimerAlarm.h" 

namespace ae {
    
namespace engine {
  
/**
 * \brief A timer.
 */
class Timer {
    /// The alarms to be fired.
    std::vector<TimerAlarm *> alarms;    
    
public:
    /** */
    Timer():alarms() {
    }
    
    /** */
    virtual ~Timer() {
    }
    
    /**
     * \brief Adds an alarm.
     * \param alarm The alaram.
     */
    void addAlarm(TimerAlarm *alarm) {
        alarms.push_back(alarm);
    }
    
    /**
     * \brief Updates the times.
     * \param time The time elapsed since the last update.
     */
    void tick(int time);
};
    
} // namespace
    
} // namespace

#endif // AE_TIMER_H