-- @brief Builds margin layout from table data.

-- @brief The function which builds margin layout from table data.
-- @param data The table data.
-- @return The margin layout.
local function marginLayoutBuilder(data)
    -- validate
    if not data.component then
        error('Margin layout without component')
    end
    
    -- layout
    local layout = ui.layout.MarginLayout.new()
    
    -- component
    local component = ui.layout.builder.build(data.component)    
    
    -- top, bottom
    if data.top then
        layout.top = ui.xheight(data.top)
    end
    if data.bottom then
        layout.bottom = ui.xheight(data.bottom)
    end
    
    -- left, right
    if data.left then
        layout.left = ui.xwidth(data.left)
    end
    if data.right then
        layout.right = ui.xwidth(data.right)
    end
    
    -- horizontal
    if data.horiz then
        if data.left or data.right then
            error('horiz or left,right can be set in margin layout')
        end
        layout.left = ui.xwidth(data.horiz)
        layout.right = ui.xwidth(data.horiz)
    end
    
    -- vertical
    if data.vert then
        if data.top or data.bottom then
            error('vert or top,bottom can be set in margin layout')
        end
        layout.top = ui.xheight(data.vert)
        layout.bottom = ui.xheight(data.vert)
    end
    
    -- margin
    if data.margin then
        if data.left or data.right or data.horiz or
           data.top or data.bottom or data.vert then
        --
            error('margin can be only set without other values')
        end
        layout.left = ui.xwidth(data.margin)        
        layout.right = ui.xwidth(data.margin)
        layout.top = ui.xheight(data.margin)
        layout.bottom = ui.xheight(data.margin)
    end
    
    layout:setComponent(component)
    return layout
end

-- @brief Initializes the margin layout builder.
local function init()
    ui.layout.builder.registerBuilder('margin',marginLayoutBuilder)
end

-- initialize
init()