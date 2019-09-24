local modules = {}

-- @brief Gets all the UI OpenGL ES modules.
-- @return The UI OpenGL ES modules as table.
function modules.get()
    return {
        'ui.gles.coords',
        'ui.gles.bounds'
    }
end

return modules