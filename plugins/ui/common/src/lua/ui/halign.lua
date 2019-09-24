-- @group UI
-- @brief Contains horizontal alignment functions.
local halign = {}

-- @brief Aligns the left edge of the inner element with the left edge of the
--   outer element.
-- @param outer The size of the outer element.
-- @param inner The size of the inner element.
-- @return The X coordinate of the left edge of the inner element relative to
--   the left edge of the outer element.
function halign.left(outer,inner)
    return 0
end

-- @brief Aligns the center of the inner element with the center of the outer
--   element.
-- @param outer The size of the outer element.
-- @param inner The size of the inner element.
-- @return The X coordinate of the left edge of the inner element relative to
--   the left edge of the outer element.
function halign.center(outer,inner)
    return (outer - inner) / 2
end

-- @brief Aligns the right edge of the inner element with the right edge of the
--   outer element.
-- @param outer The size of the outer element.
-- @param inner The size of the inner element.
-- @return The X coordinate of the left edge of the inner element relative to
--   the left edge of the outer element.
function halign.right(outer,inner)
    return outer - inner
end

return halign

