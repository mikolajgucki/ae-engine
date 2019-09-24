-- @group Texture
-- @brief Texture wrapper used by ()[ae.ResourceLoader].
-- @full Subclass must implement method `loadTexture()` which loads the
--   ()[Texture] itself and returns it.
local TextureResource = ae.oo.class()

-- @brief Constructs a texture resource.
-- @param self The texture resource object.
function TextureResource.construct(self)
end

-- @brief Loads the texture.
function TextureResource:load()
    if self.texture then
        return
    end   
    self.texture = self:loadTexture()
end

-- @brief Deletes the texture.
function TextureResource:delete()
    if not self.texture then
        return
    end
    ae.log.trace('Deleting texture %s',self.filename)
    self.texture:delete()
    self.texture = nil
end

-- @brief Gets the texture width.
-- @return The texture width or -1 if the texture it's not loaded.
function TextureResource:getWidth()
    return self.texture and self.texture:getWidth() or -1
end

-- @brief Gets the texture height.
-- @return The texture height or -1 if the texture it's not loaded.
function TextureResource:getHeight()
    return self.texture and self.texture:getHeight() or -1
end

-- @brief Gets a string which represents the texture resource.
-- @return The string representing the texture resource.
function TextureResource:__tostring()
    return ae.oo.tostring('ae.TextureResource',
        'id=' .. tostring(self.id) ..
        ', filename=' .. tostring(self.filename))
end

return TextureResource