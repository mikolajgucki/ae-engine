-- @group UI
-- @brief Draws a number of labels of the same font in a batch.
local LabelBatch,super = ae.oo.subclass(ae.ui.Component)

-- @func
-- @brief Creates a label batch.
-- @param fontData The font data.
-- @return The new label batch.
function LabelBatch.new(fontData)
    local self = ae.oo.new(LabelBatch)
    LabelBatch.construct(self,fontData)
    
    return self
end

-- @brief Constructs a label batch.
-- @param self The label batch object.
-- @param fontData The font data.
function LabelBatch.construct(self,fontData)
    super.construct(self)
    self.font = ae.ui.fonts:load(fontData)
    self.labels = {}
end

-- @brief Adds a label to draw at the beginning of the list.
-- @param label The label to draw.
function LabelBatch:prepend(label)
    label.draw = function()
    end
    ae.itable.prepend(self.labels,label)    
end

-- @brief Adds a label to draw at the end of the list.
-- @param label The label to draw.
function LabelBatch:append(label)
    label.draw = function()
    end
    ae.itable.append(self.labels,label)    
end

-- @brief Draws the labels.
function LabelBatch:draw()
    -- blending
    ae.gl.enableBlend()
        
    -- buffer
    local buffer = ae.ui.drawers.tex.buffer
    buffer:rewind()
    
    -- drawer
    local drawer = ae.ui.drawers.tex.drawer
    drawer:drawStart()
    
    -- texture
    self.font.texture.texture:bind()
    
    -- to buffer
    local totalLength = 0
    ae.itable.each(self.labels,function(label)
        if not label.itemBounds:isOutsideDisplay() then
            label.string:appendToDrawerBuffer(
                label.itemBounds.x,label.itemBounds.y,buffer)
            totalLength = totalLength + label.string.length
        end
    end)
    
    -- draw
    drawer:draw(totalLength * ae.draw2d.indicesPerRect) 
end

-- @brief Gets a string which represents the label batch.
-- @return The string representing the label batch.
function LabelBatch:__tostring()
    return ae.oo.tostring('ae.ui.LabelBatch',
        'font=' .. tostring(self.font) ..
        ', #labels=' .. tostring(self.labels),super.__tostring(self))
end

return LabelBatch
