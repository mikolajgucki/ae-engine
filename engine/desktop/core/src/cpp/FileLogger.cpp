#include <cstring>
#include <sstream>
#include <fstream>
#include <iostream>

#include "FileLogger.h"

using namespace std;

namespace ae {
 
/** */
bool FileLogger::init() {
    try {
        stream.open(filename,ios::trunc);
    } catch (ios_base::failure fail) {
        setError(fail.what());
        return false;
    }
    
    return true;
}

/** */
void FileLogger::log(const char *level,const char *tag,const char *msg) {
    ostringstream line;
    
// level
    line << level; 
    
// tag    
    line << "["; 
    int tagLength = strlen(tag);
    if (tagLength > tagMaxLength) {
        line << string(tag,0,tagMaxLength);
    }
    else {
        line << tag;
        int count = tagMaxLength - strlen(tag);
        while (count > 0) {
            line << " ";
            count--;
        }
    }
    line << "] ";
    
// message
    line << msg;
    
// print
    stream << line.str() << endl;    
}
 
/** */
void FileLogger::trace(const char *tag,const char *msg) {
    log("TRCE ",tag,msg);
}
 
/** */
void FileLogger::debug(const char *tag,const char *msg) {
    log("DEBG ",tag,msg);
}
 
/** */
void FileLogger::info(const char *tag,const char *msg) {
    log("INFO ",tag,msg);
}
 
/** */
void FileLogger::warning(const char *tag,const char *msg) {
    log("WARN ",tag,msg);
}
 
/** */
void FileLogger::error(const char *tag,const char *msg) {
    log("EROR ",tag,msg);
}

} // namespace
