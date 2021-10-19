package ppPackage;
import acm.graphics.GPoint;

/**
 * This class includes most constant parameters needed for this project.
 * 
 * Part of this class is based on code written by Prof. Frank Ferrie,
 * as part of the Fall 2021 Assignment 2.
 * 
 * @author SpacewaIker
 */
public class ppSimParams {
    // Booleans for testing and debugging
    public static final boolean SLOW = false;
    public static final boolean DEBUG = false;
    public static final boolean TEST = false;
    public static final boolean NOBOUNCE = false;
    public static final boolean TWOBALLS = true;

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
}
