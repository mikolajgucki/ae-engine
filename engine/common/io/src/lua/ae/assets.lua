-- @module assets
-- @group IO

-- @brief Reads the contents of a file.
-- @param filename The file name.
-- @return The file contents.
function assets.readAll(filename)
    local input = assets.getInputStream(filename)
    input:open()
    local contents = input:readAll()
    input:close()
    
    return contents
end