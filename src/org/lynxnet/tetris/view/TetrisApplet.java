package org.lynxnet.tetris.view;

import org.lynxnet.tetris.controller.Tetroller;
import org.lynxnet.tetris.model.Cell;
import org.lynxnet.tetris.model.Piece;
import org.lynxnet.tetris.model.PieceType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;

public class TetrisApplet extends JApplet {

    private static final int GAP_WIDTH = 4;
    private static final int SQUARE_SIDE = 20;
    private static final int OFFSET_X = 20;
    private static final int OFFSET_Y = 20;

    public void init() {
        TetroPanel tetroPanel = new TetroPanel();
        getContentPane().add(tetroPanel);
        Controls controls = new Controls(tetroPanel);
        getContentPane().add("North", controls);
        addKeyListener(tetroPanel);
        controls.addKeyListener(tetroPanel);
        tetroPanel.addKeyListener(tetroPanel);
        getContentPane().addKeyListener(tetroPanel);
        setFocusable(true);
        tetroPanel.start();
    }

    public class TetroPanel extends JPanel implements KeyListener {
        Tetroller tetroller;
        Runner runner;
        boolean active = true;

        public String command = "start";

        public TetroPanel() {
            tetroller = new Tetroller(10, 24);
            setBackground(Color.gray);
        }

        public synchronized void start() {
            active = false;
            runner = new Runner(this);
            active = true;
            new Thread(runner).start();
        }

        public synchronized void stop() {
            active = false;
        }

        public Color getBoardCellColor(int x, int y) {
            PieceType type = tetroller.getBoard().getCellFilling(x, y);
            return getColor(type);
        }

        private Color getColor(PieceType type) {
            if (type == null) {
                return Color.black;
            }
            switch (type) {
                case L:
                    return Color.blue;
                case R:
                    return Color.red;
                case T:
                    return Color.green;
                case I:
                    return Color.magenta;
                case O:
                    return Color.cyan;
                case S:
                    return Color.orange;
                case Z:
                    return Color.yellow;
            }
            return Color.pink;
        }

        public synchronized void moveRight() {
            tetroller.moveRight();
        }

        public synchronized void moveLeft() {
            tetroller.moveLeft();
        }

        public void drawBoardCell(int x, int y, Graphics2D g2, Color color) {
            GeneralPath p1 = new GeneralPath();
            p1.moveTo(OFFSET_X + x * (GAP_WIDTH + SQUARE_SIDE),
                    OFFSET_Y + y * (GAP_WIDTH + SQUARE_SIDE));
            p1.lineTo(OFFSET_X + x * (GAP_WIDTH + SQUARE_SIDE) + SQUARE_SIDE,
                    OFFSET_Y + y * (GAP_WIDTH + SQUARE_SIDE));
            p1.lineTo(OFFSET_X + x * (GAP_WIDTH + SQUARE_SIDE) + SQUARE_SIDE,
                    OFFSET_Y + y * (GAP_WIDTH + SQUARE_SIDE) + SQUARE_SIDE);
            p1.lineTo(OFFSET_X + x * (GAP_WIDTH + SQUARE_SIDE),
                    OFFSET_Y + y * (GAP_WIDTH + SQUARE_SIDE) + SQUARE_SIDE);
            p1.closePath();

            // creates an area object with the first path
            Area area = new Area(p1);
            g2.setColor(color);
            g2.fill(p1);
        }

        public void drawBoard(int w, int h, Graphics2D g2) {
            for (int x = tetroller.getBoard().getLeftTop().getX(); x <= tetroller.getBoard().getRightBottom().getX(); x++) {
                for (int y = tetroller.getBoard().getLeftTop().getY(); y <= tetroller.getBoard().getRightBottom().getY(); y++) {
                    drawBoardCell(x, y, g2, getBoardCellColor(x, y));
                }
            }
        }

        public Color getCellColor(Piece piece, int x, int y) {
            Cell topLeftCorner = Piece.findLeftTopCorner(piece.getCells());
            int deltaX = -topLeftCorner.getX();
            int deltaY = -topLeftCorner.getY();
            for (Cell cell : piece.getCells()) {
                if (cell.getX() == x - deltaX && cell.getY() == y - deltaY) {
                    return getColor(piece.getPieceType());
                }
            }
            return Color.black;
        }

