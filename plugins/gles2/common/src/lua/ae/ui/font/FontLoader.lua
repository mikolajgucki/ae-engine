-- @group UI
-- @brief Responsible for loading and deleting fonts using ()[ResourceLoader].
local FontLoader = ae.oo.class()

-- @brief Creates a font loader.
-- @param resourceLoader The resource loader.
function FontLoader.new(resourceLoader)
    local self = ae.oo.new(FontLoader)
    FontLoader.construct(self,resourceLoader)
    
    return self
end

-- @brief Constructs a font loader.
-- @param self The font loader object.
-- @param resourceLoader The resource loader.
function FontLoader.construct(self,resourceLoader)
    self.resourceLoader = resourceLoader
end

-- @brief Loads a font resource.
-- @full Loads a font resource. The font itself might not be yet loaded if
--   the resource loader is in the loading mode.
-- @param data The font data.
-- @return The font resource.
function FontLoader:load(data)    
    local id = ae.ui.font.FontResource.getId(data)
    
    -- Load the texture in advance so that it also becomes the requested
    -- resource and won't be deleted in case it was loaded in the on-display
    -- mode. 
    local texture = ae.ui.textures:load(data.texture.filename)
    
    -- check if already exists
    local font = self.resourceLoader:get(id)
    if font then
        return font
    end
    
    -- create and add the font resource
    font = ae.ui.font.FontResource.new(data)
    font.texture = texture
    
    self.resourceLoader:add(font)
    return font
end

return FontLoader