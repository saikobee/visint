varying float NdotL;

// Author: Jenny Orr
void
main() {
    // Get the position of the vertex in eye coordinates 
    vec4 ecPos = gl_ModelViewMatrix * gl_Vertex;
    vec3 ecPos3 = (vec3(ecPos)) / ecPos.w;

    // Get the light position from OGL
    vec3 LightPosition = vec3(gl_LightSource[0].position);

    // Compute the transformed normal vector
    vec3 tnorm = -normalize(gl_NormalMatrix * gl_Normal);

    // Compute the vector from the vertex to the light source 
    vec3 lightVec = normalize( LightPosition - ecPos3 );

    // Compute the dot product of the light vector and the normal
    NdotL = dot(lightVec, tnorm);

    gl_Position = ftransform();

    gl_FrontColor = gl_Color;
}
