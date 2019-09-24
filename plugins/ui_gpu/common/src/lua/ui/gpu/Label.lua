-- @module ui.gpu.Label
-- @group UI
-- @brief A label capable of displaying a single line label.
local Label,super = ae.oo.subclass(ui.Component)
    
-- @brief Creates a Label object.
-- @param fontData The font data.
-- @param text The text to display.
-- @return A new Label object. 
function Label.new(fontData,text)
    local self = ae.oo.new(Label)
    Label.construct(self,fontData,text)
    
    return self
end

-- @brief Constructs a Label object.
-- @param self The object. 
-- @param fontData The font data.
-- @param text The text to display.
function Label.construct(self,fontData,text)
    super.construct(self)
    self.fontData = fontData
    self.text = text
    self.fontResource = ae.loaders.fonts:load(fontData)
end

-- @brief Gets the texture.
-- @return The texture.
function Label:getTexture()
    return self.fontResource.textureResource.texture
end

-- @brief Gets the label (height-to-width) aspect.
-- @return The aspect.
function Label:getAspect()
    local height = 1
    local width = ui.gpu.string.getRawWidth(height,self.fontData,self.text)
    
    return height / width
end

-- @brief Appends the label to a texture drawer.
-- @param drawer The texture drawer.
function Label:appendToDrawer(drawer)
    ui.gpu.string.appendToDrawer(
        self.bounds.x,self.bounds.y,self.bounds.height,
        self.fontResource,self.text)
end

-- @brief Provides preferred size functions.
Label.size = {}

-- @brief Calculates label size so that it has fixed height.
-- @param component The component (label).
-- @param bounds The bounds in which the component is to fit.
function Label.size.fixedHeight(component,bounds)
    if not component.height then
        error('Height not set')
    end
    return {
        width = ae.size.getWidth(component.height,component:getAspect()),
        height = component.height
    }
end

-- @brief Calculates label size so that it has fixed width.
-- @param component The component (label).
-- @param bounds The bounds in which the component is to fit.
function Label.size.fixedWidth(component,bounds)
    if not component.width then
        error('Width not set')
    end
    return {
        width = component.width,
        height = ae.size.getHeight(component.width,component:getAspect())
    }
end

return Label