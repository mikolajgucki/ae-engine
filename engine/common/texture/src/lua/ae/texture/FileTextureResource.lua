-- @group Texture
-- @brief Texture wrapper which loads textures from files used by
--   ()[ae.ResourceLoader].
local FileTextureResource,super = ae.oo.subclass(ae.texture.TextureResource)

-- @brief Creates a texture resource.
-- @param filename The name of the file with the texture.
-- @return The texture resource.
function FileTextureResource.new(filename)
    local self = ae.oo.new(FileTextureResource)
    FileTextureResource.construct(self,filename)
    
    return self
end

-- @brief Constructs a texture resource.
-- @param self The texture resource object.
-- @param filename The name of the file with the texture.
function FileTextureResource.construct(self,filename)
    super.construct(self)
    self.id = FileTextureResource.getId(filename)
    self.filename = filename
end

-- @brief Gets an identifier of a texture resource.
-- @param filename The name of the file with the texture.
-- @return The texture resource identifier.
function FileTextureResource.getId(filename)
    return 'texture:' .. filename
end

-- @brief Loads the texture.
function FileTextureResource:loadTexture()
    ae.log.trace('Loading texture %s',self.filename)
    return Texture.load(self.filename)
end

return FileTextureResource