-- @module ae.ui.ViewportLayout
-- @group UI
-- @brief Lays out child components as if the layout bounds were the viewport
--   for the child components.
-- @full Lays out child components as if the layout bounds were the viewport
--   for the child components. Child bounds are normalized. That is, child
--   coordinates `0,0` correspond to the lower-left corner of the viewport
--   while coordinates `1,1` correspond to the upper-right corner of the
--   viewport. A child component of size `1x1` is as big as the viewport.
local ViewportLayout,super = ae.oo.subclass(ae.ui.ChildBoundsLayout)

-- @func
-- @brief Creates a viewport layout with no bounds set.
-- @return The viewport layout.
-- @func
-- @brief Creates a viewport layout.
-- @param bounds The initial bounds.
-- @return The viewport layout.
function ViewportLayout.new(bounds)
    local self = ae.oo.new(ViewportLayout)
    ViewportLayout.construct(self,bounds)
    
    return self
end

-- @func
-- @brief Constructs a viewport layout with no bounds set.
-- @param self The viewport layout object.
-- @func
-- @brief Constructs a viewport layout.
-- @param self The viewport layout object.
-- @param bounds The initial bounds.
function ViewportLayout.construct(self,bounds)
    super.construct(self,bounds)
end

-- @brief Lays out a child component.
-- @param component The component.
-- @param childBounds The child bounds.
function ViewportLayout:layoutChildComponent(component,childBounds)
    childBounds = childBounds or self.childBounds[component]
    local bounds = self.bounds
    self:layoutComponent(component,
        bounds.x + childBounds.x * bounds.width,
        bounds.y + childBounds.y * bounds.height,
        childBounds.width * bounds.width,
        childBounds.height * bounds.height)
end

-- @brief Gets absolute bounds of child bounds.
-- @param bounds The viewport bounds.
-- @param childBounds The child bounds.
-- @return The absolute bounds.
function ViewportLayout.getAbsoluteBounds(bounds,childBounds)
    return ae.ui.Bounds.new(
        bounds.x + childBounds.x * bounds.width,
        bounds.y + childBounds.y * bounds.height,
        childBounds.width * bounds.width,
        childBounds.height * bounds.height)        
end

-- @brief Gets a string which represents the layout.
-- @return The string representing the layout.
function ViewportLayout:__tostring()
    return ae.oo.tostring('ae.ui.ViewportLayout',nil,super.__tostring(self))
end

return ViewportLayout