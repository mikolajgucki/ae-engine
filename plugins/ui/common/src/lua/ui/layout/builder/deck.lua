-- @brief Builds deck layout from table data.

-- @brief The function which builds deck layout from table data.
-- @param data The table data.
-- @return The deck layout.
function deckLayoutBuilder(data)
    -- validate
    if not data.components then
        error('Deck layout without components')
    end
    
    -- layout
    local layout = ui.layout.DeckLayout.new()
    
    -- components
    for _,component in ipairs(data.components) do
        layout:append(ui.layout.builder.build(component))
    end
    
    return layout
end

-- @brief Initializes the deck layout builder.
function init()
    ui.layout.builder.registerBuilder('deck',deckLayoutBuilder)
end

-- initialize
init()