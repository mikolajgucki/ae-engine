-- @module ui.layout.DeckLayout
-- @group UI
-- @brief Card deck layout in which all the child components are assigned the
--   layout bounds.
local DeckLayout,super = ae.oo.subclass(ui.layout.Layout)
    
-- @brief ...
-- @return A new DeckLayout object. 
function DeckLayout.new()
    local self = ae.oo.new(DeckLayout)
    DeckLayout.construct(self)
    
    return self
end

-- @brief Constructs a DeckLayout object.
-- @param self The object. 
function DeckLayout.construct(self)
    super.construct(self)
end

-- @brief Lays out the child components in the layout bounds.
function DeckLayout:layout()
    for _,component in pairs(self.components) do
        component:setBounds(self.bounds.x,self.bounds.y,
            self.bounds.width,self.bounds.height)
    end
end

return DeckLayout