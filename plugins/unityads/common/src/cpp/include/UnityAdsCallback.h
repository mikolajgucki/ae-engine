#ifndef AE_UNITY_ADS_CALLBACK_H
#define AE_UNITY_ADS_CALLBACK_H

#include <string>

namespace ae {
    
namespace unityads {
    
/**
 * \brief The Unity Ads callback called on Unity Ads event.
 */
class UnityAdsCallback {
public:
    /** */
    enum FinishState {
        /** */
        FINISH_STATE_ERROR,
        
        /** */
        FINISH_STATE_SKIPPED,
        
        /** */
        FINISH_STATE_COMPLETED
    };
    
    /** */
    UnityAdsCallback() {
    }
    
    /** */
    virtual ~UnityAdsCallback() {
    }
    
    /**
     * \brief Sets the flag stating if there is advert ready to be shown.
     * \param placementId The placement identifier.
     */
    virtual void setReady(const std::string &placementId) = 0;
    
    /**
     * \brief Called when an advert has started.
     * \param placementId The placement identifier.
     */
    virtual void started(const std::string &placementId) = 0;
    
    /**
     * \brief Called when an advert has failed.
     * \param error The error as string.
     * \param msg The error message.
     */
    virtual void failed(const std::string &error,
        const std::string &msg) = 0;
    
    /**
     * \brief Called when an advert has started.
     * \param placementId The placement identifier.
     * \param state The finish state.
     */
    virtual void finished(const std::string &placementId,
        FinishState state) = 0;    
    
    /** */
    static const char *finishStateToStr(FinishState state);
};
    
} // namespace
    
} // namespace

#endif // AE_UNITY_ADS_CALLBACK_H