import java.awt.Color;
import acm.graphics.*;
import acm.program.*;

/**
 * A java program simulating a bouncing ping-pong ball.
 * This program uses the acm.jar package.
 * 
 * The BallSimulation class is based on code written by Prof. Frank Ferrie,
 * as part of the Fall 2021 Assignment 1.
 * 
 * @author SpacewaIker
 */

public class BallSimulation extends GraphicsProgram{
    /* Initialization of constants
     
     Debug & testing booleans:
     TEST:     if true, print t, x, y, vx and vy for each iteration of the loop
     DEBUG:    if true, print debugging statements & Energy for each iteration
     SLOW:     if true, slows down the simulation (without affecting the physics calculations)
     noBounce: if true, simulation stops after reaching the ground (will still bounce on sides)
     
     World/screen parameters:
     Uppercase X/Y are world values, lowercase x/y are screen values
     Xs/Ys: scale factors. I.e. x = X * Xs
     ptDia: trace point diameter
     
     Simulation parameters:
     g, k:               gravitational and air friction constants
     bSize, bMass:       radius (m) and mass (kg) of the ball
     xInit, yInit:       initial x and y position of the ball (m)
     SLEEP, TICK:        delay time (ms) and clock increment (0.1 ms)
     eThreshold:         Energy threshold below which the simulation will stop
     */

    private static final boolean TEST = false;
    private static final boolean DEBUG =  false;
    private static final boolean SLOW = true;
    private static final boolean noBounce = false;

    // Screen dimensions
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 600;
    private static final int OFFSET = 200;

    // Ping pong table dimensions
    private static final double ppTableLen = 2.74;
    private static final double ppTableHgt = 1.52;
    private static final double lWall = 0.05;
    private static final double rWall = 2.69;

    // World and screen parameters
    private static final double XMin = 0;
    private static final double XMax = ppTableLen;
    private static final double YMin = 0;
    private static final double YMax = ppTableHgt;
    private static final int xMin = 0;
    private static final int xMax = WIDTH;
    private static final int yMin = 0;
    private static final int yMax = HEIGHT;
    private static final double Xs = (xMax - xMin)/(XMax - XMin);
    private static final double Ys = (yMax - yMin)/(YMax - YMin);
    private static final double ptDia = 1;

    // simulation parameters
    public static final double g = 9.8;
    public static final double k = 0.1316;
    public static final double bSize = 0.02;
    public static final double bMass = 0.0027;
    public static final double xInit = lWall;
    public static final double yInit = YMax/2;
    public static final int SLEEP = 10;
    public static final double TICK = SLEEP/1000.0;
    public static final double eThreshold = 0.0005;

    /*Declaration of variables
     
     Inputs:
     v0:    initial velocity
     theta: launch angle
     x0/y0: initial position
     eLoss: energy loss coefficient
     
     Program variables:
     time:      time measured since last bounce
     simTime:   total time of simulation
     vTerminal: terminal velocity,
     v0X/v0Y:   initial x and y components of velocity
     */

    double v0, theta, x0, y0, eLoss;
    double simTime, time, vTerminal, v0X, v0Y;

