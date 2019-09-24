-- @module ui.gpu.string
-- @brief Provides basic string functions.
local ui_string = {}

-- @brief Tests if a font has all the given characters. Raises an error if
--   there is a character missing.
-- @param fontData The font data.
-- @param text The text.
function string.hasAllCharacters(fontData,text)
    -- for each character
    for index = 1,text:len() do
        local ch = string.byte(text,index)
        if not fontData.keys[ch] then
            ae.error('')
        end
        if not fontData.textureData[fontData.keys[ch]] then
            
        end
    end
end

-- @brief Gets the width of a string.
-- @param height The height.
-- @param fontData The font data.
-- @param text The text.
-- @param widthFunc The function which calculates width of a character given
--   height and the character aspect.
local function getWidth(height,fontData,text,widthFunc)
    local textureData = fontData.textureData
    local width = 0
    
    -- for each UTF-8 character
    for index,ch in utf8.codes(text) do
        local subtexture = textureData[fontData.keys[ch]]
        width = width + widthFunc(height,subtexture.aspect)
    end
    
    return width
end

-- @brief Gets the width of a string.
-- @param height The height.
-- @param fontData The font data.
-- @param text The text.
function ui_string.getRawWidth(height,fontData,text)
    return getWidth(height,fontData,text,ae.size.getRawWidth)
end

-- @brief Gets the width of a string as if it would be drawn on the display.
-- @full Gets the width of a string as if it would be drawn on the display.
--   This function takes into accout the display aspect.
-- @param height The height.
-- @param fontData The font data.
-- @param text The text.
function ui_string.getWidth(height,fontData,text)
    return getWidth(height,fontData,text,ae.size.getWidth)
end

-- @brief Appends a string to a texture drawer.
-- @param x The X coordinate of the lower-left corner.
-- @param y The Y coordinate of the lower-left corner.
-- @param height The height.
-- @param fontResource The font resource.
-- @param text The texture.
-- @param vpos The position of the first vertex from which to append.
-- @param ipos The position of the first index from which to append.
-- @param drawer The drawer to which to append.
-- @return The table with first vertex and first index positions.
function ui_string.appendToDrawer(x,y,height,fontResource,text,vpos,ipos,drawer)
    drawer = drawer or ui.gpu.drawers.tex
    
    local texture = fontResource.font.textureResource.texture
    local textureData = fontResource.fontData.textureData
    local len = text:len()

    local vpos = vpos or drawer:reserveVertices(len * 4)
    local ipos = ipos or drawer:reserveIndices(len * 6)

    local vx = x
    -- for each UTF-8 character
    for index,ch in utf8.codes(text) do
        local subtexture = textureData[fontResource.fontData.keys[ch]]
        local width = ae.size.getWidth(height,subtexture.aspect)
        
        -- UV coordinates
        local u0 = subtexture.u
        local v0 = subtexture.v
        local u1 = subtexture.u + subtexture.width
        local v1 = subtexture.v + subtexture.height
        
        -- XV coordinates
        local x0 = vx
        local y0 = y
        local x1 = vx + width
        local y1 = y + height
        
        -- set the coordinates
        drawer:setVertex(vpos,     x0,y0, u0,v1)
        drawer:setVertex(vpos + 1, x1,y0, u1,v1)
        drawer:setVertex(vpos + 2, x1,y1, u1,v0)
        drawer:setVertex(vpos + 3, x0,y1, u0,v0)
        
        -- indices
        drawer:setIndices(ipos,{
            vpos,    vpos + 1,vpos + 3,
            vpos + 3,vpos + 1,vpos + 2})
        
        -- next character
        vpos = vpos + 4
        ipos = ipos + 6
        vx = vx + width
    end
    
    return {
        vpos = vpos,
        ipos = ipos
    }    
end

return ui_string