-- @group UI
-- @brief Vertical layout.
local VLayout,super = ae.oo.subclass(ae.ui.Layout)

-- @func
-- @brief Creates a verical layout with no bounds set.
-- @return The vertical layout.
-- @func
-- @brief Creates a verical layout.
-- @param bounds The initial bounds.
-- @return The vertical layout.
function VLayout.new(bounds)
    local self = ae.oo.new(VLayout)
    VLayout.construct(self,bounds)
    
    return self
end

-- @func
-- @brief Constructs a vertical layout with no bounds set.
-- @param self The veritcal layout object.
-- @func
-- @brief Constructs a vertical layout.
-- @param self The veritcal layout object.
-- @param bounds The initial bounds.
function VLayout.construct(self,bounds)
    super.construct(self,bounds)
    
    -- the heights of the components (fixed or ratio)
    self.heights = {}
end

-- @brief Sets fixed height or a component.
-- @param component The component already added to the layout.
-- @param height The component height.
function VLayout:setHeight(component,height)
    if not ae.itable.contains(self.components,component) then
        error('Unknown component ' .. tostring(component))
    end
    
    self.heights[component] = {
        fixed = height
    }
end

-- @brief Sets component height as ratio of the layout height minus the total
--   height of the fixed-height components.
-- @param component The component already added to the layout.
-- @param ratio The height ratio.
function VLayout:setHeightRatio(component,ratio)
    if not ae.itable.contains(self.components,component) then
        error('Unknown component ' .. tostring(component))
    end
        
    self.heights[component] = {
        ratio = ratio
    }    
end

-- @brief Gets the total height of the child components with fixed height.
-- @return The total fixed height.
function VLayout:getTotalFixedHeight()
    local totalFixedHeight = 0;
    
    local layout = self
    ae.itable.each(self.components,function(component)
        local height = layout.heights[component]
        if height.fixed then
            totalFixedHeight = totalFixedHeight + height.fixed
        end
    end)
    
    return totalFixedHeight
end

-- @brief Lays out the child components.
function VLayout:layout()
    local totalFixedHeight = 0
    local totalRatioHeight = 0
    
    local layout = self
    ae.itable.each(self.components,function(component)
        local height = layout.heights[component]
        if height then
            if height.fixed then
                totalFixedHeight = totalFixedHeight + height.fixed
            end
            if height.ratio then
                totalRatioHeight = totalRatioHeight + height.ratio
            end
        end
    end)
    
    -- available height for the ratio-height components
    local availRatioHeight = self.bounds.height - totalFixedHeight
    
    local y = 0
    for _,component in ipairs(self.components) do
        local height = self.heights[component]
        if height then
            local cellHeight
            
            if height.fixed then
                cellHeight = height.fixed
            end
            if height.ratio then
                cellHeight = availRatioHeight * height.ratio / totalRatioHeight
            end
            
            self:layoutComponent(component,
                self.bounds.x,self.bounds.y + y,
                self.bounds.width,cellHeight)
            y = y + cellHeight
        end
    end    
end

-- @brief Gets a string which represents the layout.
-- @return The string representing the layout.
function VLayout:__tostring()
    return ae.oo.tostring('ae.ui.VLayout',nil,super.__tostring(self))
end

return VLayout