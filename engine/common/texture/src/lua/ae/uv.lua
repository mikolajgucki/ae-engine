-- @group GLES2
-- @brief Provides UV coordinates-related functions.
local uv = {}

-- @func
-- @brief Makes a UV rectangle smaller.
-- @param rect The original UV rectangle (remains unchanged).
-- @param top The top inset value.
-- @param right The right inset value.
-- @param bottom The bottom inset value.
-- @param left The left inset value.
-- @return The shrunk UV coordinates.
-- @func
-- @brief Makes a UV rectangle smaller.
-- @param rect The origin UV rectangle (remains unchanged).
-- @param inset The inset table with fields `top`, `right`, `bottom`,
--   `left`.
-- @return The shrunk UV coordinates.
function uv.shrink(rect,top,right,bottom,left)
    if not right then
        local margins = top
        top = margins.top
        right = margins.right
        bottom = margins.bottom
        
    end

    local copy = {
        u = rect.u + left,
        v = rect.v + top,
    
        width = rect.width - left - right,
        height = rect.height - top - bottom
    }
    
    return copy
end

-- @brief Shrinks a UV rectangle by a pixel size.
-- @param rect The origin UV rectangle (remains unchanged).
-- @param pixel The pixel size (table with values `width` and `height`).
-- @param factor The factor of the pixel size to take.
-- @return The shrunk UV coordinates.
function uv.shrinkByPixel(rect,pixel,factor)
    local hsize = pixel.width * factor
    local vsize = pixel.height * factor
    return uv.shrink(rect,vsize,hsize,vsize,hsize)
end

return uv