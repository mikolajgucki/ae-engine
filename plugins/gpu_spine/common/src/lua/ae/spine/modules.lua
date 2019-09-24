-- @brief Provides all the Spine modules.
local modules = {}

-- @brief Gets all the Spine modules.
-- @return The Spine modules as table.
function modules.get()
    return {
        'ae.spine.SpineSkeletonResource',
        'ae.spine.SpineSkeletonLoader'
    }
end

return modules