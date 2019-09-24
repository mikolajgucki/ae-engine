GPU plugins
====

There is a number of GPU-related plugins. They should be used together as some of them are abstractions while others are implementations. The core plugins are:
- `gpu` is the GPU abstraction. It should be used when working close to GPU.
- `ui` provides core classes and functions for building UI.
- `gpu_ui` provides components and functions built on top og GPU (`gpu` plugin).
- `gpu_launch` displays a launch image.

OpenGL ES
----

The OpenGL plugins are:
- `gpu_gles` is the OpenGL ES implementation.
- `ui_gles` provides basic OpenGL ES functions.
- `gpu_launch_gles` is the implementation of the launch image in OpenGL ES (on top of `gpu_gles`).

UI
----

UI components backed by GPU are drawn by an implementation of `ui.gpu.drawers.Drawer`. The UI drawers are low level drawing facilities operating on vertices. There are out-of-the-box instances of drawers:
- `ui.gpu.drawers.color` which draws solid triangles,
- `ui.gpu.drawers.tex` which draws textured triangles.

Appending a component to a drawer is by calling `appendToDrawer` on the component. Each component type must be added to only one matching drawer type. For example, `ui.gpu.SolidRectange` can only be added to `ui.gpu.drawers.ColorDrawer`. A number of components can be appended by calling `ui.gpu.drawers.Drawer:appendComponents()`.

Appending a component to a drawer is not enough to draw the component. Components must be added to a draw command and the command must be enqueued. A command is returned by the method `ui.gpu.drawers.Drawer:start(transformation)`. At this point components can be appended to the drawer. Finally, the command is enqueued via `ui.gpu.drawers.Drawer:enqueue()`.

An example could be:
```lua
-- create an image
local image = ui.gpu.Image(assets.textures.foo.bar)

-- create a draw command
ui.gpu.drawers.tex:start()

-- append the image to the default texture drawer
image:appendToDrawer(ui.gpu.drawers.tex)

-- enqueue the command
ui.gpu.drawers.tex:enqueue()
```