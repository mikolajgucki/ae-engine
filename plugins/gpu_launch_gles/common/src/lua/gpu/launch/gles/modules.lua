-- @brief Provides all the GPU Launch modules.
local modules = {}

-- @brief Gets all the GPU Launch modules.
-- @return The GPU Launch modules as table.
function modules.get()
    return {
        'gpu.launch.gles.drawer',
    }
end

return modules