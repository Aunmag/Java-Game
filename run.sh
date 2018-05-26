#!/bin/sh

set -e
export MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1";

mvn compile \
    -T 1C \
    -Dkotlin.compiler.incremental=true \
    -offline \
    -pl "aunmag.shooter:a-zombie-shooter-game" \
    -am \
    exec:java
