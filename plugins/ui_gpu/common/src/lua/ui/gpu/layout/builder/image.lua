-- @brief Builds image from table data.

-- @brief The function which builds an image from table data.
-- @param data The table data.
-- @return The image.
local function imageBuilder(data)
    -- validate
    if not data.subtexture then
        error('Image without subtexture')
    end

    local image = ui.gpu.Image.new(data.subtexture)
    image.id = data.id
    
    return image
end

-- @brief Initializes the image builder.
local function init()
    ui.layout.builder.registerBuilder('image',imageBuilder)
end

-- initialize
init()
