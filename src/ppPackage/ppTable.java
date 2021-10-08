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
    private GLabel velocityLabel;
    private GLabel angleLabel;
    private GLabel eLossLabel;

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
        this.simTimeLabel = createGLabel("Simulation Time: 0.0 s", 30, YHEIGHT + 50);
        this.velocityLabel = createGLabel("Initial velocity:", 30, YHEIGHT + 90);
        this.angleLabel = createGLabel("Launch angle:", 30, YHEIGHT + 130);
        this.eLossLabel = createGLabel("Energy loss coefficient:", 30, YHEIGHT + 170);

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
     * Setter for the {@code velocityLabel}
     * 
     * @param velocity The initial velocity to set
     */
    public void setVelLabel(double velocity){
        this.velocityLabel.setLabel(
            String.format("Initial velocity: %.1f m/s", velocity));
    }
    /**
     * Setter for the {@code angleLabel}
     * 
     * @param angle The launch angle to set
     */
    public void setAngleLabel(double angle){
        this.angleLabel.setLabel(
            String.format("Launch angle: %.1f", angle) + (char) 176);//-> °
    }
    /**
     * Setter for the {@code eLossLabel}
     * 
     * @param eLoss The energy loss coefficient to set
     */
    public void setELossLabel(double eLoss){
        this.eLossLabel.setLabel(
            String.format("Energy loss coefficient: %.1f", eLoss));
    }
}
