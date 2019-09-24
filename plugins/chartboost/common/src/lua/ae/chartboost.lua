-- @module chartboost
-- @group Advert
-- @brief Chartboost integration.

-- check if the library is loaded
if not chartboost then
    error('Chartboost library not loaded')
end

-- @brief The locations.
chartboost.location = {
    -- @var
    -- @name chartboost.location.default
    -- @brief The default location.
    default = "Default",
    
    -- @var
    -- @name chartboost.location.achievements
    -- @brief The achievements location.
    achievements = "Achievements",
    
    -- @var 
    -- @name chartboost.location.gameScreen
    -- @brief The game screen location.
    gameScreen = "Game Screen"
}

-- Indicates if interstitial ads have been cached.
local hasInterstitial = {}

-- @brief Callback function called when an interstital ad has been cached.
--   Never call this function directly.
-- @param location The location.
function chartboost.didCacheInterstitial(location)
    hasInterstitial[location] = true
    ae.log.trace('chartboost.didCacheInterstitial()')
end

-- @brief Callback function called when an interstitial could not be loaded.
--   Never call this function directly.
-- @param location The location.
function chartboost.didFailToLoadInterstitial(location)
    hasInterstitial[location] = false
    ae.log.trace('chartboost.didFailToLoadInterstitial()')
end

-- @brief Callback function called when an interstitial ad has been clicked.
--   Never call this function directly.
-- @param location The location.
function chartboost.didClickInterstitial(location)   
    ae.log.trace('chartboost.didClickInterstitial()')
end

-- @brief Callback function called when an interstitial ad has been closed.
--   Never call this function directly.
-- @param location The location.
function chartboost.didCloseInterstitial(location)
    ae.log.trace('chartboost.didCloseInterstitial()')
end

-- @brief Callback function called when an interstitial ad has been dismissed.
--   Never call this function directly.
-- @param location The location.
function chartboost.didDismissInterstitial(location)
    ae.log.trace('chartboost.didDismissInterstitial()')
end

-- @brief Callback function called when an interstital ad has been displayed.
--   Never call this function directly.
-- @param location The location.
function chartboost.didDisplayInterstitial(location)
    ae.log.trace('chartboost.didDisplayInterstitial()')
end

-- @brief Checks if an interstitial ad is cached at given location.
-- @param location The location.
-- @return `true` if cached, `false` otherwise.
function chartboost.hasInterstitial(location)
    return hasInterstitial[location]
end
