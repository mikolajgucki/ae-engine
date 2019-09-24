-- @brief A delay resource which can be added to ()[ae.ResourceLoader].
local DelayResource = ae.oo.class()
    
-- @brief The identifier sequence.
local idSeq = 0
    
-- @brief Creates a DelayResource object.
-- @param id The resource identifier.
-- @param millis The amout of time to delay in milliseconds.
-- @return A new DelayResource object. 
function DelayResource.new(id,millis)
    local self = ae.oo.new(DelayResource)
    DelayResource.construct(self,id,millis)
    
    return self
end

-- @brief Constructs a DelayResource object.
-- @param self The object. 
-- @param id The resource identifier.
-- @param millis The amout of time to delay in milliseconds.
function DelayResource.construct(self,id,millis)
    self.id = id
    self.millis = millis;
end

-- @brief Gets the next identifier.
-- @return The next identifier.
function DelayResource.getNextId()
    idSeq = idSeq + 1
    return "delay: " .. idSeq;
end

-- @brief Sleeps the given amout of time.
function DelayResource:load()
    ae.sleep(self.millis)
end

return DelayResource