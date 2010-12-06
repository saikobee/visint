#!/bin/bash

path="$(pwd)"
exec git daemon --base-path="$path"
