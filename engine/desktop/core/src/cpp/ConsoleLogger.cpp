#include <cstring>
#include <sstream>
#include <iostream>

#include "ConsoleLogger.h"

using namespace std;

namespace ae {
 
/** */
void ConsoleLogger::log(const char *level,const char *tag,const char *msg) {
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
    cout << line.str() << endl;    
}
 
/** */
void ConsoleLogger::trace(const char *tag,const char *msg) {
    log("TRCE ",tag,msg);
}
 
/** */
void ConsoleLogger::debug(const char *tag,const char *msg) {
    log("DEBG ",tag,msg);
}
 
/** */
void ConsoleLogger::info(const char *tag,const char *msg) {
    log("INFO ",tag,msg);
}
 
/** */
void ConsoleLogger::warning(const char *tag,const char *msg) {
    log("WARN ",tag,msg);
}
 
/** */
void ConsoleLogger::error(const char *tag,const char *msg) {
    log("EROR ",tag,msg);
}

} // namespace
