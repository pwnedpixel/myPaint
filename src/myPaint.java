import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

/**
 * Created by andyk on 2017-03-14.
 */
public class myPaint extends JFrame {
    private JPanel draw;
    private JPanel controls;
    private JPanel colours;
    private String mode="none";
    private JLabel selColour;
    private JLabel modeLbl;
    private int prevx,prevy,startX,startY = 0;
    private ShapeComponent shapesComp;
    private Color currentColour = Color.black;

    /**
     * Initializes the layout of the UI, adds event listeners to the buttons and paint are
     */
    public void init(){
        GridBagConstraints c = new GridBagConstraints();
        this.setTitle("my Painter 9001");
        this.setMinimumSize(new Dimension(800,400));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new FlowLayout());

        draw = new JPanel();
        controls = new JPanel();
        colours = new JPanel();
        shapesComp = new ShapeComponent();
        draw.setPreferredSize(new Dimension(375,360));
        controls.setPreferredSize(new Dimension(200,360));
        colours.setPreferredSize(new Dimension(200,360));
        //draw.setBorder(BorderFactory.createLineBorder(Color.black));
        controls.setBorder(BorderFactory.createLineBorder(Color.red));
        colours.setBorder(BorderFactory.createLineBorder(Color.blue));
        draw.setBackground(Color.white);
        this.add(draw,c);
        this.add(controls,c);
        this.add(colours,c);

        //Add The button
        modeLbl = new JLabel("Select mode");
        selColour = new JLabel("Current Colour: black");
        controls.add(modeLbl);
        controls.add(new JLabel("---------------------------------------"));

        //Control Buttons----------
        JButton undoBtn = new JButton("undo");
        JButton redoBtn = new JButton("redo");
        JButton lineBtn = new JButton("line");
        JButton rectBtn = new JButton("rectangle");
        JButton ellipseBtn = new JButton("ellipse");
        JButton drawBtn = new JButton("draw");
        JButton circleBtn = new JButton("circle");
        JButton squareBtn = new JButton("square");
        controls.add(undoBtn);
        controls.add(redoBtn);
        controls.add(lineBtn);
        controls.add(rectBtn);
        controls.add(ellipseBtn);
        controls.add(drawBtn);
        controls.add(circleBtn);
        controls.add(squareBtn);

        //ColourButtons---------------
        JButton redButton = new JButton("red");
        JButton blackButton = new JButton("black");
        JButton blueButton = new JButton("blue");
        JButton yellowButton = new JButton("yellow");
        colours.add(selColour);
        colours.add(new JLabel("---------------------------------------"));
        colours.add(redButton);
        colours.add(blackButton);
        colours.add(blueButton);
        colours.add(yellowButton);


        //colour button listeners------
        redButton.addActionListener(new ColourButtonListener());
        blackButton.addActionListener(new ColourButtonListener());
        blueButton.addActionListener(new ColourButtonListener());
        yellowButton.addActionListener(new ColourButtonListener());

        //control Button listeners-----
        lineBtn.addActionListener(new ModeListener());
        rectBtn.addActionListener(new ModeListener());
        ellipseBtn.addActionListener(new ModeListener());
        drawBtn.addActionListener(new ModeListener());
        circleBtn.addActionListener(new ModeListener());
        squareBtn.addActionListener(new ModeListener());
        undoBtn.addActionListener(new ModeListener());
        redoBtn.addActionListener(new ModeListener());

        draw.addMouseListener(new MouseButtonListener());
        draw.addMouseMotionListener(new MouseMoveListener());

