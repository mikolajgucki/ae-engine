@echo off

set BASEDIR=%~dp0
set AE_DIST=%BASEDIR%../

rem Classpath
SET CP=
SET CP=%CP%;%AE_DIST%/libs/studio/ae.studio.core.jar
SET CP=%CP%;%AE_DIST%/libs/ae.commons.jar
set ARGS=%ARGS% -cp %CP%

rem AE distribution directory
set ARGS=%ARGS% -Dae.dist.dir=%AE_DIST%

rem In order to load dependent dynamic DLL libraries
set PATH=%PATH%;%AE_DIST%/libs/studio/plugins

java %ARGS% com.andcreations.ae.studio.launcher.Launcher %*