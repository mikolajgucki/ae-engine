#ifndef AE_DLOG_H
#define AE_DLOG_H

#include <string>
#include "Logger.h"

namespace ae {    
    
/** 
 * \brief A log with non-static methods.
 */
class DLog {
    /** The logger for this log. */
    Logger *logger;
    
public:
    /**
     * \brief Constructs a log.
     * \param logger_ The logger.
     */
    DLog(Logger *logger_):logger(logger_) {
    }
    
    /**
     * \brief Gets the logger.
     * \return The logger.
     */
    Logger *getLogger() const {
        return logger;
    }
    
    /**
     * \brief Logs a message at the trace level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    void trace(const char *tag,const char *msg);    
    
    /**
     * \brief Logs a message at the trace level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    void trace(const char *tag,const std::string &msg);
    
    /**
     * \brief Logs a message at the debug level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    void debug(const char *tag,const char *msg);    
    
    /**
     * \brief Logs a message at the debug level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    void debug(const char *tag,const std::string &msg);
    
    /**
     * \brief Logs a message at the info level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    void info(const char *tag,const char *msg);    
    
    /**
     * \brief Logs a message at the info level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    void info(const char *tag,const std::string &msg);    
    
    /**
     * \brief Logs a message at the warning level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    void warning(const char *tag,const char *msg); 
    
    /**
     * \brief Logs a message at the warning level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    void warning(const char *tag,const std::string &msg);    

    /**
     * \brief Logs a message at the error level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    void error(const char *tag,const char *msg); 
    
    /**
     * \brief Logs a message at the error level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    void error(const char *tag,const std::string &msg);    
};    

} // namespace

#endif // AE_DLOG_H
