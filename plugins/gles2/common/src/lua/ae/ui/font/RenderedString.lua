-- @group UI
-- @brief Rendered string which data necessary to draw a string.
-- @full Caches data necessary to draw a string of given height to speed up
--   drawing.
local RenderedString = ae.oo.class()

-- @func
-- @brief Creates a rendered string.
-- @param font The font.
-- @param text The text.
-- @return The rendered string.
-- @func
-- @brief Creates a rendered string.
-- @param font The font.
-- @param text The text.
-- @param height The string height.
-- @return The rendered string.
function RenderedString.new(font,text,height)
    local self = ae.oo.new(RenderedString)
    RenderedString.construct(self,font,text,height)
    
    return self
end

-- @func
-- @brief Constructs a rendered string.
-- @param self The rendered string object.
-- @param font The font.
-- @param text The text.
-- @func
-- @brief Constructs a rendered string.
-- @param self The rendered string object.
-- @param font The font.
-- @param text The text.
-- @param height The string height.
function RenderedString.construct(self,font,text,height)
    self.font = font
    self.text = text
    if self.text == nil then
        ae.log.trace('nil text at %s',debug.traceback())
    end    
    self.length = string.len(self.text)
    self.height = height
    
    self:update()
end

-- @brief Updates the draw data according to the height.
function RenderedString:update()
    if not self.height then
        self.cache = nil
        return
    end
    self.cache = {
        subtextures = {},
        widths = {}
    }
    
    -- character subtextures
    local characters = self.font.data.characters
    
    -- the string width
    self.width = 0
    
    local getWidth = ae.ui.size.getWidth
    -- for each character
    for index = 1,self.length do
        local subtexture = characters[string.byte(self.text,index)]
        ae.itable.append(self.cache.subtextures,subtexture)
        
        local width = getWidth(self.height) / subtexture.aspect
        ae.itable.append(self.cache.widths,width)
        
        self.width = self.width + width
    end
end

-- @brief Sets the string height.
-- @param height The height.
function RenderedString:setHeight(height)
    self.height = height
    self:update()
end

-- @brief Appends the string rectangles to a drawer buffer.
-- @param x The X coordinate of lower-left corner of the string bounding box.
-- @param y The Y coordinate of lower-left corner of the string bounding box.
-- @param buffer The draw buffer.
function RenderedString:appendToDrawerBuffer(x,y,buffer)
    if not self.cache then
        return
    end
    
    -- cache
    local subtextures = self.cache.subtextures
    local widths = self.cache.widths
    
    -- for each character
    for index = 1,self.length do
        buffer:appendRect(x,y,widths[index],self.height,subtextures[index])
        x = x + widths[index]        
    end
end

-- @brief Appends the string rectangles to an image buffer.
-- @param x The X coordinate of lower-left corner of the string bounding box.
-- @param y The Y coordinate of lower-left corner of the string bounding box.
-- @param buffer The image buffer.
function RenderedString:appendToImageBuffer(x,y,buffer)
    if not self.cache then
        return
    end
    
    -- cache
    local subtextures = self.cache.subtextures
    local widths = self.cache.widths
    
    -- for each character
    for index = 1,self.length do
        local bounds = {
            x = x,
            y = y,
            width = widths[index],
            height = self.height
        }
        buffer:appendImage(bounds,subtextures[index])
        x = x + bounds.width    
    end    
end

-- @func
-- @brief Draws the string.
-- @param x The X coordinate of lower-left corner of the string bounding box.
-- @param y The Y coordinate of lower-left corner of the string bounding box.
-- @func
-- @brief Draws the string.
-- @param x The X coordinate of lower-left corner of the string bounding box.
-- @param y The Y coordinate of lower-left corner of the string bounding box.
-- @param color The color. 
function RenderedString:draw(x,y,color)
    if not self.cache then
        return
    end

    -- blending
    ae.gl.enableBlend()
        
    -- buffer
    local buffer = ae.ui.drawers.tex.buffer
    buffer:rewind()
    
    -- drawer
    local drawer = ae.ui.drawers.tex.drawer
    drawer:drawStart()
    
    -- color
    if color then
        drawer:setColor(color.r,color.g,color.b,color.a)
    else
        drawer:setColor(1,1,1,1)
    end
    
    -- texture
    self.font.texture.texture:bind()
    
    -- to buffer
    self:appendToDrawerBuffer(x,y,buffer)
    
    -- draw
    drawer:draw(self.length * ae.draw2d.indicesPerRect)
end

return RenderedString