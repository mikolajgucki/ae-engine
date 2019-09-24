-- @group UI
-- @brief Lays out a single component inside the layout bounds.
local ItemLayout,super = ae.oo.subclass(ae.ui.Layout)

-- @func
-- @brief Creates an item layout with no bounds set.
-- @param component The component to lay out.
-- @return The item layout.
-- @func
-- @brief Creates an item layout.
-- @param component The component to lay out.
-- @param bounds The initial bounds.
-- @return The item layout.
function ItemLayout.new(component,bounds)
    local self = ae.oo.new(ItemLayout)
    ItemLayout.construct(self,component,bounds)
    
    return self
end

-- @func
-- @brief Constructs an item layout with no bounds set.
-- @param self The item layout object.
-- @param component The component to lay out.
-- @func
-- @brief Constructs an item layout.
-- @param self The item layout object.
-- @param component The component to lay out.
-- @param bounds The initial bounds.
function ItemLayout.construct(self,component,bounds)
    super.construct(self,bounds)
    
    self.component = component
    self.halign = ui.halign.center
    self.valign = ui.valign.middle
end

-- @brief Lays out the child component.
function ItemLayout:layout()
    local component = self.component
    local x = self.bounds.x + self.halign(
        self.bounds.width,component.bounds.width)
    local y = self.bounds.y + self.valign(
        self.bounds.height,component.bounds.height)
        
    component:setLocation(x,y)
end

-- @brief Gets a string which represents the layout.
-- @return The string representing the layout.
function ItemLayout:__tostring()
    return ae.oo.tostring('ae.ui.ItemLayout',
        'component=' .. tostring(self.component),super.__tostring(self))
end

return ItemLayout