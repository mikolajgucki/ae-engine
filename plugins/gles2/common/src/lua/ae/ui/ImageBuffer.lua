-- @group UI
-- @brief Buffers and draws a number of images.
-- @full Buffers and draws a number of images. The subtextures of the images
--   must belong to the same texture.
local ImageBuffer,super = ae.oo.subclass(ae.ui.Component)

-- @func
-- @brief Creates an image buffer without color support.
-- @param texture The texture.
-- @return The new image buffer.
-- @func
-- @brief Creates an image buffer
-- @param texture The texture.
-- @param colorSupport Indicates if the buffer is to support colors.
-- @return The new image buffer.
function ImageBuffer.new(texture,colorSupport)
    local self = ae.oo.new(ImageBuffer)
    ImageBuffer.construct(self,texture,colorSupport)
    
    return self
end

-- @func
-- @brief Constructs an image buffer without color support.
-- @param self The image buffer object.
-- @param texture The texture.
-- @func
-- @brief Constructs an image buffer.
-- @param self The image buffer object.
-- @param texture The texture.
-- @param colorSupport Indicates if the buffer is to support colors.
function ImageBuffer.construct(self,texture,colorSupport)
    super.construct(self)
    self.texture = ae.ui.textures:load(texture.filename)
    self.colorSupport = colorSupport
    self.images = {}
    
    -- pick drawer
    if colorSupport then
        self.drawer = ImageBuffer.drawerColor
        self.transformation = ImageBuffer.transformationColor
    else
        self.drawer = ImageBuffer.drawer
        self.transformation = ImageBuffer.transformation
    end    
end

-- @func
-- @brief Appends an image to the buffer.
-- @param bounds The image bounds.
-- @param uv The UV coordinates.
-- @param r The red component of the image.
-- @param g The green component of the image.
-- @param b The blue component of the image.
-- @param a The alpha component of the image.
-- @return The index of the image within the buffer.
-- @func
-- @brief Appends an image to the buffer.
-- @param bounds The image bounds.
-- @param uv The UV coordinates.
-- @param color The image color.
-- @return The index of the image within the buffer.
function ImageBuffer:appendImage(bounds,uv,r,g,b,a)
    -- color
    local color = r
    
    -- rgba
    if r and g then
        color = {
            r = r,
            g = g,
            b = b,
            a = a
        }
    end
    
    ae.itable.append(self.images,{
        bounds = bounds,
        uv = uv,
        color = color
    })
    
    return #self.images - 1
end

-- @brief Creates the drawer buffer.
function ImageBuffer:createDrawerBuffer()
    local count = #self.images
    
    -- vertex/index count
    self.vertexCount = count * ae.draw2d.verticesPerRect
    self.indexCount = count * ae.draw2d.indicesPerRect
    
    -- the drawer buffer
    local drawerBuffer = ae.draw2d.TexDrawerBuffer.new(
        self.vertexCount,self.indexCount,self.colorSupport)
    self.drawerBuffer = drawerBuffer
    
    -- append rectangles
    ae.itable.each(self.images,function(image)
        local bounds = image.bounds
        drawerBuffer:appendRect(bounds.x,bounds.y,bounds.width,bounds.height,
            image.uv,image.color)
    end)
end

-- @brief Sets the image UV coordinates of an image from the drawer buffer.
-- @full Sets the image UV coordinates of an image from the drawer buffer.
--   The buffer must already be created using
--   ()[ae.ui.ImageBuffer#.createDrawerBuffer].
-- @param imageIndex The image index.
-- @param uv The UV coordinates.
function ImageBuffer:setUV(imageIndex,uv)
    self.drawerBuffer:setRectTexCoords(
        imageIndex * ae.draw2d.verticesPerRect,uv)
end

-- @brief Creates the vertex buffer object for the vertices.
function ImageBuffer:createVertexVBO()
    self.drawerBuffer:createVertexVBO()
end

-- @brief Creates the vertex buffer object for the indices.
function ImageBuffer:createIndexVBO()
    self.drawerBuffer:createIndexVBO()
end


-- @func
-- @brief Draws the images.
-- @func
-- @brief Draws the images at given coordinates.
-- @param x The X coordinate.
-- @param y The Y coordinate.
function ImageBuffer:draw(x,y)
    ae.gl.enableBlend()
    
    -- location
    x = x or self.bounds.x
    y = y or self.bounds.y
    
    -- drawer
    local drawer = self.drawer    
    drawer:drawStart(self.drawerBuffer)
    
    -- transformation
    self.transformation:translate(x,y,0)
    drawer:updateTransformation()
    
    -- texture
    self.texture.texture:bind()

    -- draw     
    drawer:draw(self.indexCount,self.drawerBuffer)
end

-- @brief Gets a string which represents the image buffer.
-- @return The string representing the image buffer.
function ImageBuffer:__tostring()
    return ae.oo.tostring('ae.ui.ImageBuffer',
        '#images=' .. tostring(#self.images) ..
        ', texture=' .. tostring(self.texture),super.__tostring(self))
end

-- @brief Initializes the image buffer.
local function init()
    -- create drawer
    ImageBuffer.drawer = ae.draw2d.TexDrawer.new(false,true,false)
    ImageBuffer.transformation = ImageBuffer.drawer:getMatrix()
    
    -- create drawer with per-vertex color support
    ImageBuffer.drawerColor = ae.draw2d.TexDrawer.new(false,true,true)
    ImageBuffer.transformationColor = ImageBuffer.drawerColor:getMatrix()
end

-- initialize the module
init()

return ImageBuffer