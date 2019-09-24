-- @module ae.tapjoy
-- @group Advert
-- @brief Tapjoy integration.

-- check if the library is loaded
if not tapjoy then
    error('Tapjoy library not loaded')
end

-- @brief The table containing names of placements with ready content.
local ready = {}

-- @brief The original function.
local requestContent = tapjoy.requestContent

-- @brief Reqests content for a placement.
-- @param placement The placement name.
function tapjoy.requestContent(placement)
    ae.log.trace('tapjoy.requestContent(%s)',placement)
    ready[placement] = nil
    requestContent(placement)
end

-- @brief Checks if content is ready.
-- @param placement The placement name.
-- @return `true` if ready, `false` otherwise.
function tapjoy.isContentReady(placement)
    return ready[placement]
end

function tapjoy.clear()
    ready = {}
end

-- @brief Callback function called when the connection to the server has
--   succeeded. Never call this function directly.
function tapjoy.connectionSucceeded()
    ae.log.trace('tapjoy.connectionSucceeded()')
end

-- @brief Callback function called when the connection to the server has
--   failed. Never call this function directly.
function tapjoy.connectionFailed()
    ae.log.trace('tapjoy.connectionFailed()')
end

-- @brief Callback function called when request for content succeeded.
--   Never call this function directly.
-- @param placement The placement name.
function tapjoy.requestSucceeded(placement)
    ae.log.trace('tapjoy.requestSucceeded(%s)',placement)
end

-- @brief Callback function called when request for content failed.
--   Never call this function directly.
-- @param placement The placement name.
-- @param errorMsg The error message.
function tapjoy.requestFailed(placement,errorMsg)
    ae.log.trace('tapjoy.requestFailed(%s,%s)',placement,errorMsg)
    ready[placement] = nil
end

-- @brief Callback function called when content is ready to be shown.
--   Never call this function directly.
-- @param placement The placement name.
function tapjoy.contentIsReady(placement)
    ae.log.trace('tapjoy.contentIsReady(%s)',placement)
    ready[placement] = true
end

-- @brief Callback function called when content has been shown.
--   Never call this function directly.
-- @param placement The placement name.
function tapjoy.contentShown(placement)
    ae.log.trace('tapjoy.contentShown(%s)',placement)
end

-- @brief Callback function called when content has been dismissed.
--   Never call this function directly.
-- @param placement The placement name.
function tapjoy.contentDismissed(placement)
    ae.log.trace('tapjoy.contentDismissed(%s)',placement)
end