varying float NdotL;

void main() 
{
  vec3 SurfaceColor = vec3(gl_FrontMaterial.diffuse);

  // Produces the stair step pattern
  float scale = ceil(0.125 * NdotL) / 0.125;

  gl_FragColor = vec4(SurfaceColor * scale, 1.0);
}
