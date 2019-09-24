#ifndef AE_GAME_CENTER_CALLBACK_H
#define AE_GAME_CENTER_CALLBACK_H

namespace ae {
    
namespace gamecenter {
  
/**
 * \brief The Game Center callback.
 */
class GameCenterCallback {
public:
    /** */
    virtual ~GameCenterCallback() {
    }
    
    /**
     * Called when the login view is about to be shown.
     */
    virtual void willShowLoginView() = 0;
    
    /**
     * \brief Called when the local player has been authenticated.
     */
    virtual void authenticated() = 0;
    
    /**
     * \brief Called when the local player has not been authenticated.
     */
    virtual void notAuthenticated() = 0;
    
    /**
     * \brief Called when the local player has not been authenticated due to
     *   an error.
     */
    virtual void notAuthenticatedWithError() = 0;
    
    /**
     * \brief Called when the login view has been hidden.
     */
    virtual void loginViewHidden() = 0;
    
    /**
     * \brief Called when the achievements have been loaded (retrieved from
     *   the server).
     */
    virtual void achievementsLoaded() = 0;
};
    
} // namespace
    
} // namespace

#endif // AE_GAME_CENTER_CALLBACK_H