package ppPackage;
import java.lang.invoke.CallSite;

import acm.graphics.GPoint;

/**
 * This class includes most constant parameters needed for this project.
 * 
 * Part of this class is based on code written by Prof. Frank Ferrie,
 * as part of the Fall 2021 Assignment 3.
 * 
 * @author SpacewaIker
 */
public class ppSimParams {
    // Booleans for testing and debugging and other modes
    public static final boolean SLOW = false;
    public static final boolean SUPERSLOW = false;
    public static final boolean MESG = true;
    public static final int STARTDELAY = 1000;

    // Screen size parameters (in pixels)
    public static final int XWIDTH = 1280;
    public static final int YHEIGHT = 600;
    public static final int OFFSET = 200;
    /* The WIDTH and HEIGHT final parameters already exist inside ImageObserver.
    When ppSim used the WIDTH and HEIGHT parameters with resize() it used the
    ImageObserver.WIDTH and ImageOberver.HEIGHT parameters instead. Hence,
    the above WIDTH and HEIGHT parameter names had to be changed. */

    // Ping-pong table parameters (in meters)
    public static final double ppTableXlen = 2.74;
    public static final double ppTableHgt = 1.52;
    public static final double XwallL = 0.05;
    public static final double XwallR = 2.69;

    // Other parameters (in simulation coordinates)
    public static final double g = 9.8;
    public static final double k = 0.1316;

    public static final double bSize = 0.02;
    public static final double bMass = 0.0027;

    public static final double TICK = 0.01;
    public static final double ETHR = 0.001;

    public static final double Xmin = 0;
    public static final double Xmax = ppTableXlen;
    public static final double Ymin = 0;
    public static final double Ymax = ppTableHgt;
    
    public static final int xmin = 0;
    public static final int xmax = XWIDTH;
    public static final int ymin = 0;
    public static final int ymax = YHEIGHT;

    public static final double Xs = (xmax - xmin) / (Xmax - Xmin);
    public static final double Ys = (ymax - ymin) / (Ymax - Ymin);

    public static final double Xinit = XwallL;
    public static final double Yinit = Ymax/2;

    public static final int PD = 1;
    // Time scale: if slow, the simulation runs 100 times slower
    // public static final double TSCALE = (SLOW) ? 1000*100 : 1000;
    public static final double TSCALE = (SUPERSLOW) ? 100000 : (SLOW) ? 10000 : 1000;


    // Paddle parameters
    static final double ppPaddleH = 8 * 2.54/100;
    static final double ppPaddleW = 0.5 * 2.54/100;
    static final double ppPaddleXinit = XwallR - ppPaddleW/2;
    static final double ppPaddleYinit = Yinit;
    static final double ppPaddleXgain = 2;
    static final double ppPaddleYgain = 1.5;

    // Parameters used by ppSim
    static final double YinitMAX = 0.75 * Ymax;
    static final double YinitMIN = 0.25 * Ymax;
    static final double eLossMAX = 0.2;
    static final double eLossMIN = 0.2;
    static final double VoMAX = 5;
    static final double VoMIN = 5;
    static final double thetaMAX = 45;
    static final double thetaMIN = 0;
    // Random number generator seed
    static final long RSEED = 8976232;

    /**
     * Converts world coordinates into simulation (pixel) coordinates
     * 
     * @param P the world coordinates
     * @return simulation coordinates
     */
    static GPoint W2S(GPoint P) {
        double X = P.getX();
        double Y = P.getY();

        double x = (X - Xmin) * Xs;
        double y = ymax - (Y - Ymin) * Ys;

        return new GPoint(x, y);
    }
    /**
     * Converts simulation (pixel) coordinates world coordinates
     * 
     * @param P the simulation coordinates
     * @return world coordinates
     */
    static GPoint S2W(GPoint P) {
        double x = P.getX();
        double y = P.getY();

        double X = x/Xs + Xmin;
        double Y = (ymax - y)/Ys + Ymin;

        return new GPoint(X, Y);
    }
}
