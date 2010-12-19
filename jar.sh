rm *.jar

jar -cmf \
    manifest.txt \
    visint.jar \
    gleem \
    shaders \
    *.class
