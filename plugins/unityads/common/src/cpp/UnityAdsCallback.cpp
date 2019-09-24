#include "UnityAdsCallback.h"

namespace ae {
    
namespace unityads {
    
/** */
const char *UnityAdsCallback::finishStateToStr(FinishState state) {
    switch (state) {
        case FINISH_STATE_ERROR:
            return "error";
            
        case FINISH_STATE_SKIPPED:
            return "skipped";
            
        case FINISH_STATE_COMPLETED:
            return "completed";
    }
    
    return (const char *)0;
}
    
} // namespace
    
} // namespace