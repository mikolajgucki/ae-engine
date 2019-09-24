-- @group UI
-- @brief A component which draws a subtexture.
local Image,super = ae.oo.subclass(ui.Component)
    
-- @brief Creates an Image object.
-- @param subtexture The subtexture.
-- @return A new Image object. 
function Image.new(subtexture)
    local self = ae.oo.new(Image)
    Image.construct(self,subtexture)
    
    return self
end

-- @brief Constructs an Image object.
-- @param self The object. 
-- @param subtexture The subtexture.
function Image.construct(self,subtexture)
    super.construct(self)
    self.subtexture = subtexture
    self.textureResource = ae.loaders.textures:load(subtexture.filename)
end

-- @brief Gets the texture.
-- @return The texture.
function Image:getTexture()
    return self.textureResource.texture
end

-- @brief Gets the image (height-to-width) aspect.
-- @return The aspect.
function Image:getAspect()
    return self.subtexture.aspect
end

-- @brief Sets the image color.
-- @param color The color.
function Image:setColor(color)
    self.color = color
end

-- @func
-- @brief Updates vertices in the default texture drawer.
-- @func
-- @brief Updates vertices in a texture drawer.
-- @param drawer The texture drawer.
function Image:updateDrawer(drawer)
    drawer = drawer or ui.gpu.drawers.tex

    local subtexture = self.subtexture
    local u0 = subtexture.u
    local v0 = subtexture.v
    local u1 = subtexture.u + subtexture.width
    local v1 = subtexture.v + subtexture.height
    
    local x0 = self.bounds.x
    local y0 = self.bounds.y
    local x1 = self.bounds:x1()
    local y1 = self.bounds:y1()

    local r,g,b,a
    if self.color then
        r,g,b,a = self.color:rgba()
    end
    
    local vpos = self.vpos
    drawer:setVertex(vpos,    x0,y0, u0,v1, r,g,b,a)
    drawer:setVertex(vpos + 1,x1,y0, u1,v1, r,g,b,a)
    drawer:setVertex(vpos + 2,x1,y1, u1,v0, r,g,b,a)
    drawer:setVertex(vpos + 3,x0,y1, u0,v0, r,g,b,a)
    
    drawer:setIndices(self.ipos,{
        vpos,    vpos + 1,vpos + 3,
        vpos + 3,vpos + 1,vpos + 2})
end

-- @func
-- @brief Appends the image to a drawer.
-- @full Appends the image to a drawer. The drawer must be either texture
--   drawer (`ui.gpu.drawers.TexDrawer`) or texture/color drawer if the color
--   is set  (`ui.gpu.drawers.TexColorDrawer`).
-- @param drawer The drawer.
-- @func
-- @brief Appends the image to a drawer.
-- @full Appends the image to a drawer. The drawer must be either texture
--   drawer (`ui.gpu.drawers.TexDrawer`) or texture/color drawer if the color
--   is set  (`ui.gpu.drawers.TexColorDrawer`).
-- @param drawer The drawer.
-- @param vpos The position of the first vertex.
-- @param ipos The position of the first index.
function Image:appendToDrawer(drawer,vpos,ipos)
    self.vpos = vpos or drawer:reserveVertices(4)
    self.ipos = ipos or drawer:reserveIndices(6)
    self:updateDrawer(drawer)
end

-- @brief Sets the subtexture
-- @param subtexture The subtexture.
function Image:setSubtexture(subtexture)
    if subtexture.filename ~= self.subtexture.filename then
        error('Attempt to set a subtexture from a different texture')
    end
    self.subtexture = subtexture
    self:updateDrawer()
end

-- @brief Gets the string representation of the image.
-- @return The string representation of the image.
function Image:__tostring()
    return ae.oo.tostring('ui.gpu.Image',string.format('subtexture=%s@%s',
        self.subtexture.id,self.subtexture.filename),super.__tostring(self))
end

return Image