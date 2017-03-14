import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by andyk on 2017-03-14.
 */
public class myPaint extends JFrame {
    private JPanel draw;
    private JPanel controls;
    private String mode="none";
    private JLabel modeLbl;
    private int prevx,prevy,startX,startY = 0;

    public void init(){
        GridBagConstraints c = new GridBagConstraints();
        this.setTitle("my Painter 9001");
        this.setMinimumSize(new Dimension(600,400));
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new FlowLayout());

        draw = new JPanel();
        controls = new JPanel();
        draw.setPreferredSize(new Dimension(375,360));
        controls.setPreferredSize(new Dimension(200,360));
        draw.setBorder(BorderFactory.createLineBorder(Color.black));
        controls.setBorder(BorderFactory.createLineBorder(Color.red));
        draw.setBackground(Color.white);
        this.add(draw,c);
        this.add(controls,c);

        //Add The button
        modeLbl = new JLabel("Select mode");
        controls.add(modeLbl);
        //create and add buttons
        JButton lineBtn = new JButton("line");
        JButton squareBtn = new JButton("rectangle");
        JButton ellipseBtn = new JButton("ellipse");
        JButton drawBtn = new JButton("draw");
        controls.add(lineBtn);
        controls.add(squareBtn);
        controls.add(ellipseBtn);
        controls.add(drawBtn);

        //add listeners
        lineBtn.addActionListener(new modeListener());
        squareBtn.addActionListener(new modeListener());
        ellipseBtn.addActionListener(new modeListener());
        drawBtn.addActionListener(new modeListener());

        draw.addMouseListener(new mouseButtonListener());
        draw.addMouseMotionListener(new mouseMoveListener());
    }

    private void drawNewLine(int stx, int sty,int newX, int newY, boolean erase){
        Graphics g = draw.getGraphics();
        if (erase) {
            g.setColor(Color.white);
            g.drawLine(stx, sty, prevx, prevy);
        }
        g.setColor(Color.black);
        g.drawLine(stx,sty,newX,newY);
        draw.paintComponents(g);
    }

    private void drawNewRect(int newX, int newY){
        int tw,th,tx,ty;
        Graphics g = draw.getGraphics();
        g.setColor(Color.white);
        tw = Math.abs(prevx-startX);
        th = Math.abs(prevy-startY);
        tx = Math.min(startX,prevx);
        ty = Math.min(startY,prevy);
        g.drawRect(tx,ty,tw,th);
        g.setColor(Color.black);
        tw = Math.abs(newX-startX);
        th = Math.abs(newY-startY);
        tx = Math.min(startX,newX);
        ty = Math.min(startY,newY);
        g.drawRect(tx,ty,tw,th);
        draw.paintComponents(g);
    }

    private void drawNewEllipse(int newX, int newY){
        int tw,th,tx,ty;
        Graphics g = draw.getGraphics();
        g.setColor(Color.white);
        tw = Math.abs(prevx-startX);
        th = Math.abs(prevy-startY);
        tx = Math.min(startX,prevx);
        ty = Math.min(startY,prevy);
        g.drawOval(tx,ty,tw,th);
        g.setColor(Color.black);
        tw = Math.abs(newX-startX);
        th = Math.abs(newY-startY);
        tx = Math.min(startX,newX);
        ty = Math.min(startY,newY);
        g.drawOval(tx,ty,tw,th);
        draw.paintComponents(g);
    }

    public static void main(String[] args){
        myPaint paint = new myPaint();
        paint.init();
    }

    //CLASSES ------------------------------------------------
    public class modeListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(e.getActionCommand());
            mode = e.getActionCommand();
            modeLbl.setText("Mode: "+mode);
            switch(mode){
                case "line":
                    //drawLine();
                    break;
                case "rectangle":
                    break;
                case "ellipse":
                    break;
                default:
                    break;
            }
        }
    }

    public class mouseButtonListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {

            switch(mode){
                case "line":
                    startX = e.getX();
                    startY = e.getY();
                    prevx = e.getX();
                    prevy = e.getY();
                    break;
                case "rectangle":
                    startX = e.getX();
                    startY = e.getY();
                    prevx = e.getX();
                    prevy = e.getY();
                    break;
                case "ellipse":
                    startX = e.getX();
                    startY = e.getY();
                    prevx = e.getX();
                    prevy = e.getY();
                    break;
                case "draw":
                    startX = e.getX();
                    startY = e.getY();
                    prevx = e.getX();
                    prevy = e.getY();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public class mouseMoveListener implements MouseMotionListener {

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
}
