-- @group UI
-- @brief The basic UI functions and variables.
local ui = {}

-- @brief The width of the display in pixels.
ui.width = 0

-- @brief The height of the display in pixels.
ui.height = 0

-- @brief The height-to-width aspect of the display.
ui.aspect = 0

-- @brief This method must be called when size of the display changes.
-- @param width The width in pixels.
-- @param height The height in pixels.
-- @full This method must be called when size of the display changes.
--   Typically this function is called from `ae.display.resize(width,height)`.
function ui.resize(width,height)
    ui.width = width
    ui.height = height    
    ui.aspect = ui.height / ui.width
end

-- @brief Loads the modules.
function ui.load()
    -- submodules
    ui.Color = require('ae.ui.Color')
    ui.size = require('ae.ui.size')
    ui.halign = require('ae.ui.halign')
    ui.valign = require('ae.ui.valign')
    ui.drawers = require('ae.ui.drawers')
    ui.Bounds = require('ae.ui.Bounds')
    ui.Component = require('ae.ui.Component')
    ui.Container = require('ae.ui.Container')
    ui.Model = require('ae.ui.Model')
    ui.Loader = require('ae.ui.Loader')
    ui.font = require('ae.ui.font.font')
    
    -- components
    ui.ItemComponent = require('ae.ui.ItemComponent')
    ui.Image = require('ae.ui.Image')
    ui.ImageBatch = require('ae.ui.ImageBatch')
    ui.ImageBuffer = require('ae.ui.ImageBuffer')
    ui.Label = require('ae.ui.Label')
    ui.LabelBatch = require('ae.ui.LabelBatch')
    ui.ColorOverlay = require('ae.ui.ColorOverlay')
    ui.ActionOverlay = require('ae.ui.ActionOverlay')
    
    -- event
    ui.GLCoordsTouchProxy = require('ae.ui.GLCoordsTouchProxy')
    
    -- gesture
    ui.GestureOverlay = require('ae.ui.GestureOverlay')
    ui.SlideLeftGesture = require('ae.ui.SlideLeftGesture')
    ui.SlideRightGesture = require('ae.ui.SlideRightGesture')
    
    -- layouts
    ui.Layout = require('ae.ui.Layout')
    ui.ItemLayout = require('ae.ui.ItemLayout')
    ui.HLayout = require('ae.ui.HLayout')
    ui.VLayout = require('ae.ui.VLayout')
    ui.ChildBoundsLayout = require('ae.ui.ChildBoundsLayout')
    ui.ViewportLayout = require('ae.ui.ViewportLayout')
    ui.RelativeLayout = require('ae.ui.RelativeLayout')
    
    -- initialize the display size
    ui.resize(ae.display.width,ae.display.height)
    
    -- full bounds
    ui.Bounds.full = ui.Bounds.copy(gl.bounds)
end

return ui
