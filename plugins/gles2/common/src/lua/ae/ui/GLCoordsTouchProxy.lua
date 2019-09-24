-- @group Event
-- @brief Converts display coordinates to GL coordinates.
local GLCoordsTouchProxy = ae.oo.class()

-- @brief Creates a GL coordinates touch proxy.
-- @param listener The listener of the events with GL coordinates.
-- @return The GL coordinates touch proxy.
function GLCoordsTouchProxy.new(listener)
    local self = ae.oo.new(GLCoordsTouchProxy)
    GLCoordsTouchProxy.construct(self,listener)
    
    return self
end

-- @brief Constructs a GL coordinates touch proxy.
-- @param self The GL coordinate touch proxy object.
-- @param listener The listener of the events with GL coordinates.
function GLCoordsTouchProxy.construct(self,listener)
    self.listener = listener
end

-- @brief Invoked on touch down event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function GLCoordsTouchProxy:touchDown(pointerId,x,y)
    local glCoords = ae.gl.getGLCoords(x,y) 
    self.listener:touchDown(pointerId,glCoords.x,glCoords.y)
end

-- @brief Invoked on touch move event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function GLCoordsTouchProxy:touchMove(pointerId,x,y)
    local glCoords = ae.gl.getGLCoords(x,y)
    self.listener:touchMove(pointerId,glCoords.x,glCoords.y)
end

-- @brief Invoked on touch up event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function GLCoordsTouchProxy:touchUp(pointerId,x,y)
    local glCoords = ae.gl.getGLCoords(x,y)
    self.listener:touchUp(pointerId,glCoords.x,glCoords.y)
end

return GLCoordsTouchProxy