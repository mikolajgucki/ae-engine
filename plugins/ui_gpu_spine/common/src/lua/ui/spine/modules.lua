-- @brief Provides all the Spine modules.
local modules = {}

-- @brief Gets all the Spine modules.
-- @return The Spine modules as table.
function modules.get()
    return {
        'ui.spine.SpineComponent',
        'ui.spine.layout.builder.spine'
    }
end

return modules