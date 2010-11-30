varying vec4 Color;
varying float LightIntensity; 

//uniform float Diam;
//uniform float Tol;
//uniform vec4  DotColor;


void
main(void) {
    float Diam = 0.5;
    float Tol = 0.01;
    vec4 DotColor = vec4(0, 1, 1, 1);
    float sp =  gl_TexCoord[0].s;
    float tp = gl_TexCoord[0].t;
    float numins = float(int(sp / Diam));
    float numint = float(int(tp / Diam));

    gl_FragColor = Color;
    if (mod(numins + numint, 2.0) == 0.0) {
        sp = sp - numins*Diam;
        tp = tp - numint*Diam;
        float radius = Diam/2.0;
        vec3 sptp =  vec3( sp, tp, 0.0 );
        vec3 cntr =  vec3( radius, radius, 0.0 );
        float d = distance( sptp, cntr );
        float t = smoothstep( radius-Tol, radius+Tol, d );

        if (d < radius) {
            discard;
        } else {
            gl_FragColor = mix( DotColor, Color, t );
        }
    }

	gl_FragColor.rgb *= LightIntensity;
}
