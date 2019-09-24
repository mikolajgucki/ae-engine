-- @group UI
-- @brief Displays a single-line label.
local Label,super = ae.oo.subclass(ae.ui.ItemComponent)

-- @brief Sets the font height to occupy as much of the component bounds as
--   possible.
-- @param self The label object.
local function fitText(self)
    local ibounds = self.itemBounds

    ibounds.height = ae.math.min(self.bounds.height,self.maxLabelHeight)
    ibounds.width = self.font:getWidth(ibounds.height,self.text)
    
    if (ibounds.width >= self.bounds.width) then
        ibounds.height = self.font:getHeight(self.bounds.width,self.text)
        ibounds.width = self.bounds.width
    end
    
    self.string:setHeight(ibounds.height)
end

-- @func
-- @brief Creates a label with no bounds set.
-- @param fontData The font data.
-- @param text The text to display.
-- @return The label.
-- @func
-- @brief Creates a label.
-- @param fontData The font data.
-- @param text The text to display.
-- @param bounds The initial bounds.
-- @return The label.
function Label.new(fontData,text,bounds)
    local self = ae.oo.new(Label)
    Label.construct(self,fontData,text,bounds)
    
    return self
end

-- @func
-- @brief Construct a label with no bounds set.
-- @param self The label object.
-- @param fontData The font data.
-- @param text The text to display.
-- @func
-- @brief Construct a label.
-- @param self The label object.
-- @param fontData The font data.
-- @param text The text to display.
-- @param bounds The initial bounds.
function Label.construct(self,fontData,text,bounds)
    super.construct(self,bounds)
    self.font = ae.ui.fonts:load(fontData)
    self.text = text
    self.string = ae.ui.font.RenderedString.new(self.font,self.text)
    self.color = ae.ui.Color.new()
    
    self.resizeItem = fitText
    self:updateItemBounds()
end

-- @func
-- @brief Sets the label color.
-- @param r The red component.
-- @param g The green component.
-- @param b The blue component.
-- @param a The alpha component.
-- @func
-- @brief Sets the label color.
-- @param color The color with the RGBA components.
function Label:setColor(r,g,b,a)
    if not g then
        local color = r
        r = color.r
        g = color.g
        b = color.b
        a = color.a
    end

    self.color:set(r,g,b,a)
end

-- @brief Sets the alpha component of the color.
-- @param a The alpha component.
function Label:setAlpha(a)
    self.color.a = a
end

-- @brief Sets the maximum label height.
-- @param maxLabelHeight The maximum label height.
function Label:setMaxLabelHeight(maxLabelHeight)
    self.maxLabelHeight = maxLabelHeight
end

-- @brief Draws the label.
function Label:draw()
    if not self.visible or self.itemBounds:isOutsideDisplay() then
        return
    end
    self.string:draw(self.itemBounds.x,self.itemBounds.y,self.color)
end

-- @brief Sets the label text.
-- @param text The text.
function Label:setText(text)
    self.text = text
    self.string = ae.ui.font.RenderedString.new(self.font,self.text)
    self:updateItemBounds()
end

-- @brief Gets a string which represents the label.
-- @return The string representing the label.
function Label:__tostring()
    return ae.oo.tostring('ae.ui.Label','text=' .. self.text,
        super.__tostring(self))
end

return Label