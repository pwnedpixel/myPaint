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
    private JPanel history;
    private JLabel infoLbl;
    private String mode="none";
    private JLabel modeLbl;
    private DefaultListModel listModel;
    private JScrollPane listScrollPane;
    private JList list;
    private int prevx,prevy,startX,startY,prevPolyX,prevPolyY = 0;
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
        history = new JPanel();
        shapesComp = new ShapeComponent();
        draw.setPreferredSize(new Dimension(375,360));
        history.setPreferredSize(new Dimension(200,360));
        controls.setPreferredSize(new Dimension(200,360));
        //draw.setBorder(BorderFactory.createLineBorder(Color.black));
        controls.setBorder(BorderFactory.createLineBorder(currentColour));
        history.setBorder(BorderFactory.createLineBorder(Color.black));
        draw.setBackground(Color.white);

        //Control Items----------------------------------
        modeLbl = new JLabel("Select mode");
        infoLbl = new JLabel("");

        JButton undoBtn = new JButton("undo");
        JButton redoBtn = new JButton("redo");
        JButton lineBtn = new JButton("line");
        JButton rectBtn = new JButton("rectangle");
        JButton ellipseBtn = new JButton("ellipse");
        JButton drawBtn = new JButton("draw");
        JButton circleBtn = new JButton("circle");
        JButton squareBtn = new JButton("square");
        JButton polygonBtn = new JButton("polygon");
        JButton changeClrButton = new JButton("Select New Colour");
        JButton moveSelBtn = new JButton("Move");
        controls.add(modeLbl);
        controls.add(new JLabel("---------------------------------------"));
        controls.add(undoBtn);
        controls.add(redoBtn);
        controls.add(lineBtn);
        controls.add(rectBtn);
        controls.add(ellipseBtn);
        controls.add(drawBtn);
        controls.add(circleBtn);
        controls.add(squareBtn);
        controls.add(polygonBtn);
        controls.add(new JLabel("---------------------------------------"));
        controls.add(changeClrButton);
        controls.add(moveSelBtn);
        controls.add(infoLbl);

        changeClrButton.addActionListener(new ColourButtonListener());

        lineBtn.addActionListener(new ModeListener());
        rectBtn.addActionListener(new ModeListener());
        ellipseBtn.addActionListener(new ModeListener());
        drawBtn.addActionListener(new ModeListener());
        circleBtn.addActionListener(new ModeListener());
        squareBtn.addActionListener(new ModeListener());
        undoBtn.addActionListener(new ModeListener());
        redoBtn.addActionListener(new ModeListener());
        polygonBtn.addActionListener(new ModeListener());
        moveSelBtn.addActionListener(new ModeListener());

        //DRAW items--------------------------------------------------------
        draw.addMouseListener(new MouseButtonListener());
        draw.addMouseMotionListener(new MouseMoveListener());

        //History items-------------------------------------------------------

        listModel = new DefaultListModel();
        JButton clearSelectionBtn = new JButton("Clear Selection");
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setPreferredSize(new Dimension(160,400));
        listScrollPane = new JScrollPane(list);
        listScrollPane.setPreferredSize(new Dimension(180,270));

        history.add(new JLabel("Current Items:"));
        history.add(new JLabel("---------------------------------------"));
        history.add(listScrollPane);
        history.add(clearSelectionBtn);

        clearSelectionBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                list.clearSelection();
            }
        });

        this.add(draw,c);
        this.add(controls,c);
        this.add(history,c);
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
     * handles moving a line from the previous location to the new coordinates
     * @param newX new x startpoint
     * @param newY new y startpoint
     * @param deltaX the width
     * @param deltaY the height
     */
    private void moveLine(int newX, int newY,int deltaX, int deltaY){
        Graphics g = draw.getGraphics();
        refreshShapes();
        g.setColor(Color.white);
        g.drawLine(prevPolyX, prevPolyY, prevPolyX+deltaX, prevPolyY+deltaY);
        g.setColor(Color.black);
        g.drawLine(newX,newY,newX+deltaX,newY+deltaY);
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

    private void moveRect(int newX, int newY, int deltaX, int deltaY){
        Graphics g = draw.getGraphics();
        refreshShapes();

        g.setColor(Color.white);
        g.drawRect(prevPolyX,prevPolyY,deltaX,deltaY);
        g.setColor(Color.black);
        g.drawRect(newX,newY,deltaX,deltaY);
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

    private void refreshHistory(){
        System.out.println("refresh History");

        list.removeAll();
        listModel.removeAllElements();
        for (int i =0;i<shapesComp.getShapes().size();i++){
            System.out.println(i);
            listModel.addElement(i+": "+shapesComp.getElement(i)[0]);
        }

        history.revalidate();
        history.repaint();
    }

    private void moveSelection(int xOffset, int yOffset){
        for (int i =0;i<list.getSelectedIndices().length;i++){
            //System.out.println("changing "+shapesComp.getElement(list.getSelectedIndices()[i])[0]);
            //modify the shape
            String[] newShape;
            newShape = shapesComp.getElement(list.getSelectedIndices()[i]);

            if (newShape[0].equals("composite")) break;
            newShape[1] = Integer.toString(Integer.valueOf(newShape[1])+xOffset);
            newShape[2] = Integer.toString(Integer.valueOf(newShape[2])+yOffset);
            newShape[3] = Integer.toString(Integer.valueOf(newShape[3])+xOffset);
            newShape[4] = Integer.toString(Integer.valueOf(newShape[4])+yOffset);
            shapesComp.replaceShape(newShape,list.getSelectedIndices()[i]);
            blankSpace();
            refreshShapes();
        }
    }

    public void dragShape(int currentX, int currentY, String[] newShape){
        int xOffset = currentX - startX;
        int yOffset = currentY - startY;
        int width=0,height =0;

        //if not a composite
        if (!newShape[0].equals("composite")) {
            width = Integer.valueOf(newShape[3]) - Integer.valueOf(newShape[1]);
            height = Integer.valueOf(newShape[4]) - Integer.valueOf(newShape[2]);
            //used to track the previous start point

        }
        switch(newShape[0]){
            case "line":
                prevPolyX = Integer.valueOf(newShape[1]) + prevx - startX;
//                prevPolyX = prevx+xOffset;
//                prevPolyY = prevy+yOffset;
                prevPolyY = Integer.valueOf(newShape[2]) + prevy - startY;
                moveLine(Integer.valueOf(newShape[1])+xOffset,Math.min(Integer.valueOf(newShape[2]),Integer.valueOf(newShape[4]))+yOffset,
                        width,height);
                break;
            case "rectangle":

                moveRect(Math.min(Integer.valueOf(newShape[1]),Integer.valueOf(newShape[3]))+xOffset,Integer.valueOf(newShape[2])+yOffset,
                        width,height);
                break;


            case "composite":
                ShapeComponent tempComp = shapesComp.getComposite(Integer.valueOf(newShape[1]));
                for (int i =0;i<tempComp.getShapes().size();i++) {
                    String[] shape = tempComp.getElement(i);

                    dragShape(currentX,currentY,shape);
                }
                break;
        }

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
        boolean compStarted = false;

        /**
         * constructor. Doesn't really do anything
         */
        private ShapeComponent(){

        }

        private boolean isCompStarted(){
            return compStarted;
        }

        /**
         * Adds a shape with the given arguments to the list of shapes
         * @param args the properties of the shape
         */
        private void addShape(String[] args){
            shapes.add(args);
            //refreshHistory();
        }

        /**
         * Returns the LinkedList of shapes
         * @return the {@link LinkedList} {@link #shapes} which contains the shapes
         */
        private LinkedList<String[]> getShapes(){
            return shapes;
        }

        private String[] getElement(int index){
            return shapes.get(index);
        }

        private void replaceShape(String[] newShape, int index){
            shapes.remove(index);
            shapes.add(index,newShape);
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
            compStarted = true;
            composites.add(new ShapeComponent());
            shapes.add(new String[]{"composite",Integer.toString(composites.size()-1)});
        }

        /**
         * Adds the currently open composite to the list of shapes. The shape title is "composite" and
         * the property is its index in {@link #composites}.
         */
        private void endComposite(){
            compStarted = false;
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
            refreshHistory();
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
            refreshHistory();
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

                    if (shapesComp.isCompStarted()){
                        shapesComp.addToComposite(new String[]{"line", Integer.toString(prevPolyX), Integer.toString(prevPolyY), Integer.toString(startX),
                                Integer.toString(startY), Integer.toString(currentColour.getRGB())});
                        drawNewLine(prevPolyX, prevPolyY, startX,startY, true);
                        shapesComp.endComposite();
                    }
                    blankSpace();
                    refreshShapes();
                    break;
                case "redo":
                    shapesComp.redo();
                    if (shapesComp.isCompStarted()){
                        shapesComp.addToComposite(new String[]{"line", Integer.toString(prevPolyX), Integer.toString(prevPolyY), Integer.toString(startX),
                                Integer.toString(startY), Integer.toString(currentColour.getRGB())});
                        drawNewLine(prevPolyX, prevPolyY, startX,startY, true);
                        shapesComp.endComposite();
                    }
                    blankSpace();
                    refreshShapes();
                    break;
                case "polygon":
                    if (shapesComp.isCompStarted()){
                        if (startX!=prevPolyX&&startY!=prevPolyX) {
                            shapesComp.endComposite();
                        }
                        blankSpace();
                        refreshShapes();
                    }
                    break;
                case "Move":

                    break;
                default:
                    break;
            }
            if (mode.equals("polygon")){
                infoLbl.setText("<html>Click near the first point to end<br>Click button again to end early");
            } else {
                infoLbl.setText("");
            }
        }
    }

    /**
     * Used to handle mouse click/release events
     */
    public class MouseButtonListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            switch(mode){
                case "polygon":
                    if (!shapesComp.isCompStarted()) {
                        System.out.println("new polygon");
                        shapesComp.startComposite();
                        startX=e.getX();
                        startY=e.getY();
                        prevPolyX=e.getX();
                        prevPolyY=e.getY();
                    }
                    else if(Math.abs(e.getX()-startX)<25&&Math.abs(e.getY()-startY)<25){
                        System.out.println("End polygon");
                        shapesComp.addToComposite(new String[]{"line", Integer.toString(prevPolyX), Integer.toString(prevPolyY), Integer.toString(startX),
                                Integer.toString(startY), Integer.toString(currentColour.getRGB())});
                        drawNewLine(prevPolyX, prevPolyY, startX,startY, true);
                        shapesComp.endComposite();
                    }
                    else {
                        shapesComp.addToComposite(new String[]{"line", Integer.toString(prevPolyX), Integer.toString(prevPolyY), Integer.toString(e.getX()),
                                Integer.toString(e.getY()), Integer.toString(currentColour.getRGB())});
                        prevPolyX = e.getX();
                        prevPolyY = e.getY();
                    }
                    break;
            }
        }

        /**
         * Called when the mouse button is pressed
         * @param e The properties of the pointer.
         */
        @Override
        public void mousePressed(MouseEvent e) {
            if (!mode.equals("polygon")) {
                startX = e.getX();
                startY = e.getY();
                prevx = e.getX();
                prevy = e.getY();
            }
            switch(mode){
                case "draw":
                    //if the current mode is "draw" a new composite shape needs to be started.
                    //lines created while in the draw mode will be added to this composite.
                    shapesComp.startComposite();
                    break;
                default:

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
                    break;
                case "Move":
                    int xOffset = e.getX() - startX;
                    int yOffset = e.getY() - startY;
                    moveSelection(xOffset,yOffset);
                    break;
                default:

                    break;
            }
            if (!mode.equals("Move")){
                refreshHistory();
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
                case "polygon":
                    if (shapesComp.isCompStarted()) {
                        drawNewLine(prevPolyX, prevPolyY, e.getX(), e.getY(), true);
                    }
                    prevx=e.getX();
                    prevy=e.getY();
                    break;
                case "Move":
                    int currentX = e.getX();
                    int currentY = e.getY();
                    for (int i = 0;i<list.getSelectedIndices().length;i++) {
                        String[] newShape = shapesComp.getElement(list.getSelectedIndices()[i]);

                        dragShape(currentX,currentY,newShape);
                    }
                    break;
                default:
                    break;
            }
                prevx = e.getX();
                prevy = e.getY();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            switch(mode){
                case "polygon":
                    if (shapesComp.isCompStarted()) {
                        drawNewLine(prevPolyX, prevPolyY, e.getX(), e.getY(), true);
                    }
                    prevx=e.getX();
                    prevy=e.getY();
                    break;
            }
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
            currentColour = JColorChooser.showDialog(controls,"Choose a Colour",currentColour);
            if (currentColour==null){
                currentColour = Color.black;
            }
            controls.setBorder(BorderFactory.createLineBorder(currentColour));
        }
    }
}
