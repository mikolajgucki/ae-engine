-- @brief The basic UI functions and variables.
local ui = {}

-- @brief The display width.
ui.width = 0

-- @brief The display height.
ui.height = 0

-- @brief The display height-to-width aspect.
ui.aspect = 0


-- @brief This method must be called when size of the display changes.
-- @param width The width in pixels.
-- @param height The height in pixels.
-- @full This method must be called when size of the display changes.
--   Typically this function is called from `ae.display.resize(width,height)`.
function ui.resize(width,height)
    ui.width = width
    ui.height = height    
    ui.aspect = ui.height / ui.width
end

-- @brief Gets the width given as factory of the full bounds width.
-- @param factor The factory.
-- @return The width.
function ui.xwidth(factor)
    return ui.Bounds.full.width * factor
end

-- @brief Gets the height given as factory of the full bounds height.
-- @param factor The factory.
-- @return The width.
function ui.xheight(factor)
    return ui.Bounds.full.height * factor
end

-- @brief Initializes the UI module.
local function init()
    if ae.display then
        ui.resize(ae.display.width,ae.display.height)
    end
end

-- initialize on load
init()

return ui