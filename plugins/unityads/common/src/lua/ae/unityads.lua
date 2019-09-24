-- @module ae.unityads
-- @group Advert
-- @brief Unity Ads integration.

-- check if the library is loaded
if not unityads then
    error('Unity Ads library not loaded')
end

-- @brief Called when an advert is ready to show.
--   Never call this function directly.
-- @param placementId The placement identifier.
function unityads.ready(placementId)
    ae.log.trace('unityads.ready(%s)',placementId)
end

-- @brief Called when an advert has started.
--   Never call this function directly.
-- @param placementId The placement identifier.
function unityads.started(placementId)
    ae.log.trace('unityads.started(%s)',placementId)
end

-- @brief Called when an advert has failed.
--   Never call this function directly.
-- @param err The Unity Ads error as string.
-- @param msg The error message.
function unityads.failed(err,msg)
    ae.log.trace('unityads.failed(%s,%d)',err,msg)
end

-- @brief Called when an advert has finished.
--   Never call this function directly.
-- @param placementId The placement identifier.
-- @param state The finish state (one of `completed`, `skipped`, `error`).
function unityads.finished(placementId,state)
    ae.log.trace('unityads.finished(%s,%s)',placementId,state)
end