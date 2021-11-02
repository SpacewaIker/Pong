package ppPackage;
import static ppPackage.ppSimParams.*;
import java.awt.Color;
import acm.graphics.GOval;
import acm.graphics.GPoint;

/**
 * A class representing a bouncing ball. This class extends {@code Thread},
 * meaning that multiple {@code ppBall}s can run at the same time.
 * 
 * The {@code ppBall} class is based on code written by Prof. Frank Ferrie,
 * as part of the Fall 2021 Assignment 3.
 * 
 * @author SpacewaIker
 */
public class ppBall extends Thread{
    // Parameters recieved through the constructor:
    private ppSim gProgram;
    private ppTable myTable;
    private ppPaddle rightPaddle;
    private double Xinit;
    private double Yinit;
    private double Vo;
    private double theta;
    private double eLoss;
    private Color color;

    private GOval ball;

    // Simulation parameters
    private double x0, y0;
    private double time, vTerminal, v0X, v0Y;

    private boolean running;

    /**
     * All the following constructor parameters are set as instance variables.
     * 
     * @param Xinit The x component of the ball's initial position (in m)
     * @param Yinit The y component of the ball's initial position (in m)
     * @param Vo The initial velocity of the ball (in m/s)
     * @param theta The launch angle of the ball (in degrees, from x+ axis)
     * @param eLoss The energy loss coefficient (in interval (0, 1))
     * @param color The color of the ball
     * @param GProgram The surface/canvas/program that must be paused
     */
    public ppBall(double Xinit, double Yinit, double Vo, double theta,
                  double eLoss, Color color, ppTable myTable, ppSim GProgram){

        this.gProgram = GProgram;
        this.myTable = myTable;
        this.Xinit = Xinit;
        this.Yinit = Yinit;
        this.Vo = Vo;
        this.theta = theta;
        this.eLoss = eLoss;
        this.color = color;

        // Create the ball
        GPoint p = W2S(new GPoint(Xinit, Yinit));
        double ScrX = p.getX();
        double ScrY = p.getY();
        this.ball = new GOval(ScrX, ScrY, 2*bSize*Xs, 2*bSize*Xs);
        // Since Xs != Ys, using Xs for both width and height makes the ball round
        this.ball.setColor(color);
        this.ball.setFilled(true);
        this.gProgram.add(this.ball);
    }
    /**
     * Main class/program loop. This is the simulation loop, where the position
     * is computed at each time, the ball position is visually updated, and the
     * {@code ppSim} program is paused. Since {@code ppBall} extends
     * {@code Thread}, multiple balls can run at the same time.
     */
    public void run(){
        // Initialize program variables
        x0 = Xinit;
        y0 = Yinit;
        time = 0;
        vTerminal = bMass * g / (4 * Math.PI * bSize * bSize * k);
        v0X = Vo * Math.cos(theta * Math.PI / 180);
        v0Y = Vo * Math.sin(theta * Math.PI / 180);

        // Simulation loop (update values until hits ground)
        running = true;

        double X, Y, vX, vY;
        double PE, KEx, KEy;

        while (running){
            // Update values
            X = v0X * vTerminal / g * (1 - Math.exp(-g * time / vTerminal));
            Y = vTerminal / g * (v0Y + vTerminal) *
                (1 - Math.exp(-g * time / vTerminal)) - vTerminal * time;
            vX = v0X * Math.exp(-g * time / vTerminal);
            vY = (v0Y + vTerminal) * Math.exp(-g * time / vTerminal) - vTerminal;

            // Simulation sleep
            this.gProgram.pause(TICK * TSCALE);

            // Check for collisions & simulation end:
            // Compute energy of the ball
            KEx = bMass * vX * vX * 0.5 * (1 - eLoss);
            KEy = bMass * vY * vY * 0.5 * (1 - eLoss);
            PE = bMass * g * (Y + y0 - 2*bSize);
            /* PE must be evaluated from the ground, which is 2*bSize, not 0. */

            // Check for simulation end via threshold
            if ((KEx + KEy + PE) < ETHR){
                running = false;
                if (MESG)
                    System.out.println("Energy Threshold reached");
                this.gProgram.reset();
            }
            // Check for simulation end by going out of the screen
            if (x0 + X > ppTableXlen){
                running = false;
                if (MESG)
                    System.out.println("Ball went out of frame");
                this.gProgram.reset();
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
            if (vX < 0 && x0 + X <= XwallL){
                // From energy, compute new velocity
                v0X = Math.sqrt(2 * KEx / bMass);
                v0Y = Math.sqrt(2 * KEy / bMass);

                // v0Y is of the same sign as before, v0X is always positive
                if (vY < 0)
                    v0Y = -v0Y;
                
                // Reinitialize time and motion parameters
                time = 0;
                x0 = XwallL;
                y0 += Y;
                X = 0;
                Y = 0;
            }
            // Collision with right paddle
            if (vX > 0 && this.rightPaddle.contact(x0 + X + bSize, y0 + Y)){
                // From energy, compute new velocity
                v0X = -Math.sqrt(2 * KEx / bMass);
                v0Y = Math.sqrt(2 * KEy / bMass);

                // v0Y is of the same sign as before, v0X is always positive
                if (vY < 0)
                    v0Y = -v0Y;
                
                // Reinitialize time and motion parameters
                time = 0;
                x0 = XwallR - 2*bSize;
                y0 += Y;
                X = 0;
                Y = 0;

                // Add kinetic energy from the paddle
                v0X *= ppPaddleXgain;
                // v0Y *= ppPaddleYgain;
                v0Y *= ppPaddleYgain * this.rightPaddle.getVelocity().getY();

                // Change y velocity sign if paddle is moving and add extra velocity
                int sign = this.rightPaddle.getSignVy();
                if (sign == 1 || sign == -1)
                    v0Y = Math.abs(v0Y) * sign;
                    /* Cannot just multiply by sign because v0Y might already
                        be negative (if it was negative before the bounce)*/
                
                // Increment number of returns
                this.myTable.incrementNumReturns();
            }

            // Update ball position, plot tick mark at current pos:
            // current pos in screen coordinates
            GPoint p = W2S(new GPoint(x0 + X, y0 + Y));
            double ScrX = p.getX();
            double ScrY = p.getY();
            this.ball.setLocation(ScrX, ScrY);
            trace(ScrX, ScrY);

            time += TICK;
        }
    }
    /**
     * Adds a small black dot at the specified location.
     * The dot has diameter {@code PD}
     * 
     * @param ScrX the x coordinate (in pixels)
     * @param ScrY the y coordinate (in pixels)
     */
    private void trace(double ScrX, double ScrY){
        GOval dot = new GOval(ScrX + bSize * Xs, ScrY + bSize * Ys, PD, PD);
        dot.setColor(this.color);
        this.gProgram.add(dot);
    }
    /**
     * Setter for the right {@code ppPaddle}.
     * 
     * @param paddle The right paddle
     */
    public void setRightPaddle(ppPaddle paddle){
        this.rightPaddle = paddle;
    }
}
