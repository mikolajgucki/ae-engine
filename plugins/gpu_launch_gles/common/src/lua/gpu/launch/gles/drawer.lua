-- @brief ...

-- @brief Appends a texture rectangle to a defautl drawer.
-- @param drawer The drawer to which to append.
-- @param x0 The X coordinate of the lower-left corner.
-- @param y0 The Y coordinate of the lower-left corner.
-- @param width The rectangle width.
-- @param height The rectangle height.
-- @param subtexture The subtexture from which to take the UV coordinates.
local function glesAppendTexRect(drawer,x0,y0,width,height,subtexture)
    local x1 = x0 + width
    local y1 = y0 + height
    
    local u0 = subtexture.u
    local v0 = subtexture.v
    local u1 = subtexture.u + subtexture.width
    local v1 = subtexture.v + subtexture.height
    
    drawer:setCoords(0,x0,y0, u0,v1)
    drawer:setCoords(1,x1,y0, u1,v1)
    drawer:setCoords(2,x0,y1, u0,v0)
    
    drawer:setCoords(3,x1,y1, u1,v0)
    drawer:setCoords(4,x1,y0, u1,v1)
    drawer:setCoords(5,x0,y1, u0,v0)    
end

-- @brief Initializes the GPU Launch drawer.
local function init()
    gpu.launch.drawer.appendTexRect = glesAppendTexRect
end

-- initialize
init()

return drawer