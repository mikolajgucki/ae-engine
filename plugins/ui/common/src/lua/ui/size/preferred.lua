-- @brief Provides function which calculate the preferred sizes of components.
local preferred = {}

-- @brief Stretches the component.
-- @param component The component.
-- @param bounds The bounds in which the component is to fit.
function preferred.stretch(component,bounds)
    return {
        width = bounds.width,
        height = bounds.height
    }
end

-- @brief Fits the component using the component aspect.
-- @param component The component.
-- @param bounds The bounds in which the component is to fit.
function preferred.fit(component,bounds)
    if not component.getAspect then
        error('Cannot fit component without aspect')
    end
    local aspect = component:getAspect()

    local width = bounds.width    
    local height = ae.size.getHeight(width,aspect)
    
    if height > bounds.height then
        height = bounds.height
        width = ae.size.getWidth(height,aspect)
    end
    
    return {
        width = width,
        height = height
    }
end

return preferred