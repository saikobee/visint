#!/bin/bash

[ -f visint.jar ] && rm visint.jar

jar -cmf \
    manifest.txt \
    visint.jar \
    *.class
