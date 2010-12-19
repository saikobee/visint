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
import java.awt.event.*;
import javax.swing.event.*;

/**
 * This class implements fly-through controls for OpenGL.
 * @author Brian Mock
 */
public class Fly
implements KeyListener {

    private Map<Integer, Boolean> keys  = new HashMap<Integer, Boolean>();
    private Map<String,  Integer> binds = new HashMap<String,  Integer>();

    private float[] eyeLoc = {0,  50, 200};
    private float[] center = {0,  50,   0};
    private float[] upVec  = {0,   1,   0};

    private float rotX = 0;
    private float rotY = 0;

    private float posX = 0;
    private float posY = 0;

    // Forward, side (right), up vectors
    private float[] f;
    private float[] s;
    private float[] u;

    protected float moveSpeed = 64.00f;
    protected float turnSpeed =  1.00f;
    protected float tiltSpeed =  1.00f;

    private   long  thisTime   = 0;
    private   long  lastTime   = 0;
    protected float timePassed = 0;

    /** Calculates the camera's coordinate vectors. */
    protected void
    calcFSU() {
        f = Util.displacementVector(center, eyeLoc);
        f = Util.normalize(f);

        upVec = Util.normalize(upVec);

        s = Util.cross(f, upVec);
        u = Util.cross(s, f);
    }

    /** Sets up the default keybindings. */
    protected void
    initKeyBinds() {
        binds.put("move_forward",  KeyEvent.VK_W);
        binds.put("move_backward", KeyEvent.VK_S);
        binds.put("move_left",     KeyEvent.VK_A);
        binds.put("move_right",    KeyEvent.VK_D);

        binds.put("move_up",   KeyEvent.VK_Q);
        binds.put("move_down", KeyEvent.VK_E);

        binds.put("turn_left",  KeyEvent.VK_J);
        binds.put("turn_right", KeyEvent.VK_L);
        binds.put("turn_up",    KeyEvent.VK_I);
        binds.put("turn_down",  KeyEvent.VK_K);

        binds.put("roll_left",  KeyEvent.VK_U);
        binds.put("roll_right", KeyEvent.VK_O);
    }

    /** Replaces gluLookAt and takes vectors instead of massive numbers of floats. */
    public void
    myLookAt(GL gl) {
        // Remember, this is transposed...
        float[] mat = {
            s[0], u[0], -f[0], 0,
            s[1], u[1], -f[1], 0,
            s[2], u[2], -f[2], 0,
            0,    0,     0,    1
        };

        gl.glMultMatrixf(mat, 0);
        gl.glTranslatef(
            -eyeLoc[0],
            -eyeLoc[1],
            -eyeLoc[2]
        );
    }

    /** Called to update time variables, so you know how much time
     * has elapsed since the last frame.
     */
    protected void
    updateTime() {
        lastTime = thisTime;
        thisTime = System.currentTimeMillis();
        timePassed  = thisTime - lastTime;
        timePassed /= 1000f; // convert to seconds
    }

    /** Calls the key handler for all currently bound and held keys. */
    protected void
    dispatchKeyActions() {
        for (Integer key: binds.values()) {
            // Key is pressed
            if (isKeyHeld(key)) {
                actOnKey(key);
            }
        }
    }

    /** Returns whether or not the key specified by code is held. */
    public boolean
    isKeyHeld(int code) {
        return keys.containsKey(code) && keys.get(code);
    }

    /** Perform the proper action given the currently held key.
     * This method is highly redundant, but attempts to abstract the bulk
     * of it into a method have failed... :(
     */
    private void
    actOnKey(int key) {
        if (key == binds.get("move_forward")) {
            float speed = +moveSpeed * timePassed;
            eyeLoc = Util.add(eyeLoc, Util.times(speed, f));
        }
        else if (key == binds.get("move_backward")) {
            float speed = -moveSpeed * timePassed;
            eyeLoc = Util.add(eyeLoc, Util.times(speed, f));
        }
        else if (key == binds.get("move_left")) {
            float speed = -moveSpeed * timePassed;
            eyeLoc = Util.add(eyeLoc, Util.times(speed, s));
        }
        else if (key == binds.get("move_right")) {
            float speed = +moveSpeed * timePassed;
            eyeLoc = Util.add(eyeLoc, Util.times(speed, s));
        }
        else if (key == binds.get("move_up")) {
            float speed = +moveSpeed * timePassed;
            eyeLoc = Util.add(eyeLoc, Util.times(speed, u));
        }
        else if (key == binds.get("move_down")) {
            float speed = -moveSpeed * timePassed;
            eyeLoc = Util.add(eyeLoc, Util.times(speed, u));
        }
        else if (key == binds.get("turn_left")) {
            float speed = -turnSpeed * timePassed;
            f = Util.plus(f, Util.times(speed, s));
            f = Util.normalize(f);
            s = Util.cross(f, u);
        }
        else if (key == binds.get("turn_right")) {
            float speed = +turnSpeed * timePassed;
            f = Util.plus(f, Util.times(speed, s));
            f = Util.normalize(f);
            s = Util.cross(f, u);
        }
        else if (key == binds.get("turn_up")) {
            float speed = +tiltSpeed * timePassed;
            f = Util.plus(f, Util.times(speed, u));
            f = Util.normalize(f);
            u = Util.cross(s, f);
        }
        else if (key == binds.get("turn_down")) {
            float speed = -tiltSpeed * timePassed;
            f = Util.plus(f, Util.times(speed, u));
            f = Util.normalize(f);
            u = Util.cross(s, f);
        }
        else if (key == binds.get("roll_left")) {
            float speed = -turnSpeed * timePassed;
            u = Util.plus(u, Util.times(speed, s));
            u = Util.normalize(u);
            s = Util.cross(f, u);
        }
        else if (key == binds.get("roll_right")) {
            float speed = +turnSpeed * timePassed;
            u = Util.plus(u, Util.times(speed, s));
            u = Util.normalize(u);
            s = Util.cross(f, u);
        }
    }

    /* Simply enter currently pressed keys into the key map.
     * We remove keys when they are released.
     * This way, the keys map keeps track of currently held keys.
     * Currently I have no need for any other kind of key event besides holding,
     * so this suffices.
     */
    public void keyPressed (KeyEvent e) { keys.put(e.getKeyCode(),  true); }
    public void keyReleased(KeyEvent e) { keys.put(e.getKeyCode(), false); }
    public void keyTyped   (KeyEvent e) { }

    public void
    displayChanged(
        GLAutoDrawable drawable,
        boolean modeChanged,
        boolean deviceChanged
    ) {
    }
}

