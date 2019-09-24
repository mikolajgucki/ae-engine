#ifndef AE_DESKTOP_HTTP_RESPONSE_H
#define AE_DESKTOP_HTTP_RESPONSE_H

#include <string>

namespace ae {

namespace http {
    
/**
 * \brief Represents a HTTP response.
 */
class DesktopHTTPResponse {
    /** */
    enum {
        // OK
        DEFAULT_CODE = 200
    };
    
    /// The request URL.
    const std::string url;
    
    /// The response body.
    std::string *body;
    
    /// The response code.
    int code;
    
    /// The error message.
    std::string *error;
    
public:
    /** */
    DesktopHTTPResponse(const std::string& url_):url(url_),
        body((std::string *)0),code(DEFAULT_CODE),error((std::string *)0) {
    }
    
    /** */
    virtual ~DesktopHTTPResponse() {
        if (body != (std::string *)0) {
            delete body;
        }
        if (error != (std::string *)0) {
            delete error;
        }
    }
    
    /** */
    const std::string &getURL() const {
        return url;
    }
    
    /** */
    void setBody(std::string *body_) {
        body = body_;
    }
    
    /** */
    const std::string *getBody() {
        return body;
    }        
    
    /** */
    void setCode(int code_) {
        code = code_;
    }
    
    /** */
    int getCode() {
        return code;
    }
    
    /** */
    void setError(std::string *error_) {
        error = error_;
    }
    
    /** */
    const std::string *getError() {
        return error;
    }
};    
    
} // namespace

} // namespace

#endif // AE_DESKTOP_HTTP_RESPONSE_H