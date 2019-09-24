-- @brief Builds grid layout from table data.

-- @brief The function which builds grid layout from table data.
-- @param data The table data.
-- @return The grid layout.
local function gridLayoutBuilder(data)
    -- validate
    if not data.columns then
        error('Grid layout without columns')
    end
    if not data.rows then
        error('Grid layout without rows')
    end
    if not data.cells then
        error('Grid layout without cells')
    end
    
    -- layout
    local layout = ui.layout.GridLayout.new(
        data.columns,data.rows,data.absolute)
    
    -- for each cell
    for _,cell in ipairs(data.cells) do
        -- validate the cell
        if not cell then
            error('Empty cell')
        end
        if not cell.x then
            error('Grid layout cell without X coordinate')
        end
        if not cell.y then
            error('Grid layout cell without X coordinate')
        end
        if not cell.component then
            error('Grid layout cell without component')
        end
        
        -- build the component
        local component = ui.layout.builder.build(cell.component)
        
        -- add to the layout
        layout:addComponent(component,cell.x,cell.y,cell.xspan,cell.yspan)
    end
    
    return layout
end

-- @brief Initializes the grid layout builder.
local function init()
    ui.layout.builder.registerBuilder('grid',gridLayoutBuilder)
end

-- initialize
init()