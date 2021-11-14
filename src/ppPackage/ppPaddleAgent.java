package ppPackage;

import static ppPackage.ppSimParams.*;

import java.awt.Color;
import acm.graphics.GPoint;

/**
 * A class representing an automated ping pong paddle. This class extends
 * {@code ppPaddle}. The movement of the paddle follows the {@code ppBall} that
 * is associated with it.
 * 
 * The {@code ppPaddleAgent} class is based on code written by Prof. Frank
 * Ferrie, as part of the Fall 2021 Assignment 4.
 * 
 * @author SpacewaIker
 */
public class ppPaddleAgent extends ppPaddle{
    private ppBall myBall;
    private ppSim gProgram;

    /**
     * Constructor for the {@code ppPaddleAgent} class.
     * 
     * @param X The X component of the position (center) of the paddle
     * @param Y The Y component of the position (center) of the paddle
     * @param color The paddle's color
     * @param myTable The {@code ppTable} on which the paddle should be
     * @param GProgram the {@code ppSim} running the program
     */
    ppPaddleAgent(double X, double Y, Color color, ppTable myTable, ppSim GProgram){
        super(X, Y, color, myTable, GProgram);
        this.gProgram = GProgram;
    }
    /**
     * Main class loop. This is the simulation loop, where the position
     * and x and y velocities are updated.
     */
    public void run(){
        while (true){
            this.gProgram.pause(TICK * this.gProgram.getTimeValue());

            // If pause is selected, don't update position
            if (this.gProgram.getPauseState())
                continue;

            /* Instead of running only every X iterations, this code
                updates the paddle's location every iteration, but the paddle
                only moves 1/X of the difference between itself and the ball.
                This gives the paddle a smooth motion even with high lag. */
            double ballY = this.myBall.getPosition().getY();
            double Y = this.getPosition().getY();
            double X = this.getPosition().getX();

            double movement = (ballY - Y) / this.gProgram.getLagValue();

            this.setPosition(new GPoint(X, Y + movement));
        }
    }
    /**
     * Setter method for the {@code ppBall} which the paddle must follow.
     * 
     * @param ball The {@code ppBall} instance to be followed
     */
    public void setBall(ppBall ball){
        this.myBall = ball;
    }
}