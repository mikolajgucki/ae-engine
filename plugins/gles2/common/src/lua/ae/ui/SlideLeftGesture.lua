-- @module ae.ui.SlideLeftGesture
-- @group UI
-- @brief Slide left gesture.
local SlideLeftGesture = ae.oo.class()

-- @func
-- @brief Creates a slide left gesture with no listener.
-- @return The gesture.
-- @func
-- @brief Creates a slide left gesture.
-- @param listener The function called when the gesture is detected.
-- @return The gesture.
function SlideLeftGesture.new(listener)
    local self = ae.oo.new(SlideLeftGesture)
    SlideLeftGesture.construct(self,listener)
    
    return self
end

-- @func
-- @brief Creates a slide left gesture with no listener.
-- @func
-- @brief Creates a slide left gesture.
-- @param listener The function called when the gesture is detected.
function SlideLeftGesture.construct(self,listener)
    self.listener = listener
    self.triggerDistance = 0.1
    self.touchDownX = {}
end

-- @brief Sets the listener.
-- @param listener The listener.
function SlideLeftGesture:setListener(listener)
    self.listener = listener
end

-- @brief Invoked on touch down event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function SlideLeftGesture:touchDown(pointerId,x,y)
    self.touchDownX[pointerId] = x
end

-- @brief Invoked on touch move event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function SlideLeftGesture:touchMove(pointerId,x,y)
end

-- @brief Invoked on touch up event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function SlideLeftGesture:touchUp(pointerId,x,y)
    if self.touchDownX[pointerId] then
        local distance = self.touchDownX[pointerId] - x
        if distance >= self.triggerDistance and self.listener then
            self.listener(pointerId)            
            return true
        end             
    end
    
    self.touchDownX[pointerId] = nil
    return false
end

-- @brief Gets a string which represents the gesture.
-- @return The string representing the gesture.
function SlideLeftGesture:__tostring()
    return ae.oo.tostring('ae.ui.SlideLeftGesture',
        'triggerDistance=' .. tostring(self.triggerDistance) ..
        ', listener=' .. tostring(self.listener),super.__tostring(self))
end

return SlideLeftGesture