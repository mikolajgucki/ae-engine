-- @group Utility
-- @brief Provides string-related utility functions
local string = {}

-- @brief Tests if a string starts with a prefix.
-- @param str The string.
-- @param prefix The prefix.
-- @return `true` if the string starts with the prefix, `false` otherwise.
function string.startsWith(str,prefix)
    return str:sub(1,prefix:len()) == prefix
end

-- @brief Tests if a string ends with a suffix.
-- @param str The string.
-- @param suffix The suffix.
-- @return `true` if the string ends with the suffix, `false` otherwise.
function string.endsWith(str,suffix)
    return str:sub(-suffix:len()) == suffix
end

-- @brief Splits a string.
-- @param str The string.
-- @param separator The separator.
-- @return The tokens.
function string.split(str,separator)
    local tokens = {}
    for token in str:gmatch("([^" .. separator .. "]+)") do
        ae.itable.append(tokens,token)
    end
    return tokens
end

-- @brief Gets a character at an index.
-- @param str The string.
-- @param index The index of the character.
-- @return The character.
function string.charAt(str,index)
    return str:sub(index,index)
end

return string