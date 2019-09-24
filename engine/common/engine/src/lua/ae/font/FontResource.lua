-- @module ae.font.FontResource
-- @group UI
-- @brief A font resource.
local FontResource = ae.oo.class()
    
-- @brief Creates a FontResource object.
-- @param fontData The font data.
-- @param textureResource The resource with texture containing the font
--   subtextures.
-- @return A new FontResource object. 
function FontResource.new(fontData,textureResource)
    local self = ae.oo.new(FontResource)
    FontResource.construct(self,fontData,textureResource)
    
    return self
end

-- @brief Constructs a FontResource object.
-- @param self The object. 
-- @param fontData The font data.
-- @param textureResource The resource with texture containing the font
--   subtextures.
function FontResource.construct(self,fontData,textureResource)
    self.id = FontResource.getId(fontData.id)
    self.fontData = fontData
    self.textureResource = textureResource
end

-- @brief Gets the resource identifier.
-- @param fontId The font identifier.
-- @return The font identifier.
function FontResource.getId(fontId)
    return 'font:' .. fontId
end

-- @brief Loads the font.
function FontResource:load()
    self.font = ae.font.Font.new(self.fontData,self.textureResource)
end

return FontResource