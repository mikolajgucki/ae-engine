-- @brief ...
local DelayLoader = ae.oo.class()
    
-- @brief Creates a DelayLoader object.
-- @param resourceLoader The resource loader.
-- @return A new DelayLoader object. 
function DelayLoader.new(resourceLoader)
    local self = ae.oo.new(DelayLoader)
    DelayLoader.construct(self,resourceLoader)
    
    return self
end

-- @brief Constructs a DelayLoader object.
-- @param self The object. 
-- @param resourceLoader The resource loader.
function DelayLoader.construct(self,resourceLoader)
    self.resourceLoader = resourceLoader
end

-- @brief Loads a delay resource.
-- @param millis The amount of time to delay in milliseconds.
function DelayLoader:load(millis)
    local id = ae.loader.DelayResource.getNextId()
    
    -- create and add the resource
    local delayResource = ae.loader.DelayResource.new(id,millis)
    self.resourceLoader:add(delayResource)
    
    return delayResource
end

return DelayLoader