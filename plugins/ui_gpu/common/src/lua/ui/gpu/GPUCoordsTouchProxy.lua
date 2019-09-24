-- @group UI
-- @brief A touch handler which converts display coordinates to GPU coordinates.
-- @full A touch handler which converts display coordinates to GPU coordinates.
--   It uses ()[ui.gpu.getGPUCoords()] for to convert the coordinates.
local GPUCoordsTouchProxy = ae.oo.class()

-- @brief Creates a GL coordinates touch proxy.
-- @param listener The listener of the events with GL coordinates.
-- @return The GL coordinates touch proxy.
function GPUCoordsTouchProxy.new(listener)
    local self = ae.oo.new(GPUCoordsTouchProxy)
    GPUCoordsTouchProxy.construct(self,listener)
    
    return self
end

-- @brief Constructs a GL coordinates touch proxy.
-- @param self The GL coordinate touch proxy object.
-- @param listener The listener of the events with GL coordinates.
function GPUCoordsTouchProxy.construct(self,listener)
    self.listener = listener
end

-- @brief Invoked on touch down event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function GPUCoordsTouchProxy:touchDown(pointerId,x,y)
    local gpuCoords = ui.gpu.getGPUCoords(x,y)
    self.listener:touchDown(pointerId,gpuCoords.x,gpuCoords.y)
end

-- @brief Invoked on touch move event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function GPUCoordsTouchProxy:touchMove(pointerId,x,y)
    local gpuCoords = ui.gpu.getGPUCoords(x,y)
    self.listener:touchMove(pointerId,gpuCoords.x,gpuCoords.y)
end

-- @brief Invoked on touch up event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function GPUCoordsTouchProxy:touchUp(pointerId,x,y)
    local gpuCoords = ui.gpu.getGPUCoords(x,y)
    self.listener:touchUp(pointerId,gpuCoords.x,gpuCoords.y)
end

return GPUCoordsTouchProxy