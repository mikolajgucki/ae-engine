#ifndef AE_NULL_LOGGER_H
#define AE_NULL_LOGGER_H

namespace ae {

/** 
 * \brief Discards log messages.
 */
class NullLogger:public Logger {
public:    
    /** */
    NullLogger():Logger() {
    }
    
    /** */
    virtual ~NullLogger() {
    }    
    
    /** */
    virtual void trace(const char *tag,const char *msg) {
    }
    
    /** */
    virtual void debug(const char *tag,const char *msg) {
    }
    
    /** */
    virtual void info(const char *tag,const char *msg) {
    }
    
    /** */
    virtual void warning(const char *tag,const char *msg) {
    }
    
    /** */
    virtual void error(const char *tag,const char *msg) {
    }
}; 

} // namespace

#endif // AE_NULL_LOGGER_H
