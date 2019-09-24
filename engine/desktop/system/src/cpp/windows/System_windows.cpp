extern "C" {
#include <windows.h>
}
#include <sstream>
#include "System.h"

using namespace std;

namespace ae {

namespace system {
 
/**
 * \brief Converts a file time to epoch time in millseconds.
 * \param filetime The file time.
 * \return The time in milliseconds since epoch.
 */
static unsigned long fileTimeToEpoch(const FILETIME &filetime) {
// the time in 100-nanosecond intervals since the epoch
    LONGLONG now = (LONGLONG)filetime.dwLowDateTime +
        ((LONGLONG)(filetime.dwHighDateTime) << 32);
        
// convert 100-nanosecond intervals to milliseconds        
    return (unsigned long)(now / 10000);
}
    
/** */
unsigned long System::getSystemTime() {
    FILETIME systime;
    GetSystemTimeAsFileTime(&systime);
    return fileTimeToEpoch(systime);
}

/** */
bool System::getFileModificationTime(const string& fileName,
    unsigned long &time) {
//
    HANDLE handle = CreateFile(fileName.c_str(),
        GENERIC_READ, // desired access
        FILE_SHARE_READ|FILE_SHARE_WRITE, // shared mode
        (LPSECURITY_ATTRIBUTES)0, // security attributes
        OPEN_EXISTING, // creation disposition
        FILE_ATTRIBUTE_NORMAL, // flags and attributes
        (HANDLE)0); // template file
    if (handle == INVALID_HANDLE_VALUE) {
        ostringstream msg;
        msg << "Failed to get modification time of file " << fileName;
        setError(msg.str());
        return false;
    }
        
    FILETIME lastWriteTime;    
    if (GetFileTime(handle,(LPFILETIME)0,(LPFILETIME)0,&lastWriteTime) == 0) {
        ostringstream msg;
        msg << "Failed to get modification time of file " << fileName;
        setError(msg.str());
        return false;
    }
    time = fileTimeToEpoch(lastWriteTime);
    
    CloseHandle(handle);
    return true;
}

/** */
void System::sleep(int milliseconds) {
    Sleep(milliseconds);
}

} // namespace

} // namespace