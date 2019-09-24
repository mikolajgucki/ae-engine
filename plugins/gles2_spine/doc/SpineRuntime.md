Spine runtime
====

Runtime source changes
----
`#include <...>` must be replaced with `#include "..."` in order to compile the souces under Xcode.
`spine-c.zip` contains the changed files.

JEdit
----
The replace can be easily done in JEdit in *Search and replace*. Search for `#include <spine/([a-zA-Z]+)\.h>` and replace with a BeanShell snippet `"#include \"spine/" + _1 + ".h\""`.