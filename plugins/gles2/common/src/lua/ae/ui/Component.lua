-- @module ae.ui.Component
-- @group UI
-- @brief The superclass for all the UI components.
local Component = ae.oo.class()

-- @func
-- @brief Creates a component with no bounds set.
-- @func
-- @brief Creates a component.
-- @param bounds The initial component bounds.
function Component.new(bounds)
    local self = ae.oo.new(Component)
    Component.construct(self,bounds)
    
    return self
end

-- @func
-- @brief Constructs a component with no bounds set.
-- @param self The component object.
-- @func
-- @brief Constructs a component.
-- @param self The component object.
-- @param bounds The initial component bounds.
function Component.construct(self,bounds)
-- @name Component.bounds
-- @var
-- @brief The component ()[ui.Bounds].
    self.bounds = bounds or ae.ui.Bounds.new()
    
-- @name Component.visible
-- @var
-- @brief Indicates if the component is visible.
-- @full Indicates if the component is visible. Show and hide the component
--   be calling appropriate methods `show()` and `hide()`.
    self.visible = true
    
-- @name Component.enabled
-- @var
-- @brief Indicates if the component is enabled.
-- @full Indicates if the component is enabled. Enabled and disable the\
--   component be calling methods functions `enable()` and `disable()`.
    self.enabled = true
end

-- @brief Gets the component identifier.
-- @return The component identifier.
function Component:getId()
    return self.id
end

-- @brief Sets the component identifier.
-- @param id The component identifier.
function Component:setId(id)
    self.id = id
end

-- @brief Called when the component bounds have changed.
function Component:boundsChanged()
end

-- @brief Sets the component bounds.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @param width The width.
-- @param height The height.
function Component:setBounds(x,y,width,height)
    self.bounds:setBounds(x,y,width,height)
    self:boundsChanged()
end

-- @brief Sets the component bounds.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @param width The width.
-- @param height The height.
-- @param top The top margin.
-- @param right The right margin.
-- @param bottom The bottom margin.
-- @param left The left margin.
function Component:setBoundsWithMargins(x,y,width,height,top,right,bottom,left)
    self:setBounds(x + left,y + bottom,
        width - left - right,height - top - bottom)
end

-- @brief Assigns bounds to the component bounds.
-- @param bounds The bounds to assign.
function Component:assignBounds(bounds)
    self.bounds:assign(bounds)
    self:boundsChanged()
end

-- @brief Sets the component location.
-- @param x The X coordinate.
-- @param y The Y coordinate.
function Component:setLocation(x,y)
    self.bounds:setLocation(x,y)
    self:boundsChanged()
end

-- @brief Sets the component size.
-- @param width The width.
-- @param height The height.
function Component:setSize(width,height)
    self.bounds:setSize(width,height)
    self:boundsChanged()    
end

-- @func
-- @brief Aligns the component bounds in the center of other bounds.
-- @param bounds The other bounds.
-- @func
-- @brief Aligns the component bounds inside other bounds.
-- @param bounds The other bounds.
-- @param halign The horizontal alignment.
-- @param valign The vertical alignment.
function Component:align(bounds,halign,valign)
    halign = halign or ae.ui.halign.center
    valign = valign or ae.ui.valign.middle
    
    local x = bounds.x + halign(bounds.width,self.bounds.width)
    local y = bounds.y + valign(bounds.height,self.bounds.height)
    self:setLocation(x,y)
end

-- @brief Makes the component visible.
function Component:show()
    self.visible = true
end

-- @brief Makes the component invisible.
function Component:hide()
    self.visible = false
end

-- @brief Sets the component (in)visible.
-- @param visible `true` if visible, `false` if invisible.
function Component:setVisible(visible)
    self.visible = visible
end

-- @brief Enables the component.
function Component:enable()
    self.enabled = true
end

-- @brief Disables the component.
function Component:disable()
    self.enabled = false
end

-- @brief Sets the component either enabled or disabled.
-- @param enabled `true` if enabled, `false` if disabled.
function Component:setEnabled(enabled)
    self.enabled = enabled
end

-- @brief Tests if a point is inside the component bounds.
-- @param x The point X coordinate.
-- @param y The point Y coordinate.
-- @return `true' if the point is inside the bounds, `false` otherwise.
function Component:inside(x,y)
    return self.bounds:inside(x,y)
end

-- @brief Draws the component.
function Component:draw()
end

-- @brief Updates the component.
-- @param time The time in milliseconds elapsed since the last frame.
function Component:update(time)
end

-- @brief Invoked on touch down event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function Component:touchDown(pointerId,x,y)
end

-- @brief Invoked on touch move event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function Component:touchMove(pointerId,x,y)
end

-- @brief Invoked on touch up event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function Component:touchUp(pointerId,x,y)
end

-- @brief Gets a string which represents the component.
-- @return The string representing the component.
function Component:__tostring()
    return ae.oo.tostring('ui.Component',
        ', bounds=' .. tostring(self.bounds) ..
        ', visible=' .. tostring(self.visible) ..
        ', enabled=' .. tostring(self.enabled))
end

return Component