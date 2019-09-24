-- @group UI
-- @brief The superclass for layouts.
local Layout,super = ae.oo.subclass(ae.ui.Container)

-- @func
-- @brief Constructs a layout with no bounds set.
-- @param self The layout object.
-- @func
-- @brief Constructs a layout.
-- @param self The layout object.
-- @param bounds The initial bounds.
function Layout.construct(self,bounds)
    super.construct(self,bounds)
    
    -- the margins of the child components
    self.margins = {}
    
    -- the translations of the child components relative to their 
    -- original location
    self.translations = {}
end

-- @func
-- @brief Sets the margins of a component.
-- @param component The component.
-- @param top The top margin.
-- @param right The right margin.
-- @param bottom The bottom margin.
-- @param left The left margin.
-- @func
-- @brief Sets the margins of a component.
-- @param component The component.
-- @param margins The margins as table.
function Layout:setMargins(component,top,right,bottom,left)
    if right then
        self.margins[component] = {
            top = top or 0,
            right = right or 0 ,
            bottom = bottom or 0,
            left = left or 0
        }
    else
        local margins = top
        self.margins[component] = {
            top = margins.top or 0,
            right = margins.right or 0,
            bottom = margins.bottom or 0,
            left = margins.left or 0
        }
    end
end

-- @brief Sets the translation of a component relative to its origin location.
-- @param component The component.
-- @param x The translation along the X axis.
-- @param y The translation along the Y axis.
function Layout:setTranslation(component,x,y)
    if not y then
        local translation = x
        self.translations[component] = {
            x = translation.x,
            y = translation.y
        }
    else
        self.translations[component] = {
            x = x,
            y = y
        }
    end
end

-- @brief Lays out a component.
-- @param component The component.
-- @param x The X coordinate of the component layout bounds.
-- @param y The Y coordinate of the component layout bounds.
-- @param width The width of the component layout bounds.
-- @param height The height of the component layout bounds.
function Layout:layoutComponent(component,x,y,width,height)
    local translation = self.translations[component]
    if translation then
        x = x + translation.x
        y = y + translation.y
    end

    local margins = self.margins[component]
    if not margins then
        component:setBounds(x,y,width,height)
    else
        component:setBoundsWithMargins(x,y,width,height,
            margins.top,margins.right,margins.bottom,margins.left)
    end
end

-- @brief Called when the component bounds have changed.
function Layout:boundsChanged()
    self:layout()
end

-- @brief Gets bounds relative to the layout location.
-- @param absoluteBounds The absolute bounds.
-- @return The bounds relative to the layout location.
function Layout:getRelativeBounds(absoluteBounds)
    return ae.ui.Bounds.new(
        absoluteBounds.x - self.bounds.x,absoluteBounds.y - self.bounds.y,
        absoluteBounds.width,absoluteBounds.height)
end

-- @brief Gets a string which represents the layout.
-- @return The string representing the layout.
function Layout:__tostring()
    return ae.oo.tostring('ae.ui.Layout',nil,super.__tostring(self))
end

return Layout