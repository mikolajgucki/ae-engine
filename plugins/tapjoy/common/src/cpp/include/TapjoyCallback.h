#ifndef AE_TAPJOY_CALLBACK_H
#define AE_TAPJOY_CALLBACK_H

namespace ae {
    
namespace tapjoy {
    
/**
 * \brief The Tapjoy callback.
 */
class TapjoyCallback {
public:
    /** */
    virtual ~TapjoyCallback() {
    }
    
    /**
     * \brief Called when the connection has succeeded.
     */
    virtual void connectionSucceeded() = 0;
    
    /**
     * \brief Called when the connection has failed failed.
     */
    virtual void connectionFailed() = 0;
    
    /**
     * \brief Called when request for content succeeded.
     * \param placement The placement name.
     */
    virtual void requestSucceeded(const std::string& placement) = 0;
    
    /**
     * \brief Called when request for content failed.
     * \param placement The placement name.
     * \param error The eror.
     */
    virtual void requestFailed(const std::string& placement,
        const std::string& error) = 0;
    
    /**
     * \brief Called when content is ready to be shown.
     * \param placement The placement name.
     */     
    virtual void contentIsReady(const std::string& placement) = 0;
    
    /**
     * \brief Called when content has been shown.
     * \param placement The placement name.
     */
    virtual void contentShown(const std::string& placement) = 0;
    
    /**
     * \brief Called when content has been dismissed.
     * \param placement The placement name.
     */
    virtual void contentDismissed(const std::string& placement) = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_TAPJOY_CALLBACK_H