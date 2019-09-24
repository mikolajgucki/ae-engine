#include "LuaLibGPGS.h"

namespace ae {

namespace gpgs {
 
/** */
void LuaLibGPGS::setCallback(GPGSCallback *callback_) {
    callback = callback_;
    
    if (getCallback() != (GPGSCallback *)0 && signedInFlag == true) {
        getCallback()->signedIn();
    }
}
    
    
/** */
void LuaLibGPGS::signedIn() {
    signedInFlag = true;
    
    if (getCallback() != (GPGSCallback *)0) {
        getCallback()->signedIn();
    }
}

/** */
void LuaLibGPGS::signedOut() {
    signedInFlag = false;
}

} // namespace
    
} // namespace