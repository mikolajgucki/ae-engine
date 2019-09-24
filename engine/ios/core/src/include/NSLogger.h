#ifndef AE_NS_LOGGER_H
#define AE_NS_LOGGER_H

#include "Logger.h"

namespace ae {
    
/**
 * \brief Logs to the iOS log.
 */
class NSLogger:public Logger {
    /** */
    enum {
        MESSAGE_CAPACITY = 1024
    };
    
    /// The full message with the tag
    char fullMsg[MESSAGE_CAPACITY];
    
    /**
     * \brief Gets the full message as compound of the tag and the message.
     * \param tag The tag.
     * \param msg The message.
     */
    const char *getFullMsg(const char *tag,const char *msg);
    
public:
    /**
     * \brief Constructs a NSLogger.
     */
    NSLogger():Logger(),fullMsg() {
    }
    
    /** */
    virtual ~NSLogger() {
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
     * \param tag The tag attached to the message.
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

#endif // AE_NS_LOGGER_H