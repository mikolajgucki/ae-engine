-- @group Animation
-- @brief Spine skeleton wrapper used by ()[ae.ResourceLoader].
local SpineSkeletonResource = ae.oo.class()

-- @brief Creates a skeleton resource.
-- @param name The name of the file with the skeleton without the suffixes.
-- @param id The skeleton identifier.
-- @return The skeleton resource.
function SpineSkeletonResource.new(name,id)
    local self = ae.oo.new(SpineSkeletonResource)
    SpineSkeletonResource.construct(self,name,id)
    
    return self
end

-- @brief Constructs a skeleton resource.
-- @param self The skeleton resource object.
-- @param name The name of the file with the skeleton without the suffixes.
-- @param id The skeleton identifier.
function SpineSkeletonResource.construct(self,name,id)
    self.id = id
    self.name = name
    self.listeners = {}
end

-- @brief Adds a listener.
-- @param listener The listener.
function SpineSkeletonResource:addListener(listener)
    ae.itable.append(self.listeners,listener)
end

-- @brief Loads the skeleton.
function SpineSkeletonResource:load()
    if self.skeleton then
        return self.skeleton
    end
    ae.log.trace('Loading skeleton ' .. self.id)
    self.skeleton = spine.loadSkeleton(self.name,1,self.id)
    
    ae.itable.each(self.listeners,function(listener)
        if listener.skeletonLoaded then
            listener.skeletonLoaded(self,self.name,self.id)
        end
    end)
end

-- @brief Deletes the skeleton.
function SpineSkeletonResource:delete()
    if not self.skeleton then
        return
    end
    ae.log.trace('Deleting skeleton ' .. self.id)
    self.skeleton = nil
end

-- @brief Gets a string which represents the skeleton resource.
-- @return The string representing the skeleton resource.
function SpineSkeletonResource:__tostring()
    return ae.oo.tostring('ae.SpineSkeletonResource',
        'id=' .. tostring(self.id) ..
        ', name=' .. tostring(self.name))
end

return SpineSkeletonResource