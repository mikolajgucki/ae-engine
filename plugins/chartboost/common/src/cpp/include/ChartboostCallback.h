#ifndef AE_CHARTBOOST_CALLBACK_H
#define AE_CHARTBOOST_CALLBACK_H

namespace ae {
    
namespace chartboost {
  
/**
 * \brief The Chartboost callback. Called on Chartboost events.
 */
class ChartboostCallback {
public:
    /** */
    virtual ~ChartboostCallback() {
    }
    
    /**
     * Called when an interstitial ad has been cached.
     *
     * \param location The location.
     */
    virtual void didCacheInterstitial(const char *location) = 0;

    /**
     * Called when an interstitial could not be loaded.
     *
     * \param location The location.
     */
    virtual void didFailToLoadInterstitial(const char *location) = 0;
    
    /**
     * Called when an interstitial ad has been clicked.
     *
     * \param location The location.
     */
    virtual void didClickInterstitial(const char *location) = 0;
    
    /**
     * Called when an interstitial ad has been closed.
     *
     * \param location The location.
     */
    virtual void didCloseInterstitial(const char *location) = 0;
    
    /**
     * Called when an interstitial ad has been dismissed.
     *
     * \param location The location.
     */
    virtual void didDismissInterstitial(const char *location) = 0;
    
    /**
     * Called when an interstitial ad has been displayed.
     *
     * \param location The location.
     */
    virtual void didDisplayInterstitial(const char *location) = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_CHARTBOOST_CALLBACK_H