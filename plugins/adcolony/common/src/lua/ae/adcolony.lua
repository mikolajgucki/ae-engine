-- @module ae.adcolony
-- @group Advert
-- @brief AdColony integration.

-- check if the library is loaded
if not adcolony then
    error('AdColony library not loaded')
end

-- @brief Callback function called when an interstitial is available.
--   Never call this function directly.
-- @param zoneId The zone identifier.
function adcolony.interstitialAvailable(zoneId)
    ae.log.trace('adcolony.interstitialAvailable(%s)',zoneId)
end

-- @brief Callback function called when no ad is available.
--   Never call this function directly.
-- @param zoneId The zone identifier.
function adcolony.interstitialUnavailable(zoneId)
    ae.log.trace('adcolony.interstitialUnavailable(%s)',zoneId)
end

-- @brief Callback function called when an interstitial has been opened.
--   Never call this function directly.
-- @param zoneId The zone identifier.
function adcolony.interstitialOpened(zoneId)
    ae.log.trace('adcolony.interstitialOpened(%s)',zoneId)
end

-- @brief Callback function called when an interstitial has been closed.
--   Never call this function directly.
-- @param zoneId The zone identifier.
function adcolony.interstitialClosed(zoneId)
    ae.log.trace('adcolony.interstitialClosed(%s)',zoneId)
end

