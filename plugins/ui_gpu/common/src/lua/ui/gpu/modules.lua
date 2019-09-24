-- @group UI
-- @brief Provides all the UI GPU modules.
local modules = {}

-- @brief Gets all the UI GPU modules.
-- @return The UI GPU modules as table.
function modules.get()
    return {
        'ui.gpu.GPUCoordsTouchProxy',
        'ui.gpu.queue',
        
        'ui.gpu.drawers.DrawerCommand',
        'ui.gpu.drawers.Drawer',
        'ui.gpu.drawers.ColorDrawer',
        'ui.gpu.drawers.color',
        'ui.gpu.drawers.TexDrawer',
        'ui.gpu.drawers.tex',
        'ui.gpu.drawers.TexColorDrawer',
        'ui.gpu.drawers.texColor',
        
        'ui.gpu.SolidRectangle',
        'ui.gpu.Image',
        'ui.gpu.string',
        'ui.gpu.Label',
        'ui.gpu.Button',
        
        'ui.gpu.layout.builder.image',
        'ui.gpu.layout.builder.label',
        
        'ui.gpu.GPUDrawGroup'
    }
end

return modules