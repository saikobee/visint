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

import java.util.*;
import java.io.*;

/**
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel) <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class Main
extends Fly
implements GLEventListener {
    private float frustumBegin =    1f;
    private float frustumEnd   = 1000f;

    private float angle = 0;
    private float color = 0;

    private float d_angle = 0.5f;

    private float pointSize = 3f;
    private float lineWidth = 1f;

    private float[] ambientLighting  = {0.25f, 0.25f, 0.25f, 1f};
    private float[] diffuseLighting  = {1.00f, 1.00f, 1.00f, 1f};
    private float[] specularLighting = {1.00f, 1.00f, 1.00f, 1f};

    private float[] lightPos = {100.00f, +900.00f, 100.00f, 0f};

    private float[] bgColor = {0.25f, 0.25f, 0.25f, 1.00f};
    private float[] white   = {1.00f, 1.00f, 1.00f, 1.00f};
    private float[] green   = {0.31f, 0.60f, 0.02f, 1.00f};
    private float[] brown   = {0.56f, 0.35f, 0.01f, 1.00f};
    private float[] blue    = {0.13f, 0.29f, 0.53f, 1.00f};
    private float[] purple  = {1.00f, 0.00f, 1.00f, 1.00f};

    //private String file = "test.dem";
    //private String file = "f.dem";
    private InputStream file = System.in;

    //private DEM dem = new DEM(filename);
    private DEM dem = new DEM(file);
    private ArrayList<ArrayList<Float>> elevations;
    private float[][][] normals;

    public static void main(String[] args) {
        Frame frame = new Frame("Digital Elevation Model Lab by Brian Mock");
        GLCanvas canvas = new GLCanvas();

        Main inst = new Main();
        canvas.addGLEventListener(inst);
        canvas.addKeyListener(inst);
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
        canvas.requestFocus();
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

        // Setup the drawing area and shading mode
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // try setting this to GL_FLAT and see what happens.
        gl.glShadeModel(GL.GL_SMOOTH);
        //gl.glShadeModel(GL.GL_FLAT);

        // BRIAN ADDED HERE
        elevations = dem.getElevationsList();
        normals = dem.getNormals();
        //elevations = dem.getElevationsListDiag();

        //System.out.println(elevations);

        // Set up larger, rounded points
        gl.glPointSize(pointSize);
        gl.glEnable(gl.GL_POINT_SMOOTH);

        // Set up thicker lines, smoother lines
        gl.glLineWidth(lineWidth);
        //gl.glEnable(gl.GL_LINE_SMOOTH);

        // Enable lighting
        gl.glEnable(gl.GL_LIGHTING);
        gl.glEnable(gl.GL_COLOR_MATERIAL);
        gl.glEnable(gl.GL_LIGHT0);

        gl.glEnable(gl.GL_NORMALIZE);

        // Enable alpha blending
        //gl.glEnable(gl.GL_BLEND);
        //gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);

        initKeyBinds();
        changeLighting(gl);
        calcFSU();
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
        glu.gluPerspective(45.0f, h, frustumBegin, frustumEnd);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void display(GLAutoDrawable drawable) {
        GL  gl  = drawable.getGL();
        GLU glu = new GLU();

        // Clear the drawing area
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();

        dispatchKeyActions();
        myLookAt(gl);

        gl.glClearColor(bgColor[0], bgColor[1], bgColor[2], bgColor[3]);

        angle += d_angle;
        angle %= 360;

        gl.glTranslatef(0, 0, -350);

        //gl.glRotatef(45,    1, 0, 0);
        //gl.glRotatef(angle, 0, 1, 0);

        gl.glTranslatef(0, -50, 0);

        //drawSurfaceLineStrip(gl);
        drawSurfaceQuadStrip(gl);
        //drawSurfacePoints(gl);

        gl.glFlush();
    }

    private void changeLighting(GL gl) {
        gl.glLightfv(gl.GL_LIGHT0, gl.GL_AMBIENT,  ambientLighting,  0);
        gl.glLightfv(gl.GL_LIGHT0, gl.GL_POSITION, lightPos,         0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE,  diffuseLighting,  0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, specularLighting, 0);
    }

    /** Draw the surface. */
    private void drawSurfacePoints(GL gl) {
        float fudge = 0.005f;

        gl.glBegin(gl.GL_POINTS);
        //gl.glBegin(gl.GL_QUAD_STRIP);

        int m = elevations.size();
        int n = elevations.get(0).size();
        for (int i=0; i < m; ++i) {
            for (int j=0; j < n; ++j) {
                float height = elevations.get(i).get(j); 

                colorFunc(gl, i, height, j);
                vertex(gl, i, height + fudge, j);
            }
        }

        gl.glEnd();
    }

    /** Draw the surface. */
    private void drawSurfaceQuadStrip(GL gl) {
        int m = elevations.size();
        int n = elevations.get(0).size();
        for (int i=0; i < (m-1); ++i) {
            gl.glBegin(gl.GL_QUAD_STRIP);

            for (int j=0; j < (n-1); ++j) {
                quadStripHelper(gl, i+0, j+0);
                quadStripHelper(gl, i+0, j+1);
                quadStripHelper(gl, i+1, j+0);
                quadStripHelper(gl, i+1, j+1);
            }

            gl.glEnd();
        }
    }

    /** Draw the surface. */
    private void drawSurfaceLineStrip(GL gl) {
        int m = elevations.size();
        int n = elevations.get(0).size();
        for (int i=0; i < (m-1); ++i) {
            gl.glBegin(gl.GL_LINE_STRIP);
            //gl.glBegin(gl.GL_LINE);

            for (int j=0; j < (n-1); ++j) {
                lineStripHelper(gl, i+0, j+0);
                lineStripHelper(gl, i+1, j+1);
            }

            gl.glEnd();
        }
    }

    private void quadStripHelper(GL gl, int i, int j) {
        float height = elevations.get(i).get(j); 

        colorFunc2(gl, i, height, j);
        vertex(gl, i, height, j);
    }

    private void lineStripHelper(GL gl, int i, int j) {
        float height = elevations.get(i).get(j); 

        colorFunc3(gl, i, height, j);
        vertex(gl, i, height, j);
    }

    private void vertex(GL gl, float i, float height, float j) {
        float x = (i - dem.rows() /2f) * 3f;
        float z = (j - dem.cols() /2f) * 3f;
        float y = height * 100f;

        gl.glNormal3fv(avgNormal((int)i, (int)j), 0);
        gl.glVertex3f(x, y, z);
    }

    private float[] avgNormal(int i, int j) {
        float[] a = normals[i-0][j-0];

        if (i < 1 || j < 1) {
            return a;
        }

        float[] b = normals[i-0][j-1];
        float[] c = normals[i-1][j-0];
        float[] d = normals[i-1][j-1];

        return scale3f(sum3f(sum3f(a,b), sum3f(c,d)), 0.25f);
    }

    /** Give a color based on height for points. */
    private void colorFunc(GL gl, float x, float y, float z) {
        float b_r = 0.10f;
        float b_g = 0.10f;
        float b_b = 0.10f;
        float b_a = 0.10f;

        gl.glColor4f(
            b_r,
            b_g,
            b_b,
            b_a
        );
    }

    /** Give a color based on height for wireframe. */
    private void colorFunc3(GL gl, float x, float y, float z) {
        float b_r = 0.10f;
        float b_g = 0.10f;
        float b_b = 0.10f;
        float b_a = 0.10f;

        gl.glColor4f(
            b_r,
            b_g,
            b_b,
            b_a
        );
    }

    /** Give a color based on height for quads. */
    private void colorFunc2(GL gl, float x, float y, float z) {
        float[] rgba;

        float fudge       = 0.15f;
        //\\//\\//\\//\\//\\//\\//
        float min         = 0.00f;
        //\\//\\//\\//\\//\\//\\//
        float waterEnd    = 0.25f;
        float blend1End   = waterEnd + fudge;
        //\\//\\//\\//\\//\\//\\//
        float grassEnd    = 0.45f;
        float blend2End   = grassEnd + fudge;
        //\\//\\//\\//\\//\\//\\//
        float brownEnd    = 0.75f;
        float blend3End   = brownEnd + fudge;
        //\\//\\//\\//\\//\\//\\//
        float max         = 1.00f;

        if (inRange(y, min, waterEnd)) {
            rgba = lerp4f(blue, blue, min, waterEnd, y);
        }
        else if (inRange(y, waterEnd, blend1End)) {
            rgba = lerp4f(blue, green, waterEnd, blend1End, y);
        }
        else if (inRange(y, blend1End, grassEnd)) {
            rgba = lerp4f(green, green, blend1End, grassEnd, y);
        }
        else if (inRange(y, grassEnd, blend2End)) {
            rgba = lerp4f(green, brown, grassEnd, blend2End, y);
        }
        else if (inRange(y, blend2End, brownEnd)) {
            rgba = lerp4f(brown, brown, blend2End, brownEnd, y);
        }
        else if (inRange(y, brownEnd, blend3End)) {
            rgba = lerp4f(brown, white, brownEnd, blend3End, y);
        }
        else if (inRange(y, blend3End, max)) {
            rgba = lerp4f(white, white, blend3End, max, y);
        }
        else {
            //throw new RuntimeException("Unexpected value: " + y);
            //System.err.println("Unexpected value: " + y);
            rgba = purple;
        }

        //color(gl, white);
        color(gl, rgba);
    }

    private float[] lerp4f(float[] a, float[] b, float s, float t, float p) {
        p = scaleP(p, s, t);
        float[] x = scale4f(a, (1 - p));
        float[] y = scale4f(b, (0 + p));
        float[] z = sum4f(x, y);
        return z;
        /*
        return sum4f(
            a,
            scale4f(
                diff4f(b, a),
                p
            )
        );
        */
    }

    private float scaleP(float p, float x, float y) {
        float a = 1.00f / (y - x);
        float b = -a*x;
        return a*p + b;
        //return (p-x)/(y-x);
    }

    private float[] scale4f(float[] rgba, float scale) {
        return new float[] {
            rgba[0] * scale,
            rgba[1] * scale,
            rgba[2] * scale,
            rgba[3] * scale
        };
    }
    private float[] sum4f(float[] rgba1, float[] rgba2) {
        return new float[] {
            rgba1[0] + rgba2[0],
            rgba1[1] + rgba2[1],
            rgba1[2] + rgba2[2],
            rgba1[3] + rgba2[3]
        };
    }
    private float[] diff4f(float[] rgba1, float[] rgba2) {
        return new float[] {
            rgba1[0] - rgba2[0],
            rgba1[1] - rgba2[1],
            rgba1[2] - rgba2[2],
            rgba1[3] - rgba2[3]
        };
    }

    private float[] scale3f(float[] rgba, float scale) {
        return new float[] {
            rgba[0] * scale,
            rgba[1] * scale,
            rgba[2] * scale
        };
    }
    private float[] sum3f(float[] rgba1, float[] rgba2) {
        return new float[] {
            rgba1[0] + rgba2[0],
            rgba1[1] + rgba2[1],
            rgba1[2] + rgba2[2]
        };
    }

    private void color(GL gl, float[] rgba) {
        gl.glColor4f(
            rgba[0],
            rgba[1],
            rgba[2],
            rgba[3]
        );
    }

    private boolean inRange(float x, float a, float b) {
        return a <= x && x <= b;
    }

    /** Clamp x's value between 0 and 1. */
    private float clamp(float x) {
        return // Who needs if/else when you have the tertiary operator?
          (x > 1f) ?    1f
        : (x < 0f) ?    0f
        :               x;
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
}

