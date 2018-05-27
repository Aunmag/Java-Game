#!/bin/sh

export MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1";
echo "Compiling..."

mvn compile \
    -T 1C \
    --quiet \
    --offline \
    -Dkotlin.compiler.incremental=true \
    -pl "aunmag.shooter:a-zombie-shooter-game" \
    -am \
    exec:java
