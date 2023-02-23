#!/usr/bin/env bash

DO_FABRIC=true
DO_FORGE=false

build() {
  cd "$1" || exit 1
  rm build/libs/*.jar 2> /dev/null
  ./gradlew build
  mv build/libs/*.jar ../build
  cd ..
}

if [ "$DO_FABRIC" = true ]; then
  build charm-fabric
fi

if [ "$DO_FORGE" = true ]; then
  build charm-forge
fi