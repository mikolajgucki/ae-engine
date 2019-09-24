-- @module ae.animation.Player
-- @group Animation
-- @brief Animation player.
local Player = ae.oo.class()
    
-- @brief Creates a Player object.
-- @return A new Player object. 
function Player.new()
    local self = ae.oo.new(Player)
    Player.construct(self)
    
    return self
end

-- @brief Constructs a Player object.
-- @param self The object. 
function Player.construct(self)
    self.animations = {}
    self.obsolete = {}
end

-- @brief Adds an animation.
-- @param animation The animation.
function Player:addAnimation(animation)
    ae.itable.append(self.animations,animation)
end

-- @brief Updates the player.
-- @param time The time elapsed since the last frame.
function Player:update(time)
    -- update the animations
    for _,animation in ipairs(self.animations) do
        if animation.animate(animation,time) then
            ae.itable.append(self.obsolete,animation)
        end
    end
    
    -- remove the obsolete animations
    for _,animation in ipairs(self.obsolete) do
        ae.itable.remove(self.animations,animation)
    end
    self.obsolete = {}
end

return Player