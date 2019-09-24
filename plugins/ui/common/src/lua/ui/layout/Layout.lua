-- @group UI
-- @brief Lays out components.
local Layout,super = ae.oo.subclass(ui.Container)
    
-- @brief Constructs a Layout object.
-- @param self The object. 
function Layout.construct(self)
    super.construct(self)
end

-- @brief Lays out the child components.
function Layout:layout()
    error('Must be implemented in a subclass')
end

-- @brief Called when the bounds have changed.
function Layout:boundsChanged()
    self:layout()
end

return Layout