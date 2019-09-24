-- @group UI
-- @brief Draws a number of images in a batch.
-- @full Draws a number of images in a batch. The subtextures of the images
--   must belong to the same texture.
local ImageBatch,super = ae.oo.subclass(ae.ui.Component)

-- @func
-- @brief Creates an image batch
-- @param texture The texture.
-- @return The new image batch.
function ImageBatch.new(texture)
    local self = ae.oo.new(ImageBatch)
    ImageBatch.construct(self,texture)
    
    return self
end

-- @func
-- @brief Constructs an image batch.
-- @param self The image batch object.
-- @param texture The texture.
function ImageBatch.construct(self,texture)
    super.construct(self)
    self.texture = ae.ui.textures:load(texture.filename)
    self.images = {}
end

-- @brief Creates a batch from images.
-- @param texture The texture with the image subtextures.
-- @param images The images.
function ImageBatch.createFromImages(texture,images)
    local batch = ImageBatch.new(texture)
    for _,image in pairs(images) do
        batch:append(image)
    end
    
    return batch
end

-- @brief Adds an image to draw at the beginning of the list.
-- @param image The image to draw.
function ImageBatch:prepend(image)
    image.draw = function()
    end
    ae.itable.prepend(self.images,image)
end

-- @brief Adds an image to draw at the end of the list.
-- @param image The image to draw.
function ImageBatch:append(image)
    image.draw = function()
    end
    ae.itable.append(self.images,image)
end

-- @brief Draws the images.
function ImageBatch:draw()
    ae.gl.enableBlend()
        
    -- buffer
    local buffer = ae.ui.drawers.tex.buffer
    buffer:rewind()
    
    -- drawer
    local drawer = ae.ui.drawers.tex.drawer   
    drawer:drawStart()
    
    -- color
    drawer:setColor(1,1,1,1)
    
    -- texture
    self.texture.texture:bind()
    
    -- append rectangles
    local count = 0
    local images = self.images
    for index = 1,#images do
        local bounds = images[index].itemBounds
        if not bounds:isOutsideDisplay() then
            count = count + 1
            buffer:appendRect(
                bounds.x,bounds.y,bounds.width,bounds.height,images[index].uv)
        end
    end
    
    -- draw
    drawer:draw(count * ae.draw2d.indicesPerRect)        
end

-- @brief Gets a string which represents the image batch.
-- @return The string representing the image batch.
function ImageBatch:__tostring()
    return ae.oo.tostring('ae.ui.ImageBatch',
        '#images=' .. tostring(#self.images) ..
        ', texture=' .. tostring(self.texture),super.__tostring(self))
end

return ImageBatch