-- @group Event
-- @brief Catches the touch events and passes events of one touch only.
local SingleTouchProxy = ae.oo.class()

-- @func
-- @brief Creates a single touch proxy.
-- @return The single touch proxy.
-- @func
-- @brief Creates a single touch proxy.
-- @param listener The listener of the filtered out touch events.
-- @return The single touch proxy.
function SingleTouchProxy.new(listener)
    local self = ae.oo.new(SingleTouchProxy)
    SingleTouchProxy.construct(self,listener)
    
    return self
end

-- @func
-- @brief Constructs a single touch proxy.
-- @param self The single touch proxy object.
-- @func
-- @brief Constructs a single touch proxy.
-- @param self The single touch proxy object.
-- @param listener The listener of the filtered out touch events.
function SingleTouchProxy.construct(self,listener)
    self.listener = listener
end

-- @brief Sets the listener.
-- @param listener The listener of the filtered out touch events.
function SingleTouchProxy:setListener(listener)
    self.listener = listener
end

-- @brief Invoked on touch down event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function SingleTouchProxy:touchDown(pointerId,x,y)
    if self.pointerId then
        return        
    end
    
    self.pointerId = pointerId
    if self.listener then
        self.listener:touchDown(pointerId,x,y)
    end
end

-- @brief Invoked on touch move event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function SingleTouchProxy:touchMove(pointerId,x,y)
    if self.pointerId == pointerId then
        if self.listener then
            self.listener:touchMove(pointerId,x,y)
        end
    end
end

-- @brief Invoked on touch up event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function SingleTouchProxy:touchUp(pointerId,x,y)
    if self.pointerId == pointerId then
        if self.listener then
            self.listener:touchUp(pointerId,x,y)
        end
        self.pointerId = nil
    end
end

return SingleTouchProxy