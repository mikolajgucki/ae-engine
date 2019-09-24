-- @module ui.layout.AlignmentLayout
-- @group UI
-- @brief Gets the child component preferred size and aligns the component
--   inside the layout bounds.
local AlignmentLayout,super = ae.oo.subclass(ui.layout.Layout)
    
-- @brief Creates an AlignmentLayout object.
-- @return A new AlignmentLayout object. 
function AlignmentLayout.new()
    local self = ae.oo.new(AlignmentLayout)
    AlignmentLayout.construct(self)
    
    return self
end

-- @brief Constructs an AlignmentLayout object.
-- @param self The object. 
function AlignmentLayout.construct(self)
    super.construct(self)
    
    -- alignment
    self.halign = ui.halign.center
    self.valign = ui.valign.middle
end

-- @brief Sets the component to be aligned.
-- @param component The component.
function AlignmentLayout:setComponent(component)
    if not component.getPreferredSize then
        error('Cannot align component without preferred size')
    end

    self.component = component
    super.append(self,component)
end

-- @brief Lays out the child components.
function AlignmentLayout:layout()
    if not self.component then
        error('No component to align')
    end
    
    local preferredSize = self.component:getPreferredSize(self.bounds)
    self.component:setBounds(
        self.bounds.x + self.halign(self.bounds.width,preferredSize.width),
        self.bounds.y + self.valign(self.bounds.height,preferredSize.height),
        preferredSize.width,preferredSize.height)
end

return AlignmentLayout