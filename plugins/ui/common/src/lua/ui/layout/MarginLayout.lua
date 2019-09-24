-- @module ui.layout.MarginLayout
-- @group UI
-- @brief Adds margins around the laid out component.
local MarginLayout,super = ae.oo.subclass(ui.layout.Layout)
    
-- @brief Creates a MarginLayout object.
-- @return A new MarginLayout object. 
function MarginLayout.new()
    local self = ae.oo.new(MarginLayout)
    MarginLayout.construct(self)
    
    return self
end

-- @brief Constructs a MarginLayout object.
-- @param self The object. 
function MarginLayout.construct(self)
    super.construct(self)
end

-- @brief Sets the component to be aligned.
-- @param component The component.
function MarginLayout:setComponent(component)
    self.component = component
    super.append(self,component)
end

-- @brief Lays out the child components.
function MarginLayout:layout()
    if not self.component then
        error('No component to align')
    end
    
    local top = ui.Bounds.full:getHeight(self.top or 0)
    local right = ui.Bounds.full:getWidth(self.right or 0)
    local bottom = ui.Bounds.full:getHeight(self.bottom or 0)
    local left = ui.Bounds.full:getWidth(self.left or 0)
    
    self.component:setBounds(
        self.bounds.x + left,self.bounds.y + bottom,
        self.bounds.width - left - right,
        self.bounds.height - bottom - top)
end

return MarginLayout