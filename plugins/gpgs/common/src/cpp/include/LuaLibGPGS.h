#ifndef AE_LUA_LIB_GPGS_H
#define AE_LUA_LIB_GPGS_H

#include <string>
#include "Error.h"
#include "GPGSCallback.h"

namespace ae {

namespace gpgs {
  
/**
 * \brief The superclass for platform specific implementations of the Google
 *   Play Game Services Lua library.
 */
class LuaLibGPGS:public Error {
    /// The Google Play Game Services callback.
    GPGSCallback *callback;
    
    /// Indicates if the player is signed in.
    bool signedInFlag;
    
protected:
    /** */
    LuaLibGPGS():Error(),callback((GPGSCallback *)0),signedInFlag(false) {
    }
    
    /**
     * \brief Gets the callback.
     * \return The callback.
     */
    GPGSCallback *getCallback() {
        return callback;
    }   
    
public:    
    /** */
    virtual ~LuaLibGPGS() {
        if (callback != (GPGSCallback *)0) {
            delete callback;
        }        
    }
    
    /**
     * \brief Sets the callback.
     * \param callback_ The callback.
     */
    void setCallback(GPGSCallback *callback_);
    
    /**
     * \brief Signs in to Google Play Game Services.
     */
    virtual void signIn() = 0;
    
    /**
     * \brief Signs out from Google Play Game Services.
     */
    virtual void signOut() = 0;
    
    /**
     * \brief Indicates if a player is signed in.
     * \return <code>true</code> if signed in, <code>false</code> otherwise.
     */
    virtual bool isSignedIn() = 0;
    
    /**
     * \brief Called when the player has signed in.     
     */
    virtual void signedIn();
    
    /**
     * \brief Called when the player has signed out.     
     */    
    virtual void signedOut();
    
    /**
     * \brief Unlocks an achievement.
     * \param id The achievement identifier.
     */
    virtual void unlockAchievement(const std::string &id) = 0;
    
    /**
     * \brief Increments an achievement.
     * \param id The achievement identifier.
     * \param steps The number of steps to increment.
     */
    virtual void incrementAchievement(const std::string &id,int steps) = 0;
    
    /**
     * \brief Sets an achievement to have steps.
     * \param id The achievement identifier.
     * \param steps The number of steps.
     */
    virtual void setAchievementSteps(const std::string &id,int steps) = 0;
    
    /**
     * \brief Submits a score.
     * \param leaderboardId The leaderboard identifier.
     * \param score The score to submit.
     */
    virtual void submitScore(const std::string &leaderboardId,long score) = 0;
    
    /**
     * \brief Displays achievements.
     */
    virtual void displayAchievements() = 0;
    
    /**
     * \brief Displays a leaderboard.
     * \param leaderboardId The leaderboard identifier.     
     */
    virtual void displayLeaderboard(const std::string &leaderboardId) = 0;
};
    
} // namespace
    
} // namespace

#endif //  AE_LUA_LIB_GPGS_H