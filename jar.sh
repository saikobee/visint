#!/bin/bash

jar -cmf \
    manifest.txt \
    "visint-1.1.jar" \
    *.class
