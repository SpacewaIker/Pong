package ppPackage;

import static ppPackage.ppSimParams.*;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;
import java.awt.Color;
import java.awt.event.MouseEvent;
import acm.graphics.GPoint;

/**
 * A program simulating a bouncing ping-pong ball. This program uses the
 * acm.jar package. It is the main class of the {@code ppPackage} package, and
 * imports the other classes of the package, namely {@code ppBall},
 * {@code ppTable}, {@code ppPaddle}, and {@code ppSimParams.}
 * 
 * The {@code ppSim} class is based on code written by Prof. Frank Ferrie,
 * as part of the Fall 2021 Assignment 3.
 * 
 * @author SpacewaIker
 */
public class ppSim extends GraphicsProgram{
    private ppTable myTable;
    private ppBall myBall;
    private ppPaddle myPaddle;

    /**
     * Entry point of the program.
     */
    public static void main(String[] args){
        new ppSim().start(args);
    }
    /**
     * Main program process. The {@code ppTable} is instantiated, the
     * {@code IODialog} used to get user input is also instantiated, the user
     * is asked for input, the {@code ppBall} is instantiated.
     */
    public void run(){
        this.resize(XWIDTH, YHEIGHT + OFFSET);
        this.resize(2*XWIDTH - getWidth(), 2*(YHEIGHT + OFFSET) - getHeight());
        /* EXPLANATION: I have found that resize() would not resize exactly
            to the right width and height, probably due to the window
            header and other such things (I have talked to TA Katrina Poulin
            about this issue). This would result in a constant (but different
            between PCs) difference between the target size and the actual size.
            The above code first resizes the window, then resizes a second
            time, but adding the (target-actual) difference to resize
            to the actual target size. Note that if this difference was = 0,
            (2*WIDTH - getWidth()) would be equal to WIDTH. */
        
        // Clear canvas (in case of reset)
        removeAll();

        // Mouse listener and random generator
        addMouseListeners();
        RandomGenerator rgen = RandomGenerator.getInstance();
        if (TRUERANDOM)
            rgen.setSeed(System.currentTimeMillis());
        else
            rgen.setSeed(RSEED);

        // Create table:
        this.myTable = new ppTable(this);

        // Generate random values for Yinit, Vo, theta, eLoss
        Color iColor = Color.RED;
        double iYinit = rgen.nextDouble(YinitMIN, YinitMAX);
        double ieLoss = rgen.nextDouble(eLossMIN, eLossMAX);
        double iVel =   rgen.nextDouble(VoMIN, VoMAX);
        double iTheta = rgen.nextDouble(thetaMIN, thetaMAX);

        // Create ball and paddle
        this.myBall = new ppBall(
            Xinit, iYinit, iVel, iTheta, ieLoss, iColor, this.myTable, this);
        this.myPaddle = new ppPaddle(ppPaddleXinit, ppPaddleYinit, this.myTable);
        this.myBall.setRightPaddle(this.myPaddle);

        // Start ball and paddle
        // pause(STARTDELAY);
        // this.myBall.start();
        // this.myPaddle.start();
        this.myPaddle.start();
        waitForClick();
        this.myBall.start();
    }
    /**
     * Mouse Handler - a moved event moves the paddle up and down in Y
     */
    public void mouseMoved(MouseEvent e){
        GPoint Pm = S2W(new GPoint(e.getX(), e.getY()));
        double PaddleX = myPaddle.getLocation().getX();
        double PaddleY = Pm.getY();
        myPaddle.setLocation(new GPoint(PaddleX, PaddleY));
    }
    /**
     * Resets the ppSim. New {@code ppBall}, {@code ppTable}, and {@code ppPaddle}
     * objects are created
     */
    public void reset(){
        waitForClick();
        this.run();
    }
}
