-- @group UI
-- @brief Horizontal layout.
local HLayout,super = ae.oo.subclass(ae.ui.Layout)

-- @func
-- @brief Creates a horizontal layout with no bounds set.
-- @return The horizontal layout.
-- @func
-- @brief Creates a horizontal layout.
-- @param bounds The initial bounds.
-- @return The horizontal layout.
function HLayout.new(bounds)
    local self = ae.oo.new(HLayout)
    HLayout.construct(self,bounds)
    
    return self
end

-- @func
-- @brief Constructs a horizontal layout with no bounds set.
-- @param self The horizontal layout object.
-- @func
-- @brief Constructs a horizontal layout.
-- @param self The horizontal layout object.
-- @param bounds The initial bounds.
function HLayout.construct(self,bounds)
    super.construct(self,bounds)
    
    -- the widths of the components (fixed or ratio)
    self.widths = {}
end

-- @brief Sets fixed width of a component.
-- @param component The component already added to the layout.
-- @param width The component width.
function HLayout:setWidth(component,width)
    if not ae.itable.contains(self.components,component) then
        error('Unknown component ' .. tostring(component))
    end
    
    self.widths[component] = {
        fixed = width
    }
end

-- @brief Sets component width as ratio of the layout width minus the total
--   width of the fixed-width components.
-- @param component The component already added to the layout.
-- @param ratio The width ratio.
function HLayout:setWidthRatio(component,ratio)
    if not ae.itable.contains(self.components,component) then
        error('Unknown component ' .. tostring(component))
    end
    
    self.widths[component] = {
        ratio = ratio
    }
end

-- @brief Lays out the child components.
function HLayout:layout()
    local totalFixedWidth = 0
    local totalRatioWidth = 0
    
    local layout = self
    ae.itable.each(self.components,function(component)
        local width = layout.widths[component]
        if width then
            if width.fixed then
                totalFixedWidth = totalFixedWidth + width.fixed
            end
            if width.ratio then
                totalRatioWidth = totalRatioWidth + width.ratio
            end
        end
    end)
    
    -- available space for the ratio-width components
    local availRatioWidth = self.bounds.width - totalFixedWidth
    
    local x = 0
    for _,component in ipairs(self.components) do
        local width = self.widths[component]
        if width then
            local cellWidth
            
            if width.fixed then
                cellWidth = width.fixed
            end
            if width.ratio then
                cellWidth = availRatioWidth * width.ratio / totalRatioWidth
            end
            
            self:layoutComponent(component,
                self.bounds.x + x,self.bounds.y,
                cellWidth,self.bounds.height)
            x = x + cellWidth
        end
    end
end

-- @brief Gets a string which represents the layout.
-- @return The string representing the layout.
function HLayout:__tostring()
    return ae.oo.tostring('ae.ui.HLayout',nil,super.__tostring(self))
end

return HLayout