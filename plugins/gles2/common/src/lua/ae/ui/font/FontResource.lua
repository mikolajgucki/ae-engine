-- @group UI
-- @brief Loads font and draws using font.
local FontResource = ae.oo.class()

-- shortcut
local getWidth = ae.ui.size.getWidth 

-- @brief Creates a font.
-- @full Creates a font. The font must be loaded before it can be used.
-- @param data The data from which to load the font.
-- @return The font.
function FontResource.new(data)
    local self = ae.oo.new(FontResource)
    FontResource.construct(self,data)
    
    return self
end

-- @brief Constructs a font.
-- @param self The font object.
-- @param data The data from which to load the font.
function FontResource.construct(self,data)
    self.id = FontResource.getId(data)
    self.data = data
end

-- @brief Gets an identifier of a font resource.
-- @param data The font data.
-- @return The font resource identifier.
function FontResource.getId(data)
    return 'font:' .. data.id
end

-- @brief Loads the font.
function FontResource:load()
end

-- @brief Deletes the font.
function FontResource:delete()
    self.texture = nil
end

-- @brief Gets string width.
-- @param height The string height.
-- @param str The string.
-- @return The string width.
function FontResource:getWidth(height,str)
    local width = 0
    local characters = self.data.characters
    
    -- for each character
    local length = string.len(str)    
    for index = 1,length do
        local ch = string.byte(str,index)
        if not characters[ch] then
            error(string.format('Missing character %s',tostring(ch)) )
        end
        width = width + getWidth(height) / characters[ch].aspect
    end
    
    return width
end

-- @brief Gets string height.
-- @param width The string width.
-- @param str The string.
-- @return The string height.
function FontResource:getHeight(width,str)
    return width / self:getWidth(1,str)
end

-- @brief Draws a string.
-- @param x The X coordinate of lower-left corner of the string bounding box.
-- @param y The Y coordinate of lower-left corner of the string bounding box.
-- @param text The text.
-- @param height The string height.
function FontResource:draw(x,y,text,height)
    ae.gl.enableBlend()
        
    -- buffer
    local buffer = ae.ui.drawers.tex.buffer
    buffer:rewind()
    
    -- drawer
    local drawer = ae.ui.drawers.tex.drawer
    drawer:drawStart()
    
    -- texture
    self.texture.texture:bind()

    -- character subtextures
    local characters = self.data.characters
    
    -- for each character
    local length = string.len(text)
    for index = 1,length do   
        local subtexture = characters[string.byte(text,index)]
        local width = getWidth(height) / subtexture.aspect
        buffer:appendRect(x,y,width,height,subtexture)
        x = x + width
    end
    
    -- draw
    drawer:draw(length * ae.draw2d.indicesPerRect)
end

return FontResource