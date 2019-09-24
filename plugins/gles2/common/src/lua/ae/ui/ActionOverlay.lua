-- @group UI
-- @brief A component which does not draw anything, but fires an action on tap.
local ActionOverlay,super = ae.oo.subclass(ae.ui.Component)

-- @func
-- @brief Creates an action overlay with no bounds set.
-- @return The new action overlay.
-- @func
-- @brief Creates an action overlay.
-- @param bounds The initial bounds.
-- @return The new action overlay.
function ActionOverlay.new(bounds)
    local self = ae.oo.new(ActionOverlay)
    ActionOverlay.construct(self,bounds)
    
    return self
end

-- @func
-- @brief Constructs an action overlay with no bounds set.
-- @param self The action overlay object.
-- @func
-- @brief Constructs an action overlay.
-- @param self The action overlay object.
-- @param bounds The initial bounds.
function ActionOverlay.construct(self,bounds)
    super.construct(self,bounds)
end

-- @brief Sets the listener.
-- @param tapFunc The listener function.
function ActionOverlay:setTapListener(tapFunc)
    self.tapFunc = tapFunc
end


-- @brief Called when the overlay has been tapped.
function ActionOverlay:tapped()
    if self.tapFunc then
        self.tapFunc(self)
    end
end

-- @brief Invoked on touch down event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function ActionOverlay:touchDown(pointerId,x,y)
    return self.visible and self.enabled
end

-- @brief Invoked on touch move event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function ActionOverlay:touchMove(pointerId,x,y)   
    return self.visible and self.enabled
end

-- @brief Invoked on touch up event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function ActionOverlay:touchUp(pointerId,x,y)
    if self.visible and self.enabled then
        self:tapped()
    end
    return true
end

-- @brief Gets a string which represents the action overlay.
-- @return The string representing the action overlay.
function ActionOverlay:__tostring()
    return ae.oo.tostring('ae.ui.ActionOverlay',
        'tapFunc=' .. tostring(self.tapFunc),super.__tostring(self))
end

return ActionOverlay
