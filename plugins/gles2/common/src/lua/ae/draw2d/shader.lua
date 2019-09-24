-- @group UI
-- @brief Provides utility functions related to GLSL shaders.
local shader = {}

-- @brief Preprocesses a shader source.
-- @full Preprocesses a shader source by allowing to include or exclude
--   parts of the source.
--   <br/>The line `gl_FragColor = u_Color;` from the sample shader below will
--   be included if `defines` contains string `color`.
--   ```glsl
--   $ifdef color
--       gl_FragColor = u_Color;
--   $end
--   ```
--   <br/>The line `gl_FragColor = vec(1,1,1,1);` from the sample shader below
--   will be included if `defines` does not contain string `color`.
--   ```glsl
--   $ifndef color
--       gl_FragColor = vec(1,1,1,1);
--   $end
--   ```
-- @param source The shader source
-- @param defines The array of definitions.
-- @return The preprocessed shader source.
function shader.preprocess(source,defines)
    local ifdef = '$ifdef '
    local ifndef = '$ifndef '
    local eat = false
    local result = ""
    
    -- for each line
    for line in source:gmatch('[^\r\n]+') do
        -- $end
        if ae.string.startsWith(line,'$end') then
            eat = false
        -- $ifdef
        elseif ae.string.startsWith(line,ifdef) then
            local def = string.sub(line,ifdef:len() + 1)
            if not ae.table.containsValue(defines,def) then
                eat = true
            end
        -- $ifndef
        elseif ae.string.startsWith(line,ifndef) then
            local def = string.sub(line,ifndef:len() + 1)
            if ae.table.containsValue(defines,def) then
                eat = true
            end
        elseif eat == false then
            result = result .. line .. '\n'
        end
    end
    
    return result
end

return shader

