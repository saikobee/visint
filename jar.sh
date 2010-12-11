rm *.jar

libs=(
"/usr/share/java/jogl/jogl.jar"
"/usr/share/java/jogl/gluegen-rt.jar"
)

cp "${libs[@]}" .

jar -cmf \
    manifest.txt \
    brian-mock-integrator.jar \
    gleem \
    shaders \
    *.class
