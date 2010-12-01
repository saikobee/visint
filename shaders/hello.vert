varying vec3 MCposition;
// Author: Jenny Orr
void
main() {
    // Pass along the texture coordinate
    gl_TexCoord[0] = gl_MultiTexCoord0;

    MCposition = vec3(gl_Vertex);
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    gl_FrontColor = gl_Color;
}
