#ifndef AE_COMM_LOGGER_H
#define AE_COMM_LOGGER_H

#include "Logger.h"
#include "OutputStream.h"

namespace ae {
    
namespace engine {
    
namespace desktop {

/** 
 * \brief The communication logger.
 */
class CommLogger:public ae::Logger {
    /// The output stream.
    ae::io::OutputStream *output;
    
    /**
     * \brief Logs a message.
     * \param level The log level.
     * \param tag The tag attached to the message.     
     * \param msg The message.
     */
    virtual void log(int level,const char *tag,const char *msg);
    
public:
    /** */
    CommLogger(ae::io::OutputStream *output_):Logger(),output(output_) {        
    }
    
    /** */
    virtual ~CommLogger() {
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
    
    /**
     * \brief Called on no memory condition.
     */
    static void noMemory();
};

} // namespace

} // namespace

} // namespace

#endif // AE_COMM_LOGGER_H 