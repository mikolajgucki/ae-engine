#ifndef AE_CONSOLE_LOGGER_H
#define AE_CONSOLE_LOGGER_H

#include "Logger.h"

namespace ae {

/** 
 * \brief Logs to the standard output.
 */
class ConsoleLogger:public Logger {
    /** */
    enum {
        /// The default maximal length of a tag.
        DEFAULT_TAG_MAX_LENGTH = 16
    };
    
    /// The maximal length of a tag.
    int tagMaxLength;
    
    /**
     * \brief Logs a message.
     * \param level The log level.
     * \param tag The tag attached to the message.     
     * \param msg The message.
     */
    virtual void log(const char *level,const char *tag,const char *msg);
    
public:
    /**
     * \brief Constructs a ConsoleLogger.
     */
    ConsoleLogger():Logger(),tagMaxLength(DEFAULT_TAG_MAX_LENGTH) {        
    }
    
    /** */
    virtual ~ConsoleLogger() {
    }    
    
    /**
     * \brief Logs a message at the trace level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    virtual void trace(const char *tag,const char *msg);
    
    /**
     * \brief Logs a message at the debug level.
     * \param tag The tag attached to the message.     
     * \param msg The message.
     */
    virtual void debug(const char *tag,const char *msg);
    
    /**
     * \brief Logs a message at the info level.
     * \param tag The tag attached to the message.     
     * \param msg The message.
     */
    virtual void info(const char *tag,const char *msg);
    
    /**
     * \brief Logs a message at the warning level.
     * \param msg The message.
     */
    virtual void warning(const char *tag,const char *msg);
    
    /**
     * \brief Logs a message at the error level.
     * \param tag The tag attached to the message.     
     * \param msg The message.
     */
    virtual void error(const char *tag,const char *msg);    
};

} // namespace

#endif // AE_CONSOLE_LOGGER_H