        public void drawNextPiece(int w, int h, Graphics2D g2) {
            if (tetroller.getNextPiece() != null) {
                for (int x = 0; x < 4; x++) {
                    for (int y = 0; y < 4; y++) {
                        drawBoardCell(
                                x + tetroller.getBoard().getRightBottom().getX() + 4,
                                y + tetroller.getBoard().getLeftTop().getY(), g2, getCellColor(tetroller.getNextPiece(), x, y));
                    }
                }
            } else {
                for (int x = 0; x < 4; x++) {
                    for (int y = 0; y < 4; y++) {
                        drawBoardCell(
                                x + tetroller.getBoard().getRightBottom().getX() + 4,
                                y + tetroller.getBoard().getLeftTop().getY(), g2, Color.black);
                    }
                }
            }
        }

        public void drawScore(int w, int h, Graphics2D g2) {
            FontRenderContext frc = g2.getFontRenderContext();
            Font f = new Font("Helvetica", Font.PLAIN, 16);
            TextLayout tl = new TextLayout("Score: ", f, frc);
            g2.setColor(Color.white);
            tl.draw(g2,
                    OFFSET_X + (tetroller.getBoard().getRightBottom().getX() + 4) * (GAP_WIDTH + SQUARE_SIDE),
                    OFFSET_Y + (tetroller.getBoard().getLeftTop().getY() + 6) * (GAP_WIDTH + SQUARE_SIDE));
            tl = new TextLayout("Lines: ", f, frc);
            g2.setColor(Color.white);
            tl.draw(g2,
                    OFFSET_X + (tetroller.getBoard().getRightBottom().getX() + 4) * (GAP_WIDTH + SQUARE_SIDE),
                    OFFSET_Y + (tetroller.getBoard().getLeftTop().getY() + 7) * (GAP_WIDTH + SQUARE_SIDE));

            f = new Font("Helvetica", Font.BOLD, 18);
            tl = new TextLayout(Integer.toString(tetroller.getBoard().getScore()), f, frc);
            g2.setColor(Color.white);
            tl.draw(g2,
                    OFFSET_X + (tetroller.getBoard().getRightBottom().getX() + 6) * (GAP_WIDTH + SQUARE_SIDE),
                    OFFSET_Y + (tetroller.getBoard().getLeftTop().getY() + 6) * (GAP_WIDTH + SQUARE_SIDE));
            tl = new TextLayout(Integer.toString(tetroller.getBoard().getRemovedRows()), f, frc);
            g2.setColor(Color.white);
            tl.draw(g2,
                    OFFSET_X + (tetroller.getBoard().getRightBottom().getX() + 6) * (GAP_WIDTH + SQUARE_SIDE),
                    OFFSET_Y + (tetroller.getBoard().getLeftTop().getY() + 7) * (GAP_WIDTH + SQUARE_SIDE));

            if (tetroller.isGameEnded()) {
                f = new Font("Helvetica", Font.BOLD, 20);
                tl = new TextLayout("GAME OVER", f, frc);
                g2.setColor(Color.RED);
                tl.draw(g2,
                        OFFSET_X + (tetroller.getBoard().getRightBottom().getX() + 4) * (GAP_WIDTH + SQUARE_SIDE),
                        OFFSET_Y + (tetroller.getBoard().getLeftTop().getY() + 9) * (GAP_WIDTH + SQUARE_SIDE));
            }
        }

