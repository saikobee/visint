varying vec3 MCposition;

// Author: Jenny Orr
void
main() {
    // Example 1
    // gl_FragColor = vec4( 0.0,0.8,0.0,1.0);
    gl_FragColor = gl_Color;

    /*
    // Example 2
    float scale = 20.0 / 800.0;
    float fr = fract(gl_FragCoord.x * scale);
    gl_FragColor = vec4(
    step( 0.5, fr ),0.0,0.0,1.0
    );
     */

    /*
    // Example 3
    float scale = 20.0 / 800.0;
    float fr = fract(gl_FragCoord.x * scale);
    gl_FragColor = vec4( 
    smoothstep( 0.2, 0.8, fr ),0.0,0.0,1.0 
    );
     */

    /*
    // Example 4
    float scale = 5.0;
    float fr = fract(gl_TexCoord[0].x * scale);
    gl_FragColor = vec4( 
    smoothstep( 0.2,0.8, fr ),0.0,0.0,1.0 
    );
     */

    /*	
    // Example 5
    vec3 col1 = vec3(0.8,0.8,0.8);
    vec3 col2 = vec3(0.0,0.0,0.8);
    float value = 0.5 * (1.0 + (sin( MCposition.x * 5.0 ) * sin( MCposition.z * 20.0 )) );
    vec3 color = mix( col1, col2, value );

    gl_FragColor = vec4(color,1.0);
     */

    /*	
    // Example 6
    vec2 threshold = vec2(0.13,0.13);
    vec2 scale = vec2(10.0,10.0);
    float ss = fract(gl_TexCoord[0].s * scale.s);
    float tt = fract(gl_TexCoord[0].t * scale.t);

    if( (ss > threshold.s) && (tt > threshold.t) ) discard;

    gl_FragColor = vec4(1.0,0.0,0.0,0.0);
     */
}
