-- @group Utility
-- @brief Provides stack function which turn a table into a stack.
local stack = {}

-- @brief Pushes an element onto a stack.
-- @param s The stack.
-- @param element The element.
function stack.push(s,element)
    ae.itable.prepend(s,element)
end

-- @brief Peeks the top element of a stack.
-- @param s The stack.
-- @return The top element or `nil` if the stack is empty.
function stack.peek(s)
    return ae.itable.first(s)
end

-- @brief Pops the top element from a stack.
-- @param s The stack.
-- @return The top element or `nil` if the stack is empty.
function stack.pop(s)
    if ae.itable.empty(s) then
        return nil
    end
    
    local element = ae.itable.first(s)
    ae.itable.remove(s,element)
    
    return element
end

-- @brief Checks if a stack is empty.
-- @param s The stack.
-- @return `true` if the stack is nil or empty, `false` otherwise.
function stack.empty(s)
    return s == nil or #s == 0
end

return stack