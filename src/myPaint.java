import javax.swing.*;
import java.applet.Applet;
import java.awt.*;

/**
 * Created by andyk on 2017-03-14.
 */
public class myPaint extends JFrame {
    JPanel draw;
    JPanel controls;
    String mode="none";
    JLabel modeLbl;

    public myPaint(){
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
        this.add(draw,c);
        this.add(controls,c);

        //Add The button
        modeLbl = new JLabel("Select mode");
        controls.add(modeLbl);
        controls.add(new JButton("Line"));
        controls.add(new JButton("Square"));
    }

    public static void main(String[] args){
        myPaint paint = new myPaint();
//        paint.setVisible(true);
    }
}
