#ifndef AE_VUNGLE_CALLBACK_H
#define AE_VUNGLE_CALLBACK_H

namespace ae {

namespace vungle {

/**
 * \brief The Vungle callback. Called on Vungle events.
 */
class VungleCallback {
public:
    /** */
    virtual ~VungleCallback() {
    }
    
    /**
     * \brief Called when the ad playable state has changed.
     * \param isAdPlayable_ Indicates if there's a playable ad.
     */
    virtual void adPlayableChanged(bool isAdPlayable_) = 0;
    
    /**
     * \brief Called when an ad is about to be shown.
     */
    virtual void willShowAd() = 0;
    
    /**
     * \brief Called when an ad is about to be closed.
     */
    virtual void willCloseAd() = 0;
    
    /**
     * \brief Called when an ad has been clicked.
     */
    virtual void adClicked() = 0;
    
    /**
     * \brief Called when it failed to play an ad.
     */
    virtual void failedToPlayAd() = 0;
    
    /**
     * \brief Called when the video can be considered a full view.
     */
    virtual void viewCompleted() = 0;
};
    
} // namespace

} // namespace

#endif // AE_VUNGLE_CALLBACK_H