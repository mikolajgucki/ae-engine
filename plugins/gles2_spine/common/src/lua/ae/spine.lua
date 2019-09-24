-- @group Animation
-- @brief Spine integration.

-- @brief Gets the path to the asset with the JSON skeleton data from JSON name.
-- @param name The skeleton name.
-- @return The path.
function spine.getJsonPath(name)
    return name .. '.json'
end

-- @brief Gets the path to the asset with the texture atlas data from atlas
--   name.
-- @param name The skeleton name.
-- @return The path.
function spine.getAtlasPath(name)
    return name .. '.atlas'
end

-- @brief Translates a texture path before loading the texture.
-- @param path The original path to the texture.
-- @return The translated path.
function spine.translateTexturePath(path)
    return path
end

-- @func
-- @brief Loads a Spine skeleton.
-- @full Loads a Spine skeleton. The skeleton is loaded from files `name`.json
--   and `name`.atlas.
-- @param name The name of the file with the skeleton without the suffixes.
-- @return The skeleton.
-- @func
-- @brief Loads a Spine skeleton.
-- @full Loads a Spine skeleton. The skeleton is loaded from files `name`.json
--   and `name`.atlas.
-- @param name The name of the file with the skeleton without the suffixes.
-- @param scale The skeleton scale.
-- @param id The skeleton identifier. Must be set to call listener functions
--   for the skeleton.
-- @param processJsonFunc The function which allows to process the read JSON
--   before the skeleton is created.
-- @return The skeleton.
function spine.loadSkeleton(name,scale,id,processJsonFunc)
    -- read
    local jsonData = assets.readAll(spine.getJsonPath(name))
    local atlasData = assets.readAll(spine.getAtlasPath(name))
        
    -- process
    if processJsonFunc then
        jsonData = processJsonFunc(jsonData)
    end
    
    -- create
    scale = scale or 1
    return spine.Skeleton.load(jsonData,atlasData,scale,id)
end

-- @brief Creates (loads) a texture used by Spine skeletons.
-- @param path The path to the texture file.
-- @return The texture.
function spine.createTexture(path)
    -- This function is called when Spine loads a skeleton texture. A skeleton
    -- is loaded when the skeleton resource is loaded. This means the texture
    -- will be loaded as well and we can return the texture field.
    local tex = ae.ui.textures:load(spine.translateTexturePath(path))
    return tex.texture
end

-- @brief Initializes the Spine module.
function spine.init()
    -- submodules
    spine.SpineComponent = require('ae.spine.SpineComponent')
    spine.SpineSkeletonResource = require('ae.spine.SpineSkeletonResource')
    spine.SpineSkeletonLoader = require('ae.spine.SpineSkeletonLoader')
    
    -- initialize
    spine.Skeleton.init('spine.createTexture')
end
