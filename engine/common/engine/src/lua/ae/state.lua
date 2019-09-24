-- @brief Permanently stores and restores the game state.
-- @full Permanently stores and restores the game state. The state is
--   persistent between game starts.
local state = {}

-- The name of the file (in the storage) with the state.
state.filename = 'state'

-- @brief Gets the last component of table and path.
-- @param tbl The table.
-- @param path The path.
-- @return The last table on the path and key 
local function getLastComponent(tbl,path)
    local keys = {}
    for key in path:gmatch('[a-zA-z0-9_]+') do
        table.insert(keys,key)
    end
    if #keys == 1 then       
        return {
            tbl = tbl,
            key = path
        }
    else
        for index = 1,#keys - 1 do
            local key = keys[index]
            if not tbl[key] then
                tbl[key] = state.createTable()
            end
            tbl = tbl[key]
        end
        return {
            tbl = tbl,
            key = keys[#keys]
        }
    end
end

-- @brief Gets a state table value by path.
-- @param tbl The state table.
-- @param path The path.
-- @param ... The other path components.
-- @return The table value.
local function getByPath(tbl,path,...)
    local args = {...}
    if #args > 0 then
        path = string.format(path,...)
    end

    local component = getLastComponent(tbl,path)
    return rawget(component.tbl,component.key)
end
    
-- @brief Sets a state table value by path.
-- @param tbl The table.
-- @param path The path.
-- @param ... The other path components and the value to set.
local function setByPath(tbl,path,...)
    local args = {...}
    local value = args[#args]
    if #args > 1 then
        table.remove(args,#args)
        path = string.format(path,table.unpack(args))
    end

    local component = getLastComponent(tbl,path)
    component.tbl[component.key] = value
end

-- @brief Creates an empty state table.
-- @return The empty state table.
function state.createTable()
    local tbl = {}
    tbl.get = function(path,...)
        return getByPath(tbl,path,...)
    end
    tbl.set = function(path,...)
        setByPath(tbl,path,...)
    end
    
    return tbl
end

-- @brief Stores the state in the permament storage.
-- @full Stores the state in the permament ()[storage].
--   The state can be restored by calling `restore()`.
-- @param stateToStore The state to store.
function state.store(stateToStore)
    local outputStream = storage.getOutputStream(state.filename)
    outputStream:open()
    ae.table.pack(outputStream,stateToStore)
    outputStream:close()
end

-- @brief Restores the state from the permanent storage.
-- @full Restores the state from the permanent ()[storage].
--   Restores the state stored by calling `store()`.
-- @return The restored state.
function state.restore()
    local inputStream = storage.getInputStream(ae.state.filename)
    inputStream:open()
    local restoredState = ae.table.unpack(inputStream)
    inputStream:close()
    
    return restoredState
end

-- @brief Wraps a table into a state table with the get and set functions.
-- @param stateToWrap The state table to wrapped.
-- @return The wrapped table.
function state.wrap(stateToWrap)
    if not stateToWrap then
        return
    end
    local wrapped = state.createTable()
    
    for key,value in pairs(stateToWrap) do
        if ae.types.istable(value) then
            wrapped[key] = state.wrap(value)
        else
            wrapped[key] = value
        end
    end
    
    return wrapped
end

-- @brief Initializes the state.
local function init()
    storage.createFile(state.filename)
end

-- initialize
init()

return state