-- @module vungle
-- @group Advert
-- @brief Vungle integration.

-- check if the library is loaded
if not vungle then
    error('Vungle library not loaded')
end

-- Indicates if an advert is available.
local hasAd = false

-- @brief Callback function called when the ad playable state has changed.
--   Never call this function directly.
-- @param isAdPlayable Indicates if there's a playable ad.
function vungle.adPlayableChanged(isAdPlayable)
    ae.log.trace('vungle.adPlayableChanged(%s)',tostring(isAdPlayable))
    hasAd = isAdPlayable
end

-- @brief Callback function called when an ad has been cached.
--   Never call this function directly.
function vungle.willShowAd()
    ae.log.trace('vungle.willShowAd()')
end

-- @brief Callback function called when an ad has been cached.
--   Never call this function directly.
function vungle.willCloseAd()
    ae.log.trace('vungle.willCloseAd()')
end

-- @brief Callback function called when an ad has been cached.
--   Never call this function directly.
function vungle.adClicked()
    ae.log.trace('vungle.adClicked()')
end

-- @brief Callback function called when it failed to play an ad.
--   Never call this function directly.
function vungle.failedToPlayAd()
    ae.log.trace('vungle.failedToPlayAd()')
end

-- @brief Callback function called when the video can be considered a full
--   view. Never call this function directly.
function vungle.viewCompleted()
    ae.log.trace('vungle.viewCompleted()')
end

-- @brief Indicates if an ad is available to play.
-- @return `true` if available, `false` otherwise.
function vungle.hasAd()
    return hasAd
end