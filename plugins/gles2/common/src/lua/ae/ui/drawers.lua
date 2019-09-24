-- @group UI
-- @brief Provides various drawers.
local drawers = {}

-- @brief Draws textured triangles.
drawers.tex = {
    -- @name drawers.tex.triangleCount
    -- @var
    -- @brief The maximum number of triangles the textured triangles drawer
    --   can draw.
    triangleCount = 1024
}

-- @brief Draws textured triangles with color support.
drawers.texColor = {
    -- @name drawers.texColor.triangleCount
    -- @var 
    -- @brief The maximum number of triangles the textured triangles drawer
    --   with color support can draw.
    triangleCount = 1024
}

-- @brief Initializes the textured triangles drawing drawer.
local function initTexDrawer()
    local triangleCount = drawers.tex.triangleCount
    -- buffer
    drawers.tex.buffer = ae.draw2d.TexDrawerBuffer.new(
        triangleCount * ae.draw2d.indicesPerTriangle,
        triangleCount * ae.draw2d.verticesPerTriangle)
        
    -- drawer
    drawers.tex.drawer = ae.draw2d.TexDrawer.new(
        true,false,false,drawers.tex.buffer)
end

-- @brief Initializes the textured triangles with color support drawer.
local function initTexColorDrawer()
    local triangleCount = drawers.tex.triangleCount
    -- buffer
    drawers.texColor.buffer = ae.draw2d.TexDrawerBuffer.new(
        triangleCount * ae.draw2d.indicesPerTriangle,
        triangleCount * ae.draw2d.verticesPerTriangle,true)
        
    -- drawer
    drawers.texColor.drawer = ae.draw2d.TexDrawer.new(
        false,false,true,drawers.texColor.buffer)
end

-- @brief Initializes the drawers.
local function init()
    initTexDrawer()
    initTexColorDrawer()
end

-- initialize the module
init()

return drawers