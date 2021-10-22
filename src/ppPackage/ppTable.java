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
 * as part of the Fall 2021 Assignment 3.
 * 
 * @author SpacewaIker
 */
public class ppTable {
    private ppSim gProgram;

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

        // Debug/test labels:
        if (SUPERSLOW){
            final GLabel slowModeLabel = createGLabel(
                "SUPERSLOW MODE", 800, YHEIGHT + 140, Color.BLUE);
        } else if (SLOW){
            final GLabel slowModeLabel = createGLabel(
                "SLOW MODE", 800, YHEIGHT + 140, Color.BLUE);
        }
        if (MESG){
            final GLabel mesgModeLabel = createGLabel(
                "MESSAGES ENABLED", 800, YHEIGHT + 60, new Color(0, 180, 0));// dark green
        }

        // Create walls:
        // Create ground plane
        final GRect gPlane = new GRect(0, YHEIGHT, XWIDTH + OFFSET, 3);
        gPlane.setColor(Color.BLACK);
        gPlane.setFilled(true);
        this.gProgram.add(gPlane);
        
        // Create left wall
        final GRect lWallPlane = new GRect(XwallL * Xs, 0, 1, YHEIGHT);
        lWallPlane.setColor(Color.BLUE);
        lWallPlane.setFilled(true);
        this.gProgram.add(lWallPlane);
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
    public ppSim getDisplay(){
        return this.gProgram;
    }
}
