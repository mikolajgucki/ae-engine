-- @group Animation
-- @brief Responsible for loading and deleting Spine skeletons using
--   ()[ae.ResourceLoader].
local SpineSkeletonLoader = ae.oo.class()

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

-- @func
-- @brief Loads a Spine skeleton resource of identifier equal to the name.
-- @full Loads a Spine skeleton resource. The skeleton might not yet be loaded
--   if the resource loader is in the loading mode.
-- @param name The name of the file with the skeleton without the suffixes.
-- @return The Spine skeleton resource.
-- @func
-- @brief Loads a Spine skeleton resource.
-- @full Loads a Spine skeleton resource. The skeleton might not yet be loaded
--   if the resource loader is in the loading mode.
-- @param name The name of the file with the skeleton without the suffixes.
-- @param id The skeleton identifier.
-- @return The Spine skeleton resource.
-- @func
-- @brief Loads a Spine skeleton resource.
-- @full Loads a Spine skeleton resource. The skeleton might not yet be loaded
--   if the resource loader is in the loading mode.
-- @param name The name of the file with the skeleton without the suffixes.
-- @param id The skeleton identifier.
-- @param processJsonFunc The function which allows to process the read JSON
--   before the skeleton is created.
-- @return The Spine skeleton resource.
function SpineSkeletonLoader:load(name,id,processJsonFunc)
    if not name then
        error('Attempt to load a skeleton without name')
    end
    id = id or name 
    
    -- Let's each skeleton have unique identifier so that it will never be
    -- taken from a resource already loaded. This is due to the way textures
    -- are loaded in Spine. We don't know the texture in advance and hence
    -- cannot load it here so that the resource loaded won't delete it.
    id = string.format('%s-%d',id,idSequence)
    idSequence = idSequence + 1
    
    -- create and add the skeleton resource
    local skeletonResource =
        spine.SpineSkeletonResource.new(name,id,processJsonFunc)
    self.resourceLoader:add(skeletonResource)
    
    return skeletonResource
end

return SpineSkeletonLoader
