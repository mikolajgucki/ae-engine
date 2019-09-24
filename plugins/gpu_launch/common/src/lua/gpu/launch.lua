-- @brief The main GPU Launch image functions.
local launch = {}

-- The launch image model.
local model

-- @brief Runs the launch image.
-- @param color The background color.
-- @param subtexture The subtexture.
-- @param size The size given as factor of the display.
-- @param runFunc The function run when the image is drawn.
function launch.run(color,subtexture,size,runFunc)
    gpu.launch.model.init(color,subtexture,size)

    launch.runFunc = function()
        runFunc()
        ae.removeModel(model)
        gpu.launch.model.destroy()
    end
    model = ae.appendModel('gpu.launch.model.draw','gpu.launch.model.update')
end

return launch