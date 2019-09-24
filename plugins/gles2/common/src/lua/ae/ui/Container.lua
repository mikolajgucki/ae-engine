-- @group UI
-- @brief Holds components in given order.
local Container,super = ae.oo.subclass(ae.ui.Component)

-- @func
-- @brief Creates a component container with no bounds set.
-- @return The container.
-- @func
-- @brief Creates a component container.
-- @param bounds The initial bounds.
-- @return The container.
function Container.new(bounds)
    local self = ae.oo.new(Container)
    Container.construct(self,bounds)
    
    return self
end

-- @func
-- @brief Constructs a component container with no bounds set.
-- @param self The container object.
-- @func
-- @brief Constructs a component container.
-- @param self The container object.
-- @param bounds The initial bounds.
function Container.construct(self,bounds)
    super.construct(self,bounds)
    
    self.components = {}
    self.touchComponents = {}
end

-- @brief Sets the alpha color component of the child components.
-- @param alpha The alpha value.
function Container:setAlpha(alpha)
    ae.itable.each(self.components,function(component)
        if component.setAlpha then
            component:setAlpha(alpha)
        end
    end)
end

-- @brief Prepends a component.
-- @param component The component.
function Container:prepend(component)
    if not component then
        error('Attempt to prepend a nil component')
    end
    ae.itable.prepend(self.components,component)
end

-- @brief Appends a component.
-- @param component The component.
function Container:append(component)
    if not component then
        error('Attempt to append a nil component')
    end
    ae.itable.append(self.components,component)
end

-- @brief Removes a component.
-- @param component The component.
function Container:remove(component)
    ae.itable.remove(self.components,component)
end

-- @brief Removes all the components from the container.
function Container:removeAll()
    self.components = {}
end

-- @brief Draws the child components.
function Container:draw()
    if not self.visible or self.bounds:isOutsideDisplay() then
        return
    end

    for index = 1,#self.components do
        self.components[index]:draw()
    end
end

-- @brief Updates the child components.
-- @param time The time in milliseconds elapsed since the last frame.
function Container:update(time)
    if not self.enabled then
        return
    end

    for index = 1,#self.components do
        self.components[index]:update(time)
    end
end

-- @brief Invoked on touch down event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function Container:touchDown(pointerId,x,y)
    if not self.visible or not self.enabled then
        return false
    end
    
    local index = #self.components
    while index > 0 do
        local component = self.components[index]
        if component:inside(x,y) and component:touchDown(pointerId,x,y) then
            self.touchComponents[pointerId] = component
            return true
        end
        index = index - 1
    end
    
    return false
end

-- @brief Invoked on touch move event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function Container:touchMove(pointerId,x,y)
    if not self.visible or not self.enabled or
       not self.touchComponents[pointerId] then
    --
        return false
    end
    
    return self.touchComponents[pointerId]:touchMove(pointerId,x,y)
end

-- @brief Invoked on touch up event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function Container:touchUp(pointerId,x,y)
    if not self.visible or not self.enabled or
       not self.touchComponents[pointerId] then
   --
        return false
    end
    
    local touchComponent = self.touchComponents[pointerId]
    self.touchComponents[pointerId] = nil

    return touchComponent:touchUp(pointerId,x,y)
end

-- @brief Gets a string which represents the container.
-- @return The string representing the containter.
function Container:__tostring()
    return ae.oo.tostring('ae.ui.Container',
        '#components=' .. tostring(#self.components),super.__tostring(self))
end

return Container
