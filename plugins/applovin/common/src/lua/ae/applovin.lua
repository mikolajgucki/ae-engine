-- @module applovin
-- @group Advert
-- @brief AppLovin integration.

-- check if the library is loaded
if not applovin then
    error('AppLovin library not loaded')
end

-- @brief Callback function called when an interstitial ad has been loaded.
--   Never call this function directly.
function applovin.interstitialAdLoaded()
    ae.log.trace('applovin.interstitialAdLoaded()')
end

-- @brief Callback function called when there are no interstital ads to
--   display. Never call this function directly.
function applovin.interstitialAdNoFill()
    ae.log.trace('applovin.interstitialAdNoFill()')
end

-- @brief Callback function called when loading of interstital ad fails.
--   Never call this function directly.
function applovin.interstitialAdFailed()
    ae.log.trace('applovin.interstitialAdFailed()')
end

-- @brief Callback function called when an interstitial ad has been displayed.
--   Never call this function directly.
function applovin.interstitialAdDisplayed()
    ae.log.trace('applovin.interstitialAdDisplayed()')
end

-- @brief Callback function called when an interstitial ad has been hidden.
--   Never call this function directly.
function applovin.interstitialAdHidden()
    ae.log.trace('applovin.interstitialAdHidden()')
end

-- @brief Callback function called when an interstitial ad has been clicked.
--   Never call this function directly.
function applovin.interstitialAdClicked()
    ae.log.trace('applovin.interstitialAdClicked()')
end
