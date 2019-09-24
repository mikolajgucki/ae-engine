-- @group UI
-- @brief Provides functions related to calculating width and height
local size = {}

-- @func
-- @brief Gets the width of an rectangle so that it will be drawn as square.
-- @param height The height of the rectangle.
-- @return The width of the rectangle.
-- @func
-- @brief Gets the width of an rectangle so that the rectangle aspect is
--   preserved when drawn on the display.
-- @param height The height of the rectangle.
-- @param aspect The aspect of the rectangle.
-- @return The width of the rectangle.
function size.getWidth(height,aspect)
    aspect = aspect or 1
    return height * ae.ui.aspect / aspect
end

-- @func
-- @brief Gets the height of an rectangle so that it will be drawn as square.
-- @param width The width of the rectangle.
-- @param aspect The aspect of the rectangle.
-- @return The height of the rectangle.
-- @func
-- @brief Gets the height of an rectangle so that the rectangle aspect is
--   preserved when drawn on the rectangle.
-- @param width The width of the rectangle.
-- @param aspect The aspect of the rectangle.
-- @return The height of the rectangle.
function size.getHeight(width,aspect)
    aspect = aspect or 1
    return width * aspect / ae.ui.aspect
end

return size