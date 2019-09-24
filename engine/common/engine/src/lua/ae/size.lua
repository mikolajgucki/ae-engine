-- @group UI
-- @brief Provides functions related to calculating width and height
local size = {}

-- @func
-- @brief Gets the width of a square.
-- @param height The height of the square.
-- @func
-- @brief Gets the width of an rectangle keeping its aspect.
-- @param height The height of the rectangle.
-- @param aspect The (height-to-width) aspect.
function size.getRawWidth(height,aspect)
    aspect = aspect or 1
    return height / aspect
end

-- @func
-- @brief Gets the height of a square.
-- @param width The width of the square.
-- @func
-- @brief Gets the height of an rectangle keeping its aspect.
-- @param width The width of the rectangle.
-- @param aspect The (height-to-width) aspect.
function size.getRawHeight(width,aspect)
    aspect = aspect or 1
    return width * aspect
end

-- @func
-- @brief Gets the width of an rectangle so that it will be drawn as square.
-- @full Gets the width of an rectangle so that it will be drawn as square.
--   This function takes into account the display aspect.
-- @param height The height of the rectangle.
-- @return The width of the rectangle.
-- @func
-- @brief Gets the width of an rectangle so that the rectangle aspect is
--   preserved when drawn on the display.
-- @full Gets the width of an rectangle so that the rectangle aspect is
--   preserved when drawn on the display. This function takes into account the
--   display aspect.
-- @param height The height of the rectangle.
-- @param aspect The (height-to-width) aspect of the rectangle.
-- @return The width of the rectangle.
function size.getWidth(height,aspect)
    aspect = aspect or 1
    return height * ae.display.aspect / aspect
end

-- @func
-- @brief Gets the height of an rectangle so that it will be drawn as square.
-- @full Gets the height of an rectangle so that it will be drawn as square.
--   This function takes into account the display aspect.
-- @param width The width of the rectangle.
-- @return The height of the rectangle.
-- @func
-- @brief Gets the height of an rectangle so that the rectangle aspect is
--   preserved when drawn on the rectangle.
-- @full Gets the height of an rectangle so that the rectangle aspect is
--   preserved when drawn on the rectangle. This function takes into account the
--   display aspect.
-- @param width The width of the rectangle.
-- @param aspect The (height-to-width) aspect of the rectangle.
-- @return The height of the rectangle.
function size.getHeight(width,aspect)
    aspect = aspect or 1
    return width * aspect / ae.display.aspect
end

return size