#ifndef AE_GPGS_CALLBACK_H
#define AE_GPGS_CALLBACK_H

namespace ae {
    
namespace gpgs {
  
/**
 * \brief The Google Play Game Services callback.
 */
class GPGSCallback {
public:
    /** */
    virtual ~GPGSCallback() {        
    }
    
    /**
     * \brief Called when a player has signed in.
     */
    virtual void signedIn() = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_GPGS_CALLBACK_H