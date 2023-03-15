#!/usr/bin/env bash

DO_FABRIC=true
DO_FORGE=true

build() {
  cd "$1" || exit 1
  rm build/libs/*.jar 2> /dev/null
  ./gradlew build
  mv build/libs/*.jar ../build
  cd ..
}

if [ "$DO_FABRIC" = true ]; then
  build api-fabric
  cp build/charm_api-fabric* core-fabric/libs/
  build core-fabric

  # Copy API and Core for Fabric into Charm
  cp build/charm_api-fabric* charm-fabric/libs/
  cp build/charm_core-fabric* charm-fabric/libs/
  build charm-fabric
fi

if [ "$DO_FORGE" = true ]; then
  build api-forge
  cp build/charm_api-forge* core-forge/libs/
  build core-forge

  # Copy API and Core for Forge into Charm
  cp build/charm_api-forge* charm-forge/src/main/resources/META-INF/jarjar/
  cp build/charm_core-forge* charm-forge/src/main/resources/META-INF/jarjar/
  build charm-forge
fi