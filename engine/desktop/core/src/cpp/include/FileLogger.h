#ifndef AE_FILE_LOGGER_H
#define AE_FILE_LOGGER_H

#include <fstream>

#include "Error.h"
#include "Logger.h"

namespace ae {

/** 
 * \brief Logs to a file.
 */
class FileLogger:public Logger,public Error {
    /** */
    enum {
        /// The default maximal length of a tag.
        DEFAULT_TAG_MAX_LENGTH = 16
    };
    
    /// The name of the file to which to log.
    const char *filename;
    
    /// The maximal length of a tag.
    int tagMaxLength;
    
    /// The output stream.
    std::ofstream stream;
    
    /**
     * \brief Logs a message.
     * \param level The log level.
     * \param tag The tag attached to the message.     
     * \param msg The message.
     */
    virtual void log(const char *level,const char *tag,const char *msg);
    
public:
    /**
     * \brief Constructs a FileLogger.
     */
    FileLogger(const char *filename_):Logger(),Error(),filename(filename_),
        tagMaxLength(DEFAULT_TAG_MAX_LENGTH),stream() {        
    }
    
    /** */
    virtual ~FileLogger() {
        if (stream.is_open()) {
            stream.close();
        }
    }    
    
    /**
     * \brief Initializes the logger.
     * \return <code>true</code> on success, sets error and returns
     *   <code>false</code> otherwise.
     */
    bool init();
    
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

#endif // AE_FILE_LOGGER_H
