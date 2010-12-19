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

import javax.swing.*;

import java.util.*;
import java.io.*;

/**
 * This class is in charge of making a new OpenGL window, closing it,
 * and asking to draw the pieces of the visualization.
 * @author Brian Mock
 */
public class Visualizer
implements GLEventListener {
    private float frustumBegin =      1f;
    private float frustumEnd   = 100000f;

    private float angle = 0;
    private float color = 0;

    private float d_angle = 0.5f;

    private float pointSize =  5f;
    private float lineWidth =  3f;

    private static int[] defaultRes    = {800, 600};
    //private static int[] defaultRes    = {640, 480};
    //private static int[] defaultRes    = {320, 240};
    private static int   defaultWidth  = defaultRes[0];
    private static int   defaultHeight = defaultRes[1];

    private float[] ambientLighting  = {0.50f, 0.50f, 0.50f, 1f};
    private float[] diffuseLighting  = {0.50f, 0.50f, 0.50f, 1f};
    private float[] specularLighting = {1.00f, 1.00f, 1.00f, 1f};

    private float[] lightPos = {0.00f, 50.00f, 0.00f, 0f};

    private float[] bgColor = Colors.BLACK;

    private Func  theFunc;
    private Cache theCache;

    private ShaderSetup shaders;

    private Axes theAxes;

    public GLCanvas canvas;

    private static String theTitle = "Visual Integrator by Brian Mock";

    private MouseControls mouseControls = new MouseControls();
    private Fly fly = new Fly();

    public
    Visualizer(Func aFunc, String shaderName) {
        setFunc(aFunc);
        initShaderObject(shaderName);
    }

    public static void
    launchWith(Func aFunc, String shaderName) {
        JFrame frame = new JFrame(theTitle);
        GLCanvas canvas = new GLCanvas();

        final Visualizer inst = new Visualizer(aFunc, shaderName);
        inst.canvas = canvas;
        //inst.initShaderObject(shaderName);
        //inst.setFunc(aFunc);
        canvas.addGLEventListener(inst);

        frame.add(canvas);
        frame.setSize(defaultWidth, defaultHeight);
        final Animator animator = new Animator(canvas);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread() {
                    public void run() {
                        animator.stop();
                        // Commented this so all the windows won't close
                        //System.exit(0);
                        inst.stopThreads();
                    }
                }.start();
            }
        });
        // Center frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        animator.start();
        canvas.requestFocus();
    }

    public void
    init(GLAutoDrawable drawable) {
        // Use debug pipeline
        // drawable.setGL(new DebugGL(drawable.getGL()));

        GL gl = drawable.getGL();
        //System.err.println("INIT GL IS: " + gl.getClass().getName());

        // Keyboard controls
        canvas.addKeyListener(fly);

        // Mouse controls
        canvas.addMouseListener(mouseControls);
        canvas.addMouseWheelListener(mouseControls);
        canvas.addMouseMotionListener(mouseControls);

        // Enable VSync
        gl.setSwapInterval(1);

        // Enable depth testing
        gl.glEnable(gl.GL_DEPTH_TEST);

        // Setup the drawing area and shading mode
        gl.glClearColor(0, 0, 0, 0);

        // try setting this to GL_FLAT and see what happens.
        gl.glShadeModel(gl.GL_SMOOTH);
        //gl.glShadeModel(gl.GL_FLAT);

        //System.out.println(elevations);

        // Set up larger, rounded points
        gl.glPointSize(pointSize);
        //gl.glEnable(gl.GL_POINT_SMOOTH);

        // Set up thicker lines, smoother lines
        gl.glLineWidth(lineWidth);
        //gl.glEnable(gl.GL_LINE_SMOOTH);

        // Enable lighting
        gl.glEnable(gl.GL_LIGHTING);
        gl.glEnable(gl.GL_COLOR_MATERIAL);
        gl.glEnable(gl.GL_LIGHT0);

        // Enable vertex arrays
        gl.glEnable(gl.GL_VERTEX_ARRAY);

        // Normalize surface normals
        gl.glEnable(gl.GL_NORMALIZE);

        // Enable alpha blending
        gl.glEnable(gl.GL_BLEND);
        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);

        fly.initKeyBinds();
        changeLighting(gl);
        shaders.setupShader(gl);
        fly.calcFSU();
        makeCache();
        makeAxes();
    }

    public void
    initShaderObject(String shader) {
        shaders = new ShaderSetup(shader);
    }

    public void
    makeAxes() {
        theAxes = new Axes();
    }

    public void
    makeCache() {
        theCache = new Cache(theFunc);
    }

    public void
    setFunc(Func aFunc) {
        theFunc = aFunc;
    }

    public void
    stopThreads() {
        theCache.stopThreads();
    }

    public void
    reshape(
        GLAutoDrawable drawable,
        int x, int y,
        int width, int height
    ) {
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
        glu.gluPerspective(45.0f, h, frustumBegin, frustumEnd);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void
    display(GLAutoDrawable drawable) {
        GL  gl  = drawable.getGL();
        GLU glu = new GLU();

        // Clear the drawing area
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();

        fly.updateTime();
        fly.dispatchKeyActions();
        mouseControls.dolly(gl);
        mouseControls.track(gl);
        fly.myLookAt(gl);
        mouseControls.rotate(gl);

        Util.clearColor(gl, bgColor);

        angle += d_angle;
        angle %= 360;

        theAxes  .draw(gl);
        theCache .draw(gl);

        gl.glFlush();
    }

    private void
    changeLighting(GL gl) {
        gl.glLightfv(gl.GL_LIGHT0, gl.GL_POSITION, lightPos,         0);
        gl.glLightfv(gl.GL_LIGHT0, gl.GL_AMBIENT,  ambientLighting,  0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE,  diffuseLighting,  0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, specularLighting, 0);
    }

    public void
    displayChanged(
        GLAutoDrawable drawable,
        boolean modeChanged,
        boolean deviceChanged
    ) {
    }
}

