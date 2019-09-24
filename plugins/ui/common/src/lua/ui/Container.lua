-- @group UI
-- @brief Holds components in given order.
local Container,super = ae.oo.subclass(ui.Component)

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

-- @brief Gets all the child components.
-- @return The child components.
function Container:getChildren()
    return self.components
end

-- @brief Looks for a component among all the descendants
-- @param id The identifier of the component to find.
-- @param ignoreNotFound Returns `nil` instead of raising error if there is no
--   such component.
-- @return The component or `nil` if there is no such component and
--   `ignoreNotFound` if false.
function Container:find(id,ignoreNotFound)
    -- look for it among the children
    for index = 1,#self.components do
        if self.components[index].id == id then
            return self.components[index]
        end
    end
    
    -- go deeper
    for index = 1,#self.components do
        if self.components[index].find then
            local component = self.components[index]:find(id,true)
            if component then
                return component
            end
        end
    end
    
    if not ignoreNotFound then
        error(string.format('Component %s not found',id))
    end
end

-- @brief Replaces a component.
-- @param self The container object.
-- @param index The index of the old component.
-- @param newComponent The new component.
local function replaceComponent(self,index,newComponent)
    local oldComponent = self.components[index]
    
    newComponent:assignBounds(oldComponent.bounds)
    newComponent.id = oldComponent.id
    
    self.components[index] = newComponent
end

-- @func
-- @brief Looks for a component among all the descendants and replaces it.
-- @full Looks for a component among all the descendants and replaces it.
--   Fails with an error if there is no such component.
-- @param id The identifier of the component to find.
-- @param component The new component.
-- @func
-- @brief Looks for a component among all the descendants and replaces it.
-- @param id The identifier of the component to find.
-- @param component The new component.
-- @param ignoreNotFound Returns `false` if set to `true` and there is no
--   component of the given identifier.
-- @return `false` if there is no such component, `true` otherwise.
function Container:replace(id,component,ignoreNotFound)
    -- try to replace
    for index = 1,#self.components do
        if self.components[index].id == id then
            replaceComponent(self,index,component)
            return true
        end
    end
    
    -- go deeper
    for index = 1,#self.components do
        if self.components[index].replace then
            if self.components[index]:replace(id,component,true) == true then
                return true
            end
        end
    end
    
    if not ignoreNotFound then
        error(string.format('Component %s not found',id))
    end

    return false
end

-- @func
-- @brief Replaces a number of components among all the descendants in one go.
--   Fails with an error if there is no componentn of a given identifier.
-- @param components The table with the components (key - identifier,
--   value - component).
--   component of an identifier from the given components table.
-- @func
-- @brief Replaces a number of components among all the descendants in one go.
-- @param components The table with the components (key - identifier,
--   value - component).
-- @param ignoreNotFound Returns `false` if set to `true` and there is no
--   component of a given identifier.
-- @return `true` on success, `false` if at least one component could not be
--   found.
function Container:replaceAll(components,ignoreNotFound)
    local notFound = false
    for id,component in pairs(components) do
        if not self:replace(id,component,ignoreNotFound) then
            notFound = true
        end
    end
    return notFound
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
    
    -- draw from the bottom to the top component
    ae.itable.each(self.components,function(component)
        component:draw()
    end)
end

-- @brief Updates the child components.
-- @param time The time in milliseconds elapsed since the last frame.
function Container:update(time)
    if not self.enabled then
        return
    end

    -- update from the bottom to the top component
    ae.itable.each(self.components,function(component)
        component:update(time)
    end)
end

-- @brief Invoked on touch down event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function Container:touchDown(pointerId,x,y)
    if not self.visible or not self.enabled then
        return false
    end
    
    -- pass the event from the top to the bottom component
    local index = #self.components
    while index > 0 do
        local component = self.components[index]
        if component:touchDown(pointerId,x,y) then
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
    return ae.oo.tostring('ui.Container',
        '#components=' .. tostring(#self.components),super.__tostring(self))
end

return Container
