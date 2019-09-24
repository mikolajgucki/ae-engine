-- @brief Provides all the UI modules.
local modules = {}

-- @brief Gets all the GPU UI modules.
-- @return The GPU UI modules as table.
function modules.get()
    return {
        'ui',
        'ui.size.preferred',
        'ui.halign',
        'ui.valign',
        'ui.Bounds',
        'ui.Color',
        'ui.Component',
        'ui.Container',
        
        'ui.DefaultGrid',
        'ui.grid',
        
        'ui.layout.Layout',
        'ui.layout.GridLayout',
        'ui.layout.AlignmentLayout',
        'ui.layout.MarginLayout',
        'ui.layout.DeckLayout',
        
        'ui.layout.builder',
        'ui.layout.builder.grid',
        'ui.layout.builder.alignment',
        'ui.layout.builder.margin',
        'ui.layout.builder.deck'
    }
end

return modules