local modules = {}

-- @brief Gets all the GPU OpenGL ES modules.
-- @return The GPU OpenGL ES modules as table.
function modules.get()
    return {
        'gpu.gles.bounds'
    }
end

return modules