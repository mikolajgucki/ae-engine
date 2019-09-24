local types = {}

-- @brief Tests if a value is a number.
-- @param value The value.
-- @return `true` if the value is a number, `false` otherwise.
function types.isnumber(value)
    return type(value) == 'number'
end

-- @brief Tests if a value is a string.
-- @param value The value.
-- @return `true` if the value is a string, `false` otherwise.
function types.isstring(value)
    return type(value) == 'string'
end

-- @brief Tests if a value is a table.
-- @param value The value.
-- @return `true` if the value is a table, `false` otherwise.
function types.istable(value)
    return type(value) == 'table'
end

return types