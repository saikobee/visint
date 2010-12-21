#!/bin/bash

jar -cmf \
    manifest.txt \
    "visint-1.0-beta.jar" \
    *.class
