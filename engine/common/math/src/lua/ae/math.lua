-- @module ae.math
-- @group Math
-- @brief Basic math functions.
local ae_math = {}

-- @name .min
-- @brief Gets the minimum of two values (can be nil).
-- @param a The first value.
-- @param b The second value.
-- @return The minimum of the values or `nil` if both of the values are nil.
function ae_math.min(a,b)
    -- a,b
    if a and b then
        if a < b then
            return a
        end
        return b
    end
    
    -- a
    if a then
        return a
    end
    
    -- b
    if b then
        return b
    end
    
    -- none
    return nil
end

-- @name .max
-- @brief Gets the maximum of two values (can be nil).
-- @param a The first value.
-- @param b The second value.
-- @return The maximum of the values or `nil` if both of the values are nil.
function ae_math.max(a,b)
    -- a,b
    if a and b then
        if a > b then
            return a
        end
        return b
    end
    
    -- a
    if a then
        return a
    end
    
    -- b
    if b then
        return b
    end
    
    -- none
    return nil
end

-- @name .pow
-- @brief Returns power
-- @param base The base.
-- @param index The index.
-- @return The power.
function ae_math.pow(base,index)
    return base ^ index
end

-- @name .cubicBezier
-- @brief Calculates a point on a cubic Bezier curve.
-- @param p0 The initial point.
-- @param p1 The first control point.
-- @param p2 The second control point.
-- @param p3 The final point.
-- @param t The t-value between 0 and 1 inclusive.
-- @return The point on the curve.
function ae_math.cubicBezier(p0,p1,p2,p3,t)
    local t1 = 1 - t
    
    local a = t1 * t1 * t1
    local b = 3 * t1 * t1 * t
    local c = 3 * t1  * t * t
    local d = t * t * t
    
    return {
        x = a * p0.x + b * p1.x + c * p2.x + d * p3.x,
        y = a * p0.y + b * p1.y + c * p2.y + d * p3.y
    }
end

-- @name .inside2DTriangle
-- @func
-- @brief Tests if a 2D point is inside a 2D triagle.
-- @param p The point.
-- @param pa A triangle vertex.
-- @param pb A triangle vertex.
-- @param pc A triangle vertex.
-- @return `true` if inside, `false` otherwise.
-- @func
-- @brief Tests if a 2D point is inside a 2D triagle.
-- @param p The point.
-- @param pa A triangle vertex.
-- @param pb A triangle vertex.
-- @param pc A triangle vertex.
-- @param epsilon The triangle edge thinkness.
-- @return `true` if inside, `false` otherwise.
function ae_math.insideTriangle(p,pa,pb,pc,epsilon)
    -- calculate barycentric coordinates
    local denominator = ((pb.y - pc.y) * (pa.x - pc.x) +
        (pc.x - pb.x) * (pa.y - pc.y))
    
    local a = ((pb.y - pc.y) * (p.x - pc.x) +
        (pc.x - pb.x) * (p.y - pc.y)) / denominator
    local b = ((pc.y - pa.y) * (p.x - pc.x) +
        (pa.x - pc.x) * (p.y - pc.y)) / denominator
    local c = 1 - a - b
    
    epsilon = epsilon or 0
    return
        a >= -epsilon and a <= 1 + epsilon and
        b >= -epsilon and b <= 1 + epsilon and
        c >= -epsilon and c <= 1 + epsilon;
end

return ae_math