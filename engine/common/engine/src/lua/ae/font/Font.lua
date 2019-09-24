-- @module ae.font.Font
-- @brief Represents a font with subtextures as glyphs.
local Font = ae.oo.class()
    
-- @brief Creates a Font object.
-- @param fontData The font data.
-- @param textureResource The texture resource from which to get subtextures.
-- @return A new Font object. 
function Font.new(fontData,textureResource)
    local self = ae.oo.new(Font)
    Font.construct(self,fontData,textureResource)
    
    return self
end

-- @brief Constructs a Font object.
-- @param self The object. 
-- @param fontData The font data.
-- @param textureResource The texture resource from which to get subtextures.
function Font.construct(self,fontData,textureResource)
    self.fontData = fontData
    self.textureResource = textureResource
end

-- @brief Binds texture which contains font subtextures to a font.
-- @full Binds texture which contains font subtextures to a font. This is
--   necessary in order to load fonts.
-- @param fontData The font data.
-- @param textureData The texture data.
function Font.bindTexture(fontData,textureData)
    if not fontData then
        error('No font data')
    end
    if not textureData then
        error('No texture data')
    end

    fontData.textureData = textureData
end

return Font