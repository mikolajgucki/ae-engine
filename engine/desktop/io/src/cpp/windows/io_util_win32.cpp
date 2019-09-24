#include <iostream>
#include "io_util.h"

extern "C" {
#include <windows.h>
#include <shlwapi.h>
}

using namespace std;

namespace ae {

namespace io {
    
/** */
bool checkFileExists(const string& filename) {
    return PathFileExists(filename.c_str()) == TRUE;
}
    
} // namespace    
    
} // namespace
