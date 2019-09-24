-- @brief Builds Spine component from table data.

-- @brief The function which builds Spine component from table data.
-- @param data The table data.
-- @return The Spine component.
local function spineBuilder(data)
    -- validate
    if not data.name then
        error('Spine without name')
    end
    
    
    local component = ui.spine.SpineComponent.new(skeletonResource,id,bounds)
end

-- @brief Initializes the Spine builder.
local function init()
    ui.layout.builder.registerBuilder('spine',spineBuilder)
end

-- initialize
init()