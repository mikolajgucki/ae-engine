-- @group Utility
-- @brief Provides utility object-oriented functions.
local oo = {}

-- @brief Creates a class table.
-- @return The class table with no functions (methods).
function oo.class()
    local class = {}
    class.__index = class
    
    return class
end

-- @brief Creates a subclass.
-- @param superclass The superclass to subclass.
-- @return The subclass and superclass.
function oo.subclass(superclass)
    local subclass = {}
    subclass.__index = subclass
        
    setmetatable(subclass,{
        __index = superclass
    })
       
    return subclass,superclass
end

-- @brief Creates a new object.
-- @param class The class of the object.
-- @return The object.
function oo.new(class)
    local object = setmetatable({},class)
    return object
end

-- @func
-- @brief Gets string representation of a class.
-- @param className The class name.
-- @func
-- @brief Gets string representation of a class.
-- @param className The class name.
-- @param classFields The class fields.
-- @func
-- @brief Gets string representation of a class.
-- @param className The class name.
-- @param classFields The class fields.
-- @param superToString The string representation of the superclass.
function oo.tostring(className,classFields,superToString)
    local str = className .. ' ['
    if classFields then
        str = str .. classFields
    end
    str = str .. ']'
    
    if superToString then
        str = str .. ' <- ' .. superToString
    end
    
    return str
end

return oo