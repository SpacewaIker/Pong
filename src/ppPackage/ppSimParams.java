package ppPackage;

import acm.graphics.GPoint;
import java.awt.Font;

/**
 * This class includes most constant parameters needed for this project.
 * 
 * Part of this class is based on code written by Prof. Frank Ferrie,
 * as part of the Fall 2021 Assignment 3.
 * 
 * @author SpacewaIker
 */
public class ppSimParams {
    public static final Font FONT_XS = new Font("SansSerif", 0, 15);
    public static final Font FONT_S = new Font("SansSerif", 0, 20);
    public static final Font FONT_L = new Font("SansSerif", 0, 30);
    public static final Font FONT_XL = new Font("SansSerif", 0, 80);

    // Screen size parameters (in pixels)
    public static final int XWIDTH = 1300;
    public static final int YHEIGHT = 650;
    public static final int XOFFSET = 25;
    public static final int YOFFSET = 200;
    
    // Color chooser screen parameters
    public static final int COLORWIDTH = 450;
    public static final int COLORHEIGHT = 400;
    public static final int HGAP = 30;
    public static final int VGAP = 20;
    public static final String[] COLORCHOICES = {"Red", "Blue", "Green"};

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

    public static final double VoxMAX = 10;

    public static final int PD = 1;

    // Paddle parameters
    static final double ppPaddleH = 8 * 2.54/100;
    static final double ppPaddleW = 0.5 * 2.54/100;
    static final double ppPaddleXinit = XwallR - ppPaddleW/2;
    static final double ppPaddleYinit = Yinit;
    static final double ppPaddleXgain = 1.4;
    static final double ppPaddleYgain = 1.5;

    static final double agentPaddleXinit = XwallL - ppPaddleW/2;
    static final double agentPaddleYinit = Yinit;
    static final double agentPaddleXgain = 1.4;
    static final double agentPaddleYgain = 1.5;

    static final int lagSliderMin = 1;
    static final int lagSliderInit = 15;
    static final int lagSliderMax = 60;

    static final int timeSliderMin = 100;
    static final int timeSliderInit = 1500;
    static final int timeSliderMax = 5000;

    // Parameters used by ppSim
    static final double YinitMAX = 0.8 * Ymax;
    static final double YinitMIN = 0.2 * Ymax;
    static final double eLossMAX = 0.2;
    static final double eLossMIN = 0.2;
    static final double VoMAX = 5;
    static final double VoMIN = 5;
    static final double thetaMAX = 20;
    static final double thetaMIN = 0;
    // Random number generator seed
    static final long RSEED = 8976232;

    // Would use text block for the follwing with Java 15+
    static final String HELPSTRING = (
        "<html><b>Help Dialog</b><br>" +
        "With each new serve, click on the simulation window to start the simulation.<br><br>" +
        "Below is a description of each button and associated key.<br>The " +
        "brackets indicate the hotkey (if empty, no hotkey is associated).<br><br>" +
        "<br><u>Top buttons:</u><br><br>" +
        "[ ] Left color: Choose the left paddle color<br>" +
        "[ ] Ball color: Choose the ball color<br>" +
        "[ ] Right color: Choose the right paddle color<br>" +
        "[ ] Agent: Rename the left player.<br>" +
        "[ ] Human: Rename the right player.<br>" +
        "<br><u>Bottom buttons:</u><br><br>" +
        "[C] Clear: Clear the scoreboard.<br>" + 
        "[N] New Serve: Request a new serve from the left player.<br>" +
        "[T] Trace: Toggle button. Adds a trace to the ball.<br>" + 
        "[M] mesg: Toggle button. Prints debug messages in the console output.<br>" + 
        "[Q] Quit: Exits the program.<br>" +
        "[R/L arrows] t slider: Slider controlling the simulation speed.<br>" +
        "[ ] rtime: Resets the time slider.<br>" +
        "[U/D arrows] lag slider: Slider controlling the left player's reaction speed.<br>" +
        "[ ] rlag: Resets the lag slider.<br>" +
        "[space] Pause: Pauses the simulation.<br>" +
        "[H] Help: Show this dialog.");

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
