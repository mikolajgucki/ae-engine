-- @module ui.gpu.SolidRectangle
-- @brief A solid rectangle.
local SolidRectangle,super = ae.oo.subclass(ui.Component)
    
-- @func
-- @brief Creates a SolidRectangle object.
-- @param r The red component.
-- @param g The green component.
-- @param b The blue component.
-- @return A new SolidRectangle object. 
-- @func
-- @brief Creates a SolidRectangle object.
-- @param r The red component.
-- @param g The green component.
-- @param b The blue component.
-- @param a The alpha component.
-- @return A new SolidRectangle object. 
function SolidRectangle.new(r,g,b,a)
    local self = ae.oo.new(SolidRectangle)
    SolidRectangle.construct(self,r,g,b,a)
    
    return self
end

-- @func
-- @brief Constructs a SolidRectangle object.
-- @param self The object. 
-- @param r The red component.
-- @param g The green component.
-- @param b The blue component.
-- @func
-- @brief Constructs a SolidRectangle object.
-- @param self The object. 
-- @param r The red component.
-- @param g The green component.
-- @param b The blue component.
-- @param a The alpha component.
function SolidRectangle.construct(self,r,g,b,a)
    super.construct(self)
    self.r = r
    self.g = g
    self.b = b
    self.a = a or 1
end

-- @func
-- @brief Updates vertices in the default color drawer.
-- @func
-- @brief Updates vertices in a color drawer.
-- @param drawer The color drawer.
function SolidRectangle:updateDrawer(drawer)
    drawer = drawer or ui.gpu.drawers.color

    local x0 = self.bounds.x
    local y0 = self.bounds.y
    local x1 = self.bounds:x1()
    local y1 = self.bounds:y1()
    
    local r = self.r
    local g = self.g
    local b = self.b
    local a = self.a
    
    local vpos = self.vpos
    drawer:setVertex(vpos,    x0,y0, r,g,b,a)
    drawer:setVertex(vpos + 1,x1,y0, r,g,b,a)
    drawer:setVertex(vpos + 2,x1,y1, r,g,b,a)
    drawer:setVertex(vpos + 3,x0,y1, r,g,b,a)
    
    drawer:setIndices(self.ipos,{
        vpos,    vpos + 1,vpos + 3,
        vpos + 3,vpos + 1,vpos + 2})
end

-- @brief Appends the rectangle to a color drawer.
-- @param drawer The color drawer.
function SolidRectangle:appendToDrawer(drawer)
    self.vpos = drawer:reserveVertices(3)
    self.ipos = drawer:reserveIndices(6)
    self:updateDrawer(drawer)
end

return SolidRectangle