-- @brief Provides display-related values and functions. 
local display = {}

-- @brief The display width in pixels
display.width = nil

-- @brief The display height in pixels.
display.height = nil

-- @brief Called when the display size has changed.
-- @param width The width of the display in pixels.
-- @param height The height of the display in pixels.
function display.resize(width,height)
    ae.log.trace('Display size is %ix%i',width,height)
    
    display.width = width
    display.height = height
    display.aspect = height / width
end

return display
