-- @module ui.gpu.drawers.TexColorDrawer
-- @brief A texture and color drawer wrapper.
local TexColorDrawer,super = ae.oo.subclass(ui.gpu.drawers.TexDrawer)
    
-- @brief Creates a TexColorDrawer object.
-- @param drawerCapacity The number of vertices supported by the drawer.
-- @param indexCapacity The number of vertices supported by the index.
-- @return A new TexColorDrawer object. 
function TexColorDrawer.new(drawerCapacity,indexCapacity)
    local self = ae.oo.new(TexColorDrawer)
    TexColorDrawer.construct(self,drawerCapacity,indexCapacity)
    
    return self
end

-- @brief Constructs a TexColorDrawer object.
-- @param self The object. 
-- @param drawerCapacity The number of vertices supported by the drawer.
-- @param indexCapacity The number of vertices supported by the index.
function TexColorDrawer.construct(self,drawerCapacity,indexCapacity)
    super.construct(self,drawerCapacity,indexCapacity)
end

-- @brief Creates the drawer itself.
-- @param capacity The number of vertices supported by the drawer.
-- @return The created drawer.
function TexColorDrawer:createDrawer(capacity)
    local drawer = gpu.DefaultDrawer.new('texture,vertex.color',capacity)
    
    -- keep the buffer on the GPU side
    drawer:moveToGPU()
    drawer:deleteCPUData()
    
    return drawer
end

-- @func
-- @brief Sets a vertex.
-- @param position The vertex position.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @param u The U coordinate.
-- @param v The V coordinate.
-- @param r The red component.
-- @param g The green component.
-- @param b The blue component.
-- @param a The alpha component.
-- @func
-- @brief Sets a vertex.
-- @param position The vertex position.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @param u The U coordinate.
-- @param v The V coordinate.
-- @param r The red component.
-- @param g The green component.
-- @param b The blue component.
function TexColorDrawer:setVertex(position,x,y,u,v,r,g,b,a)
    self.drawer.drawer:setCoords(position,x,y,u,v)
    self.drawer.drawer:setVertexColor(position,r,g,b,a or 1)
end

-- @func
-- @brief Sets a rectangle.
-- @param position The position of the first vertex.
-- @param x The X coordinate of the lower-left corner.
-- @param y The Y coordinate of the lower-left corner.
-- @param width The rectangle width.
-- @param height The rectangle height.
-- @param subtexture The subtexture.
-- @param r The red component.
-- @param g The green component.
-- @param b The blue component.
-- @param a The alpha component.
-- @func
-- @brief Sets a rectangle.
-- @param position The position of the first vertex.
-- @param x The X coordinate of the lower-left corner.
-- @param y The Y coordinate of the lower-left corner.
-- @param width The rectangle width.
-- @param height The rectangle height.
-- @param subtexture The subtexture.
-- @param r The red component.
-- @param g The green component.
-- @param b The blue component.
function TexColorDrawer:setRect(position,x,y,width,height,subtexture,r,g,b,a)
    local x1 = x + width
    local y1 = y + height
    
    local u0 = subtexture.u
    local v0 = subtexture.v
    local u1 = subtexture.u + subtexture.width
    local v1 = subtexture.v + subtexture.height

    self:setVertex(position,     x,y,   u0,v1, r,g,b,a)
    self:setVertex(position + 1, x1,y,  u1,v1, r,g,b,a)
    self:setVertex(position + 2, x1,y1, u1,v0, r,g,b,a)
    self:setVertex(position + 3, x,y1,  u0,v0, r,g,b,a)    
end

return TexColorDrawer