        public synchronized void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            Dimension d = getSize();
            g2.setBackground(getBackground());
            g2.clearRect(0, 0, d.width, d.height);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            drawBoard(d.width, d.height, g2);
            drawNextPiece(d.width, d.height, g2);
            drawScore(d.width, d.height, g2);
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int id = e.getID();
            if (id != KeyEvent.KEY_TYPED) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_LEFT:
                        synchronized (this) {
                            tetroller.moveLeft();
                        }
                        repaint();
                        break;
                    case KeyEvent.VK_RIGHT:
                        synchronized (this) {
                            tetroller.moveRight();
                        }
                        repaint();
                        break;
                    case KeyEvent.VK_UP:
                        synchronized (this) {
                            tetroller.rotateLeft();
                        }
                        repaint();
                        break;
                    case KeyEvent.VK_DOWN:
                        synchronized (this) {
                            tetroller.moveDown();
                        }
                        repaint();
                        break;
                    case KeyEvent.VK_PAGE_DOWN:
                        synchronized (this) {
                            tetroller.rotateRight();
                        }
                        repaint();
                        break;
                    case KeyEvent.VK_PAGE_UP:
                        synchronized (this) {
                            tetroller.rotateLeft();
                        }
                        repaint();
                        break;
                    case KeyEvent.VK_SPACE:
                        synchronized (this) {
                            tetroller.drop();
                        }
                        repaint();
                        break;
                }
            } else {
                char c = e.getKeyChar();
                if (c == ' ') {
                    synchronized (this) {
                        tetroller.drop();
                    }
                    repaint();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        public synchronized void reset() {
            stop();
            tetroller = new Tetroller(10, 24);
            start();
        }

        public synchronized void tick() {
            if (!tetroller.isGameEnded()) {
                tetroller.tick();
            }
            repaint();
        }

        public Tetroller getTetroller() {
            return tetroller;
        }

        public void setTetroller(Tetroller tetroller) {
            this.tetroller = tetroller;
        }

        public Runner getRunner() {
            return runner;
        }

        public void setRunner(Runner runner) {
            this.runner = runner;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }
    }

    static class Runner implements Runnable {
        private TetroPanel tetroPanel;

        public Runner(TetroPanel tetroPanel) {
            this.tetroPanel = tetroPanel;
        }

        @Override
        public void run() {
            while (tetroPanel.isActive()) {
                synchronized (tetroPanel) {
                    if (tetroPanel.tetroller != null && !tetroPanel.tetroller.isGameEnded()) {
                        tetroPanel.tick();
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * The DemoControls class provides buttons for choosing
     * CAG operations as well as no CAG operations and multiple
     * CAG operations (pear).
     */
    static class Controls extends JPanel implements ActionListener {
        TetroPanel tetroPanel;
        JToolBar toolbar;
        JComboBox combo;

        public Controls(TetroPanel tetroPanel) {
            this.tetroPanel = tetroPanel;
            setBackground(Color.gray);
            add(toolbar = new JToolBar());
            toolbar.setFloatable(false);
            toolbar.setFocusable(false);
            addTool("start", "Start a new game", true);
//            addTool("next", "next", false);
//            addTool("<", "left", false);
//            addTool(">", "right", false);
//            addTool("V", "down", false);
//            addTool("rl", "rotate left", false);
//            addTool("rr", "rotate right", false);
        }

        public void addTool(String str, String tooltip, boolean state) {
            JButton b = (JButton) toolbar.add(new JButton(str));
            b.setBackground(state ? Color.green : Color.lightGray);
            b.setToolTipText(tooltip);
            b.setSelected(state);
            b.setFocusable(false);
            b.addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < toolbar.getComponentCount(); i++) {
                JButton b = (JButton) toolbar.getComponentAtIndex(i);
                b.setBackground(Color.lightGray);
            }
            JButton b = (JButton) e.getSource();
            b.setBackground(Color.green);
            tetroPanel.command = b.getText();
            if (tetroPanel.command.equals("start")) {
                tetroPanel.reset();
            }
//            else if (tetroPanel.command.equals(">")) {
//                tetroPanel.tetroller.moveRight();
//            } else if (tetroPanel.command.equals("<")) {
//                tetroPanel.tetroller.moveLeft();
//            } else if (tetroPanel.command.equals("V")) {
//                tetroPanel.tetroller.moveDown();
//            } else if (tetroPanel.command.equals("rl")) {
//                tetroPanel.tetroller.rotateLeft();
//            } else if (tetroPanel.command.equals("rr")) {
//                tetroPanel.tetroller.rotateRight();
//            } else if (tetroPanel.command.equals("next")) {
//                tetroPanel.tick();
//            }
            tetroPanel.repaint();
        }
    }

    public static void main(String argv[]) {
        final TetrisApplet demo = new TetrisApplet();
        demo.init();
        Frame f = new Frame("Tetravesty");
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.add(demo);
        f.pack();
        f.setSize(new Dimension(500, 720));
        f.show();
    }
}