-- @brief Builds alignment layout from table data.

-- @brief The function which builds alignment layout from table data.
-- @param data The table data.
-- @return The alignment layout.
local function alignmentLayoutBuilder(data)
    -- validate
    if not data.component then
        error('Alignment layout without component')
    end
    
    -- layout
    local layout = ui.layout.AlignmentLayout.new()
    
    -- component
    local component = ui.layout.builder.build(data.component)
    
    -- preferred size (optional)
    if data.size then
        component.getPreferredSize = data.size
    end
    
    -- horizontal alignment (optional)
    if data.halign then
        layout.halign = data.halign
    end
    
    -- vertical alignment (optional)
    if data.valign then
        layout.valign = data.valign
    end
    
    -- aspect
    if data.aspect then
        component.getAspect = function()
            return data.aspect
        end
    end
    
    layout:setComponent(component)
    return layout
end

-- @brief Initializes the alignment layout builder.
local function init()
    ui.layout.builder.registerBuilder('alignment',alignmentLayoutBuilder)
end

-- initialize
init()