-- @group UI
-- @brief A drawer command.
local DrawerCommand = ae.oo.class()
    
-- @brief Creates a DrawerCommand object.
-- @return A new DrawerCommand object. 
function DrawerCommand.new()
    local self = ae.oo.new(DrawerCommand)
    DrawerCommand.construct(self)
    
    return self
end

-- @brief Constructs a DrawerCommand object.
-- @param self The object. 
function DrawerCommand.construct(self)
    self:show()
end

-- @brief Hides the drawn elements.
function DrawerCommand:hide()
    self:setVisible(false)
end

-- @brief Shows the drawn elements.
function DrawerCommand:show()
    self:setVisible(true)
end

-- @brief Makes the drawn elements (in)visible.
-- @param visible The visibility state.
function DrawerCommand:setVisible(visible)
    self.visible = visible
end

return DrawerCommand