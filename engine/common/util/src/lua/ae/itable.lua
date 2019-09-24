-- @group Utility
-- @brief Provides functions manipulating on tables of numerical indices (keys).
local itable = {}

-- @brief Adds an element at the beginning of a table.
-- @param t The table to which to prepend.
-- @param element The element to prepend.
function itable.prepend(t,element)
    table.insert(t,1,element)
end

-- @brief Adds an element at the end of a table.
-- @param t The table to which to append.
-- @param element The element to append.
function itable.append(t,element)
    table.insert(t,element)
end

-- @brief Adds elements at the end of a table.
-- @param t The table to which to append.
-- @param elements The elements to append.
function itable.appendAll(t,elements)
    for _,element in ipairs(elements) do
        itable.append(t,element)
    end
end

-- @brief Calls a function with each of the table items as argument.
-- @full Calls a function with each of the table items as argument. The
--   function takes arguments `function(item,t,index,hasNext)` where `item` is
--   the table item, `t` is the table, `index` is the index of the item in
--   the table and 'hasNext' indicate if there is a next element.
-- @param t The table to iterate.
-- @param func The function to call.
function itable.each(t,func)
    for index = 1,#t do
        func(t[index],t,index,index < #t)
    end
end

-- @brief Calls a function with each of the table items in the reverse order
--   as argument.
-- @full Calls a function with each of the table items in the reverse order
--   as argument. The function takes arguments `function(item,t,index)`
--   where `item` is the table item, `t` is the table and `index` is the index
--   of the item in the table.
-- @param t The table to iterate.
-- @param func The function to call.
function itable.reach(t,func)
    for index = #t,1,-1 do
        func(t[index],t,index)
    end
end

-- @brief Gets the first item.
-- @param t The table.
-- @return The first item in the table.
function itable.first(t)
    if #t == 0 then
        return nil
    end
    return t[1]
end

-- @brief Gets the last item.
-- @param t The table.
-- @return The last item in the table.
function itable.last(t)
    if #t == 0 then
        return nil
    end
    return t[#t]
end

-- @brief Checks if a table contains an element.
-- @full Checks if a table contains an element. Elements are compared using
--   the == operator.
-- @param t The table.
-- @param element The element.
-- @return `true` if the table contains the element, `false` otherwise.
function itable.contains(t,element)
    for _,value in pairs(t) do
        if value == element then
            return true
        end
    end
    
    return false
end

-- @brief Checks if a table is empty.
-- @param t The table.
-- @return `true` if the table is nil or empty, `false` otherwise.
function itable.empty(t)
    return t == nil or #t == 0
end

-- @brief Gets the index of an element.
-- @param t The table.
-- @param element The element.
-- @return The index of the element in the table or `nil` if the tables
--   does not contain such element.
function itable.indexOf(t,element)
    for index = 1,#t do
        if t[index] == element then
            return index
        end
    end    

    return nil
end

-- @brief Removes an element from a table.
-- @param t The table.
-- @param element The element to remove.
function itable.remove(t,element)
    local indexOf = itable.indexOf(t,element)
    if indexOf == nil then
        return
    end
    
    for index = indexOf,#t - 1 do
        t[index] = t[index + 1]
    end
    t[#t] = nil
end

-- @brief Removes the last element from a table.
-- @param t The table.
function itable.removeLast(t)
    itable.remove(t,itable.last(t))
end

return itable