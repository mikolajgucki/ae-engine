-- @group UI
-- @brief The UI component which displays a texture subimage.
local Image,super = ae.oo.subclass(ae.ui.ItemComponent)

-- @brief Resizes the image to occupy as much of the component bounds as
--   possible keeping the aspect of the texture subimage.
-- @param self The image object.
local function fitImage(self)
    local aspect = self.subtexture.aspect
    local ibounds = self.itemBounds
   
    -- try to fit horizontally
    ibounds.width = self.bounds.width
    ibounds.height = ae.ui.size.getHeight(ibounds.width,aspect)
   
    -- go vertically if horizontally doesn't fit
    if (ibounds.height > self.bounds.height) then
        ibounds.height = self.bounds.height
        ibounds.width = ae.ui.size.getWidth(ibounds.height,aspect)
    end
end

-- @brief Resizes the image to occupy the entire component bounds keeping
--   the aspect of the texture image.
-- @param self The image object.
local function fillImage(self)
    local aspect = self.subtexture.aspect
    local ibounds = self.itemBounds

    -- try to fill horizontally
    ibounds.width = self.bounds.width
    ibounds.height = ae.ui.size.getHeight(ibounds.width,aspect)
   
    -- go vertically if horizontally doesn't fill
    if (ibounds.height < self.bounds.height) then
        ibounds.height = self.bounds.height
        ibounds.width = ae.ui.size.getWidth(ibounds.height,aspect)
    end
end

-- @brief Resizes the image to occupy the entire component bounds. The entire
--   subtexture occupies the component bounds.
-- @param self The image object.
local function occupyImage(self)
    self.itemBounds:assign(self.bounds)
end

-- @func
-- @brief Creates an image with no bounds set.
-- @param subtexture The sub-texture.
-- @return The image.
-- @func
-- @brief Creates an image.
-- @param subtexture The sub-texture.
-- @param bounds The initial image bounds.
-- @return The image.
function Image.new(subtexture,bounds)
    local self = ae.oo.new(Image)
    Image.construct(self,subtexture,bounds)
    
    return self
end

-- @func
-- @brief Constructs an image with no bounds set.
-- @param self The image object.
-- @param subtexture The texture subimage.
-- @func
-- @brief Constructs an image.
-- @param self The image object.
-- @param subtexture The texture subimage.
-- @param bounds The initial image bounds.
function Image.construct(self,subtexture,bounds)
    super.construct(self,bounds)
    
    self.subtexture = subtexture
    self.uv = subtexture
    self.blend = true
    self.color = ae.ui.Color.new()
    self.texture = ae.ui.textures:load(subtexture.filename)
    
    self.resizeItem = fitImage
    self:updateItemBounds()
end

-- @brief Enables or disables the blend when drawing.
-- @param enabled `true` to enable, `false` otherwise.
function Image:setBlend(enabled)
    self.blend = enabled
end

-- @brief Sets the image color.
-- @param r The red component.
-- @param g The green component.
-- @param b The blue component.
-- @param a The alpha component.
function Image:setColor(r,g,b,a)
    self.color:set(r,g,b,a)
end

-- @brief Sets the alpha component of the color.
-- @param a The alpha component.
function Image:setAlpha(a)
    self.color.a = a
end

-- @brief Sets the subtexture.
-- @param subtexture The subtexture.
function Image:setSubTexture(subtexture)
    self.subtexture = subtexture
    self.uv = subtexture
end

-- @brief Fits the image to occupy as much of the bounds as possible keeping
--   the aspect of the texture subimage.
function Image:fit()
   self.resizeItem = fitImage
end

-- @brief Fills the image to occupy the entire component bounds keeping the
--   aspect of the subtexture.
function Image:fill()
    self.resizeItem = fillImage
end

-- @brief Fills the image to occupy the entire component bounds. The entire
--   subtexture occupies the component bounds.
function Image:occupy()
    self.resizeItem = occupyImage
end

-- @brief Sets the UV coordinates of the texture subimage.
-- @param u The U coordinate.
-- @param v The V coordinate.
-- @param width The width of the texture subimage.
-- @param height The height of the texture subimage.
function Image:setUV(u,v,width,height)
    self.uv = {
        u = u,
        v = v,
        width = width,
        height = height        
    }
end

-- @brief Draws the image.
function Image:draw()
    if not self.visible or self.itemBounds:isOutsideDisplay() then
        return
    end
    
    -- texture subimage
    Image.buffer:setRectTexCoords(0,self.uv)
    
    -- start drawing
    local drawer = Image.drawer
    drawer:drawStart()
    
    -- color
    drawer:setColor(self.color)    
    
    -- blend, texture
    ae.gl.setBlendEnabled(self.blend)
    self.texture.texture:bind()
    
    -- coordinates
    local ibounds = self.itemBounds
    drawer:getMatrix():translateAndScale(
        ibounds.x,ibounds.y,0,ibounds.width,ibounds.height,1)
    drawer:updateTransformation()
    
    -- draw
    drawer:draw(ae.draw2d.indicesPerRect)
end

-- @brief Gets a string which represents the image.
-- @return The string representing the image.
function Image:__tostring()
    return ae.oo.tostring('ae.ui.Image',nil,super.__tostring(self))
end

-- @brief Initializes the image component.
local function init()
    -- drawer buffer
    local buffer = ae.draw2d.TexDrawerBuffer.new(
        ae.draw2d.verticesPerRect,ae.draw2d.indicesPerRect)
    Image.buffer = buffer
    
    -- rectangle
    buffer:appendRect(0,0, 1,1)
    
    -- VBO
    buffer:createIndexVBO()
    
    -- create drawer
    Image.drawer = ae.draw2d.TexDrawer.new(true,true,false,buffer)
end

-- initialize the module
init()

return Image
