-- @module ae.FuncCallResource
-- @brief A function resource which can be added to ()[ae.ResourceLoader] and
--   gets called when loaded.
local FuncCallResource = ae.oo.class()

-- @brief The identifier sequence.
local idSeq = 0
    
-- @brief Gets the next identifier.
-- @return The next identifier.
function nextId()
    local id = idSeq
    idSeq = idSeq + 1
    return id
end
    
-- @brief Creates a FuncCallResource.
-- @param func The function to call.
-- @return A new FuncCallResource object. 
function FuncCallResource.new(func)
    local self = ae.oo.new(FuncCallResource)
    FuncCallResource.construct(self,func)
    
    return self
end

-- @brief Constructs a FuncCallResource object.
-- @param self The object. 
-- @param func The function to call.
function FuncCallResource.construct(self,func)
    self.id = nextId()
    self.func = func
end

-- @brief Calls the function.
function FuncCallResource:load()
    self.func()
end

return FuncCallResource