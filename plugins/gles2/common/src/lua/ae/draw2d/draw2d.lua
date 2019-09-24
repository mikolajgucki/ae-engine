-- @group UI
-- @brief Provides various draw 2D functions.
local draw2d = {}

-- @brief The number of indices necessary to draw a standalone triangle.
draw2d.indicesPerTriangle = 3

-- @brief The number of vertices necessary to draw a standalone triangle.
draw2d.verticesPerTriangle = 3

-- @brief The number of indices necessary to draw a standalone rectangle.
draw2d.indicesPerRect = 6

-- @brief The number of vertices necessary to draw a standalone rectangle.
draw2d.verticesPerRect = 4

-- @brief Enables or disables color blending.
-- @param enabled `true` to enable, `false` to disable.
function draw2d.setBlendEnabled(enabled)
    error('draw2d.setBlendEnabled not specified')
end

-- @brief Loads the necessary sources and performs initialization.
function draw2d.init()
    draw2d.setBlendEnabled = ae.gl.setBlendEnabled
end

-- submodules
draw2d.shader = require('ae.draw2d.shader')
draw2d.ColorDrawerBuffer = require('ae.draw2d.ColorDrawerBuffer')
draw2d.ColorDrawer = require('ae.draw2d.ColorDrawer')
draw2d.TexDrawer = require('ae.draw2d.TexDrawer')
draw2d.TexDrawerBuffer = require('ae.draw2d.TexDrawerBuffer')

return draw2d