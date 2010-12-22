#!/bin/sh
zip -r              \
    "visint-1.1.zip" \
    COPYING.txt       \
    README.txt         \
    *.dll               \
    *.so                 \
    lib/                  \
    "visint-1.1.jar"
