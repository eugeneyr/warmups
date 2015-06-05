package org.lynxnet.life.gui;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Eugene
 * Date: 17/10/2006
 * Time: 23:12:09
 * To change this template use File | Settings | File Templates.
 */
public class LifeApp {

    public static void main(String [] as) {
        SwingUtilities.invokeLater(new LifeAppThread());
    }
}

class LifeAppThread implements Runnable {
    public void run() {
        buildGui();
    }

    void buildGui() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("The Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Life placeholder");
        frame.getContentPane().add(label);

        frame.pack();
        frame.setVisible(true);
    }
}
