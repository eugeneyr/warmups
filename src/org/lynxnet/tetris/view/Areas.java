package org.lynxnet.tetris.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import javax.swing.*;


/**
 * The Areas class demonstrates the CAG (Constructive Area Geometry)
 * operations: Add(union), Subtract, Intersect, and ExclusiveOR.
 */
public class Areas extends JApplet {

    public void init() {
        Demo demo = new Demo();
        getContentPane().add(demo);
        getContentPane().add("North", new DemoControls(demo));
    }



    /**
     * The Demo class performs the CAG operations and renders the shapes.
     */
    public class Demo extends JPanel {

        public String areaType = "nop";

        public Demo() {
            setBackground(Color.white);
        }


        public void drawDemo(int w, int h, Graphics2D g2) {
            GeneralPath p1 = new GeneralPath();

            // draws the polygon on the left side
            p1.moveTo( w * .25f, 0.0f);
            p1.lineTo( w * .75f, h * .5f);
            p1.lineTo( w * .25f, (float) h);
            p1.lineTo( 0.0f, h * .5f);
            p1.closePath();

            GeneralPath p2 = new GeneralPath();

            // draws the polygon on the right side
            p2.moveTo( w * .75f, 0.0f);
            p2.lineTo( (float) w, h * .5f);
            p2.lineTo( w * .75f, (float) h);
            p2.lineTo( w * .25f, h * .5f);
            p2.closePath();

            // creates an area object with the first path
            Area area = new Area(p1);
            g2.setColor(Color.yellow);

            /*
             * fills both paths if 'nop' is selected; otherwise, creates
             * an Area object with p2 and performs the selected CAG
             * operation with the two Area objects
             */
            if (areaType.equals("nop")) {
                g2.fill(p1);
                g2.fill(p2);
                g2.setColor(Color.red);
                g2.draw(p1);
                g2.draw(p2);
                return;
            } else if (areaType.equals("add")) {
                area.add(new Area(p2));
            } else if (areaType.equals("sub")) {
                area.subtract(new Area(p2));
            } else if (areaType.equals("xor")) {
                area.exclusiveOr(new Area(p2));
            } else if (areaType.equals("int")) {
                area.intersect(new Area(p2));
            } else if (areaType.equals("pear")) {

                double sx = w/100;
                double sy = h/140;
                g2.scale(sx, sy);
                double x = w/sx/2;
                double y = h/sy/2;

                /*
                 * creates the first leaf by filling the intersection of
                 * two Area objects created from an ellipse.
                 */
                Ellipse2D leaf = new Ellipse2D.Double(x-16, y-29, 15.0, 15.0);
                Area leaf1 = new Area(leaf);
                leaf.setFrame(x-14, y-47, 30.0, 30.0);
                Area leaf2 = new Area(leaf);
                leaf1.intersect(leaf2);
                g2.setColor(Color.green);
                g2.fill(leaf1);

                // creates the second leaf.
                leaf.setFrame(x+1, y-29, 15.0, 15.0);
                leaf1 = new Area(leaf);
                leaf2.intersect(leaf1);
                g2.fill(leaf2);

                /*
                 * creates the stem by filling the Area resulting from the
                 * subtraction of two Area objects created from an
                 * ellipse.
                 */
                Ellipse2D stem = new Ellipse2D.Double(x, y-42, 40.0, 40.0);
                Area st1 = new Area(stem);
                stem.setFrame(x+3, y-47, 50.0, 50.0);
                st1.subtract(new Area(stem));
                g2.setColor(Color.black);
                g2.fill(st1);

                /*
                 * creates the pear itself by filling the Area resulting
                 * from the union of two Area objects created by two
                 * different ellipses.
                 */
                Ellipse2D circle = new Ellipse2D.Double(x-25, y, 50.0, 50.0);
                Ellipse2D oval = new Ellipse2D.Double(x-19, y-20, 40.0, 70.0);
                Area circ = new Area(circle);
                circ.add(new Area(oval));

                g2.setColor(Color.yellow);
                g2.fill(circ);
                return;
            }

            g2.fill(area);
            g2.setColor(Color.red);
            g2.draw(area);
        }


        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            Dimension d = getSize();
            g2.setBackground(getBackground());
            g2.clearRect(0, 0, d.width, d.height);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
            drawDemo(d.width, d.height, g2);
        }
    } // End Demo class



    /**
     * The DemoControls class provides buttons for choosing
     * CAG operations as well as no CAG operations and multiple
     * CAG operations (pear).
     */
    static class DemoControls extends JPanel implements ActionListener {

        Demo demo;
        JToolBar toolbar;
        JComboBox combo;

        public DemoControls(Demo demo) {
            this.demo = demo;
            setBackground(Color.gray);
            add(toolbar = new JToolBar());
            toolbar.setFloatable(false);
            addTool("nop", "no area operation", true);
            addTool("add", "add", false);
            addTool("sub", "subtract", false);
            addTool("xor", "exclusiveOr", false);
            addTool("int", "intersection", false);
            addTool("pear", "pear", false);
        }


        public void addTool(String str, String tooltip, boolean state) {
            JButton b = (JButton) toolbar.add(new JButton(str));
            b.setBackground(state ? Color.green : Color.lightGray);
            b.setToolTipText(tooltip);
            b.setSelected(state);
            b.addActionListener(this);
        }


        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < toolbar.getComponentCount(); i++) {
                JButton b = (JButton) toolbar.getComponentAtIndex(i);
                b.setBackground(Color.lightGray);
            }
            JButton b = (JButton) e.getSource();
            b.setBackground(Color.green);
            demo.areaType = b.getText();
            demo.repaint();
        }
    } // End DemoControls class



    public static void main(String argv[]) {
        final Areas demo = new Areas();
        demo.init();
        Frame f = new Frame("Java 2D(TM) Demo - Areas");
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        f.add(demo);
        f.pack();
        f.setSize(new Dimension(400,300));
        f.show();
    }
}