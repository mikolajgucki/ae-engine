-- @brief Provides the default drawer with the texture feature.

-- the drawer capacity
local drawerCapacity = 1024
    
-- the index capacity
local indexCapacity = 1024

-- @var
-- @name $modname
-- @brief The default texture drawer.
return ui.gpu.drawers.TexDrawer.new(drawerCapacity,indexCapacity)
