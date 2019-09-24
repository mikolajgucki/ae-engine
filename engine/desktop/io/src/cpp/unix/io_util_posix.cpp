#include "io_util.h"
extern "C" {
#include <sys/stat.h>
}

using namespace std;

namespace ae {

namespace io {
    
/** */
bool checkFileExists(const string& filename) {    
    struct stat filestat;
    int result = stat(filename.c_str(),&filestat);
    if (result != 0) {
        return false;
    }
    
    return S_ISREG(filestat.st_mode) != 0;
}
    
} // namespace    
    
} // namespace
