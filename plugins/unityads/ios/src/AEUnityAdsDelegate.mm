#include <AEUnityAdsDelegate.h>

@implementation AEUnityAdsDelegate

/** */
-(void)unityAdsDidError:(UnityAdsError)unityAdsError withMessage:(NSString *)msg {
    std::string error;
    
    switch (unityAdsError) {
        case kUnityAdsErrorAdBlockerDetected:
            error = "AdBlockerDetected";
            break;
        case kUnityAdsErrorDeviceIdError:
            error = "DeviceIdError";
            break;
        case kUnityAdsErrorFileIoError:
            error = "FileIoError";
            break;
        case kUnityAdsErrorInitializedFailed:
            error = "InitializedFailed";
            break;
        case kUnityAdsErrorInitSanityCheckFail:
            error = "InitSanityCheckFail";
            break;
        case kUnityAdsErrorInternalError:
            error = "InternalError";
            break;
        case kUnityAdsErrorInvalidArgument:
            error = "InvalidArgument";
            break;
        case kUnityAdsErrorNotInitialized:
            error = "NotInitialized";
            break;
        case kUnityAdsErrorShowError:
            error = "ShowError";
            break;
        case kUnityAdsErrorVideoPlayerError:
            error = "VideoPlayerError";
            break;
        default:
            error = "?";
            break;
    }
    
    _luaLibUnityAds->failed(error,[msg UTF8String]);
}

/** */
-(void)unityAdsDidFinish:(NSString *)placementId withFinishState:(UnityAdsFinishState)unityAdsState {
    ::ae::unityads::UnityAdsCallback::FinishState state;
    
    switch (unityAdsState) {
        case kUnityAdsFinishStateCompleted:
            state = ::ae::unityads::UnityAdsCallback::FINISH_STATE_COMPLETED;
            break;
        case kUnityAdsFinishStateSkipped:
            state = ::ae::unityads::UnityAdsCallback::FINISH_STATE_SKIPPED;
            break;
        case kUnityAdsFinishStateError:
            state = ::ae::unityads::UnityAdsCallback::FINISH_STATE_ERROR;
            break;
    }
    
    _luaLibUnityAds->finished([placementId UTF8String],state);
}

/** */
-(void)unityAdsDidStart:(NSString *)placementId {
    _luaLibUnityAds->started([placementId UTF8String]);
}

/** */
-(void)unityAdsReady:(NSString *)placementId {
    _luaLibUnityAds->setReady([placementId UTF8String]);
}

@end