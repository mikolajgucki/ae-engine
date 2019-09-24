-- @module ae.FuncCallLoader
-- @brief Responsible for loading function calls.
local FuncCallLoader = ae.oo.class()
    
-- @brief Creates a new FuncCallLoader object.
-- @param resourceLoader The resource loader.
-- @return A new FuncCallLoader object. 
function FuncCallLoader.new(resourceLoader)
    local self = ae.oo.new(FuncCallLoader)
    FuncCallLoader.construct(self,resourceLoader)
    
    return self
end

-- @brief Constructs a FuncCallLoader object.
-- @param self The object. 
-- @param resourceLoader The resource loader.
function FuncCallLoader.construct(self,resourceLoader)
    self.resourceLoader = resourceLoader
end

-- @brief Loads a function call.
-- @param func The function to call.
function FuncCallLoader:load(func)
    local resource = ae.FuncCallResource.new(func)
    self.resourceLoader:add(resource)
end

return FuncCallLoader