-- @brief Provides the default drawer with the texture and vertex color
--   features.

-- the drawer capacity
local drawerCapacity = 4096
    
-- the index capacity
local indexCapacity = 4096

-- @var
-- @name $modname
-- @brief The default texture drawer.
return ui.gpu.drawers.TexColorDrawer.new(drawerCapacity,indexCapacity)
