#include <iostream>
#include "Log.h"

namespace ae {
    
/** */
Logger *Log::logger = (Logger *)0;
    
/**  */
void Log::trace(const char *tag,const char *msg) {
    if (logger == (Logger *)0) {
        return;
    }
    logger->trace(tag,msg);
}

/**  */
void Log::trace(const char *tag,const std::string& msg) {
    if (logger == (Logger *)0) {
        return;
    }
    logger->trace(tag,msg.c_str());
}
    
/**  */
void Log::debug(const char *tag,const char *msg) {
    if (logger == (Logger *)0) {
        return;
    }
    logger->debug(tag,msg);
}
    
/**  */
void Log::debug(const char *tag,const std::string& msg) {
    if (logger == (Logger *)0) {
        return;
    }
    logger->debug(tag,msg.c_str());
}

/**  */
void Log::info(const char *tag,const char *msg) {
    if (logger == (Logger *)0) {
        return;
    }
    logger->info(tag,msg);
}
    
/**  */
void Log::info(const char *tag,const std::string& msg) {
    if (logger == (Logger *)0) {
        return;
    }
    logger->info(tag,msg.c_str());
}
    
/**  */
void Log::warning(const char *tag,const char *msg) {
    if (logger == (Logger *)0) {
        return;
    }
    logger->warning(tag,msg);
}
    
/**  */
void Log::warning(const char *tag,const std::string& msg) {
    if (logger == (Logger *)0) {
        return;
    }
    logger->warning(tag,msg.c_str());
}
    
/**  */
void Log::error(const char *tag,const char *msg) {
    if (logger == (Logger *)0) {
        return;
    }
    logger->error(tag,msg);
}
    
/**  */
void Log::error(const char *tag,const std::string& msg) {
    if (logger == (Logger *)0) {
        return;
    }
    logger->error(tag,msg.c_str());
}
    
} // namespace
