package ppPackage;

import static ppPackage.ppSimParams.*;
import acm.program.GraphicsProgram;
import acm.util.ErrorException;
import acm.io.IODialog;
import java.awt.Color;

/**
 * A program simulating a bouncing ping-pong ball. This program uses the
 * acm.jar package. It is the main class of the {@code ppPackage} package, and
 * imports the other classes of the package, namely {@code ppBall},
 * {@code ppTable}, and {@code ppSimParams.}
 * 
 * The {@code ppSim} class is based on code written by Prof. Frank Ferrie,
 * as part of the Fall 2021 Assignment 2.
 * 
 * @author SpacewaIker
 */
public class ppSim extends GraphicsProgram{
    private ppTable myTable;

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
        
        // Create table:
        myTable = new ppTable(this);

        // Set ball color labels
        if (TWOBALLS){
            myTable.setBallLabelL("red");
            myTable.setBallLabelR("blue");
        }

        // get data from user
        IODialog ioDialog = new IODialog();
        ioDialog.setExceptionOnError(true);
        // Will raise an exception for invalid inputs

        double Vo = ioDialog.readDouble("Enter initial velocity (m/s)");
        myTable.setVelLabel(Vo);

        double theta = ioDialog.readDouble("Enter launch angle (degrees)");
        myTable.setAngleLabel(theta);

        double eLoss;
        /* Loop over try/catch block until eLoss input is valid.
            ioDialog.readDouble() will raise an acm.util.ErrorException if the
            input is invalid. */
        while (true){
            try {
                eLoss = ioDialog.readDouble(
                    "Enter energy loss coefficient [0, 1]", 0, 1); // 0, 1 -> range of inputs
                break;
            } catch (ErrorException e){
                ioDialog.showErrorMessage(
                    "The energy loss coefficient must be between 0 and 1.");
            }
        }
        myTable.setELossLabel(eLoss);

        // Create ball
        ppBall myBall = new ppBall(
            Xinit, Yinit, Vo, theta, eLoss, Color.RED, this);
        myBall.start();

        // Right ball
        if (TWOBALLS){
            ppBall myBall2 = new ppBall(
                XwallR - 2*bSize, Yinit, Vo, 180 - theta, eLoss, Color.BLUE, this);
            myBall2.start();
        }
        // 180 - theta: angle flipped
    }
    /**
     * This method is needed for the {@code myTable.simTimeLabel} to be
     * accessible from the {@code ppBall} class.
     *
     * @param simTime The simulation time to which {@code myTable.simTimeLabel}
     *      must be set.
     */
    public void setSimTimeLabel(double simTime){
        this.myTable.setSimTimeLabel(simTime);
    }
}
