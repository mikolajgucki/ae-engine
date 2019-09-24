-- @group UI
-- @brief Gesture overlay.
local GestureOverlay,super = ae.oo.subclass(ae.ui.Component)

-- @func
-- @brief Creates a gesture overlay with no bounds set.
-- @param underneath The underneath component.
-- @return The gesture overlay.
-- @func
-- @brief Creates a gesture overlay.
-- @param underneath The underneath component.
-- @param bounds The initial bounds.
-- @return The gesture overlay.
function GestureOverlay.new(underneath,bounds)
    local self = ae.oo.new(GestureOverlay)
    GestureOverlay.construct(self,underneath,bounds)
    
    return self
end

-- @func
-- @brief Creates a gesture overlay with no bounsd set.
-- @param self The gesture overlay object.
-- @param underneath The underneath component.
-- @func
-- @brief Creates a gesture overlay.
-- @param self The gesture overlay object.
-- @param underneath The underneath component.
-- @param bounds The initial bounds.
function GestureOverlay.construct(self,underneath,bounds)
    super.construct(self,bounds)
    self.underneath = underneath
    
    -- the gestures contained in this overlay    
    self.gestures = {}
end

-- @brief Adds a gesture.
-- @param gesture The gesture.
function GestureOverlay:addGesture(gesture)
    ae.itable.append(self.gestures,gesture)
end

-- @brief Invoked on touch down event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function GestureOverlay:touchDown(pointerId,x,y)
    ae.itable.each(self.gestures,function(gesture)
        gesture:touchDown(pointerId,x,y)
    end)
    
    return true
end

-- @brief Invoked on touch move event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function GestureOverlay:touchMove(pointerId,x,y)
    ae.itable.each(self.gestures,function(gesture)
        gesture:touchMove(pointerId,x,y)
    end)
    
    return true
end

-- @brief Invoked on touch up event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function GestureOverlay:touchUp(pointerId,x,y)
    local anyGesture = false
    
    ae.itable.each(self.gestures,function(gesture)
        if gesture:touchUp(pointerId,x,y) then
            anyGesture = true
        end
    end)
    
    if not anyGesture and self.underneath then
        self.underneath:touchDown(pointerId,x,y)
        self.underneath:touchUp(pointerId,x,y)
    end    
    
    return true
end

-- @brief Gets a string which represents the overlay.
-- @return The string representing the overlay.
function GestureOverlay:__tostring()
    return ae.oo.tostring('ae.ui.GestureOverlay',
        '#gestures=' .. tostring(#self.gestures),super.__tostring(self)) 
end

return GestureOverlay