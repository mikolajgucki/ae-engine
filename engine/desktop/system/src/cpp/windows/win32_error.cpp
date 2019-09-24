#include "win32_error.h"
extern "C" {
#include <windows.h>
}

using namespace std;

namespace ae {

namespace system {

/** */
bool getLastErrorAsString(string &str) {
    DWORD error = GetLastError();
    if (error == 0) {
        return false;
    }

// get the message
    LPSTR buffer = (LPSTR)0;
    FormatMessageA(
        FORMAT_MESSAGE_ALLOCATE_BUFFER |
        FORMAT_MESSAGE_FROM_SYSTEM |
        FORMAT_MESSAGE_IGNORE_INSERTS,
        NULL,error,MAKELANGID(LANG_NEUTRAL,SUBLANG_DEFAULT),
        (LPSTR)&buffer,0,NULL);

// append
    str.append(buffer);

// free the buffer
    LocalFree(buffer);

    return true;    
}

/** */
bool getLastErrorAsString(Error *error) {
    string str;
    if (getLastErrorAsString(str) == false) {
        return false;
    }
    
    error->setError(str);
    return true;
}

} // namespace

} // namespace