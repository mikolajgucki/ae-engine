#!/bin/bash

SCRIPT_PATH="${BASH_SOURCE[0]}";
if ([ -h "${SCRIPT_PATH}" ]) then
  while([ -h "${SCRIPT_PATH}" ]) do SCRIPT_PATH=`readlink "${SCRIPT_PATH}"`; done
fi
pushd . > /dev/null
cd `dirname ${SCRIPT_PATH}` > /dev/null
SCRIPT_PATH=`pwd`;
popd  > /dev/null

AE_DIST=$SCRIPT_PATH/..

CP=$AE_DIST/libs/studio/ae.studio.core.jar
ARGS="$ARGS -cp $CP"

# AE distribution directory
ARGS="$ARGS -Dae.dist.dir=$AE_DIST"

# Icon
ARGS="$ARGS -Xdock:icon=$SCRIPT_PATH/ae_studio.png"
ARGS="$ARGS -Ddock.icon=$SCRIPT_PATH/ae_studio.png"

DYLD_LIBRARY_PATH=$DYLD_LIBRARY_PATH:$AE_DIST/libs/studio/plugins
DYLD_LIBRARY_PATH=$DYLD_LIBRARY_PATH:$AE_DIST/bin
export DYLD_LIBRARY_PATH=$DYLD_LIBRARY_PATH

java $ARGS com.andcreations.ae.studio.launcher.Launcher %*