        this.setVisible(true);
    }

    /**
     * Erases the old line, and then draws a line to the new coordinates
     * @param stx x coordinate of the line start
     * @param sty y coordinate of the line start
     * @param newX x coordinate of the line end
     * @param newY y coordinate of the line end
     * @param erase where or not to erase the old line
     */
    private void drawNewLine(int stx, int sty,int newX, int newY, boolean erase){
        Graphics g = draw.getGraphics();
        refreshShapes();
        if (erase) {
            g.setColor(Color.white);
            g.drawLine(stx, sty, prevx, prevy);
        }
        g.setColor(currentColour);
        g.drawLine(stx,sty,newX,newY);
        draw.paintComponents(g);
    }

    /**
     * Draws a new rectangle from the starting points to the new coordinates.
     * Erases the rectangle that went from start to previous coordinates.
     * @param newX the new second x coordinate
     * @param newY the new second y coordinate
     */
    private void drawNewRect(int newX, int newY){
        int tw,th,tx,ty;
        refreshShapes();
        Graphics g = draw.getGraphics();
        g.setColor(Color.white);
        tw = Math.abs(prevx-startX);
        th = Math.abs(prevy-startY);
        tx = Math.min(startX,prevx);
        ty = Math.min(startY,prevy);
        g.drawRect(tx,ty,tw,th);

        g.setColor(currentColour);
        tw = Math.abs(newX-startX);
        th = Math.abs(newY-startY);
        tx = Math.min(startX,newX);
        ty = Math.min(startY,newY);
        g.drawRect(tx,ty,tw,th);
        draw.paintComponents(g);

    }

    /**
     * Draws an ellipse from the startX and startY to the given x and y parameters.
     * Erases the ellipse that went from start to previous coordinates.
     * @param newX the new second x coordinate
     * @param newY the new second y coordinate
     */
    private void drawNewEllipse(int newX, int newY){
        int tw,th,tx,ty;
        refreshShapes();
        Graphics g = draw.getGraphics();
        g.setColor(Color.white);
        tw = Math.abs(prevx-startX);
        th = Math.abs(prevy-startY);
        tx = Math.min(startX,prevx);
        ty = Math.min(startY,prevy);
        g.drawOval(tx,ty,tw,th);

        g.setColor(currentColour);
        tw = Math.abs(newX-startX);
        th = Math.abs(newY-startY);
        tx = Math.min(startX,newX);
        ty = Math.min(startY,newY);
        g.drawOval(tx,ty,tw,th);
        draw.paintComponents(g);
    }

    /**
     * Draws a square from the startX and startY to the given x and y parameters.
     * Erases the square that went from start to previous coordinates.
     * @param newX the new second x coordinate
     * @param newY the new second y coordinate
     */
    private void drawNewSquare(int newX, int newY){
        int tw,th,max,newStartX,newStartY;
        refreshShapes();
        Graphics g = draw.getGraphics();
        g.setColor(Color.white);
        tw = Math.abs(prevx-startX);
        th = Math.abs(prevy-startY);
        max = Math.max(tw,th);
        newStartX=(prevx-startX<0)?startX-max:startX;
        newStartY=(prevy-startY<0)?startY-max:startY;
        g.drawRect(newStartX,newStartY,max,max);

        g.setColor(currentColour);
        tw = Math.abs(newX-startX);
        th = Math.abs(newY-startY);
        max = Math.max(tw,th);
        newStartX=(newX-startX<0)?startX-max:startX;
        newStartY=(newY-startY<0)?startY-max:startY;
        g.drawRect(newStartX,newStartY,max,max);
        draw.paintComponents(g);

    }

    /**
     * Draws a circle from the startX and startY to the given x and y parameters.
     * Erases the circle that went from start to previous coordinates.
     * @param newX the new second x coordinate
     * @param newY the new second y coordinate
     */
    private void drawNewCircle(int newX, int newY){
        int tw,th,max,newStartX,newStartY;
        refreshShapes();
        Graphics g = draw.getGraphics();
        g.setColor(Color.white);
        tw = Math.abs(prevx-startX);
        th = Math.abs(prevy-startY);
        max = Math.max(tw,th);
        newStartX=(prevx-startX<0)?startX-max:startX;
        newStartY=(prevy-startY<0)?startY-max:startY;
        g.drawOval(newStartX,newStartY,max,max);

        g.setColor(currentColour);
        tw = Math.abs(newX-startX);
        th = Math.abs(newY-startY);
        max = Math.max(tw,th);
        newStartX=(newX-startX<0)?startX-max:startX;
        newStartY=(newY-startY<0)?startY-max:startY;
        g.drawOval(newStartX,newStartY,max,max);
        draw.paintComponents(g);
    }

    /**
     * called to redraw all saved shapes and shape components
     */
    private void refreshShapes(){
        drawShapeComponent(shapesComp);
    }

    /**
     * Draws the shapes from the given shape component
     * @param componentToDraw The component containing the shapes (or further components)
     */
    private void drawShapeComponent(ShapeComponent componentToDraw){
        LinkedList<String[]> shapes = componentToDraw.getShapes();
        Graphics g = draw.getGraphics();
        int max, newStartX,newStartY;
        for (String[] shape : shapes){
            switch(shape[0]){
                case "line":
                    g.setColor(new Color(Integer.parseInt(shape[5])));
                    g.drawLine(Integer.valueOf(shape[1]),Integer.valueOf(shape[2]),Integer.valueOf(shape[3]),Integer.valueOf(shape[4]));
                    break;
                case "rectangle":
                    g.setColor(new Color(Integer.parseInt(shape[5])));
                    g.drawRect(Math.min(Integer.valueOf(shape[1]),Integer.valueOf(shape[3])),Math.min(Integer.valueOf(shape[2]),Integer.valueOf(shape[4])),
                            Math.abs(Integer.valueOf(shape[1])-Integer.valueOf(shape[3])),Math.abs(Integer.valueOf(shape[2])-Integer.valueOf(shape[4])));
                    break;
                case "ellipse":
                    int[] ellVals = {Integer.valueOf(shape[1]),Integer.valueOf(shape[2]),Integer.valueOf(shape[3]),Integer.valueOf(shape[4])};
                    g.setColor(new Color(Integer.parseInt(shape[5])));
                    g.drawOval(Math.min(ellVals[0],ellVals[2]),Math.min(ellVals[1],ellVals[3]),Math.abs(ellVals[2]-ellVals[0]), Math.abs(ellVals[1]-ellVals[3]));
                    break;
                case "square" :
                    int[] sqVals = {Integer.valueOf(shape[1]),Integer.valueOf(shape[2]),Integer.valueOf(shape[3]),Integer.valueOf(shape[4])};
                    g.setColor(new Color(Integer.parseInt(shape[5])));

                    max = Math.max(Math.abs(sqVals[2]-sqVals[0]),Math.abs(sqVals[3]-sqVals[1]));
                    newStartX=(sqVals[2]-sqVals[0]<0)?sqVals[0]-max:sqVals[0];
                    newStartY=(sqVals[3]-sqVals[1]<0)?sqVals[1]-max:sqVals[1];
                    g.drawRect(newStartX,newStartY,max,max);
                    break;
                case "circle" :
                    int[] cirVals = {Integer.valueOf(shape[1]),Integer.valueOf(shape[2]),Integer.valueOf(shape[3]),Integer.valueOf(shape[4])};
                    g.setColor(new Color(Integer.parseInt(shape[5])));

                    max = Math.max(Math.abs(cirVals[2]-cirVals[0]),Math.abs(cirVals[3]-cirVals[1]));
                    newStartX=(cirVals[2]-cirVals[0]<0)?cirVals[0]-max:cirVals[0];
                    newStartY=(cirVals[3]-cirVals[1]<0)?cirVals[1]-max:cirVals[1];
                    g.drawOval(newStartX,newStartY,max,max);
                    break;
                case "composite":
                    drawShapeComponent(componentToDraw.getComposite(Integer.valueOf(shape[1])));
                    break;
            }
        }
        draw.paintComponents(g);
    }

    /**
     * Sets the entire drawing area to white. Used before calling a refresh
     */
    private void blankSpace(){
        Graphics g = draw.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0,0,draw.getWidth(),draw.getHeight());
        draw.paintComponents(g);
    }

    /**
     * Run this.
     * Creates an instance of myPaint and calls the init function
     * @param args
     */
    public static void main(String[] args){
        myPaint paint = new myPaint();
        paint.init();
    }

    //CLASSES ------------------------------------------------

    /**
     * handles the storage of shapes, composites and redo/undo functions
     */
    private class ShapeComponent {
        LinkedList<String[]> shapes = new LinkedList<>();
        LinkedList<String[]> undone = new LinkedList<>();
        LinkedList<ShapeComponent> composites = new LinkedList<>();

        /**
         * constructor. Doesn't really do anything
         */
        private ShapeComponent(){

        }

        /**
         * Adds a shape with the given arguments to the list of shapes
         * @param args the properties of the shape
         */
        private void addShape(String[] args){
            shapes.add(args);
        }

        /**
         * Returns the LinkedList of shapes
         * @return the {@link LinkedList} {@link #shapes} which contains the shapes
         */
        private LinkedList<String[]> getShapes(){
            return shapes;
        }

        /**
         * Composites are store in a seperate list. This function allows access to one at a given index.
         * The index is saved in one of the composite type shape's properties.
         * @param index the index of the composite to fetch.
         * @return a {@link #ShapeComponent()} object.
         */
        private ShapeComponent getComposite(int index){
            return composites.get(index);
        }

        /**
         * creates a new composite. Calls to {@link #addToComposite(String[])} will add the shapes to this new
         * composite.
         */
        private void startComposite(){
            System.out.println("start Composite");
            composites.add(new ShapeComponent());
        }

        /**
         * Adds the currently open composite to the list of shapes. The shape title is "composite" and
         * the property is its index in {@link #composites}.
         */
        private void endComposite(){
            shapes.add(new String[]{"composite",Integer.toString(composites.size()-1)});
        }

        /**
         * Adds a shape (and it's properties) to the currently open composite.
         * @param args The properties of the shape.
         */
        private void addToComposite(String[] args){
            composites.get(composites.size()-1).addShape(args);
        }

        /**
         * Undoes the last drawn shape by moving it from {@link #shapes} to {@link #undone}.
         * This will prevent it from getting redrawn, but make it available to be redone.
         */
        private void undo(){
            if (shapes.size()!=0){
                System.out.println("UndoCalled");
                undone.add(shapes.get(shapes.size()-1));
                shapes.remove(shapes.size()-1);
            }
        }

        /**
         * Re-adds the last drawn shape by moving it from {@link #undone} to {@link #shapes}.
         * This will allow to to be draw on the next shape refresh.
         */
        private void redo(){
            if (undone.size()!=0){
                System.out.println("RedoCalled");
                shapes.add(undone.get(undone.size()-1));
                undone.remove(undone.size()-1);
            }
        }
    }

    /**
     * Used to detect changes to the current draw mode
     */
    public class ModeListener implements ActionListener{

        /**
         * Called when one of the mode buttons is pressed.
         * @param e the object that cause the action.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(e.getActionCommand());

            //sets the current mode to the button that was pressed.
            //If the given button was "undo" or "redo" then the mode stays the same.
            mode = (e.getActionCommand().equals("undo") || e.getActionCommand().equals("redo"))?mode:e.getActionCommand();
            modeLbl.setText("Mode: "+mode);
            switch(e.getActionCommand()){
                case "undo":
                    shapesComp.undo();
                    blankSpace();
                    refreshShapes();
                    break;
                case "redo":
                    shapesComp.redo();
                    blankSpace();
                    refreshShapes();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Used to handle mouse click/release events
     */
    public class MouseButtonListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        /**
         * Called when the mouse button is pressed
         * @param e The properties of the pointer.
         */
        @Override
        public void mousePressed(MouseEvent e) {
            switch(mode){
                case "draw":
                    //if the current mode is "draw" a new composite shape needs to be started.
                    //lines created while in the draw mode will be added to this composite.
                    shapesComp.startComposite();
                default:
                    startX = e.getX();
                    startY = e.getY();
                    prevx = e.getX();
                    prevy = e.getY();
                    break;
            }
        }

        /**
         * Handles the mouse button release event. When the mouse is release, the drawn shape is added to the
         * List of shapes. (it gets saved)
         * @param e the properties of the event.
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            switch(mode){
                case "line":
                    shapesComp.addShape(new String[]{"line",Integer.toString(startX),Integer.toString(startY),Integer.toString(e.getX()),
                            Integer.toString(e.getY()),Integer.toString(currentColour.getRGB())});
                    System.out.println("new Colour: "+ currentColour.toString());
                    break;
                case "rectangle" :
                    shapesComp.addShape(new String[]{"rectangle",Integer.toString(startX),Integer.toString(startY),Integer.toString(e.getX())
                            ,Integer.toString(e.getY()),Integer.toString(currentColour.getRGB())});
                    break;
                case "ellipse" :
                    shapesComp.addShape(new String[]{"ellipse",Integer.toString(startX),Integer.toString(startY), Integer.toString(e.getX()),
                            Integer.toString(e.getY()),Integer.toString(currentColour.getRGB())});
                    break;
                case "square":
                    shapesComp.addShape(new String[]{"square",Integer.toString(startX),Integer.toString(startY),Integer.toString(e.getX())
                            ,Integer.toString(e.getY()),Integer.toString(currentColour.getRGB())});
                    break;
                case "circle":
                    shapesComp.addShape(new String[]{"circle",Integer.toString(startX),Integer.toString(startY),Integer.toString(e.getX())
                            ,Integer.toString(e.getY()),Integer.toString(currentColour.getRGB())});
                    break;
                case "draw" :
                    //we are done adding to the current composite shape. it will add itself to the list of shapes
                    //when endComposite() is called.
                    shapesComp.endComposite();
                default:

                    break;
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    /**
     * Listener for mouse movement events.
     */
    public class MouseMoveListener implements MouseMotionListener {

        /**
         * Event triggered when the mouse button is pressed while the mouse moves.
         * Starts drawing shapes from the start coordinates to the current mouse coordinates.
         * @param e properties of the pointer
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            switch(mode){
                case "line":
                    drawNewLine(startX,startY,e.getX(), e.getY(),true);
                    break;
                case "rectangle":
                    drawNewRect(e.getX(),e.getY());
                    break;
                case "ellipse":
                    drawNewEllipse(e.getX(),e.getY());
                    break;
                case "draw":
                    //Add the most recent line to the composite shape
                    drawNewLine(prevx,prevy,e.getX(), e.getY(),false);
                    shapesComp.addToComposite(new String[]{"line",Integer.toString(prevx),Integer.toString(prevy),Integer.toString(e.getX()),Integer.toString(e.getY()),Integer.toString(currentColour.getRGB())});
                    break;
                case "square":
                    drawNewSquare(e.getX(),e.getY());
                    break;
                case "circle":
                    drawNewCircle(e.getX(),e.getY());
                    break;
                default:
                    break;
            }
            prevx = e.getX();
            prevy = e.getY();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            //System.out.println("Move");
        }
    }

    /**
     * Listener for click events on the colour buttons
     */
    public class ColourButtonListener implements ActionListener{

        /**
         * event triggered when one of the colour buttons are clicked.
         * Changes the currently selected colour
         * @param e properties of the button.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(e.getActionCommand());
            switch(e.getActionCommand()){
                //depending on the choice, the current colour and label are set accordingly.
                case "red":
                    currentColour = Color.red;
                    selColour.setText("Current Colour: red");
                    break;
                case "blue":
                    currentColour = Color.blue;
                    selColour.setText("Current Colour: blue");
                    break;
                case "black":
                    currentColour = Color.black;
                    selColour.setText("Current Colour: black");
                    break;
                case "yellow":
                    currentColour = Color.yellow;
                    selColour.setText("Current Colour: yellow");
                    break;
            }
        }
    }
}
