#!/bin/bash

text=(*.txt)
libs=(visint *.dll *.so lib/)
jar="visint-1.1.jar"

stuff=("${text[@]}" "${libs[@]}" "$jar")

output="visint-1.1"

# Windows
echo "Packaging for Windows"
zip -r "$output.zip" "${stuff[@]}"

echo

# Linux
echo "Packaging for Linux"
if [ -e "$output" ]; then
    rm -r "$output"
fi
mkdir "$output"
cp -r "${stuff[@]}" "$output"
tar -czvf "$output.tar.gz" "$output"
rm -r "$output"
