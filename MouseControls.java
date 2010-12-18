import javax.media.opengl.GL;

import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;

public class MouseControls
extends MouseInputAdapter {
    private int curMouseBtn = 0;

    private int lastMouseX = 0;
    private int lastMouseY = 0;

    private int mouseX = 0;
    private int mouseY = 0;

    private int mouseDeltaX = 0;
    private int mouseDeltaY = 0;

    private float dx = 0f;
    private float dy = 0f;

    private float drx = 0f;
    private float dry = 0f;

    private float rotX = 0f;
    private float rotY = 0f;

    private float posX = 0f;
    private float posY = 0f;

    private int dollyVal = 0;

    private float dollySpeed  = 8.00f;
    private float trackSpeed  = 0.15f;
    private float tumbleSpeed = 0.25f;

    public void
    rotate(GL gl) {
        gl.glRotatef(+(rotX + drx), 0, 1, 0);
        gl.glRotatef(-(rotY + dry), 1, 0, 0);
    }

    public void
    dolly(GL gl) {
        gl.glTranslatef(0, 0, dollyVal);
    }

    public void
    track(GL gl) {
        gl.glTranslatef(posX + dx, posY + dy, 0);
    }

    public void
    mouseWheelMoved(MouseWheelEvent e) {
        int wheel = e.getWheelRotation();
        //System.out.println("wheel moved");
        //System.out.println("wheel clicks " + wheel);
        dollyVal += -wheel * dollySpeed;
    }

    public void
    mouseDragged(MouseEvent e) {
        //System.out.println("mouse dragged");

        mouseX = e.getX();
        mouseY = e.getY();

        mouseDeltaX = mouseX - lastMouseX;
        mouseDeltaY = mouseY - lastMouseY;

        // Java's y faces down, correct for that
        mouseDeltaY *= -1;

        if (curMouseBtn == 1) {
            dx = trackSpeed * mouseDeltaX;
            dy = trackSpeed * mouseDeltaY;
        }
        else if (curMouseBtn == 3) {
            drx = tumbleSpeed * mouseDeltaX;
            dry = tumbleSpeed * mouseDeltaY;
        }
    }

    public void
    mousePressed(MouseEvent e) {
        //System.out.println("mouse pressed - button = " + e.getButton());
        curMouseBtn = e.getButton();

        lastMouseX = e.getX();
        lastMouseY = e.getY();
    }

    public void
    mouseReleased(MouseEvent e) {
        //System.out.println("mouse released");
        int button = e.getButton();

        lastMouseX = e.getX();
        lastMouseY = e.getY();

        if (button == 1) {
            posX += dx;
            posY += dy;
        }
        else if (button == 3) {
            rotX += drx;
            rotY += dry;
        }

        dx = 0;
        dy = 0;

        drx = 0;
        dry = 0;
    }
}
