-- @group UI
-- @brief An abstract layout which assigns bounds to each child component.
-- @full An abstract layout which assigns bounds to each child component.
--   <br/>Subclasses must implement function
--   `layoutChildComponent(component,childBounds)` which lays out a child
--   components using child bounds.
local ChildBoundsLayout,super = ae.oo.subclass(ae.ui.Layout)

-- @func
-- @brief Creates a child bounds layout with no bounds set.
-- @return The layout.
-- @func
-- @brief Creates a child bounds layout.
-- @param bounds The initial bounds.
-- @return The layout.
function ChildBoundsLayout.new(bounds)
    local self = ae.oo.new(ChildBoundsLayout)
    ChildBoundsLayout.construct(self,bounds)
    
    return self
end

-- @func
-- @brief Constructs a child bounds layout with no bounds set.
-- @param self The relative layout object.
-- @func
-- @brief Constructs a child bounds layout.
-- @param self The relative layout object.
-- @param bounds The initial bounds.
function ChildBoundsLayout.construct(self,bounds)
    super.construct(self,bounds)
    self.childBounds = {}
end

-- @func
-- @brief Set the relative bounds of a child component.
-- @param component The child component.
-- @param bounds The relative bounds.
-- @func
-- @brief Set the relative bounds of a child component.
-- @param component The child component.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @param width The width.
-- @param height The height.
function ChildBoundsLayout:setChildBounds(component,x,y,width,height)
    if not ae.itable.contains(self.components,component) then
        error('Unknown component ' .. tostring(component))
    end
    
    if y then
        self.childBounds[component] = {
            x = x,
            y = y,
            width = width,
            height = height
        }
    else
        local bounds = x
        self.childBounds[component] = {
            x = bounds.x,
            y = bounds.y,
            width = bounds.width,
            height = bounds.height
        }
    end
end

-- @brief Lays out the child components.
function ChildBoundsLayout:layout()
    local layout = self
    
    ae.itable.each(self.components,function(component)
        local childBounds = layout.childBounds[component]
        if childBounds then
            self:layoutChildComponent(component,childBounds)
        end        
    end)
end

-- @brief Gets a string which represents the layout.
-- @return The string representing the layout.
function ChildBoundsLayout:__tostring()
    return ae.oo.tostring('ae.ui.ChildBoundsLayout',nil,super.__tostring(self))
end

return ChildBoundsLayout