    public static void main(String[] args){
        new BallSimulation().start();
        /* main() and start() methods are needed for other IDEs than Eclipse.
            But this does not affect how the code runs in Eclipse */
    }
    public void run(){
        // Initialize window size
        this.resize(WIDTH, HEIGHT + OFFSET);
        this.resize(2*WIDTH - getWidth(), 2*(HEIGHT + OFFSET) - getHeight());
        /* EXPLANATION: I have found that resize() would not resize exactly
            to the right width and height, probably due to the window
            header and other such things (I have talked to TA Katrina Poulin
            about this issue). This would result in a constant (but different
            between PCs) difference between the target size and the actual size.
            The above code first resizes the window, then resizes a second
            time, but adding the (target-actual) difference to resize
            to the actual target size. Note that if this difference was = 0,
            (2*WIDTH - getWidth()) would be equal to WIDTH. */

        // Create ground plane
        GRect gPlane = new GRect(0, HEIGHT, WIDTH + OFFSET, 3);
        gPlane.setColor(Color.BLACK);
        gPlane.setFilled(true);
        add(gPlane);
        
        // Create left and right walls
        GRect lWallPlane = new GRect(lWall * Xs, 0, 1, HEIGHT);
        lWallPlane.setColor(Color.BLUE);
        lWallPlane.setFilled(true);
        add(lWallPlane);

        GRect rWallPlane = new GRect(rWall * Xs - 1, 0, 1, HEIGHT);
        rWallPlane.setColor(Color.RED);
        rWallPlane.setFilled(true);
        add(rWallPlane);

        // Create the ball
        GPoint p = W2S(new GPoint(xInit, yInit));
        double ScrX = p.getX();
        double ScrY = p.getY();
        GOval ball = new GOval(ScrX, ScrY, 2 * bSize * Xs, 2 * bSize * Xs);
        // Since Xs != Ys, using Xs for both width and height makes the ball round
        ball.setColor(Color.RED);
        ball.setFilled(true);
        add(ball);

        pause(500);

        // Labels initialization
        // Mode labels:
        if (TEST){
            GLabel testModeLabel = createGLabel("TEST MODE", 800, HEIGHT + 60);
            testModeLabel.setColor(new Color(0, 180, 0)); // dark green
        }
        if (DEBUG){
            GLabel debugModeLabel = createGLabel("DEBUG MODE", 1000, HEIGHT + 60);
            debugModeLabel.setColor(Color.RED);
        }
        if (SLOW){
            GLabel slowModeLabel = createGLabel("SLOW MODE", 800, HEIGHT + 140);
            slowModeLabel.setColor(Color.BLUE);
        }
        if (noBounce){
            GLabel noBounceModeLabel = createGLabel(
                "NO BOUNCE MODE", 1000, HEIGHT + 140);
            noBounceModeLabel.setColor(Color.ORANGE);
        }

        // Parameter labels:
        GLabel simTimeLabel = createGLabel("Simulation Time: 0.0 s", 30, HEIGHT + 50);
        GLabel velocityLabel = createGLabel("Initial velocity:", 30, HEIGHT + 90);
        GLabel angleLabel = createGLabel("Launch angle:", 30, HEIGHT + 130);
        GLabel eLossLabel = createGLabel("Energy loss coefficient:", 30, HEIGHT + 170);

        // Get input from user
        v0 = new myDialogProgram().readDouble("Enter initial velocity (m/s)");
        velocityLabel.setLabel("Initial velocity:  " + v0 + " m/s");

        theta = new myDialogProgram().readDouble("Enter launch angle (degrees)");
        angleLabel.setLabel("Launch angle:  " + theta + (char) 176); //-> °

        eLoss = new myDialogProgram().readDouble("Enter energy loss coefficient [0, 1]");
        eLossLabel.setLabel("Energy loss coefficient:  " + eLoss);

        // Initialize program variables
        x0 = xInit;
        y0 = yInit;
        time = 0;
        simTime = 0;
        vTerminal = bMass * g / (4 * Math.PI * bSize * bSize * k);
        v0X = v0 * Math.cos(theta * Math.PI / 180);
        v0Y = v0 * Math.sin(theta * Math.PI / 180);

        // Simulation loop (update values until hits ground)
        boolean running = true;

        if (TEST)
            System.out.println("\t\t\t Ball Position and Velocity");

        double X, Y, vX, vY;
        double PE, KEx, KEy;

        while (running){
            // Update values
            X = v0X * vTerminal / g * (1 - Math.exp(-g * time / vTerminal));
            Y = vTerminal / g * (v0Y + vTerminal) *
                (1 - Math.exp(-g * time / vTerminal)) - vTerminal * time;
            vX = v0X * Math.exp(-g * time / vTerminal);
            vY = (v0Y + vTerminal) * Math.exp(-g * time / vTerminal) - vTerminal;

            // Print values if TEST
            if (TEST)
                System.out.printf(
                    "t: %.2f\t\t x: %.2f\t y: %.2f\t vx: %.2f\t vy: %.2f\n",
                    time, (X + x0), (Y + y0), vX, vY
                );
            // Simulation sleep (if SLOW, pauses for 1s instead of SLEEP)
            if (SLOW)
                pause(1000);
            else
                pause(SLEEP);

            if (noBounce && Y + y0 < bSize)
                running = false;

            // Check for collisions & simulation end:
            // Compute energy of the ball
            KEx = bMass * vX * vX * 0.5 * (1 - eLoss);
            KEy = bMass * vY * vY * 0.5 * (1 - eLoss);
            PE = bMass * g * (Y + y0 - 2*bSize);
            /* PE must be evaluated from the ground, which is 2*bSize, not 0. */
            if (DEBUG)
                System.out.printf(
                    "KEx: %.4f\t KEy: %.4f\t PE: %.4f\t Total: %.4f\n",
                    KEx, KEy, PE, (KEx + KEy + PE)
                );

            // Check for simulation end
            if ((KEx + KEy + PE) < eThreshold){
                running = false;
                if (DEBUG)
                    System.out.println("Energy Threshold reached");
            }

            // Collision with floor
            if (vY < 0 && y0 + Y < 2*bSize){
                // From energy, compute new velocity
                v0X = Math.sqrt(2 * KEx / bMass);
                v0Y = Math.sqrt(2 * KEy / bMass);

                // v0Y is always positive, v0X is of the same sign as before
                if (vX < 0)
                    v0X = -v0X;

                // Reinitialize time and motion parameters
                time = 0;
                x0 += X;
                y0 = 2*bSize;
                X = 0;
                Y = 0;
            }
            // Collision with left wall
            if (vX < 0 && x0 + X <= lWall + bSize){
                // From energy, compute new velocity
                v0X = Math.sqrt(2 * KEx / bMass);
                v0Y = Math.sqrt(2 * KEy / bMass);

                // v0Y is of the same sign as before, v0X is always positive
                if (vY < 0)
                    v0Y = -v0Y;
                
                // Reinitialize time and motion parameters
                time = 0;
                x0 = lWall;
                y0 += Y;
                X = 0;
                Y = 0;
            }
            // Collision with right wall
            if (vX > 0 && x0 + X >= rWall - 2*bSize){
                // From energy, compute new velocity
                v0X = -Math.sqrt(2 * KEx / bMass);
                v0Y = Math.sqrt(2 * KEy / bMass);

                // v0Y is of the same sign as before, v0X is always negative
                if (vY < 0)
                    v0Y = -v0Y;
                
                // Reinitialize time and motion parameters
                time = 0;
                x0 = rWall - 2*bSize;
                y0 += Y;
                X = 0;
                Y = 0;
            }

            // Update ball position, plot tick mark at current pos:
            // current pos in screen coordinates
            p = W2S(new GPoint(x0 + X, y0 + Y));
            ScrX = p.getX();
            ScrY = p.getY();
            ball.setLocation(ScrX, ScrY);
            trace(ScrX, ScrY);

            time += TICK;
            simTime += TICK;

            simTimeLabel.setLabel(
                String.format("Simulation time: %.2f s", simTime)
            );
        }
    }
    /**
     * Converts world coordinates into simulation (pixel) coordinates
     * 
     * @param P the world coordinates
     * @return simulation coordinates
     */
    private GPoint W2S(GPoint P) {
        double X = P.getX();
        double Y = P.getY();

        double x = (X - XMin) * Xs;
        double y = yMax - (Y - YMin) * Ys;

        return new GPoint(x, y);
    }
    /**
     * Adds a small black dot at the specified location.
     * The dot has diameter {@code ptDia}
     * 
     * @param ScrX the x coordinate (in pixels)
     * @param ScrY the y coordinate (in pixels)
     */
    private void trace(double ScrX, double ScrY){
        GOval dot = new GOval(ScrX + bSize * Xs, ScrY + bSize * Ys, ptDia, ptDia);
        add(dot);
    }
    /**
     * Creates a GLabel with a font size of 24 pt. The GLabel is also added to
     * the canvas.
     * 
     * @param text The text of the GLabel
     * @param x The x position
     * @param y The y position
     * @return GLabel with font size 24 pt, added to the canvas
     */
    private GLabel createGLabel(String text, int x, int y){
        GLabel label = new GLabel(text, x, y);
        label.setFont("-24");
        add(label);
        return label;
    }
}
/**
 * An extension of {@code DialogProgram} that can be instantiated.
 */
class myDialogProgram extends DialogProgram{
}
