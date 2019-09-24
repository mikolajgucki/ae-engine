-- @module ae.font.FontLoader
-- @brief Responsible for loading fonts.
local FontLoader = ae.oo.class()
    
-- @brief Creates a FontLoader object.
-- @param resourceLoader The resource loader.
-- @return A new FontLoader object. 
function FontLoader.new(resourceLoader)
    local self = ae.oo.new(FontLoader)
    FontLoader.construct(self,resourceLoader)
    
    return self
end

-- @brief Constructs a FontLoader object.
-- @param self The object. 
-- @param resourceLoader The resource loader.
function FontLoader.construct(self,resourceLoader)
    self.resourceLoader = resourceLoader
end

-- @brief Loads a font.
-- @param fontData The font data.
-- @return The font resource.
function FontLoader:load(fontData)
    local id = ae.font.FontResource.getId(fontData.id)
    
    -- Load the texture in advance so that it also becomes the requested
    -- resource and won't be deleted in case it was loaded in the on-display
    -- mode. 
    local textureResource = ae.loaders.textures:load(
        fontData.textureData.filename)
    
    -- check if already exists
    local fontResource = self.resourceLoader:get(id)
    if fontResource then
        return fontResource
    end
    
    -- create and add the font resource
    fontResource = ae.font.FontResource.new(fontData,textureResource)
    self.resourceLoader:add(fontResource)
    
    return fontResource
end

return FontLoader