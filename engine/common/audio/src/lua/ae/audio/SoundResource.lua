-- @group Audio
-- @brief Sound wrapper used by ()[ae.SoundLoader].
local SoundResource = ae.oo.class()

-- @brief Creates a sound resource.
-- @param filename The name of the file with the sound.
-- @return The sound resource.
function SoundResource.new(filename)
    local self = ae.oo.new(SoundResource)
    SoundResource.construct(self,filename)
    
    return self
end

-- @brief Constructs a sound resource.
-- @param self The sound resource object.
-- @param filename The name of the file with the sound.
function SoundResource.construct(self,filename)
    self.id = SoundResource.getId(filename)
    self.filename = filename
end

-- @brief Gets an identifier of a sound resource.
-- @param filename The name of the file with the sound.
-- @return The sound resource identifier.
function SoundResource.getId(filename)
    return 'sound:' .. filename
end

-- @brief Loads the sound.
function SoundResource:load()
    if self.sound then
        return
    end
    ae.log.trace('Loading sound ' .. self.filename)
    self.sound = Sound.load(self.filename)    
end

-- @brief Deletes the sound.
function SoundResource:delete()
    if not self.sound then
        return
    end
    ae.log.trace('Deleting sound ' .. self.filename)
    self.sound:delete()
    self.sound = nil
end

-- @brief Plays the sound.
function SoundResource:play()
    self.sound:play()
end

-- @brief Gets a string which represents the sound resource.
-- @return The string representing the sound resource.
function SoundResource:__tostring()
    return ae.oo.tostring('ae.SoundResource',
        'id=' .. tostring(self.id) ..
        ', filename=' .. tostring(self.filename))
end

return SoundResource