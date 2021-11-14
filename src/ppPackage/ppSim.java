package ppPackage;

import static ppPackage.ppSimParams.*;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.ActionEvent;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

import acm.io.IODialog;
import acm.graphics.GLine;
import acm.graphics.GPoint;

/**
 * A program simulating a bouncing ping-pong ball. This program uses the
 * acm.jar package. It is the main class of the {@code ppPackage} package, and
 * imports the other classes of the package, namely {@code ppBall},
 * {@code ppTable}, {@code ppPaddle}, {@code ppPaddleAgent}, and
 * {@code ppSimParams}.
 * 
 * The {@code ppSim} class is based on code written by Prof. Frank Ferrie,
 * as part of the Fall 2021 Assignment 4.
 * 
 * @author SpacewaIker
 */
public class ppSim extends GraphicsProgram{
    private double ballYinit;

    private RandomGenerator rgen;
    private IODialog ioDialog;

    private ppTable myTable;
    private ppBall myBall;
    private ppPaddle myPaddle;
    private ppPaddleAgent agentPaddle;

    private Color leftColor = Color.BLUE;
    private Color rightColor = Color.BLACK;
    private Color ballColor = Color.RED;
    
    private JToggleButton traceToggle;
    private JToggleButton mesgToggle;
    private JToggleButton pauseToggle;
    private JSlider timeSlider;
    private JSlider lagSlider;

