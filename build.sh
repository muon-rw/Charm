#!/usr/bin/env bash

build() {
  cd "$1" || exit 1
  rm build/libs/*.jar 2> /dev/null
  ./gradlew build
  mv build/libs/*.jar ../builds
  cd ..
}

build fabric
build forge