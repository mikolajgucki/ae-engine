#ifndef AE_LOGGER_H
#define AE_LOGGER_H

namespace ae {

/** 
 * \brief Declares the low-level logger functions which write to the log.
 */
class Logger {
public:    
    /** */
    virtual ~Logger() {
    }    
    
    /**
     * \brief Logs a message at the trace level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    virtual void trace(const char *tag,const char *msg) = 0;
    
    /**
     * \brief Logs a message at the debug level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    virtual void debug(const char *tag,const char *msg) = 0;
    
    /**
     * \brief Logs a message at the info level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    virtual void info(const char *tag,const char *msg) = 0;
    
    /**
     * \brief Logs a message at the warning level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    virtual void warning(const char *tag,const char *msg) = 0;
    
    /**
     * \brief Logs a message at the error level.
     * \param tag The tag attached to the message.
     * \param msg The message.
     */
    virtual void error(const char *tag,const char *msg) = 0;
}; 

} // namespace

#endif // AE_LOGGER_H
