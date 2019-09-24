#ifndef AE_LOG_H
#define AE_LOG_H

#include <string>
#include "Logger.h"

namespace ae {    
    
/** 
 * \brief The default log whic provides static log methods.
 */
class Log {
    /** The logger for this log. */
    static Logger *logger;
    
public:
    /**
     * \brief Initializes the log with logger.
     * \param logger_ The logger.
     */
    static void init(Logger *logger_) {
        logger = logger_;
    }
    
    /**
     * \brief Gets the logger.
     * \return The logger.
     */
    static Logger *getLogger() {
        return logger;
    }
    
    /**
     * \brief Logs a message at the trace level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    static void trace(const char *tag,const char *msg);    
    
    /**
     * \brief Logs a message at the trace level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    static void trace(const char *tag,const std::string &msg);
    
    /**
     * \brief Logs a message at the debug level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    static void debug(const char *tag,const char *msg);    
    
    /**
     * \brief Logs a message at the debug level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    static void debug(const char *tag,const std::string &msg);
    
    /**
     * \brief Logs a message at the info level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    static void info(const char *tag,const char *msg);    
    
    /**
     * \brief Logs a message at the info level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    static void info(const char *tag,const std::string &msg);    
    
    /**
     * \brief Logs a message at the warning level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    static void warning(const char *tag,const char *msg); 
    
    /**
     * \brief Logs a message at the warning level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    static void warning(const char *tag,const std::string &msg);    

    /**
     * \brief Logs a message at the error level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    static void error(const char *tag,const char *msg); 
    
    /**
     * \brief Logs a message at the error level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    static void error(const char *tag,const std::string &msg);    
};    

} // namespace

#endif // AE_LOG_H
