#ifndef AE_AD_COLONY_CALLBACK_H
#define AE_AD_COLONY_CALLBACK_H

#include <string>

namespace ae {
    
namespace adcolony {
    
/**
 * \brief The AdColony callback called on AdColony events.
 */
class AdColonyCallback {
public:
    /** */
    AdColonyCallback() {
    }
    
    /** */
    virtual ~AdColonyCallback() {
    }    
    
    /**
     * \brief Called when an interstitial is available.
     * \param zoneId The zone identifier.
     */
    virtual void interstitialAvailable(const std::string &zoneId) = 0;
    
    /**
     * \brief Called when an interstitial is unavailable.
     * \param zoneId The zone identifier.
     */
    virtual void interstitialUnavailable(const std::string &zoneId) = 0;
    
    /**
     * \brief Called when an interstitial has been opened.
     * \param zoneId The zone identifier.
     */
    virtual void interstitialOpened(const std::string &zoneId) = 0;
    
    /**
     * \brief Called when an interstitial has been closed.
     * \param zoneId The zone identifier.
     */
    virtual void interstitialClosed(const std::string &zoneId) = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_AD_COLONY_CALLBACK_H