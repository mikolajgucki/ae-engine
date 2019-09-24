#ifndef AE_TIMER_ALARM_H
#define AE_TIMER_ALARM_H

namespace ae {
    
namespace engine {
  
// Forward declaration.
class Timer;
    
/**
 * \brief A timer alarm.
 */
class TimerAlarm {
    // Let timer update the time.
    friend class Timer;
    
    /// The time remaining to the alarm.
    int time;
    
    /**
     * \brief Sets the time remaining to the alarm.
     * \param time_ The time remaining to the alarm.
     */    
    void setTime(int time_) {
        time = time_;
    }
    
protected:
    /**
     * \brief Construcs a TimerAlarm.
     * \param time_ The time remaining to the alarm.
     */
    TimerAlarm(int time_):time(time_) {
    }
    
public:
    /** */
    virtual ~TimerAlarm() {
    }
    
    /**
     * \brief Gets the time remaining to the alarm.
     * \return The time remaining to the alarm.
     */
    int getTime() const {
        return time;
    }
    
    /**
     * \brief Fired on alarm.
     */
    virtual void fire() = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_TIMER_ALARM_H