-- @group UI
-- @brief ...
local Button,super = ae.oo.subclass(ui.Component)
    
-- @brief The configuration.
local cfg = {
    -- The default tap period.
    tapPeriod = 200
}

-- @brief Creates a Button object.
-- @return A new Button object. 
function Button.new()
    local self = ae.oo.new(Button)
    Button.construct(self)
    
    return self
end

-- @brief Constructs a Button object.
-- @param self The object. 
function Button.construct(self)
    super.construct(self)
    self:setTouchable()
    
    self.componentTapped = function()
        if self.tapFunc then
            self.tapFunc(self,true)
        end
        
        self.tapTime = self.tapPeriod or cfg.tapPeriod
    end
end

-- @brief Sets the tap period.
-- @param tapPeriod The period in milliseconds.
function Button:setTapPeriod(tapPeriod)
    self.tapPeriod = tapPeriod
end

-- @brief Sets the tap function.
-- @param tapFunc The tap function.
-- @return The button.
function Button:setTapFunc(tapFunc)
    self.tapFunc = tapFunc
    return self
end

-- @brief Sets the action function.
-- @param actionFunc The action function.
-- @return The button.
function Button:setActionFunc(actionFunc)
    self.actionFunc = actionFunc
    return self
end

-- @brief Updates the component.
-- @param time The time in milliseconds elapsed since the last frame.
function Button:update(time)
    if self.tapTime then
        self.tapTime = self.tapTime - time
        
        -- fire event?
        if self.tapTime <= 0 then
            self.tapTime = nil
            
            if self.tapFunc then
                self.tapFunc(self,false)
            end
            if self.actionFunc then
                self.actionFunc(self)
            end
        end
    end
end

return Button