#ifndef AE_ERROR_H
#define AE_ERROR_H

#include <string>
#include <iostream>
#include <sstream>

namespace ae {
  
/**
 * \brief The superclass for classes which can fail with an error.
 */
class Error {
    /// The error message.
    std::string msg;
    
public:
    /**
     * \brief Constructs an Error.
     */
    Error():msg() {
    }
    
    /** */
    virtual ~Error() {
    }

    /**
     * \brief Sets the error message.
     * \param _msg The error message.
     */
    void setError(const std::string &_msg) {
        msg = _msg;
    }
    
    /**
     * \brief Assigns another error to this error.
     * \param error The other error.
     */
    void setError(const Error &error) {
        msg = error.getError();
    }
    
    /**
     * \brief Sets the no memory error.
     */
    void setNoMemoryError() {
        msg = std::string("Out of memory");
    }
    
    /**
     * \brief Checks if error is set.
     * \return <code>true</code> if set, <code>false</code> otherwise.
     */
    bool chkError() const {
        return msg.empty() == false;
    }
    
    /**
     * \brief Gets the error message.
     * \return The error message.
     */
    const std::string getError() const {
        return msg;
    }
    
    /**
     * \brief Clears the error.
     */
    void clearError() {
        msg = std::string("");
    }    
};
    
} // namespace

#endif // AE_ERROR_H 