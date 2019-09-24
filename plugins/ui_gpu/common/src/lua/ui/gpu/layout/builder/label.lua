-- @brief Builds label from table data.

-- @brief The function which builds a label from table data.
-- @param data The table data.
-- @return The label.
local function labelBuilder(data)
    -- validate
    if not data.font then
        error('Label without font data')
    end
    if not data.text then
        error('Label without text')
    end
    
    local label = ui.gpu.Label.new(data.font,data.text)
    label.id = data.id
    
    -- size
    if data.height then
        label.height = ui.Bounds.full:getHeight(data.height)
    end
    if data.width then
        label.width = ui.Bounds.full:getWidth(data.width)
    end
    
    return label
end

-- @brief Initialize the label builder.
function init()
    ui.layout.builder.registerBuilder('label',labelBuilder)
end

-- initialize
init()