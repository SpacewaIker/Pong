package ppPackage;

import static ppPackage.ppSimParams.*;
import acm.graphics.GRect;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JButton;

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

    // Number of returns and label
    private int rightPoints = 0;
    private JLabel rightPointsLabel;
    private JButton rightName;
    private int leftPoints = 0;
    private JLabel leftPointsLabel;
    private JButton leftName;

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

        // Scoreboard
        leftName = new JButton("Agent");
        leftName.setActionCommand("setLeftName");
        leftName.setFont(FONT_L);
        this.gProgram.add(leftName, "North");

        leftPointsLabel = new JLabel("0");
        leftPointsLabel.setFont(FONT_L);
        this.gProgram.add(leftPointsLabel, "North");

        JLabel separator = new JLabel("  |  ");
        separator.setFont(FONT_L);
        this.gProgram.add(separator, "North");

        rightName = new JButton("Human");
        rightName.setActionCommand("setRightName");
        rightName.setFont(FONT_L);
        this.gProgram.add(rightName, "North");

        rightPointsLabel = new JLabel("0");
        rightPointsLabel.setFont(FONT_L);
        this.gProgram.add(rightPointsLabel, "North");

        newScreen();
    }
    /**
     * Getter for the {@code ppTable} display/canvas.
     * 
     * @return The {@code ppSim} instance serving as a display.
     */
    public ppSim getDisplay(){
        return this.gProgram;
    }
    /**
     * Increments the number of returns of the {@code returnsLabel}
     */
    public void incrementRightPoints(){
        this.rightPointsLabel.setText(
            Integer.toString(++this.rightPoints));
    }
    /**
     * Increments the number of returns of the {@code returnsLabel}
     */
    public void incrementLeftPoints(){
        this.leftPointsLabel.setText(
            Integer.toString(++this.leftPoints));
    }
    /**
     * Setter method for the left player's name
     * 
     * @param name The name to be set
     */
    public void setLeftName(String name){
        this.leftName.setText(name);
    }
    /**
     * Setter method for the right player's name
     * 
     * @param name The name to be set
     */
    public void setRightName(String name){
        this.rightName.setText(name);
    }
    /**
     * Clears the scoreboard by setting all points to 0.
     */
    public void clearScoreboard(){
        this.leftPoints = 0;
        this.rightPoints = 0;
        this.leftPointsLabel.setText("0");
        this.rightPointsLabel.setText("0");
    }
    /**
     * Creates a new screen: the ground plane is created and added.
     */
    public void newScreen(){
        // Create ground plane
        final GRect gPlane = new GRect(0, YHEIGHT, XWIDTH + XOFFSET, 3);
        gPlane.setColor(Color.BLACK);
        gPlane.setFilled(true);
        this.gProgram.add(gPlane);
    }
}
