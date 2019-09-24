-- @brief Provides the default drawer with the color feature.

-- the drawer capacity
local drawerCapacity = 1024
    
-- the index capacity
local indexCapacity = 1024

-- @var
-- @name $modname
-- @brief The default color drawer.
return ui.gpu.drawers.ColorDrawer.new(drawerCapacity,indexCapacity)
