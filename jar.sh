rm *.jar

jar -cmf \
    manifest.txt \
    visint.jar \
    *.class
