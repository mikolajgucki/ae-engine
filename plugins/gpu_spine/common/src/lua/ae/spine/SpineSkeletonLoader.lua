-- @group Animation
-- @brief Responsible for loading and deleting Spine skeletons using
--   ()[ae.ResourceLoader].
local SpineSkeletonLoader = ae.oo.class()

-- the Spine identifier sequence
local idSequence = 1

-- @brief Creates a Spine skeleton loader.
-- @param resourceLoader The resource loader.
function SpineSkeletonLoader.new(resourceLoader)
    local self = ae.oo.new(SpineSkeletonLoader)
    SpineSkeletonLoader.construct(self,resourceLoader)
    
    return self
end

-- @brief Constructs a Spine skeleton loader.
-- @param self The Spine skeleton loader object.
-- @param resourceLoader The resource loader.
function SpineSkeletonLoader.construct(self,resourceLoader)
    self.resourceLoader = resourceLoader
end

-- @brief Loads a Spine skeleton resource.
-- @full Loads a Spine skeleton resource. The skeleton might not yet be loaded
--   if the resource loader is in the loading mode.
-- @param name The name of the file with the skeleton without the suffixes.
-- @param id The skeleton identifier.
-- @return The Spine skeleton resource.
function SpineSkeletonLoader:load(name,id)
    if not name then
        error('Attempt to load a skeleton without name')
    end
    id = id or name 
    
    -- Let's each skeleton have unique identifier so that it will never be
    -- taken from a resource already loaded. This is due to the way textures
    -- are loaded in Spine. We don't know the texture in advance and therefore
    -- cannot load it here so that the resource loader won't delete it.
    id = string.format('%s-%d',id,idSequence)
    idSequence = idSequence + 1
    
    -- create and add the skeleton resource
    local skeletonResource =
        ae.spine.SpineSkeletonResource.new(name,id)
    self.resourceLoader:add(skeletonResource)
    
    return skeletonResource
end

return SpineSkeletonLoader
