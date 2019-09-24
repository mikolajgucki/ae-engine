extern "C" {
#include <sys/time.h>
#include <sys/stat.h>
#include <unistd.h>
}
#include <sstream>
#include "System.h"

using namespace std;

namespace ae {
    
namespace system {
 
/** */
unsigned long System::getSystemTime() {
    struct timeval systime;
    gettimeofday(&systime,(struct timezone *)0);

    return (unsigned long)(systime.tv_sec) * 1000 +
        (unsigned long)(systime.tv_usec) / 1000;
}

/** */
bool System::getFileModificationTime(const string& fileName,
    unsigned long &time) {
//
    struct stat fileStat;
    if (lstat(fileName.c_str(),&fileStat) != 0) {
        ostringstream msg;
        msg << "Failed to get modification time of file " << fileName;
        setError(msg.str());        
        return false;
    }
    
    time = (unsigned long)fileStat.st_mtime * 1000;
    return true;
}

/** */
void System::sleep(int milliseconds) {
    usleep((useconds_t)(milliseconds * 1000));
}
    
} // namespace

} // namespace