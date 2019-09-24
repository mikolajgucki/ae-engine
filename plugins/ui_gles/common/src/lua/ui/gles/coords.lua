-- @brief Provides functions related to OpenGL coordinates.

-- @brief Converts display coordinates to GL coordinates.
-- @param x The display X coordinate.
-- @param y The display Y coordinate.
-- @return The GL coordinates `{ x = ..., y = ... }`.
local function getGLCoords(x,y)
    return {
        x = (x / ae.display.width) * 2 - 1,
        y = 1 - (y / ae.display.height) * 2
    }    
end

-- Initializes the UI coordinates.
local function init()
    ui.gpu.getGPUCoords = getGLCoords
end

-- initialize
init()