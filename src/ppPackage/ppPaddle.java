package ppPackage;

import static ppPackage.ppSimParams.*;
import acm.graphics.GRect;
import acm.graphics.GPoint;

/**
 * A class representing a ping pong paddle. This class extends {@code Thread},
 * meaning that multiple {@code ppPaddle}s and {@code ppBall}s can run at the
 * same time.
 * 
 * The {@code ppPaddle} class is based on code written by Prof. Frank Ferrie,
 * as part of the Fall 2021 Assignment 3.
 * 
 * @author SpacewaIker
 */
public class ppPaddle extends Thread{
    private GRect paddle;
    private ppTable myTable;

    private double Vx, Vy, X, Y, lastX, lastY;

    /**
     * Constructor for the {@code ppPaddle} class.
     * 
     * @param X The X component of the position (center) of the paddle
     * @param Y The Y component of the position (center) of the paddle
     * @param myTable The {@code ppTable} on which the paddle should be
     */
    ppPaddle(double X, double Y, ppTable myTable){
        this.myTable = myTable;
        this.X = X;
        this.Y = Y;

        GPoint p = W2S(new GPoint(this.X - ppPaddleW/2, this.Y + ppPaddleH/2));
        double ScrX = p.getX();
        double ScrY = p.getY();
        this.paddle = new GRect(ScrX, ScrY, ppPaddleW*Xs, ppPaddleH*Ys);
        this.paddle.setFilled(true);
        this.myTable.getDisplay().add(this.paddle);
    }
    /**
     * Main class/program loop. This is the simulation loop, where the position
     * and x and y velocities are updated.
     */
    public void run(){
        lastX = X;
        lastY = Y;

        while (true){
            this.Vx = (X - lastX)/TICK;
            this.Vy = (Y - lastY)/TICK;
            lastX = X;
            lastY = Y;
            this.myTable.getDisplay().pause(TICK * TSCALE);
        }
    }
    /**
     * Getter method for the paddle's velocity
     * 
     * @return A {@code GPoint} containing the X and Y velocities of the paddle
     *      in world coordinates
     */
    public GPoint getVelocity(){
        return new GPoint(this.Vx, this.Vy);
    }
    /**
     * Setter for the paddle's location. The coordinates must be the center of
     * the paddle, in world coordinates.
     * 
     * @param P A {@code GPoint} containing the X and Y positions of the
     *      center of the paddle, in world coordinates
     */
    public void setLocation(GPoint P){
        // Set paddle variables to new location
        double X = P.getX();
        double Y = P.getY();
        this.X = X;
        this.Y = Y;

        // Ajust for upper left corner
        X -= ppPaddleW/2;
        Y += ppPaddleH/2;

        // Convert to simulation coordinates
        GPoint screenP = W2S(new GPoint(X, Y));

        double ScrX = screenP.getX();
        double ScrY = screenP.getY();
        this.paddle.setLocation(ScrX, ScrY);
    }
    /**
     * Getter for the paddle's location. The coordinates are the center of
     * the paddle, in world coordinates.
     * 
     * @return A {@code GPoint} containing the X and Y positions of the
     *      center of the paddle, in world coordinates
     */
    public GPoint getLocation(){
        // Simulation coordinates:
        GPoint p = this.paddle.getLocation();

        // World coordinates:
        GPoint P = S2W(p);
        // Adjust for center
        double X = P.getX() + ppPaddleW/2;
        double Y = P.getY() - ppPaddleH/2;
        return new GPoint(X, Y);
    }
    /**
     * Getter for the sign of the Y velocity. Returns 1 if the velocity is
     * positive (up), -1 if it is negative (down), or 0 if the paddle is unmoving.
     * 
     * @return The sign of the Y velocity
     */
    public int getSignVy(){
        double vy = this.Vy;
        int sign = (int) (vy/Math.abs(vy));
        return sign;
    }
    /**
     * Whether the coordinates are in contact with the paddle.
     * 
     * @param X The X coordinate in world coordinates
     * @param Y The Y coordinate in world coordinates
     * 
     * @return {@code true} if the coordinates are in contact with the paddle,
     *      {@code false} otherwise
     */
    public boolean contact(double X, double Y){
        // Conditions in Y
        boolean notOver = Y < this.Y + ppPaddleH/2;
        boolean notUnder = Y > this.Y - ppPaddleH/2;

        // Conditions in X
        boolean notBefore = X >= this.X - ppPaddleW/2;
        boolean notBeyond = X < ppTableXlen;
        // boolean notBeyond = X < this.X + ppPaddleW/2;

        return notOver && notUnder && notBefore && notBeyond;
    }
}
