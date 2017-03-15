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

    public void init(){
        GridBagConstraints c = new GridBagConstraints();
        this.setTitle("my Painter 9001");
        this.setMinimumSize(new Dimension(800,400));
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new FlowLayout());

        draw = new JPanel();
        controls = new JPanel();
        colours = new JPanel();
        shapesComp = new ShapeComponent(new String[]{});
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

        //control Button listeners-----
        lineBtn.addActionListener(new ModeListener());
        rectBtn.addActionListener(new ModeListener());
        ellipseBtn.addActionListener(new ModeListener());
        drawBtn.addActionListener(new ModeListener());
        circleBtn.addActionListener(new ModeListener());
        squareBtn.addActionListener(new ModeListener());
        undoBtn.addActionListener(new ModeListener());
        redoBtn.addActionListener(new ModeListener());

        //ColourButtons---------------
        JButton redButton = new JButton("red");
        JButton blackButton = new JButton("black");
        JButton blueButton = new JButton("blue");
        JButton yellowButton = new JButton("yellow");
        colours.add(redButton);
        colours.add(blackButton);
        colours.add(blueButton);
        colours.add(yellowButton);
        colours.add(selColour);

        //colour button listeners------
        redButton.addActionListener(new ColourButtonListener());
        blackButton.addActionListener(new ColourButtonListener());
        blueButton.addActionListener(new ColourButtonListener());
        yellowButton.addActionListener(new ColourButtonListener());

        draw.addMouseListener(new MouseButtonListener());
        draw.addMouseMotionListener(new MouseMoveListener());
    }

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

    private void drawNewSquare(int newX, int newY){
        Graphics g = draw.getGraphics();
        g.setColor(Color.white);
        g.drawRect(startX,startY,Math.max(prevx-startX,prevy-startY),Math.max(prevx-startX,prevy-startY));

        g.setColor(currentColour);
        g.drawRect(startX,startY,Math.max(newX-startX,newY-startY),Math.max(newX-startX,newY-startY));
        //draw.paintComponents(g);

    }

    private void refreshShapes(){
        Graphics g = draw.getGraphics();
        LinkedList<String[]> shapes = shapesComp.getShapes();
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
                    int[] vals = {Integer.valueOf(shape[1]),Integer.valueOf(shape[2]),Integer.valueOf(shape[3]),Integer.valueOf(shape[4])};
                    g.setColor(new Color(Integer.parseInt(shape[5])));
                    g.drawOval(Math.min(vals[0],vals[2]),Math.min(vals[1],vals[3]),Math.abs(vals[2]-vals[0]), Math.abs(vals[1]-vals[3]));
                    break;

            }
            draw.paintComponents(g);
        }
        draw.paintComponents(g);
    }

    private void blankSpace(){
        Graphics g = draw.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0,0,draw.getWidth(),draw.getHeight());
        draw.paintComponents(g);
    }

    public static void main(String[] args){
        myPaint paint = new myPaint();
        paint.init();
    }

    //CLASSES ------------------------------------------------
    private class ShapeComponent {
        LinkedList<String[]> shapes = new LinkedList<>();
        LinkedList<String[]> undone = new LinkedList<>();
        private ShapeComponent(String[] args){

        }

        private void addShape(String[] args){
            System.out.println("Added "+args[0]);
            shapes.add(args);
        }

        private LinkedList<String[]> getShapes(){
            return shapes;
        }

        public String[] getShape(int index){
            return shapes.get(index);
        }

        private void undo(){
            if (shapes.size()!=0){
                System.out.println("UndoCalled");
                undone.add(shapes.get(shapes.size()-1));
                shapes.remove(shapes.size()-1);
            }
        }

        private void redo(){
            if (undone.size()!=0){
                System.out.println("UndoCalled");
                shapes.add(undone.get(undone.size()-1));
                undone.remove(undone.size()-1);
            }
        }
    }

    public class ModeListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(e.getActionCommand());
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
                case "ellipse":
                    break;
                default:
                    break;
            }
        }
    }

    public class MouseButtonListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {


            switch(mode){
                default:
                    startX = e.getX();
                    startY = e.getY();
                    prevx = e.getX();
                    prevy = e.getY();
                    break;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //System.out.println("Release");
            switch(mode){
                case "line":
                    shapesComp.addShape(new String[]{"line",Integer.toString(startX),Integer.toString(startY),Integer.toString(e.getX()),Integer.toString(e.getY()),Integer.toString(currentColour.getRGB())});
                    System.out.println("new Colour: "+ currentColour.toString());
                    break;
                case "rectangle" :
                    shapesComp.addShape(new String[]{"rectangle",Integer.toString(startX),Integer.toString(startY),Integer.toString(e.getX()),Integer.toString(e.getY()),Integer.toString(currentColour.getRGB())});
                    break;
                case "ellipse" :
                    shapesComp.addShape(new String[]{"ellipse",Integer.toString(startX),Integer.toString(startY), Integer.toString(e.getX()),Integer.toString(e.getY()),Integer.toString(currentColour.getRGB())});
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

    public class MouseMoveListener implements MouseMotionListener {

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
                    drawNewLine(prevx,prevy,e.getX(), e.getY(),false);
                    break;
                case "square":
                    drawNewSquare(e.getX(),e.getY());
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

    public class ColourButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(e.getActionCommand());
            switch(e.getActionCommand()){
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
