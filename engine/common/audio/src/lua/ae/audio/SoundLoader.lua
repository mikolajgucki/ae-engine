-- @group Audio
-- @brief Responsible for loading and deleting sounds.
local SoundLoader = ae.oo.class()

-- @brief Creates a sound loader.
-- @param resourceLoader The resource loader.
function SoundLoader.new(resourceLoader)
    local self = ae.oo.new(SoundLoader)
    SoundLoader.construct(self,resourceLoader)
    
    return self
end

-- @brief Constructs a sound loader.
-- @param self The source loader object.
-- @param resourceLoader The resource loader.
function SoundLoader.construct(self,resourceLoader)
    self.resourceLoader = resourceLoader
end

-- @brief Loads a sound resource.
-- @full Loads a sound resource. The sound itself might not be yet loaded if
--   the resource loader is in the loading mode.
-- @param filename The name of the file with the sound.
-- @return The source resource.
function SoundLoader:load(filename)
    local id = ae.audio.SoundResource.getId(filename)
    
    -- check if already exists
    local soundResource = self.resourceLoader:get(id)
    if soundResource then
        return soundResource
    end
    
    -- create and add the sound resource
    soundResource = ae.audio.SoundResource.new(filename)
    self.resourceLoader:add(soundResource)
    
    return soundResource
end

return SoundLoader