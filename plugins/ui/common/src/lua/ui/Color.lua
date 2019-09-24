-- @group UI
-- @brief Represents a RGBA color.
local Color = ae.oo.class()

-- @func
-- @brief Creates a white color.
-- @return The new color.
-- @func
-- @brief Creates a color.
-- @param r The red component. Defaults to 1.
-- @param g The green component. Defaults to 1.
-- @param b The blue component. Defaults to 1.
-- @param a The alpha component. Defaults to 1.
-- @return The new color.
function Color.new(r,g,b,a)
    local self = ae.oo.new(Color)
    Color.construct(self,r,g,b,a)
    
    return self
end

-- @func
-- @brief Constructs a white color.
-- @param self The color object.
-- @func
-- @brief Constructs a color.
-- @param self The color object.
-- @param r The red component. Defaults to 1.
-- @param g The green component. Defaults to 1.
-- @param b The blue component. Defaults to 1.
-- @param a The alpha component. Defaults to 1.
function Color.construct(self,r,g,b,a)
-- @name Color.r
-- @var
-- @brief The red component.
    self.r = r or 1
    
-- @name Color.g
-- @var
-- @brief The green component.
    self.g = g or 1
    
-- @name Color.b
-- @var
-- @brief The blue component.
    self.b = b or 1
    
-- @name Color.a
-- @var
-- @brief The alpha component.
    self.a = a or 1
end

-- @func 
-- @brief Sets the components.
-- @param r The red component.
-- @param g The green component.
-- @param b The blue component.
-- @func
-- @brief Sets the components.
-- @param r The red component.
-- @param g The green component.
-- @param b The blue component.
-- @param a The alpha component.
function Color:set(r,g,b,a)
    self.r = r
    self.g = g
    self.b = b
    self.a = a or 1
end

-- @brief Multiplies the RGB components by a factor.
-- @param factor The factor.
-- @return The color.
function Color:mulRGB(factor)
    self.r = self.r * factor
    self.g = self.g * factor
    self.b = self.b * factor
    
    return self
end

-- @brief Multiplies the RGBA components by a factor.
-- @param factor The factor.
-- @return The color.
function Color:mulRGBA(factor)
    self.r = self.r * factor
    self.g = self.g * factor
    self.b = self.b * factor
    self.a = self.a * factor
    
    return self
end

-- @brief Gets the RGB components.
-- @return The RGB components as 3 values.
function Color:rgb()
    return self.r,self.g,self.b
end

-- @brief Gets the RGBA components.
-- @return The RGBA components as 4 values.
function Color:rgba()
    return self.r,self.g,self.b,self.a
end

-- @brief Gets a string which represents the color.
-- @return The string representing the color.
function Color:__tostring()
    return ae.oo.tostring('ae.ui.Color',
        'r=' .. tostring(self.r) ..
        ', g=' .. tostring(self.g) ..
        ', b=' .. tostring(self.b) ..
        ', a=' .. tostring(self.a))
end

-- @brief Creates a color from hex representation.
-- @param hex The hex representation in form rrggbb[aa].
-- @return The color.
function Color.fromHex(hex)
    local r = tonumber(hex:sub(1,2),16) / 255
    local g = tonumber(hex:sub(3,4),16) / 255 
    local b = tonumber(hex:sub(5,6),16) / 255
   
    local a = 1
    if hex:len() >= 8 then
        a = tonumber(hex:sub(7,8),16) / 255
    end
    
    return Color.new(r,g,b,a)
end

-- @name Color.black
-- @var 
-- @brief The black color.
Color.black = ae.table.readonly(Color.new(0,0,0))

-- @name Color.white
-- @var
-- @brief The white color.
Color.white = ae.table.readonly(Color.new(1,1,1))

return Color