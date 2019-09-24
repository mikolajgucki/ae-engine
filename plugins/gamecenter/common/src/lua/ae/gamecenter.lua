-- @module gamecenter
-- @group Services
-- @brief The Game Center integration.

-- @brief The listeners.
local listeners = {}

-- @brief Indicates if the player has been authenticated.
local authenticatedFlag

-- @brief Indicates if the login view is visible.
local loginViewVisibleFlag

-- @brief Indicates if the achievements have been loaded.
local achievementsLoadedFlag

-- @brief Notifies all the listeners.
-- @param funcName The name of the function to call.
local function notifyListeners(funcName)
    for _,listener in ipairs(listeners) do
        if listener[funcName] then
            listener[funcName]()
        end
    end
end

-- @brief Called when the login view is about to be shown.
--   Never call this function directly.
function gamecenter.willShowLoginView()
    ae.log.trace('gamecenter.willShowLoginView()')
    loginViewVisibleFlag = true
    notifyListeners('willShowLoginView')
end

-- @brief Called when the local player has been authenticated.
--   Never call this function directly.
function gamecenter.authenticated()
    ae.log.trace('gamecenter.authenticated()')
    authenticatedFlag = true
    notifyListeners('authenticated')
    gamecenter.loginViewHidden()
end

-- @brief Called when the local player has not been authenticated.
--   Never call this function directly.
function gamecenter.notAuthenticated()
    ae.log.trace('gamecenter.notAuthenticated()')
    notifyListeners('notAuthenticated')
    gamecenter.loginViewHidden()
end

-- @brief Called when the local player has not been authenticated
--   due to an error. Never call this function directly.
function gamecenter.notAuthenticatedWithError()
    gamecenter.notAuthenticated()
end

-- @brief Called when the login view has been hidden.
--   Never call this function directly.
function gamecenter.loginViewHidden()
    ae.log.trace('gamecenter.loginViewHidden()')
    loginViewVisibleFlag = false
    notifyListeners('loginViewHidden')
end

-- @brief Called when the achievements have been loaded (retrieved from
--   the server). Never call this function directly.
function gamecenter.achievementsLoaded()
    ae.log.trace('gamecenter.achievementsLoaded()')
    achievementsLoadedFlag = true
    notifyListeners('achievementsLoaded')
end

-- @brief Adds a listener.
-- @param listener The listener.
function gamecenter.addListener(listener)
    ae.itable.append(listeners,listener)
end

-- @brief Checks if the login view is visible.
-- @return `true` if visible, `false` otherwise.
function gamecenter.isLoginViewVisible()
    return loginViewVisibleFlag
end

-- @brief Checks if the player has been authenticated.
-- @return `true` if authenticated, `false` otherwise.
function gamecenter.isAuthenticated()
    return authenticatedFlag
end

-- @brief Checks if the achievements have been loaded.
-- @return `true` if loaded, `false` otherwise.
function gamecenter.areAchievementsLoaded()
    return achievementsLoadedFlag
end