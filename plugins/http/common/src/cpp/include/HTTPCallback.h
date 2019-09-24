#ifndef AE_HTTP_CALLBACK_H
#define AE_HTTP_CALLBACK_H

#include <string>

namespace ae {
    
namespace http {
    
/**
 * \brief The HTTP callback. Called on HTTP events.
 */
class HTTPCallback {
public:
    /** */
    virtual ~HTTPCallback() {
    }
    
    /**
     * \brief Called when a response has been received.
     * \param id The request identifier.
     * \param code The response code.
     * \param body The response body.
     */
    virtual void receivedResponse(const std::string& id,int code,
        const std::string& body) = 0;
    
    /**
     * \brief Called when it failed to receive a response.
     * \param id The request identifier.
     * \param error The error message.
     */
    virtual void failedToReceiveResponse(const std::string& id,
        const std::string& error) = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_HTTP_CALLBACK_H