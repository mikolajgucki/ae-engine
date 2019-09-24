-- @group Animation
-- @brief The ease functions.
local ease = {}

-- @brief The linear brief.
-- @param t The input progress.
-- @return The output progress.
function ease.linear(t)
    return t
end

-- @brief Calculates ease value based on the cubic Bezier curve. The initial
--   points is (0,0) and the final one is (1,1)
-- @param x1 The X coordinate of the first control point.
-- @param y1 The Y coordinate of the first control point.
-- @param x2 The X coordinate of the second control point.
-- @param y2 The Y coordinate of the second control point.
-- @param t The input progress.
-- @return The output progress.
function ease.cubicBezier(x1,y1,x2,y2,t)
    local p = ae.math.cubicBezier(
        { x = 0, y = 0 },{ x = x1, y = y1 },
        { x = x2, y = y2 },{ x = 1, y = 1 },t)
    return p.y
end

return ease