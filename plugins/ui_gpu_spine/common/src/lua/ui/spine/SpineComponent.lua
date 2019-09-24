-- @brief Wraps a Spine skeleton and skeleton drawer.
-- @full Wraps a Spine skeleton and skeleton drawer.
local SpineComponent,super = ae.oo.subclass(ui.Component)
    
-- @brief Creates a SpineComponent object.
-- @param skeletonResource The skeleton resource.
-- @param id The component identifier.
-- @param bounds The initial bounds.
-- @return A new SpineComponent object. 
function SpineComponent.new(skeletonResource,id,bounds)
    local self = ae.oo.new(SpineComponent)
    SpineComponent.construct(self,skeletonResource,id,bounds)
    
    return self
end

-- @brief Called when the skeleton has been loaded.
-- @param self The Spine component object.
local function skeletonLoaded(self)
    self.skeleton = self.skeletonResource.skeleton
    self.drawer = spine.SpineSkeletonDrawer.new(self.skeleton)
    self:updateTransformationMatrix()
end

-- @brief Constructs a SpineComponent object.
-- @param self The object. 
-- @param skeletonResource The skeleton resource.
-- @param id The component identifier.
-- @param bounds The initial bounds.
function SpineComponent.construct(self,skeletonResource,id,bounds)
    super.construct(self,bounds)
    super.setId(self,id)

    -- skeleton resource
    self.skeletonResource = skeletonResource
    self.updateSkeleton = true
    
    -- color
    self.color = ui.Color.new()
    
    -- transformation
    self.transformation = Mat4.new()
    self.transformation:identity()
    self.scale = {
        x = 1,
        y = 1
    }
    
    -- if the skeleton is not loaded...
    if not self.skeletonResource.skeleton then
        self.skeletonResource:addListener({
            skeletonLoaded = function()
                skeletonLoaded(self)
            end
        })
    else
        -- ...or if it's already loaded
        skeletonLoaded(self)
    end
end

-- @brief Checks if the skeleton is loaded. Reports error if not loaded.
-- @param self The Spine component object.
local function chkLoaded(self)
    if not self.skeleton then
        error('Skeleton not loaded')
    end
end

-- @brief Checks if the skeleton is loaded.
-- @return `true` if loaded, `false` otherwise.
function SpineComponent:isLoaded()
    return self.skeleton and true or false
end

-- @brief Gets the skeleton.
-- @return The skeleton.
function SpineComponent:getSkeleton()
    chkLoaded(self)
    return self.skeleton
end

-- @brief Gets the skeleton width considering the scale.
-- @return The width.
function SpineComponent:getWidth()
    chkLoaded(self)
    return self.skeleton:getWidth() * self.scale.x
end

-- @brief Gets the skeleton height considering the scale.
-- @return The height.
function SpineComponent:getHeight()
    chkLoaded(self)
    return self.skeleton:getHeight() * self.scale.y
end

-- @brief Gets the transformation matrix.
-- @return The transformation matrix.
function SpineComponent:getTransformationMatrix()
    return self.transformation
end

-- @brief Sets the alpha component of the skeleton color.
-- @param a The alpha.
function SpineComponent:setAlpha(a)
    self.color.a = a
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

-- @brief Updates the transformation matrix.
function SpineComponent:updateTransformationMatrix()
    if not self:isLoaded() then
        return
    end

    local x = self.bounds.x
    local y = self.bounds.y

    -- align horizontally
    if self.halign then
        x = self.bounds.x + self.halign(self.bounds.width,self:getWidth())
    end
    
    -- align vertically
    if self.valign then
        y = self.bounds.y + self.valign(self.bounds.height,self:getHeight())
    end

    local transformation = self.transformation
    transformation:identity()
    transformation:mulTranslate(x,y)
    transformation:mulScale(self.scale.x,self.scale.y)
 end

-- @brief Enqueues the component to `ui.gpu.queue`.
function SpineComponent:enqueue()
    chkLoaded(self)
    if self.command then
        error('Already in the queue')
    end
    
    self.command = {
        execute = function(gpuQueue)
            local color = self.color
            self.drawer:enqueue(gpuQueue,self.transformation,
                color.r,color.g,color.b,color.a)
        end            
    }
    ui.gpu.queue.enqueue(self.command)
end

-- @brief Updates the component.
-- @param time The time in milliseconds elapsed since the last frame.
function SpineComponent:update(time)
    if self:isLoaded() and self.updateSkeleton then
        self.skeleton:update(time)
    end
end

return SpineComponent