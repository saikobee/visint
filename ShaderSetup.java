import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javax.media.opengl.GL;

/**
 * @author Jenny Orr
 */
public class ShaderSetup  {
    //String frag = "shaders/cel.frag";
    //String vert = "shaders/cel.vert";

    //String frag = "shaders/dots.frag";
    //String vert = "shaders/dots.vert";

    //String frag = "shaders/hello.frag";
    //String vert = "shaders/hello.vert";

    String frag = "shaders/phong.frag";
    String vert = "shaders/phong.vert";

    int prog;

    public ShaderSetup() {}

    public void setFragShader(String fragName) {
        frag = fragName;
    }

    public void setVertShader(String vertName) {
        vert = vertName;
    }

    // Reads in the vertex and fragment shaders, and links them to the program.   
    public void setupShader(GL gl) {
        int v = gl.glCreateShader(GL.GL_VERTEX_SHADER);
        int f = gl.glCreateShader(GL.GL_FRAGMENT_SHADER);

        String vshaderCode[] = new String[1];
        String fshaderCode[] = new String[1];
        String gshaderCode[] = new String[1];

        int[] len = null;
        try {
            vshaderCode[0] = fileString(vert);
            fshaderCode[0] = fileString(frag);
        } catch (Exception e) {
            System.out.println("Error reading file " + e);
        }
        gl.glShaderSource(v, 1, vshaderCode, len, 0);
        gl.glShaderSource(f, 1, fshaderCode, len, 0);

        gl.glCompileShader(v);
        gl.glCompileShader(f);
       
        checkLogInfo(gl, v);
        checkLogInfo(gl, f);
        
        prog = gl.glCreateProgram();
        gl.glAttachShader(prog, v);
        gl.glAttachShader(prog, f);
        gl.glLinkProgram(prog);
        gl.glUseProgram(prog);

    }


    /**  Check for errors in shader code.
    
    glGetInfoLogARB returns the information log for the specified OpenGL-managed 
    object. The information log for a shader object is modified when the shader 
    is compiled, and the information log for a program object is modified when 
    the program object is linked or validated.
    
    The information log is a string that may contain diagnostic messages, warning 
    messages, and other information about the last compile operation (for shader 
    objects) or the last link or validate operation (for program objects). When a 
    shader object or a program object is created, its information log will be a 
    string of length 0.
    
    The information log is the OpenGL implementor's only mechanism for conveying 
    information about compiling, linking, and validating, so the information log 
    can be helpful to application developers during the development process even 
    when compiling and linking operations are successful.
     **/
    private void checkLogInfo(GL gl, int obj) {
        IntBuffer iVal = IntBuffer.allocate(1);
        gl.glGetObjectParameterivARB(obj, GL.GL_OBJECT_INFO_LOG_LENGTH_ARB, iVal);

        int length = iVal.get();

        if (length <= 1) {
            return;
        }

        ByteBuffer infoLog = ByteBuffer.allocate(length);

        iVal.flip();
        gl.glGetInfoLogARB(obj, length, iVal, infoLog);

        byte[] infoBytes = new byte[length];
        infoLog.get(infoBytes);
        System.out.println("GLSL Validation >> " + new String(infoBytes));
    }

    public String fileString(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String res = "";
        String line;
        while ((line = br.readLine()) != null) {
            res += line + "\n";
        }
        return res;
    }
}

