#ifndef AE_DEBUG_LOG_H
#define AE_DEBUG_LOG_H

#include <string>
#include "Logger.h"

namespace ae {
    
namespace engine {
    
namespace desktop {
  
/**
 * \brief Debug log.
 */
class DebugLog {
    /// The logger to which to log.
    static Logger *logger;
    
public:
    /** */
    static void init(Logger *logger_) {
        logger = logger_;
    }
    
    /** */
    static void trace(const char *tag,const char *msg) {
        if (logger != (Logger *)0) {
            logger->trace(tag,msg);
        }
    }
    
    /** */
    static void trace(const char *tag,const std::string &msg) {
        trace(tag,msg.c_str());
    }
    
    /** */
    static void debug(const char *tag,const char *msg) {
        if (logger != (Logger *)0) {
            logger->debug(tag,msg);
        }
    }
    
    /** */
    static void debug(const char *tag,const std::string &msg) {
        debug(tag,msg.c_str());
    }
    
    /** */
    static void info(const char *tag,const char *msg) {
        if (logger != (Logger *)0) {
            logger->info(tag,msg);
        }
    }
    
    /** */
    static void info(const char *tag,const std::string &msg) {
        info(tag,msg.c_str());
    }
    
    /** */
    static void error(const char *tag,const char *msg) {
        if (logger != (Logger *)0) {
            logger->error(tag,msg);
        }
    }
    
    /** */
    static void error(const char *tag,const std::string &msg) {
        error(tag,msg.c_str());
    }
};
    
} // namespace

} // namespace

} // namespace

#endif // AE_DEBUG_LOG_H    