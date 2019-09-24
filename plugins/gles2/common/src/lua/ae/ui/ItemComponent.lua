-- @group UI
-- @brief An item component is a component which contains a single item
--   with its own size aligned inside the component bounds.
local ItemComponent,super = ae.oo.subclass(ae.ui.Component)

-- @brief Aligns the item inside the component bounds.
-- @full Aligns the item inside the component bounds according to the set
--   horizontal and vertical alignments.
-- @param self The item component object.
local function alignItem(self)
    local bounds = self.bounds
    local ibounds = self.itemBounds

    ibounds.x = bounds.x + self.halign(bounds.width,ibounds.width)
    ibounds.y = bounds.y + self.valign(bounds.height,ibounds.height)
end

-- @brief Keeps the item bounds as they are.
local function keepBounds()
end

-- @func
-- @brief Constructs an item component with no bounds set.
-- @param self The item component object.
-- @func
-- @brief Constructs an item component.
-- @param self The item component object.
-- @param bounds The initial component bounds.
function ItemComponent.construct(self,bounds)
    super.construct(self,bounds)
    
    self.halign = ae.ui.halign.center
    self.valign = ae.ui.valign.middle
    self.resizeItem = keepBounds
    
    self.itemBounds = ae.ui.Bounds.new()
    self:updateItemBounds()    
end

-- @brief Sets the horizontal alignment.
-- @full Sets the horizontal alignment of the item inside the component
--   bounds.<br/> See ()[ui.halign] for default horizontal alignments.
-- @param halign The horizontal alignment.
function ItemComponent:setHAlign(halign)
    self.halign = halign
end

-- @brief Sets the vertical alignment.
-- @full Sets the vertical alignment of the item inside the component
--   bounds.<br/> See ()[ui.valign] for default vertical alignments.
-- @param valign The vertical alignment.
function ItemComponent:setVAlign(valign)
    self.valign = valign
end

-- @brief Sets the horizontal and vertical alignment.
-- @full Sets the horizontal and vertical alignment of the item inside the
--   component bounds.
-- @param halign The horizontal alignment.
-- @param valign The vertical alignment.
function ItemComponent:setAlign(halign,valign)
    self:setHAlign(halign)
    self:setVAlign(valign)
end

-- @brief Updates the item bounds.
function ItemComponent:updateItemBounds()
    self.resizeItem(self)
    alignItem(self)
end

-- @brief Called when the component bounds have changed.
function ItemComponent:boundsChanged()
    self:updateItemBounds()
end

-- @brief Called when the item has been tapped.
function ItemComponent:tapped()
    if self.tapFunc then
        self.tapFunc(self)
    end
end

-- @brief Sets the function that will be called when the item has been tapped.
-- @param tapFunc The tap function.
function ItemComponent:setTapListener(tapFunc)
    self.tapFunc = tapFunc
end

-- @brief Invoked on touch down event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function ItemComponent:touchDown(pointerId,x,y)
    if self.enabled and self.tapFunc and self.itemBounds:inside(x,y) then
        return true
    end
    return false
end

-- @brief Invoked on touch up event.
-- @param pointerId The pointer identifier.
-- @param x The X-coordinate at which the event occured.
-- @param y The Y-coordinate at which the event occured.
function ItemComponent:touchUp(pointerId,x,y)
    if self.itemBounds:inside(x,y) then
        self:tapped()
    end
end

-- @brief Gets a string which represents the item component.
-- @return The string representing the item component.
function ItemComponent:__tostring()
    return ae.oo.tostring('ae.ui.ItemComponent',
        'itemBounds=' .. tostring(self.itemBounds),super.__tostring(self))
end

return ItemComponent