-- @module ae.LuaModuleResource
-- @brief Lua module wrapper used by ()[ae.LuaModuleLoader].
local LuaModuleResource = ae.oo.class()
    
-- @brief Creates a LuaModuleResource object.
-- @param name The module name.
-- @param dst The destination object (table) given as string (will be created
--   if does not exist).
-- @return A new LuaModuleResource object. 
function LuaModuleResource.new(name,dst)
    local self = ae.oo.new(LuaModuleResource)
    LuaModuleResource.construct(self,name,dst)
    
    return self
end

-- @brief Constructs a LuaModuleResource object.
-- @param self The object. 
-- @param name The module name.
-- @param dst The destination object given (table) as string (will be created
--   if does not exist).
function LuaModuleResource.construct(self,name,dst)
    self.id = LuaModuleResource.getId(name)
    self.name = name
    self.dst = dst
    
    if not self.dst then
        self.dst = self.name
    end
end

-- @brief Gets an identifier of a Lua module resource.
-- @param name The Lua module name.
-- @return The identifier.
function LuaModuleResource.getId(name)
    return name
end

-- @brief Loads a Lua module.
function LuaModuleResource:load()
    ae.modules.load(self.name,self.dst)
end

-- @brief Gets a string which represents the Lua module resource.
-- @return The string representing the Lua module resource.
function LuaModuleResource:__tostring()
    return ae.oo.tostring('ae.LuaModuleResource','name=' .. name)
end

return LuaModuleResource