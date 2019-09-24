#include <iostream>
#include "Timer.h"

using namespace std;

namespace ae {
    
namespace engine {

/** */
void Timer::tick(int time) {
    vector<TimerAlarm *>::iterator itr;
    for (itr = alarms.begin(); itr != alarms.end();) {
        TimerAlarm *alarm = *itr;
        
    // update time
        int alarmTime = alarm->getTime();
        alarmTime -= time;
        
    // fire
        if (alarmTime <= 0) {
            itr = alarms.erase(itr);
            alarm->fire();
        }
        else {
            alarm->setTime(alarmTime);
            ++itr;
        }            
    }
}
    
} // namespace
    
} // namespace