    /**
     * Entry point of the program.
     */
    public static void main(String[] args){
        new ppSim().start(args);
    }
    /**
     * Main program process. The {@code ppTable} is instantiated, the
     * {@code IODialog} used to get user input and help message is also
     * instantiated. The help screen is created and the listeners are instanciated.
     */
    public void run(){
        this.setSize(XWIDTH + XOFFSET, YHEIGHT + YOFFSET);        

        rgen = RandomGenerator.getInstance();
        rgen.setSeed(System.currentTimeMillis());
        ioDialog = new IODialog();

        // Setup start screen
        JLabel title = new JLabel("PONG");
        title.setFont(FONT_XL);
        add(title, XWIDTH/2 - 375, YHEIGHT/2 - 150);
        JLabel description = new JLabel(
            "<html>A pong-style game<br>Made by Thibaut Baguette<br>"+ 
            "Fall 2021<br><i>Part of the code for this game was written by Prof. Frank Ferrie</i>");
        description.setFont(FONT_XS);
        add(description, XWIDTH/2 - 425, YHEIGHT/2);
        add(new GLine(XWIDTH/2 + 125, 50, XWIDTH/2 + 125, YHEIGHT));
        JLabel helpText = new JLabel(HELPSTRING);
        helpText.setFont(FONT_XS);
        add(helpText, XWIDTH/2 + 150, YHEIGHT/2 - 250);

        // Add start button and instantiate mesgToggle:
        createJButton("Start");

        revalidate();
        repaint();

        mesgToggle = new JToggleButton("<html><u>m</u>esg");

        addActionListeners();
        addKeyListeners();
        addMouseListeners();
    }
    /**
     * Mouse handler. Moves the {@code ppPaddle} following the mouse movement.
     */
    public void mouseMoved(MouseEvent e){
        // Do nothing if myBall has not been instanciated
        if (this.myBall == null)
            return;

        GPoint Pm = S2W(new GPoint(e.getX(), e.getY()));
        double PaddleX = this.myPaddle.getPosition().getX();
        double PaddleY = Pm.getY();
        this.myPaddle.setPosition(new GPoint(PaddleX, PaddleY));
    }
    /**
     * Key handler. When keys are pressed, the associated button gets "pressed":
     * {@code actionPerformed} gets called with the right {@code actionCommand}.
     */
    public void keyPressed(KeyEvent e){
        int keyCode = e.getKeyCode();
        switch (keyCode){
            case 'S':
                actionPerformed(new ActionEvent(new Object(), 0, "Start"));
                break;
            case 'C':
                actionPerformed(new ActionEvent(new Object(), 0, "Clear"));
                break;
            case 'N':
                actionPerformed(new ActionEvent(new Object(), 0, "New Serve"));
                break;
            case 'T':
                this.traceToggle.setSelected(!this.traceToggle.isSelected());
                break;
            case 'M':
                this.mesgToggle.setSelected(!this.mesgToggle.isSelected());
                break;
            case 'Q':
                actionPerformed(new ActionEvent(new Object(), 0, "Quit"));
                break;
            case 'H':
                actionPerformed(new ActionEvent(new Object(), 0, "Help"));
                break;
            case KeyEvent.VK_LEFT:
                this.timeSlider.setValue(this.timeSlider.getValue() - 100);
                break;
            case KeyEvent.VK_RIGHT:
                this.timeSlider.setValue(this.timeSlider.getValue() + 100);
                break;
            case KeyEvent.VK_DOWN:
                this.lagSlider.setValue(this.lagSlider.getValue() - 1);
                break;
            case KeyEvent.VK_UP:
                this.lagSlider.setValue(this.lagSlider.getValue() + 1);
                break;
            case KeyEvent.VK_SPACE:
                this.pauseToggle.setSelected(!this.pauseToggle.isSelected());
                break;
            default:
                if (getMESG())
                    System.out.println("KeyEvent: " + keyCode + " pressed");
                break;
        }
    }
    /**
     * Action handler. Following the {@code actionCommand}, certain actions
     * are taken.
     */
    public void actionPerformed(ActionEvent e){
        String command = e.getActionCommand();
        String name; // For the setLeftName and setRightName cases
        if (getMESG())
            System.out.println("ActionEvent: " + command);

        switch (command){
            case "Quit":
                System.exit(0);
            case "Left Color":
                this.leftColor = JColorChooser.showDialog(this,
                    "Left player paddle color", this.leftColor);
                this.agentPaddle.setColor(this.leftColor);
                break;
            case "Right Color":
                this.rightColor = JColorChooser.showDialog(this,
                    "Right player paddle color", this.rightColor);
                this.myPaddle.setColor(this.rightColor);
                break;
            case "Ball Color":
                this.ballColor = JColorChooser.showDialog(this,
                    "Ball color", this.ballColor);
                this.myBall.setColor(this.ballColor);
                break;
            case "GitHub":
                try {
                    URI url = new URI("https://github.com/spacewaiker/pong/");
                    java.awt.Desktop.getDesktop().browse(url);
                } catch (Exception exc){
                    exc.printStackTrace();
                }
                break;
            case "Start":
                TimerTask tt1 = new TimerTask(){
                    public void run(){
                        startInit();
                    }
                };
                Timer t1 = new Timer();
                t1.schedule(tt1, 100);
                break;
            case "New Serve":
                TimerTask tt2 = new TimerTask(){
                    public void run(){
                        reset();
                    }
                };
                Timer t2 = new Timer();
                t2.schedule(tt2, 100);
                break;
            case "rtime":
                this.timeSlider.setValue(timeSliderInit);
                break;
            case "rlag":
                this.lagSlider.setValue(lagSliderInit);
                break;
            case "Clear":
                this.myTable.clearScoreboard();
                break;
            case "setLeftName":
                name = ioDialog.readLine("Left name:");
                this.myTable.setLeftName(name);
                break;
            case "setRightName":
                name = ioDialog.readLine("Right name:");
                this.myTable.setRightName(name);
                break;
            case "Help":
                ioDialog.print(HELPSTRING);
                ioDialog.println();
                break;
            default:
                if (getMESG())
                    System.out.println("ActionEvent: No action performed");
        }
    }
    /**
     * This method erases the starting screen (including the start button) and
     * places the buttons and labels for the game screen.
     */
    private void startInit(){
        // Remove "start" button
        this.getRegionPanel(SOUTH).removeAll();

        // Add color buttons to NORTH:
        createJButton("Left Color", NORTH);
        createJButton("Ball Color", NORTH);
        createJButton("Right Color", NORTH);
        // Add spacer for the scoreboard:
        JLabel spacer = new JLabel("          ");
        spacer.setFont(FONT_L);
        add(spacer, NORTH);

        // Create table (and scoreboard)
        this.myTable = new ppTable(this);

        // Add spacer and github button
        JLabel spacer2 = new JLabel("                              ");
        spacer2.setFont(FONT_L);
        add(spacer2, NORTH);
        JButton github = new JButton("GitHub");
        github.setFont(FONT_S);
        add(github, NORTH);

        // Add buttons and sliders to SOUTH:
        createJButton("<html><u>C</u>lear", "Clear");
        createJButton("<html><u>N</u>ew Serve", "New Serve");
        traceToggle = new JToggleButton("<html><u>T</u>race");
        traceToggle.setActionCommand("Trace");
        traceToggle.setFont(FONT_S);
        add(traceToggle, SOUTH);
        mesgToggle.setActionCommand("mesg");
        mesgToggle.setFont(FONT_S);
        add(mesgToggle, SOUTH);
        createJButton("<html><u>Q</u>uit", "Quit");
        createJLabel("+t");
        timeSlider = new JSlider(timeSliderMin, timeSliderMax, timeSliderInit);
        add(timeSlider, SOUTH);
        createJLabel("-t");
        createJButton("rtime");
        createJLabel("-lag");
        lagSlider = new JSlider(lagSliderMin, lagSliderMax, lagSliderInit);
        add(lagSlider, SOUTH);
        createJLabel("+lag");
        createJButton("rlag");

        pauseToggle = new JToggleButton("Pause");
        pauseToggle.setFont(FONT_S);
        add(pauseToggle, SOUTH);

        createJButton("<html><u>H</u>elp", "Help");

        this.revalidate();
        this.repaint();
        addActionListeners();

        reset();
    }
    /**
     * Resets the ppSim. The screen is erased and gets repainted. New
     * {@code ppBall}, {@code ppTable}, {@code ppPaddle}, and
     * {@code ppPaddleAgent} objects are created.
     */
    public void reset(){
        removeAll();

        this.myTable.newScreen();

        newBall();
        newPaddles();

        this.myBall.setRightPaddle(this.myPaddle);
        this.myBall.setLeftPaddle(this.agentPaddle);
        this.agentPaddle.setBall(this.myBall);

        this.myPaddle.start();
        this.agentPaddle.start();

        waitForClick();

        this.myBall.start();
    }
    /**
     * Creates a new ball associated with the {@code this.myBall} instance
     * variable. The previous {@code ppBall} Thread is stopped.
     */
    private void newBall(){
        /* Even though the Thread.stop() method is depreciated, it solves the
            issue of many threads being created and running continuously,
            which slowed the program a lot after 3-4 games. */
        if (this.myBall != null)
            this.myBall.stop();

        this.ballYinit = rgen.nextDouble(YinitMIN, YinitMAX);
        double ieLoss = rgen.nextDouble(eLossMIN, eLossMAX);
        double iVel   = rgen.nextDouble(VoMIN, VoMAX);
        double iTheta = rgen.nextDouble(thetaMIN, thetaMAX);

        this.myBall = new ppBall(
            Xinit, this.ballYinit, iVel, iTheta, ieLoss, this.ballColor,
            this.myTable, this);
    }
    /**
     * Creates new paddles associated with the {@code this.myPaddle} and
     * {@code this.agentPaddle} instance variables. The previous
     * {@code ppPaddle} and {@code ppPaddleAgent} Threads are stopped.
     */
    private void newPaddles(){
        /* Even though the Thread.stop() method is depreciated, it solves the
            issue of many threads being created and running continuously,
            which slowed the program a lot after 3-4 games. */
        if (this.myPaddle != null)
            this.myPaddle.stop();
        if (this.agentPaddle != null)
            this.agentPaddle.stop();


        this.myPaddle = new ppPaddle(
            ppPaddleXinit, ppPaddleYinit, this.rightColor, this.myTable, this);
        this.agentPaddle = new ppPaddleAgent(
            agentPaddleXinit, this.ballYinit, this.leftColor, this.myTable, this);
    }
    /**
     * Getter method for the trace {@code JToggleButton}.
     * 
     * @return Whether or not the button is selected.
     */
    public boolean getTraceState(){
        return this.traceToggle.isSelected();
    }
    /**
     * Getter method for the mesg {@code JToggleButton}.
     * 
     * @return Whether or not the button is selected.
     */
    public boolean getMESG(){
        return this.mesgToggle.isSelected();
    }
    /**
     * Getter method for the pause {@code JToggleButton}.
     * 
     * @return Whether or not the button is selected.
     */
    public boolean getPauseState(){
        return this.pauseToggle.isSelected();
    }
    /**
     * Getter method for the time {@code JSlider}.
     * 
     * @return The value of that the slider is set to.
     */
    public double getTimeValue(){
        return (double) this.timeSlider.getValue();
    }
    /**
     * Getter method for the lag {@code JSlider}.
     * 
     * @return The value of that the slider is set to.
     */
    public int getLagValue(){
        return this.lagSlider.getValue();
    }
    /**
     * Creates a {@code JButton} with the specified text, actionCommand,
     * sets the font to {@code FONT_S}, and adds it to the specified region.
     * 
     * @param text The button's text
     * @param actionCommand The actionCommand to be called whenever the button is pressed
     * @param region The region in which to add the button
     * 
     * @return The {@code JButton}
     */
    private JButton createJButton(String text, String actionCommand, String region){
        JButton myJButton = new JButton(text);
        myJButton.setActionCommand(actionCommand);
        myJButton.setFont(FONT_S);
        this.add(myJButton, region);
        return myJButton;
    }
    /**
     * Creates a {@code JButton} with the specified text and actionCommand, OR
     * text and region. Also sets the font to {@code FONT_S}.
     * 
     * @param text The button's text
     * @param actionCommand The actionCommand to be called whenever the button
     *      is pressed, OR the region in which to add the button
     * 
     * @return The {@code JButton}
     */
    private JButton createJButton(String text, String actionCommand){
        /* If the actionCommand string is not the action command but instead
            it is one of NORTH, SOUTH, EAST, WEST, return the appropriate JButton*/
        if (actionCommand.equals(NORTH) || actionCommand.equals(SOUTH) ||
            actionCommand.equals(EAST) || actionCommand.equals(WEST))
            return createJButton(text, text, actionCommand);

        return createJButton(text, actionCommand, SOUTH);
    }
    /**
     * Creates a {@code JButton} with the specified text, sets the font to
     * {@code FONT_S}, and adds it to {@code SOUTH}.
     * 
     * @param text The button's text
     * 
     * @return The {@code JButton}
     */
    private JButton createJButton(String text){
        return createJButton(text, text, SOUTH);
    }
    /**
     * Creates a {@code JLabel} with the specified text and sets the font to
     * {@code FONT_S}.
     * 
     * @param text The button's text
     * 
     * @return The {@code JLabel}
     */
    private JLabel createJLabel(String text){
        JLabel myJLabel = new JLabel(text);
        myJLabel.setFont(FONT_S);
        this.add(myJLabel, SOUTH);
        return myJLabel;
    }
}
