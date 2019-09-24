-- @group Texture
-- @brief Responsible for loading and deleting textures using
--   ()[ae.ResourceLoader].
local TextureLoader = ae.oo.class()

-- @brief Creates a texture loader.
-- @param resourceLoader The resource loader.
function TextureLoader.new(resourceLoader)
    local self = ae.oo.new(TextureLoader)
    TextureLoader.construct(self,resourceLoader)
    
    return self
end

-- @brief Constructs a texture loader.
-- @param self The texture loader object.
-- @param resourceLoader The resource loader.
function TextureLoader.construct(self,resourceLoader)
    self.resourceLoader = resourceLoader
end

-- @brief Loads a texture resource.
-- @full Loads a texture resource. The texture might not yet be loaded if
--   the resource loader is in the loading mode.
-- @param filename The name of the file with the texture.
-- @return The texture resource.
function TextureLoader:load(filename)
    local id = ae.texture.FileTextureResource.getId(filename)
    
    -- check if already exists
    local textureResource = self.resourceLoader:get(id)
    if textureResource then
        return textureResource
    end
    
    -- create and add the texture resource
    textureResource = ae.texture.FileTextureResource.new(filename)
    self.resourceLoader:add(textureResource)
    
    return textureResource
end

return TextureLoader