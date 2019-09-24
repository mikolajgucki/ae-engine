-- @group Event
-- @brief Passes the move and up events related to a pointer to listener
--   registered with the down event
local DefaultTouchHandler = ae.oo.class()

-- @brief Creates a touch handler.
-- @return The touch handler object.
function DefaultTouchHandler.new()
    local self = ae.oo.new(DefaultTouchHandler)
    DefaultTouchHandler.construct(self)
    
    return self
end

-- @brief Constructs a touch handler.
-- @param self The touch handler object.
function DefaultTouchHandler.construct(self)
    self.pointers = {} -- table of active pointers
end

-- @brief Invoked on touch down event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
-- @param listener The listener which will receive move and down events
--   related to this pointer.
function DefaultTouchHandler:touchDown(pointerId,x,y,listener)
    local handled = listener:touchDown(pointerId,x,y)
    if handled then
        local pointer = {
            id = pointerId,
            x = x,
            y = y,
            listener = listener
        }
        table.insert(self.pointers,pointer)
    end
    
    return handled
end

-- @brief Invoked on touch move event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function DefaultTouchHandler:touchMove(pointerId,x,y)
-- update pointer coordinates
    for _,pointer in pairs(self.pointers) do
        if (pointer.id == pointerId) then
            pointer.x = x
            pointer.y = y
            
            if pointer.listener and pointer.listener.touchMove then
                pointer.listener:touchMove(pointerId,x,y)
            end            
            break
        end
    end
end

-- @brief Invoked on touch up event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function DefaultTouchHandler:touchUp(pointerId,x,y)
    local pointerIndex = nil
-- remove the pointer from the pointers table
    for index,pointer in ipairs(self.pointers) do
        if (pointer.id == pointerId) then
            if pointer.listener and pointer.listener.touchUp then
                pointer.listener:touchUp(pointerId,x,y)
            end
            
            pointerIndex = index
        end
    end
    
    if pointerIndex ~= nil then
        table.remove(self.pointers,pointerIndex)
    end
end

return DefaultTouchHandler
