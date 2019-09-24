#import <Foundation/Foundation.h>
#include <cstring>
#include "SDL_Log.h"

#include "NSLogger.h"

namespace ae {
    
/** */
const char *NSLogger::getFullMsg(const char *tag,const char *msg) {
    sprintf(fullMsg,"[%s] %s",tag,msg);
    return fullMsg;
}

/** */
void NSLogger::trace(const char *tag,const char *msg) {
    const char *fullMsg = getFullMsg(tag,msg);
    NSLog(@"%s",fullMsg);
}

/** */
void NSLogger::debug(const char *tag,const char *msg) {
    const char *fullMsg = getFullMsg(tag,msg);
    NSLog(@"%s",fullMsg);
}

/** */
void NSLogger::info(const char *tag,const char *msg) {
    const char *fullMsg = getFullMsg(tag,msg);
    NSLog(@"%s",fullMsg);
}

/** */
void NSLogger::warning(const char *tag,const char *msg) {
    const char *fullMsg = getFullMsg(tag,msg);
    NSLog(@"%s",fullMsg);
}

/** */
void NSLogger::error(const char *tag,const char *msg) {
    const char *fullMsg = getFullMsg(tag,msg);
    NSLog(@"%s",fullMsg);
}
    
} // namespace