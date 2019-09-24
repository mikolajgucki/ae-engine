-- @module ae.animation.Chain
-- @group Animation
-- @brief Chain animation.
local Chain = ae.oo.class()
    
-- @brief Creates a Chain object.
-- @param player The animation player.
-- @return A new Chain object. 
function Chain.new(player)
    local self = ae.oo.new(Chain)
    Chain.construct(self,player)
    
    return self
end

-- @brief Constructs a Chain object.
-- @param self The object. 
-- @param player The animation player.
function Chain.construct(self,player)
    self.player = player
end

-- @brief Chains the next entry.
-- @param self The chain object.
-- @param entry The entry.
local function chainEntry(self,entry)
    if not self.first then
        self.first = entry
        self.last = entry
        return
    end

    self.last.next = entry
    self.last = entry
end

-- @brief Runs a function.
-- @param func The function to run.
function Chain:run(func)
    local entry = {
        animate = function(entry)
            func()
            if entry.next then
                self.player:addAnimation(entry.next)
            end
            return true
        end
    }
    chainEntry(self,entry)
    return self
end

-- @brief Delays given amount of time.
-- @param duration The duration in milliseconds.
function Chain:delay(duration)
    local entry = {
        animate = function(entry,time)
            duration = duration - time
            if duration <= 0 then
                if entry.next then
                    self.player:addAnimation(entry.next)
                end
                return true
            end
        end
    }
    chainEntry(self,entry)
    return self
end

-- @func
-- @brief Runs an animation.
-- @param duration The duration in milliseconds.
-- @param ease The ease function.
-- @param func The animation function.
-- @func
-- @brief Runs a linear animation.
-- @param duration The duration in milliseconds.
-- @param func The animation function.
function Chain:animate(duration,ease,func)
    if not func then
        func = ease
        ease = ae.animation.ease.linear
    end    
    local counter = 0
    
    local entry = {
        animate = function(entry,time)
            counter = counter + time
            local t = counter / duration
            if t > 1 then
                t = 1
            end
            func(ease(t))
            if t >= 1 then
                if entry.next then
                    self.player:addAnimation(entry.next)
                end
                return true
            end
        end
    }
    chainEntry(self,entry)
    return self
end

-- @brief Starts the animation.
function Chain:go()
    self.player:addAnimation(self.first)
end

return Chain