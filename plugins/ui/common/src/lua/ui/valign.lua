-- @group UI
-- @brief Contains vertical alignment functions.
local valign = {}

-- @brief Aligns the top of the inner element with the top of the outer element.
-- @param outer The size of the outer element.
-- @param inner The size of the inner element.
-- @return The Y coordinate of the bottom of the inner element relative to the
--   bottom of the outer element.
function valign.top(outer,inner)
    return outer - inner
end

-- @brief Aligns the center of the inner element with the center of the outer
--   element.
-- @param outer The size of the outer element.
-- @param inner The size of the inner element.
-- @return The Y coordinate of the bottom of the inner element relative to the
--   bottom of the outer element.
function valign.middle(outer,inner)
    return (outer - inner) / 2
end

-- @brief Aligns the bottom of the inner element with the bottom of the outer
--   element.
-- @param outer The size of the outer element.
-- @param inner The size of the inner element.
-- @return The Y coordinate of the bottom of the inner element relative to the
--   bottom of the outer element.
function valign.bottom(outer,inner)
    return 0
end

return valign