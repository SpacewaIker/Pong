package ppPackage;

import static ppPackage.ppSimParams.*;
import acm.graphics.GLabel;
import acm.graphics.GRect;
import java.awt.Color;

/**
 * A class representing the ping pong table on which the {@code ppBall} will
 * bounce. This class creates the walls/floor and the {@code GLabel}s.
 * 
 * The {@code ppTable} class is based on code written by Prof. Frank Ferrie,
 * as part of the Fall 2021 Assignment 2.
 * 
 * @author SpacewaIker
 */
public class ppTable {
    private ppSim gProgram;
    private GLabel simTimeLabel;

    private GLabel ballLabelL;
    private GLabel velocityLabelL;
    private GLabel angleLabelL;
    private GLabel eLossLabelL;

    private GLabel ballLabelR;
    private GLabel velocityLabelR;
    private GLabel angleLabelR;
    private GLabel eLossLabelR;

    /**
     * The constructor parameter is set as an instance variable. The parameter
     * and "mode" {@code GLabel}s are instantiated. The walls and floor are
     * created.
     * 
     * @param GProgram The surface/canvas/program on which the walls, floor,
     *      and {@code GLabel}s will be added.
     */
    public ppTable(ppSim GProgram){
        this.gProgram = GProgram;

        // Parameter labels:
        this.simTimeLabel = createGLabel("Simulation Time: 0.0 s", 30, YHEIGHT + 30);

        // Left ball:
        this.velocityLabelL = createGLabel("Initial velocity:", 30, YHEIGHT + 100);
        this.angleLabelL = createGLabel("Launch angle:", 30, YHEIGHT + 130);
        this.eLossLabelL = createGLabel("Energy loss coefficient:", 30, YHEIGHT + 160);

        // Right ball:
        if (TWOBALLS){
            this.ballLabelL = createGLabel("", 30, YHEIGHT + 70);
            this.ballLabelR = createGLabel("", 400, YHEIGHT + 70);

            this.velocityLabelR = createGLabel("Initial velocity:", 400, YHEIGHT + 100);
            this.angleLabelR = createGLabel("Launch angle:", 400, YHEIGHT + 130);
            this.eLossLabelR = createGLabel("Energy loss coefficient:", 400, YHEIGHT + 160);
        }


        // Debug/test labels:
        if (TEST){
            final GLabel testModeLabel = createGLabel(
                "TEST MODE", 800, YHEIGHT + 60, new Color(0, 180, 0));// dark green
        }
        if (DEBUG){
            final GLabel debugModeLabel = createGLabel(
                "DEBUG MODE", 1000, YHEIGHT + 60, Color.RED);
        }
        if (SLOW){
            final GLabel slowModeLabel = createGLabel(
                "SLOW MODE", 800, YHEIGHT + 140, Color.BLUE);
        }
        if (NOBOUNCE){
            final GLabel noBounceModeLabel = createGLabel(
                "NO BOUNCE MODE", 1000, YHEIGHT + 140, Color.ORANGE);
        }

        // Create walls:
        // Create ground plane
        final GRect gPlane = new GRect(0, YHEIGHT, XWIDTH + OFFSET, 3);
        gPlane.setColor(Color.BLACK);
        gPlane.setFilled(true);
        this.gProgram.add(gPlane);
        
        // Create left and right walls
        final GRect lWallPlane = new GRect(XwallL * Xs, 0, 1, YHEIGHT);
        lWallPlane.setColor(Color.BLUE);
        lWallPlane.setFilled(true);
        this.gProgram.add(lWallPlane);

        final GRect rWallPlane = new GRect(XwallR * Xs - 1, 0, 1, YHEIGHT);
        rWallPlane.setColor(Color.RED);
        rWallPlane.setFilled(true);
        this.gProgram.add(rWallPlane);
    }
    /**
     * Creates a GLabel with a font size of 24 pt. The GLabel is also added to
     * the {@code gProgram}.
     * 
     * @param label The GLabel's label
     * @param x The x position of the GLabel
     * @param y The y position of the GLabel
     * @param color The GLabel's color
     * 
     * @return The created GLabel
     */
    private GLabel createGLabel(String label, int x, int y, Color color){
        GLabel myGLabel = new GLabel(label, x, y);
        myGLabel.setFont("-24");
        myGLabel.setColor(color);

        this.gProgram.add(myGLabel);
        return myGLabel;
    }
    /**
     * Creates a GLabel with a font size of 24 pt and a black font color.
     * The GLabel is also added to the {@code gProgram}.
     * 
     * @param label The GLabel's label
     * @param x The x position of the GLabel
     * @param y The y position of the GLabel
     * 
     * @return The created GLabel
     */
    private GLabel createGLabel(String label, int x, int y){
        return createGLabel(label, x, y, Color.BLACK);
    }
    /**
     * Setter for the {@code simTimeLabel}
     * 
     * @param simTime The new simulation time to set
     */
    public void setSimTimeLabel(double simTime){
        this.simTimeLabel.setLabel(
            String.format("Simulation time: %.2f s", simTime));
    }
    /**
     * Setter for {@code velocityLabelL} and {@code velocityLabelR}. The two
     * initial velocities are always equal.
     * 
     * @param velocity The initial velocity to set the labels to
     */
    public void setVelLabel(double velocity){
        this.velocityLabelL.setLabel(
            String.format("Initial velocity: %.1f m/s", velocity));
        if (TWOBALLS){
            this.velocityLabelR.setLabel(
                String.format("Initial velocity: %.1f m/s", velocity));
        }
    }
    /**
     * Setter for {@code angleLabelL} and {@code angleLabelR}. The right angle
     * is equal to 180 - left angle.
     * 
     * @param angle The launch angle to set the left angle to (180 - {@code angle} will be the right angle)
     */
    public void setAngleLabel(double angle){
        this.angleLabelL.setLabel(
            String.format("Launch angle: %.1f\260", angle)); // 0o260 = 176 -> °
        if (TWOBALLS){
            this.angleLabelR.setLabel(
                String.format("Launch angle: %.1f\260", 180 - angle));
        }
    }
    /**
     * Setter for {@code eLossLabelL} and {@code eLossLabelR}. The two energy
     * loss coefficients are always equal.
     * 
     * @param eLoss The energy loss coefficient to set the energy loss coefficient labels to
     */
    public void setELossLabel(double eLoss){
        this.eLossLabelL.setLabel(
            String.format("Energy loss coefficient: %.1f", eLoss));
        if (TWOBALLS){
            this.eLossLabelR.setLabel(
                String.format("Energy loss coefficient: %.1f", eLoss));
        }
    }
    /**
     * Setter for the {@code ballLabelL}. The color name's first letter is
     * capitalized.
     * 
     * @param color The color of the left ball
     */
    public void setBallLabelL(String color){
        // Color with first letter capitalized:
        String capColor = color.substring(0, 1).toUpperCase() + color.substring(1);
        
        this.ballLabelL.setLabel(capColor + " ball:");
    }
     /**
     * Setter for the {@code ballLabelR}. The color name's first letter is
     * capitalized.
     * 
     * @param color The color of the left ball
     */
    public void setBallLabelR(String color){
        // Color with first letter capitalized:
        String capColor = color.substring(0, 1).toUpperCase() + color.substring(1);
        
        this.ballLabelR.setLabel(capColor + " ball:");
    }
}
