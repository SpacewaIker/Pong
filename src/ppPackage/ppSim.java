package ppPackage;

import static ppPackage.ppSimParams.*;
import acm.program.GraphicsProgram;
import acm.program.DialogProgram;
import java.awt.Color;

public class ppSim extends GraphicsProgram{
    private ppTable myTable;

    public static void main(String[] args){
        new ppSim().start(args);
    }
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

        // get data from user
        double Vo = new myDialogProgram().readDouble("Enter initial velocity (m/s)");
        myTable.setVelLabel(Vo);

        double theta = new myDialogProgram().readDouble("Enter launch angle (degrees)");
        myTable.setAngleLabel(theta);

        double eLoss = new myDialogProgram().readDouble("Enter energy loss coefficient [0, 1]");
        myTable.setELossLabel(eLoss);

        // Create ball
        ppBall myBall = new ppBall(
            Xinit, Yinit, Vo, theta, eLoss, Color.RED, this);
        myBall.start();
    }
    /**
     * This class is needed for the {@code myTable.simTimeLabel} to be
     * accessible from the {@code ppBall} class.
     *
     * @param simTime The simulation time to which {@code myTable.simTimeLabel}
     *      must be set.
     */
    public void setSimTimeLabel(double simTime){
        this.myTable.setSimTimeLabel(simTime);
    }
}
/**
 * An extension of {@code DialogProgram} that can be instantiated.
 */
class myDialogProgram extends DialogProgram{
}