-- @group UI
-- @brief Lays out child components as if the layout local coordinate system
--   was the child global coordinate system.
local RelativeLayout,super = ae.oo.subclass(ae.ui.ChildBoundsLayout)

-- @func
-- @brief Creates a relative layout with no bounds set.
-- @return The relative layout.
-- @func
-- @brief Creates a relative layout.
-- @param bounds The initial bounds.
-- @return The relative layout.
function RelativeLayout.new(bounds)
    local self = ae.oo.new(RelativeLayout)
    RelativeLayout.construct(self,bounds)
    
    return self
end

-- @func
-- @brief Constructs a relative layout with no bounds set.
-- @param self The relative layout object.
-- @func
-- @brief Constructs a relative layout.
-- @param self The relative layout object.
-- @param bounds The initial bounds.
function RelativeLayout.construct(self,bounds)
    super.construct(self,bounds)
end

-- @brief Lays out a child component.
-- @param component The component.
-- @param childBounds The child bounds.
function RelativeLayout:layoutChildComponent(component,childBounds)
    local bounds = self.bounds
    self:layoutComponent(component,
        bounds.x + childBounds.x,bounds.y + childBounds.y,
        childBounds.width,childBounds.height)
end

-- @brief Gets child bounds for absolute bounds.
-- @param absoluteBounds The absolute bounds.
-- @return The child bounds.
function RelativeLayout:getChildBounds(absoluteBounds)
    local bounds = self.bounds
    return {
        x = absoluteBounds.x - bounds.x,
        y = absoluteBounds.y - bounds.y,
        width = absoluteBounds.width,
        height = absoluteBounds.height
    }
end

-- @brief Calculates child bounds from component absolute bounds.
-- @param component The child component.
function RelativeLayout:calcChildBounds(component)
    self.childBounds[component] = self:getChildBounds(component.bounds)
end

-- @brief Gets a string which represents the layout.
-- @return The string representing the layout.
function RelativeLayout:__tostring()
    return ae.oo.tostring('ae.ui.RelativeLayout',nil,super.__tostring(self))
end

return RelativeLayout