import com.sun.opengl.util.Animator;
import com.sun.opengl.util.GLUT;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

/**
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel) <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class Test implements GLEventListener {

    public float angle   = 0.00f;
    public float d_angle = 0.50f;

    public float[] bgColor    = {0.10f, 0.10f, 0.10f, 1.00f};
    public float[] solidColor = {0.25f, 0.25f, 0.25f, 1.00f};
    public float[] wireColor  = {0.22f, 0.22f, 0.22f, 1.05f};

    public static void main(String[] args) {
        Frame frame = new Frame("Lighting Test by Brian Mock");
        GLCanvas canvas = new GLCanvas();

        canvas.addGLEventListener(new Test());
        frame.add(canvas);
        frame.setSize(640, 480);
        final Animator animator = new Animator(canvas);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {

                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        // Center frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        animator.start();
    }

    public void init(GLAutoDrawable drawable) {
        // Use debug pipeline
        // drawable.setGL(new DebugGL(drawable.getGL()));

        GL gl = drawable.getGL();
        //System.err.println("INIT GL IS: " + gl.getClass().getName());

        // Enable VSync
        gl.setSwapInterval(1);

        // Enable depth testing
        gl.glEnable(GL.GL_DEPTH_TEST);

        gl.glEnable(gl.GL_COLOR_MATERIAL);
        gl.glEnable(gl.GL_LIGHTING);
        gl.glEnable(gl.GL_LIGHT0);

        // Setup the drawing area and shading mode
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // try setting this to GL_FLAT and see what happens.
        gl.glShadeModel(GL.GL_SMOOTH);
        //gl.glShadeModel(GL.GL_FLAT);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        GLU glu = new GLU();

        // avoid a divide by zero error!
        if (height <= 0) {
            height = 1;
        }

        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1.0, 20.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void display(GLAutoDrawable drawable) {
        GL   gl   = drawable.getGL();
        GLUT glut = new GLUT();

        angle += d_angle;

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        clearBG(gl);

        gl.glTranslatef(0f, 0f, -5f);
        gl.glRotatef(angle, 0.0f, 1.0f, 0.0f);

        drawObject(gl, glut);

        gl.glFlush();
    }

    public void drawObject(GL gl, GLUT glut) {
        color(gl, solidColor);
        drawSolid(gl, glut);

        scaleUniform(gl, 1.005f);
        color(gl, wireColor);
        drawWire(gl, glut);
    }

    public void drawSolid(GL gl, GLUT glut) {
        glut.glutSolidTeapot(1);
    }
    public void drawWire(GL gl, GLUT glut) {
        glut.glutWireTeapot(1);
    }

    public void color(GL gl, float[] rgba) {
        gl.glColor4f(
            rgba[0],
            rgba[1],
            rgba[2],
            rgba[3]
        );
    }

    public void scaleUniform(GL gl, float scale) {
        gl.glScalef(scale, scale, scale);
    }

    public void clearBG(GL gl) {
        gl.glClearColor(
            bgColor[0],
            bgColor[1],
            bgColor[2],
            bgColor[3]
        );
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
}

