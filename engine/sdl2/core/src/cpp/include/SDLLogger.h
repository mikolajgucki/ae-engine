#ifndef AE_SDL_LOGGER_H
#define AE_SDL_LOGGER_H

#include "Logger.h"

namespace ae {
  
/**
 * \brief Logs to the SDL log.
 */
class SDLLogger:public Logger {
    /** */
    enum {
        MESSAGE_CAPACITY = 1024
    };
    
public:
    /**
     * \brief Constructs a SDLLogger.
     */
    SDLLogger():Logger() {
    }
    
    /** */
    virtual ~SDLLogger() {
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

#endif // AE_SDL_LOGGER_H
