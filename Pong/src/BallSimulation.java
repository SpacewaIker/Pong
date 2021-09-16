/**
 * A java program simulating a bouncing ping-pong ball.
 * This program uses the acm.jar package.
 * 
 * @author SpacewaIker
 */

import java.awt.Color;
import acm.graphics.*;
import acm.program.*;

public class BallSimulation extends GraphicsProgram{
    /**
     * Initialization of constants
     * 
     * World/screen parameters:
     * Uppercase X/Y are world values, lowercase x/y are screen values
     * Xs/Ys: ?
     * ptDia: trace point diameter
     * 
     * Simulation parameters:
     * g, k:               gravitational and air friction constants
     * bSize, bMass:       radius (m) and mass (kg) of the ball
     * xInit, yInit:       initial x and y position of the ball (m)
     * vDflt, tDflt:       default velocity (m/s) and angle (degrees) of ball
     * SLEEP, TICK:        delay time (ms) and clock increment (0.1 ms)
     */

    private static final boolean TEST = false;
    private static final boolean DEBUG = true;
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
    public static final double xInit = 0;
    public static final double yInit = YMax/2;
    public static final double vDflt = 3;
    public static final double tDflt = 30;
    public static final int SLEEP = 10;
    public static final double TICK = SLEEP/1000.0;
    public static final double eThreshold = 0.001;

    /**
     * Declaration of variables
     * 
     * Inputs:
     * v0:    initial velocity
     * theta: launch angle
     * x0/y0: initial position
     * eLoss: energy loss coefficient
     * 
     * Program variables:
     * time, vTerminal: terminal velocity,
     * vX/vY: initial x and y components of velocity
     */

    double v0, theta, x0, y0, eLoss;
    double time, vTerminal, v0X, v0Y;

    public static void main(String[] args){
        new BallSimulation().start();
    }
    public void run(){
        // Initialize window size
        this.resize(WIDTH, HEIGHT + OFFSET);

        // Create ground plane
        GRect gPlane = new GRect(0, HEIGHT, WIDTH + OFFSET, 3);
        gPlane.setColor(Color.BLACK);
        gPlane.setFilled(true);
        add(gPlane);
        
        // Create left and right walls
        GRect lWallPlane = new GRect(0, 0, 3, HEIGHT);
        lWallPlane.setColor(Color.BLUE);
        lWallPlane.setFilled(true);
        add(lWallPlane);

        GRect rWallPlane = new GRect(WIDTH - 25, 0, 3, HEIGHT);
        rWallPlane.setColor(Color.RED);
        rWallPlane.setFilled(true);
        add(rWallPlane);

        // Create the ball
        GPoint p = W2S(new GPoint(xInit, yInit));
        // GPoint p = W2S(new GPoint(0, 0));
        double ScrX = p.getX();
        double ScrY = p.getY();
        GOval ball = new GOval(ScrX, ScrY, 2 * bSize * Xs, 2 * bSize * Ys);
        ball.setColor(Color.RED);
        ball.setFilled(true);
        add(ball);

        pause(1000);

        // Get input from user
        v0 = readDouble(" Enter initial velocity: ");
        theta = readDouble("Enter launch angle: ");
        eLoss = readDouble("Enter energy loss coefficient: ");

        // Initialize program variables
        x0 = xInit;
        y0 = yInit;
        time = 0;
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
            pause(SLEEP);

            if (noBounce && Y + y0 < bSize)
                running = false;

            // Check for collisions:
            // Compute energy of the ball
            KEx = bMass * vX * vX * 0.5 * (1 - eLoss);
            KEy = bMass * vY * vY * 0.5 * (1 - eLoss);
            PE = bMass * g * Y;

            // Check for simulation end
            if ((KEx + KEy + PE) < eThreshold){
                running = false;
                if (DEBUG)
                    System.out.println("Energy Threshold reached");
            }

            // Collision with floor
            if (vY < 0 && y0 + Y < bSize){
                // From energy, compute new velocity
                v0X = Math.sqrt(2 * KEx / bMass);
                v0Y = Math.sqrt(2 * KEy / bMass);

                if (vX < 0)
                    v0X = -v0X;

                // Reinitialize time and motion parameters
                time = 0;
                x0 += X;
                y0 = bSize;
                X = 0;
                Y = 0;
            }
            // Collision with left wall
            if (vX < 0 && x0 + X <= lWall + bSize){
                // From energy, compute new velocity
                v0X = Math.sqrt(2 * KEx / bMass);
                v0Y = Math.sqrt(2 * KEy / bMass);

                if (vY < 0)
                    v0Y = -v0Y;
                
                // Reinitialize time and motion parameters
                time = 0;
                x0 = bSize;
                y0 += Y;
                X = 0;
                Y = 0;
            }
            // Collision with right wall
            if (vX > 0 && x0 + X >= rWall - bSize){
                // From energy, compute new velocity
                v0X = -Math.sqrt(2 * KEx / bMass);
                v0Y = Math.sqrt(2 * KEy / bMass);

                if (vY < 0)
                    v0Y = -v0Y;
                
                // Reinitialize time and motion parameters
                time = 0;
                x0 = rWall - bSize;
                y0 += Y;
                X = 0;
                Y = 0;
            }

            // Update ball position, plot tick mark at current pos:
            // current pos in screen coordinates
            p = W2S(new GPoint(x0 + X - bSize, y0 + Y + bSize));
            ScrX = p.getX();
            ScrY = p.getY();
            ball.setLocation(ScrX, ScrY);
            trace(ScrX, ScrY);

            time += TICK;
        }
    }
    GPoint W2S(GPoint P) {
        double X = P.getX();
        double Y = P.getY();

        double x = (X - XMin) * Xs;
        double y = yMax - (Y - YMin) * Ys;

        return new GPoint(x, y);
    }
    private void trace(double ScrX, double ScrY){
        GOval dot = new GOval(ScrX + bSize * Xs, ScrY + bSize * Ys, ptDia, ptDia);
        add(dot);
    }
}
