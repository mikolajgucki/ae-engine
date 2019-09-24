-- @group UI
-- @brief The UI model is the model added to the engine and which contains
--   UI components.
local Model = ae.oo.class()

-- @brief Constructs a UI model.
-- @return The UI model.
function Model.new()
    local self = ae.oo.new(Model)
    Model.construct(self)
    
    return self
end

-- @brief Constructs a model.
-- @param self The model object.
function Model.construct(self)
    -- the table with the UI components
    self.components = {}
    self.touchHandler = ae.event.DefaultTouchHandler.new()
end

-- @brief Adds a component at the beginning of the component list.
-- @param component The component to add.
function Model:prepend(component)
    ae.itable.prepend(self.components,component)
end

-- @brief Adds a component at the end of the component list.
-- @param component The component to add.
function Model:append(component)
    ae.itable.append(self.components,component)
end

-- @brief Adds a number of components at the end of the component list.
-- @param components The components to add.
function Model:appendAll(components)
    for _,component in pairs(components) do
        ae.itable.append(self.components,component)
    end
end

-- @brief Removes all the components.
function Model:removeAll()
    self.components = {}
end

-- @brief Draws all the visible components.
function Model:draw()
    ae.itable.each(self.components,function(component)
        if component.draw then
            component:draw()
        end
    end)
end

-- @brief Updates all the components.
-- @param time The time in milliseconds elapsed since the last frame.
function Model:update(time)
    ae.itable.each(self.components,function(component)
        if (component.update) then
            component:update(time)
        end
    end)
end

-- @brief Invoked on touch down event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function Model:touchDown(pointerId,x,y)
    for _,component in ipairs(self.components) do
        if (component.bounds and component.touchDown and
            component.bounds:inside(x,y)) then
        --
            if self.touchHandler:touchDown(pointerId,x,y,component) then
                return true
            end
        end
    end
    
    return false
end

-- @brief Invoked on touch move event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function Model:touchMove(pointerId,x,y)
    self.touchHandler:touchMove(pointerId,x,y)
end

-- @brief Invoked on touch up event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function Model:touchUp(pointerId,x,y)
    self.touchHandler:touchUp(pointerId,x,y)
end

-- @brief Gets a string which represents the model.
-- @return The string representing the model.
function Model:__tostring()
    return ae.oo.tostring('ui.Model','#components=' ..
        string.format('%d',#self.components))
end

return Model
