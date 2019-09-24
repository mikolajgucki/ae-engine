-- @group Animation
-- @brief Draws and update a Spine skeleton.
local SpineComponent,super = ae.oo.subclass(ae.ui.Component)

-- @brief Creates a Spine component.
-- @param skeleton The skeleton drawn by the component.
-- @param id The component bounds.
-- @param bounds The initial bounds.
-- @return The Spine component.
function SpineComponent.new(skeleton,id,bounds)
    local self = ae.oo.new(SpineComponent)
    SpineComponent.construct(self,skeleton,id,bounds)
    
    return self
end

-- @func
-- @brief Constructs a Spine component.
-- @param self The component object.
-- @param skeleton The skeleton drawn by the component.
-- @func
-- @brief Constructs a Spine component.
-- @param self The component object.
-- @param skeleton The skeleton drawn by the component.
-- @param id The component identifier.
function SpineComponent.construct(self,skeleton,id,bounds)
    super.construct(self,bounds)
    self:setId(id)
    
    self.color = ae.ui.Color.new()
    self.skeleton = skeleton
    self.scale = {
        x = 1,
        y = 1
    }
    self.updateSkeleton = true
    self.transformation = Mat4.new()
    self:updateTransformationMatrix()
end

-- @brief Gets the skeleton width considering the scale.
-- @return The width.
function SpineComponent:getWidth()
    local width = self.skeleton.skeleton:getWidth()
    if not width then
        error('Spine skeleton without width')
    end
    
    return width * self.scale.x
end

-- @brief Gets the skeleton height considering the scale.
-- @return The height.
function SpineComponent:getHeight()
    local height = self.skeleton.skeleton:getHeight()
    if not height then
        error('Spine skeleton without height')
    end
    
    return height * self.scale.y
end

-- @brief Sets the alpha component of the skeleton color.
-- @param a The alpha.
function SpineComponent:setAlpha(a)
    self.color.a = a
end

-- @brief Attaches this component to a parent Spine component.
-- @param parent The parent Spine component.
-- @param boneName The bone name.
function SpineComponent:attachToParent(parent,boneName)
    self.parent = {
        component = parent,
        boneName = boneName        
    }
end

-- @brief Updates the transformation matrix.
function SpineComponent:updateTransformationMatrix()
    local x = self.bounds.x
    local y = self.bounds.y

    -- align horizontally (if the skeleton is loaded)
    if self.halign and self.skeleton.skeleton then
        x = self.bounds.x + self.halign(self.bounds.width,self:getWidth())
    end
    
    -- align vertically (if the skeleton is loaded)
    if self.valign and self.skeleton.skeleton then
        y = self.bounds.y + self.valign(self.bounds.height,self:getHeight())
    end

    local transformation = self.transformation
    transformation:identity()
    transformation:mulTranslate(x,y)
    transformation:mulScale(self.scale.x,self.scale.y)
 end

-- @func
-- @brief Sets the scale. Does not consider the display aspect.
-- @param sx The scale along the X axis.
-- @param sy The scale along the Y axis.
-- @func
-- @brief Sets the scale. Does not consider the display aspect.
-- @param scale The scale as table.

function SpineComponent:setScale(sx,sy)
    if not sy then
        local scale = sx
        sx = scale.x
        sy = scale.y        
    end

    self.scale.x = sx
    self.scale.y = sy
    self:updateTransformationMatrix()
end

-- @brief Sets the horizontal alignment.
-- @full Sets the horizontal alignment of the item inside the component
--   bounds.<br/> See ()[ui.halign] for default horizontal alignments.
-- @param halign The horizontal alignment.
function SpineComponent:setHAlign(halign)
    self.halign = halign
    self:updateTransformationMatrix()
end

-- @brief Sets the vertical alignment.
-- @full Sets the vertical alignment of the item inside the component
--   bounds.<br/> See ()[ui.valign] for default vertical alignments.
-- @param valign The vertical alignment.
function SpineComponent:setVAlign(valign)
    self.valign = valign
    self:updateTransformationMatrix()
end

-- @brief Sets the horizontal and vertical alignment.
-- @full Sets the horizontal and vertical alignment of the skeleton inside the
--   component bounds. The skeleton must provide width and height.
-- @param halign The horizontal alignment.
-- @param valign The vertical alignment.
function SpineComponent:setAlign(halign,valign)
    self.halign = halign
    self.valign = valign
    self:updateTransformationMatrix()
end

-- @brief Called when the component bounds have changed.
function SpineComponent:boundsChanged()
    self:updateTransformationMatrix()
    self.updateSkeleton = not self.bounds:isOutsideDisplay()
end

-- @brief Draws the skeleton.
function SpineComponent:draw()
    if not self.visible then
        return
    end
    
    if self.parent then
        self.parent.component:applyBoneToMatrix(
            self.parent.boneName,self.transformation)
    end
    
    SpineComponent.drawer:draw(self.skeleton.skeleton,self.transformation,
        self.color.r,self.color.g,self.color.b,self.color.a)
end

-- @brief Updates the component.
-- @param time The time in milliseconds elapsed since the last frame.
function SpineComponent:update(time)
    if not self.visible or not self.updateSkeleton then
        return
    end
    self.skeleton.skeleton:update(time)
end

-- @brief Gets a string which represents the Spine component.
-- @return The string representing the Spine component.
function SpineComponent:__tostring()
    return ae.oo.tostring('ae.spine.SpineComponent',nil,super.__tostring(self))
end

-- @brief Initializes the Spine component.
local function init()
    SpineComponent.drawer = spine.SkeletonDrawer.new()
end

-- initialize the module
init()

return SpineComponent