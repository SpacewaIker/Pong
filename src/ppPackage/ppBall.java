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
 * as part of the Fall 2021 Assignment 4.
 * 
 * @author SpacewaIker
 */
public class ppBall extends Thread{
    // Parameters recieved through the constructor:
    private ppSim gProgram;
    private ppTable myTable;
    private ppPaddle rightPaddle;
    private ppPaddle leftPaddle;
    private double Xinit;
    private double Yinit;
    private double Vo;
    private double theta;
    private double eLoss;
    private Color color;

    private GOval ball;

    // Simulation parameters
    private double x0, y0, X, Y, vX, vY;
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
     * @param myTable The {@code ppTable} on which the ball will go
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

        this.y0 = Yinit;
        this.x0 = Xinit;

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

        double PE, KEx, KEy;
        String server = "left";

        while (running){
            // Simulation sleep
            this.gProgram.pause(TICK * this.gProgram.getTimeValue());

            // If pause is selected, don't update position
            if (this.gProgram.getPauseState()){
                // pause the program for an additional 500ms
                this.gProgram.pause(500);
                continue;
            }

            // Update values
            X = v0X * vTerminal / g * (1 - Math.exp(-g * time / vTerminal));
            Y = vTerminal / g * (v0Y + vTerminal) *
                (1 - Math.exp(-g * time / vTerminal)) - vTerminal * time;
            vX = v0X * Math.exp(-g * time / vTerminal);
            vY = (v0Y + vTerminal) * Math.exp(-g * time / vTerminal) - vTerminal;

            // Check if vX goes beyond VoxMAX
            if (vX > VoxMAX)
                vX = VoxMAX;

            // Check if y0 + Y goes out of bounds
            if (y0 + Y > Ymax){
                running = false;
                if (this.gProgram.getMESG())
                    System.out.println("Ball went out of bounds");

                if (server == "right")
                    this.myTable.incrementLeftPoints();
                if (server == "left")
                    this.myTable.incrementRightPoints();
            }
            // Check if the ball goes out (right)
            if (x0 + X > ppTableXlen){
                running = false;
                if (this.gProgram.getMESG())
                    System.out.println("Ball went out to the right");
                
                this.myTable.incrementLeftPoints();
            }
            // Check if the ball goes out (left)
            if (x0 + X < 0){
                running = false;
                if (this.gProgram.getMESG())
                    System.out.println("Ball went out to the left");

                this.myTable.incrementRightPoints();
            }

            // Check for collisions & simulation end:
            // Compute energy of the ball
            KEx = bMass * vX * vX * 0.5 * (1 - eLoss);
            KEy = bMass * vY * vY * 0.5 * (1 - eLoss);
            PE = bMass * g * (Y + y0 - 2*bSize);
            /* PE must be evaluated from the ground, which is 2*bSize, not 0. */

            // Check for simulation end via threshold
            if ((KEx + KEy + PE) < ETHR){
                running = false;
                if (this.gProgram.getMESG())
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
            if (vX < 0 && x0 + X - bSize < agentPaddleXinit &&
                this.leftPaddle.contact(y0 + Y)){
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

                // Add kinetic energy from the paddle
                v0X *= ppPaddleXgain;
                // v0Y *= ppPaddleYgain;
                v0Y *= ppPaddleYgain * this.leftPaddle.getVelocity().getY();

                // Change y velocity sign if paddle is moving
                int sign = this.leftPaddle.getSignVy();
                if (sign == 1 || sign == -1)
                    v0Y = Math.abs(v0Y) * sign;
                    /* Cannot just multiply by sign because v0Y might already
                        be negative (if it was negative before the bounce)*/
                
                // Set server
                server = "left";
                
            }
            // Collision with right paddle
            if (vX > 0 && x0 + X + bSize > ppPaddleXinit &&
                this.rightPaddle.contact(y0 + Y)){
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
                double multiplier = Math.pow(
                    Math.abs(this.rightPaddle.getVelocity().getY()), 1./4);
                if (multiplier < 1)
                    multiplier = 1;
                v0Y *= ppPaddleYgain * multiplier;

                // Change y velocity sign if paddle is moving and add extra velocity
                int sign = this.rightPaddle.getSignVy();
                if (sign == 1 || sign == -1)
                    v0Y = Math.abs(v0Y) * sign;
                    /* Cannot just multiply by sign because v0Y might already
                        be negative (if it was negative before the bounce)*/
                
                // Set server
                server = "right";
            }

            // Update ball position, plot tick mark at current pos:
            // current pos in screen coordinates
            GPoint p = W2S(new GPoint(x0 + X, y0 + Y));
            double ScrX = p.getX();
            double ScrY = p.getY();
            this.ball.setLocation(ScrX, ScrY);

            if (this.gProgram.getTraceState())
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
    /**
     * Setter for the left {@code ppPaddleAgent}.
     * 
     * @param paddle The left paddle
     */
    public void setLeftPaddle(ppPaddle paddle){
        this.leftPaddle = paddle;
    }
    /**
     * Getter for the ball's position
     * 
     * @return A {@code GPoint} with the ball's position
     */
    public GPoint getPosition(){
        return new GPoint(this.x0 + this.X, this.y0 + this.Y);
    }
    /**
     * Getter for the ball's velocity
     * 
     * @return A {@code GPoint} with the ball's velocity
     */
    public GPoint getVelocity(){
        return new GPoint(this.vX, this.vY);
    }
    /**
     * Setter method for the ball's color.
     * 
     * @param color The {@code Color} to set
     */
    public void setColor(Color color){
        this.ball.setColor(color);
    }
}
