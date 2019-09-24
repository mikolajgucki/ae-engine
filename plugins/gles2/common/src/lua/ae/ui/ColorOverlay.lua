-- @group UI
-- @brief Fills the component bounds with a single color.
local ColorOverlay,super = ae.oo.subclass(ae.ui.Component)

-- @func
-- @brief Creates a black overlay.
-- @return The new color overlay.
-- @func
-- @brief Creates a color overlay.
-- @param color The overlay color.
-- @return The new color overlay.
function ColorOverlay.new(color)
    local self = ae.oo.new(ColorOverlay)
    ColorOverlay.construct(self,color)
    
    return self
end

-- @func
-- @brief Constructs a black overlay.
-- @param self The color overlay object.
-- @func
-- @brief Constructs a color overlay.
-- @param self The color overlay object.
-- @param color The overlay color.
function ColorOverlay.construct(self,color)
    super.construct(self)
    self.color = color or ae.ui.Color.new(0,0,0,0)
end

-- @func
-- @brief Sets the overlay color.
-- @param color The overlay color.
-- @func
-- @brief Sets the overlay color.
-- @param r The red component.
-- @param g The green component.
-- @param b The blue component.
-- @param a The alpha component.
function ColorOverlay:setColor(r,g,b,a)
    if g then
        self.color:set(r,g,b,a)
    else
        local color = r
        self.color:set(color.r,color.g,color.b,color.a)
    end    
end

-- @brief Sets the alpha component of the overlay color.
-- @param a The alpha component.
function ColorOverlay:setAlpha(a)
    self.color.a = a
end

-- @brief Draws the color overlay.
function ColorOverlay:draw()
    if not self.visible then
        return
    end
    
    -- start drawing
    local drawer = ColorOverlay.drawer
    drawer:drawStart()    
    
    -- blend, color
    ae.gl.setBlendEnabled(true)    
    drawer:setColor(self.color)

    -- coordinates
    drawer:getMatrix():translateAndScale(
        self.bounds.x,self.bounds.y,0,self.bounds.width,self.bounds.height,1)
    drawer:updateTransformation()
    
    -- draw
    drawer:draw(ae.draw2d.indicesPerRect)    
end

-- @brief Gets a string which represents the color overlay.
-- @return The string representing the color overlay.
function ColorOverlay:__tostring()
    return ae.oo.tostring('ae.ui.ColorOverlay',
        'color=' .. tostring(self.color),super.__tostring(self))
end

-- @brief Initializes the color overlay component.
local function init()
    -- drawer buffer
    local buffer = ae.draw2d.ColorDrawerBuffer.new(
        ae.draw2d.verticesPerRect,ae.draw2d.indicesPerRect)
    ColorOverlay.buffer = buffer
    
    -- rectangle
    buffer:appendRect(0,0, 1,1)
    
    -- drawer
    ColorOverlay.drawer = ae.draw2d.ColorDrawer.new(true,buffer)
end

-- initialize the module
init()

return ColorOverlay