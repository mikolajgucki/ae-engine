-- @group UI
-- @brief Represents rectangle bounds.
local Bounds = ae.oo.class()

-- @func
-- @brief Constructs UI bounds with coordinates (0,0) and size 0x0.
-- @return The bounds.
-- @func
-- @brief Constructs UI bounds.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @param width The bounds width.
-- @param height The bounds height.
-- @return The bounds.
function Bounds.new(x,y,width,height)
    local self = ae.oo.new(Bounds)
    Bounds.construct(self,x,y,width,height)
    
    return self
end

-- @func
-- @brief Constructs bounds with coordinates (0,0) and size 0x0.
-- @param self The bounds object.
-- @func
-- @brief Constructs bounds.
-- @param self The bounds object.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @param width The bounds width.
-- @param height The bounds height.
function Bounds.construct(self,x,y,width,height)
-- @name Bounds.x
-- @var
-- @brief The X coordinate.
    self.x = x or 0
    
-- @name Bounds.y
-- @var
-- @brief The Y coordinate.
    self.y = y or 0
    
-- @name Bounds.width
-- @var
-- @brief The bounds width.
    self.width = width or 0
    
-- @name Bounds.height
-- @var
-- @brief The bounds height.
    self.height = height or 0
end

-- @brief Makes a deep copy of other bounds object.
-- @param bounds The bounds.
-- @return The new bounds object.
function Bounds.copy(bounds)
    return Bounds.new(bounds.x,bounds.y,bounds.width,bounds.height)
end

-- @brief Assigns values of other bounds to these bounds.
-- @param bounds The other bounds.
function Bounds:assign(bounds)
    self.x = bounds.x
    self.y = bounds.y
    self.width = bounds.width
    self.height = bounds.height
end

-- @brief Sets the location.
-- @param x The X coordinate.
-- @param y The Y coordinate.
function Bounds:setLocation(x,y)
    self.x = x
    self.y = y
end

-- @brief Sets the size.
-- @param width The bounds width.
-- @param height The bounds height.
function Bounds:setSize(width,height)
    self.width = width
    self.height = height
end

-- @brief Sets the location and size.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @param width The bounds width.
-- @param height The bounds height.
function Bounds:setBounds(x,y,width,height)
    self.x = x
    self.y = y
    self.width = width
    self.height = height
end

-- @brief Gets the X coordinate of the upper-right corner.
-- @return The X coordinate of the upper-right corner.
function Bounds:x1()
    return self.x + self.width
end

-- @brief Gets the Y coordinate of the upper-right corner.
-- @return The Y coordinate of the upper-right corner.
function Bounds:y1()
    return self.y + self.height
end

-- @brief Gets the coordinates of the bounds center.
-- @return The bounds center.
function Bounds:center()
    return {
        x = self.x + self.width / 2,
        y = self.y + self.height / 2
    }
end

-- @brief Tests if a point is inside the bounds.
-- @param x The point X coordinate.
-- @param y The point Y coordinate.
-- @return `true' if the point is inside the bounds, `false` otherwise.
function Bounds:inside(x,y)
    return x >= self.x and x < (self.x + self.width) and
        y >= self.y and y < (self.y + self.height)
end

-- @brief Tests if the bounds are fully outside other bounds.
-- @param bounds The other bounds.
-- @return `true` if the bounds are fully outside the other bounds,
--   `false` otherwise.
function Bounds:outside(bounds)
    return
        self.x >= (bounds.x + bounds.width) or
        self.y >= (bounds.y + bounds.height) or
        (self.x + self.width) <= bounds.x or
        (self.y + self.height) <= bounds.y
end

-- @brief Makes the bounds fill the entire display.
function Bounds:setFull()
    self:assign(Bounds.full)
end

-- @brief Tests if the bounds are outside the display.
-- @return `true` if outside, `false` otherwise.
function Bounds:isOutsideDisplay()
    return self:outside(Bounds.full)
end

-- @func
-- @brief Applies margins to the bounds.
-- @param horizontal The top and bottom margin.
-- @param vertical The right and left margin.
-- @func
-- @brief Applies margins to the bounds.
-- @param top The top margin.
-- @param right The right margin.
-- @param bottom The bottom margin.
-- @param left The left margin.
function Bounds:applyMargins(top,right,bottom,left)
    bottom = bottom or top
    left = left or right

    self.x = self.x + left
    self.y = self.y + bottom
    
    self.width = self.width - left - right
    self.height = self.height - top - bottom
end

-- @func
-- @brief Gets XY from UV coordinates as if the bounds were a texture.
-- @param uv The UV coordinates in a single table.
-- @return The XY coordinates in a single table.
-- @func
-- @brief Gets XY from UV coordinates as if the bounds were a texture.
-- @param u The U coordinate.
-- @param v The V coordinate.
-- @return The XY coordinates in a single table.
function Bounds:coordsFromUV(u,v)
    if not v then
        local uv = u
        u = uv.u
        v = uv.v
    end

    return {
        x = self.x + self.width * u,
        y = self.y + self.height * v
    }
end

-- @brief Scales the bounds around the center. The center of the bounds
--   remains unchanged.
-- @param sx The scale along the X axis.
-- @param sy The scale along the Y axis.
function Bounds:scaleAroundCenter(sx,sy)
    local newWidth = self.width * sx
    local newHeight = self.height * sy
    
    self.x = self.x + (self.width - newWidth) / 2
    self.y = self.y + (self.height - newHeight) / 2
    self.width = newWidth
    self.height = newHeight
end

-- @brief Gets a string which represents the bounds.
-- @return The string representing the bounds.
function Bounds:__tostring()
    return ae.oo.tostring('ae.ui.Bounds',
        'x=' .. self.x ..
        ', y=' .. self.y .. 
        ', width=' .. self.width ..
        ', height=' .. self.height)
end

return Bounds

