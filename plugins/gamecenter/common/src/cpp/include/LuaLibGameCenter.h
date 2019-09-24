#ifndef AE_LUA_LIB_GAME_CENTER_H
#define AE_LUA_LIB_GAME_CENTER_H

#include <string>
#include "Error.h"
#include "GameCenterCallback.h"

namespace ae {
    
namespace gamecenter {
    
/**
 * \brief The superclass for platform specific implementations of the Game
 *   center.
 */
class LuaLibGameCenter:public Error {
    /// The Game Center callback.
    GameCenterCallback *callback;
    
protected:
    LuaLibGameCenter():Error(),callback((GameCenterCallback *)0) {
    }
    
    /**
     * \brief Gets the callback.
     * \return The callback.
     */
    GameCenterCallback *getCallback() {
        return callback;
    }

public:
    virtual ~LuaLibGameCenter() {
        if (callback != (GameCenterCallback *)0) {
            delete callback;
        }
    }
    
    /**
     * \brief Sets the callback.
     * \param callback_ The callback.
     */
    void setCallback(GameCenterCallback *callback_);
    
    /**
     * \brief Initializes the library.
     */
    virtual void init() = 0;
    
    /**
     * \brief Authenticates the local player.
     */
    virtual void authenticate() = 0;
    
    /**
     * \brief Shows the Game Center UI.
     */
    virtual void show() = 0;
    
    /**
     * \brief Reports score.
     * \param leaderboardId The leaderboard identifier.
     * \param score The score.
     */
    virtual void reportScore(const std::string& leaderboardId,long score) = 0;
    
    /**
     * \brief Reports progress on achievement.
     * \param achievementId The achievement identifier.
     * \param percentComplete The achievement progress in percents.
     */
    virtual void reportAchievement(const std::string& achievementId,
        double percentComplete) = 0;

    /**
     * \brief Checks if an achievement is completed.
     * \param achievementId The achievement identifier.
     * \param completed The completed flag.
     * \return false if there is no such achievement or achievements haven't been
     *   yet loaded, true otherwise.
     */
    virtual bool isAchievementCompleted(const std::string& achievementId,
        bool& completed) = 0;
    
    /**
     * \brief Gets achievement progress.
     * \param achievementId The achievement identifier.
     * \param progress The achievment progress.
     * \return false if there is no such achievement or achievements haven't been
     *   yet loaded, true otherwise.
     */
    virtual bool getAchievementProgress(const std::string& achievementId,
        double& progress) = 0;
    
    /**
     * \brief Loads the achievements.
     */
    virtual void loadAchievements() = 0;    

    /**
     * \brief Resets the achievements progress.
     */
    virtual void resetAchievements() = 0;
    
    /**
     * \brief Called when the login view is about to be shown.
     */
    virtual void willShowLoginView();
    
    /**
     * \brief Called when the local player has been authenticated.
     */
    virtual void authenticated();
    
    /**
     * \brief Called when the local player has not been authenticated.
     */
    virtual void notAuthenticated();
    
    /**
     * \brief Called when the local player has not been authenticated due to
     *   an error.
     */
    virtual void notAuthenticatedWithError();
    
    /**
     * \brief Called when the login view has been hidden.
     */
    virtual void loginViewHidden();
    
    /**
     * \brief Called when the achievements have been loaded (retrieved from
     *   the server).
     */
    virtual void achievementsLoaded();
};
    
} // namespace
    
} // namespace

#endif // AE_LUA_LIB_GAME_CENTER_H
