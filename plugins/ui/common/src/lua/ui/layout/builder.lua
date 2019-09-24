-- @brief Creates layouts from table data.
-- @group UI
local builder = {}

-- @brief The table with registered builders in which the builder type is the
--   key and the builder itself is the value.
local builderFuncs = {}

-- @brief Registers a builder.
-- @param builderType The builder type.
-- @param builderFunc The builder function.
function builder.registerBuilder(builderType,builderFunc)
    if builderFuncs[builderType] then
        error(string.format('Layout builder %s already registered',builderType))
    end
    builderFuncs[builderType] = builderFunc
end

-- @brief Builds a layout from table data.
-- @param data The table data.
-- @return The layout built from the data.
function builder.build(data)
    -- convert string to a component
    if ae.types.isstring(data) then
        return ui.Component.new():setId(data)
    end

    -- type
    local builderType = data.builder
    if not builderType then
        error('Cannot build layout without specyfing builder')
    end
    
    -- builder
    local builderFunc = builderFuncs[builderType]
    if not builderFunc then
        error(string.format('%s is an unknown builder',builderType))
    end
    
    -- builder
    return builderFunc(data)
end

return builder