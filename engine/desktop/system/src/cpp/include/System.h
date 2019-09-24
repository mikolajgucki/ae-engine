#ifndef AE_SYSTEM_TIME_H
#define AE_SYSTEM_TIME_H

#include <string>
#include "Error.h" 

namespace ae {
    
namespace system {
   
/**
 * \brief Provides system utility methods.
 */
class System:public Error {
public:
    /** */
    enum OS {
        /// Windows
        OS_WIN32,
        
        /// Mac OS X
        OS_MACOSX,
        
        /// Unknown OS
        OS_UNKNOWN
    };
    
    /** */    
    System():Error() {
    }
    
    /**
     * \brief Gets the system time in milliseconds since an epoch.
     * \return The time in milliseconds since an epoch.
     */
    unsigned long getSystemTime();
       
    /**
     * \brief Gets the modification time of a file.
     * \param fileName The file name.
     * \param time The result modification time in milliseconds since the epoch.
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> otherwise.
     */
    bool getFileModificationTime(const std::string& fileName,
        unsigned long &time);
    
    /**
     * \brief Sleeps for given milliseconds.
     * \param milliseconds The number of milliseconds to sleep.
     */
    void sleep(int milliseconds);
    
    /**
     * \brief Gets the OS under which the app is running.
     * \return The OS.
     */
    static OS getOS();
};

} // namespace

} // namespace

#endif // AE_SYSTEM_TIME_H