-- @group Animation
-- @brief Spine skeleton wrapper used by ()[ae.ResourceLoader].
local SpineSkeletonResource = ae.oo.class()

-- @func
-- @brief Creates a skeleton resource.
-- @param name The name of the file with the skeleton without the suffixes.
-- @param id The skeleton identifier.
-- @return The skeleton resource.
-- @func
-- @brief Creates a skeleton resource.
-- @param name The name of the file with the skeleton without the suffixes.
-- @param id The skeleton identifier.
-- @param processJsonFunc The function which allows to process the read JSON
--   before the skeleton is created.
-- @return The skeleton resource.
function SpineSkeletonResource.new(name,id,processJsonFunc)
    local self = ae.oo.new(SpineSkeletonResource)
    SpineSkeletonResource.construct(self,name,id,processJsonFunc)
    
    return self
end

-- @func
-- @brief Constructs a skeleton resource.
-- @param self The skeleton resource object.
-- @param name The name of the file with the skeleton without the suffixes.
-- @param id The skeleton identifier.
-- @func
-- @brief Constructs a skeleton resource.
-- @param self The skeleton resource object.
-- @param name The name of the file with the skeleton without the suffixes.
-- @param processJsonFunc The function which allows to process the read JSON
--   before the skeleton is created.
-- @param id The skeleton identifier.
function SpineSkeletonResource.construct(self,name,id,processJsonFunc)
    self.id = id
    self.name = name
    self.processJsonFunc = processJsonFunc
end

-- @brief Loads the skeleton.
function SpineSkeletonResource:load()
    if self.skeleton then
        return self.skeleton
    end
    ae.log.trace('Loading skeleton ' .. self.id)
    self.skeleton = spine.loadSkeleton(self.name,1,self.id,self.processJsonFunc)
    
    if self.loadedFunc then
        self.loadedFunc()
    end
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