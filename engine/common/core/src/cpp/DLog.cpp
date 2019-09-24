#include <iostream>
#include "DLog.h"

namespace ae {
    
/**  */
void DLog::trace(const char *tag,const char *msg) {
    logger->trace(tag,msg);
}

/**  */
void DLog::trace(const char *tag,const std::string& msg) {
    logger->trace(tag,msg.c_str());
}
    
/**  */
void DLog::debug(const char *tag,const char *msg) {
    if (logger == (Logger *)0) {
        return;
    }
    logger->debug(tag,msg);
}
    
/**  */
void DLog::debug(const char *tag,const std::string& msg) {
    if (logger == (Logger *)0) {
        return;
    }
    logger->debug(tag,msg.c_str());
}

/**  */
void DLog::info(const char *tag,const char *msg) {
    if (logger == (Logger *)0) {
        return;
    }
    logger->info(tag,msg);
}
    
/**  */
void DLog::info(const char *tag,const std::string& msg) {
    if (logger == (Logger *)0) {
        return;
    }
    logger->info(tag,msg.c_str());
}
    
/**  */
void DLog::warning(const char *tag,const char *msg) {
    if (logger == (Logger *)0) {
        return;
    }
    logger->warning(tag,msg);
}
    
/**  */
void DLog::warning(const char *tag,const std::string& msg) {
    if (logger == (Logger *)0) {
        return;
    }
    logger->warning(tag,msg.c_str());
}
    
/**  */
void DLog::error(const char *tag,const char *msg) {
    if (logger == (Logger *)0) {
        return;
    }
    logger->error(tag,msg);
}
    
/**  */
void DLog::error(const char *tag,const std::string& msg) {
    if (logger == (Logger *)0) {
        return;
    }
    logger->error(tag,msg.c_str());
}
    
} // namespace
