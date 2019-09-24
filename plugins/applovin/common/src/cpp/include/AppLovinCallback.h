#ifndef AE_APPLOVIN_CALLBACK_H
#define AE_APPLOVIN_CALLBACK_H

namespace ae {
    
namespace applovin {

/**
 * \brief The Lua AppLovin callback. Called on AppLovin events.
 */
class AppLovinCallback {
public:
    /** */
    virtual ~AppLovinCallback() {
    }
    
    /**
     * \brief Called when an interstital ad has been loaded.
     */
    virtual void interstitialAdLoaded() = 0;

    /**
     * \brief Called when there are no interstital ads to display.
     */
    virtual void interstitialAdNoFill() = 0;
    
    /**
     * \brief Called when loading of interstital ad fails.
     * \param errorCode The error code.
     */
    virtual void interstitialAdFailed(int errorCode) = 0;
    
    /**
     * \brief Called when an interstitial ad has been displayed.
     */
    virtual void interstitialAdDisplayed() = 0;

    /**
     * \brief Called when an interstitial ad has been hidden.
     */
    virtual void interstitialAdHidden() = 0;
    
    /**
     * \brief Called when an interstitial ad has been clicked.
     */
    virtual void interstitialAdClicked() = 0;
};

} // namespace

} // namespace

#endif // AE_APPLOVIN_CALLBACK_H