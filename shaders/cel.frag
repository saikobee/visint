varying float NdotL;

// Author: Jenny Orr
void
main() {
    float k = 16.0;

    // Produces the stair step pattern
    float scale = ceil(k * NdotL) / k;

    gl_FragColor = gl_Color * scale;
}
