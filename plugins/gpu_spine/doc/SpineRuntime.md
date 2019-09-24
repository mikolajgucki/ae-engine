Spine runtime
====

Runtime source changes
----
- `#include <...>` must be replaced with `#include "..."` in order to compile the souces under Xcode.
- In `Skeleton.c` cast to `_spUpdate *` was added in `_addToUpdateCache` while calling `realloc`.
- In `SkeletonJson.c` in the function `spSkeletonJson_readSkeletonData` locale handling was changed as `setlocale()` on Android might return null. Null-handling was added.

`spine-c-ae.zip` contains the changed files. `spine-c.zip` contains the origina files.

JEdit
----
The replace can be easily done in JEdit in *Search and replace*. Search for `#include <spine/([a-zA-Z]+)\.h>` and replace with a BeanShell snippet `"#include \"spine/" + _1 + ".h\""`.