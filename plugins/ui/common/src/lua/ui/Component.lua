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
    self.bounds = bounds or ui.Bounds.new()
    
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
-- @return The component.
function Component:setId(id)
    self.id = id
    return self
end

-- @brief Makes the component touchable.
-- @return The component.
function Component:setTouchable()
    self.touchable = true
    return self
end

-- @brief Called when the component bounds have changed.
function Component:boundsChanged()
end

-- @brief Sets the component bounds.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @param width The width.
-- @param height The height.
-- @return The component.
function Component:setBounds(x,y,width,height)
    self.bounds:setBounds(x,y,width,height)
    self:boundsChanged()
    return self
end

-- @brief Assigns bounds to the component bounds.
-- @param bounds The bounds to assign.
-- @return The component.
function Component:assignBounds(bounds)
    self.bounds:assign(bounds)
    self:boundsChanged()
    return self
end

-- @brief Sets the component location.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @return The component.
function Component:setLocation(x,y)
    self.bounds:setLocation(x,y)
    self:boundsChanged()
    return self
end

-- @brief Sets the component size.
-- @param width The width.
-- @param height The height.
-- @return The component.
function Component:setSize(width,height)
    self.bounds:setSize(width,height)
    self:boundsChanged()    
    return self
end

-- @brief Shows the component.
-- @return The component.
function Component:show()
    self:setVisible(true)
    return self
end

-- @brief Hides the component.
-- @return The component.
function Component:hide()
    self:setVisible(false)
    return self
end

-- @brief Sets the component (in)visible.
-- @param visible `true` if visible, `false` if invisible.
-- @return The component.
function Component:setVisible(visible)
    self.visible = visible
    return self
end

-- @brief Enables the component.
-- @return The component.
function Component:enable()
    self:setEnabled(true)
    return self
end

-- @brief Disables the component.
-- @return The component.
function Component:disable()
    self:setEnabled(false)
    return self
end

-- @brief Sets the component either enabled or disabled.
-- @param enabled `true` if enabled, `false` if disabled.
-- @return The component.
function Component:setEnabled(enabled)
    self.enabled = enabled
    return self
end

-- @brief Tests if a point is inside the component bounds.
-- @param x The point X coordinate.
-- @param y The point Y coordinate.
-- @return `true' if the point is inside the bounds, `false` otherwise.
function Component:inside(x,y)
    return self.bounds:inside(x,y)
end

-- @brief Tests if a point is inside transformed bounds.
-- @param x The X coordinate of the point.
-- @param y The Y coordinate of the point.
-- @return `true` if inside, `false` otherwise.
function Component:insideTransformedBounds(x,y)
    local p = {
        x = x,
        y = y
    }
    
    local p0 = self:transformPoint({
        x = self.bounds:x0(),
        y = self.bounds:y0()
    })
    local p1 = self:transformPoint({
        x = self.bounds:x1(),
        y = self.bounds:y0()
    })
    local p2 = self:transformPoint({
        x = self.bounds:x0(),
        y = self.bounds:y1()
    })
    local p3 = self:transformPoint({
        x = self.bounds:x1(),
        y = self.bounds:y1()
    })
    
    local epsilon = 0.001
    return ae.math.insideTriangle(p,p0,p1,p2,epsilon) or
        ae.math.insideTriangle(p,p1,p2,p3,epsilon)
end

-- @brief Transforms a point using the component transformation.
-- @param point The point to transform.
-- @return The transformed point.
function Component:transformPoint(point)
    return point
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
    if not self.enabled or not self.visible or
       not self.componentTapped or not self.touchable then
   --
        return false
    end

    return self:insideTransformedBounds(x,y)
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
    self:componentTapped(pointerId,x,y)
end

-- @brief Gets a string which represents the component.
-- @return The string representing the component.
function Component:__tostring()
    return ae.oo.tostring('ui.Component',
        'id=' .. tostring(self.id) ..
        ', bounds=' .. tostring(self.bounds) ..
        ', visible=' .. tostring(self.visible) ..
        ', enabled=' .. tostring(self.enabled))
end

return Component