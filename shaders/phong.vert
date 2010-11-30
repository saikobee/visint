/*
* vertex shader template
*/

varying vec3 N;
varying vec3 L;
varying vec3 E;

const float PI = 3.14159;
const float TWO_PI = PI * 2.0;


void main() {

    gl_Position = ftransform();
  
    vec4 eyePosition = gl_ModelViewMatrix * gl_Vertex;
    vec4 eyeLightPos   = gl_LightSource[0].position;

    N = -normalize(gl_NormalMatrix * gl_Normal);
    L = eyeLightPos.xyz - eyePosition.xyz;
    E = -eyePosition.xyz;

    //gl_FragColor = gl_Color;
}
