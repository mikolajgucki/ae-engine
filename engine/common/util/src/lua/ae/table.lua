-- @group Utility
-- @brief Provides table-related utility functions.
local ae_table = {}

-- @name .readonly
-- @brief Gets a read-only version of a table.
-- @param t The table.
-- @return The read-only table `t`.
function ae_table.readonly(t)
    return setmetatable({},{
        __index = t,
        __newindex = function(t,key,value)
            error('Attempt to modify a read-only table')
        end,
        __metatable = false
    })        
end

-- @name .size
-- @brief Gets the number of items in a table.
-- @param t The table.
-- @return The item count.
function ae_table.size(t)
    local count = 0
    for _ in pairs(t) do
        count = count + 1
    end
   
    return count
end

-- @name .containsValue
-- @brief Tests if a table contains a value.
-- @param t The table.
-- @param value The value.
-- @return `true` if the table contains the value, `false` otherwise.
function ae_table.containsValue(t,value)
    for _,tableValue in pairs(t) do
        if tableValue == value then
            return true
        end
    end
    
    return false
end

-- @name .addAll
-- @func
-- @brief Adds all values from a source to a destination table.
-- @param dst The destination table.
-- @param src The source table.
-- @func
-- @brief Adds all values from a source to a destination table.
-- @param dst The destination table.
-- @param src The source table.
-- @param overwrite Indicates if to overwrite existing values in the
--   destination table.
function ae_table.addAll(dst,src,overwrite)
    overwrite = overwrite or true
    
    for key,value in pairs(src) do
        if not dst[key] or overwrite then
            dst[key] = src[key]
        end
    end
end

-- @name .pretty
-- @func
-- @brief Converts a table into a string using indenting.
-- @param value The table.
-- @return The string representation.
-- @func
-- @brief Converts a table into a string using indenting.
-- @param value The table.
-- @param indent The indent size.
-- @return The string representation.
function ae_table.pretty(value,indent)
    indent = indent or 1
    
    if (type(value) == 'table') then
        local str = '{\n'
        for tableKey,tableValue in pairs(value) do
            str = str .. string.rep(' ',indent * 4) .. tableKey .. ' = '
            str = str .. ae.table.pretty(tableValue,indent + 1)
        end
        return str .. string.rep(' ',(indent - 1) * 4) .. '}\n'
    end
    
    if (type(value) == 'string') then
        return "'" .. value .. "'\n"
    end
    
    return tostring(value) .. '\n'
end

-- if message pack libraries defined
if MsgPack and MsgPackPacker and MsgPackUnpacker then
    ae_table.msgpack = {
        -- Contains message pack types and their values.
        types = ae_table.readonly(MsgPack.getTypesTable()),
        
        -- The message pack packer.
        packer = MsgPackPacker.new(),
        
        -- The message pack unpacker
        unpacker = MsgPackUnpacker.new()
    }
end

-- @brief The packable types.
local packableTypes = {
    'nil','boolean','number','string','table'
}

-- @brief Indicates if a table key/value can be packed.
-- @param value The value to check.
-- @return `true` if the value can be packed, `false` otherwise.
local function canPack(value)
    return ae.table.containsValue(packableTypes,type(value))
end

-- @name .pack
-- @brief Packs a table into a message pack.
-- @param outputStream The stream to which to write.
-- @param value The table to pack.
function ae_table.pack(outputStream,value)
    -- nil
    if type(value) == 'nil' then
        ae.table.msgpack.packer:writeNil(outputStream)
        return
    end
    
    -- boolean
    if type(value) == 'boolean' then    
        ae.table.msgpack.packer:writeBool(outputStream,value)
        return
    end
    
    -- number
    if type(value) == 'number' then
        ae.table.msgpack.packer:writeInt32(outputStream,value)
        return
    end
    
    -- string
    if type(value) == 'string' then
        ae.table.msgpack.packer:writeStr16(outputStream,value)
        return
    end
    
    -- table
    if type(value) == 'table' then
        -- length
        local length = 0
        for tableKey,tableValue in pairs(value) do
            if canPack(tableKey) and canPack(tableValue) then
                length = length + 1
            end            
        end
        ae.table.msgpack.packer:writeMap16(outputStream,length)
        
        -- keys, values
        for tableKey,tableValue in pairs(value) do
            if canPack(tableKey) and canPack(tableValue) then
                ae.table.pack(outputStream,tableKey)
                ae.table.pack(outputStream,tableValue)
            end
        end
        return
    end
    
    error("Don't know how to pack value of type " .. type(value))
end

-- @name .unpack
-- @func
-- @brief Unpacks a table from an input stream.
-- @param inputStream The stream from which to unpack.
-- @return The unpacked table.
-- @func
-- @brief Unpacks a table from an input stream.
-- @param inputStream The stream from which to unpack.
-- @param newTableFunc The function which creates a new table.
-- @return The unpacked table.
function ae_table.unpack(inputStream,newTableFunc)
    local readType = ae.table.msgpack.unpacker:readType(inputStream)
    if readType == -1 then
        return nil
    end

    -- nil
    if readType == ae.table.msgpack.types.value_nil then
        return nil
    end
    
    -- false
    if readType == ae.table.msgpack.types.value_false then
        return false
    end
    
    -- true
    if readType == ae.table.msgpack.types.value_true then
        return true
    end
    
    -- int32
    if readType == ae.table.msgpack.types.int32 then
        return ae.table.msgpack.unpacker:readInt32(inputStream)
    end
    
    -- str16
    if readType == ae.table.msgpack.types.str16 then
        return ae.table.msgpack.unpacker:readStr16(inputStream)
    end
    
    -- map16
    if readType == ae.table.msgpack.types.map16 then
        local length = ae.table.msgpack.unpacker:readMap16(inputStream)
        local value = newTableFunc and newTableFunc() or {}
        
        for index = 1,length do
            local tableKey = ae.table.unpack(inputStream,newTableFunc)
            local tableValue = ae.table.unpack(inputStream,newTableFunc)
            
            value[tableKey] = tableValue
        end
        
        return value
    end
    
    error("Don't know how to unpack value of type " ..
        string.format('%d',readType))
end

return ae_table
