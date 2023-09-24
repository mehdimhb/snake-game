import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame extends JFrame {
    static int highestScore;
    static int applesEaten;
    Boolean cross;

    SnakeGame(Boolean cross){
        super("Snake");

        this.cross = cross;
        add(new GamePanel());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    public class GamePanel extends JPanel implements ActionListener {

        static final int SCREEN_WIDTH = 600;
        static final int SCREEN_HEIGHT = 600;
        static final int UNIT_SIZE = 25;
        static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
        static final int DELAY = 75;

        final int[] x = new int[GAME_UNITS];
        final int[] y = new int[GAME_UNITS];
        int bodyParts = 6;
        int appleX;
        int appleY;
        char direction = 'R';
        Boolean running = false;
        Timer timer;
        Random random;

        GamePanel() {
            random = new Random();
            setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
            setBackground(Color.BLACK);
            setFocusable(true);
            addKeyListener(new MyKeyAdapter());
            startGame();
        }

        public void startGame() {
            applesEaten = 0;
            newApple();
            running = true;
            timer = new Timer(DELAY, this);
            timer.start();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            draw(g);
        }

        public void draw(Graphics g) {
            if (running) {
            /*
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
             */
                //draw apple
                g.setColor(new Color(255, 77, 66));
                g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

                //draw snake
                for (int i = 0; i < bodyParts; i++) {
                    if (i == 0) {
                        g.setColor(new Color(14, 227, 0));
                    } else {
                        g.setColor(new Color(45, 100, 0));
                    }
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }

                //draw score
                g.setColor(new Color(140, 255, 240));
                g.setFont(new Font("Ink Free", Font.BOLD, 40));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());

            } else {
                gameOver(g);
            }
        }

        public void newApple() {
            appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
            appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
        }

        public void move() {
            for (int i = bodyParts; i > 0; i--) {
                x[i] = x[i - 1];
                y[i] = y[i - 1];
            }
            switch (direction) {
                case 'U' -> y[0] -= UNIT_SIZE;
                case 'D' -> y[0] += UNIT_SIZE;
                case 'L' -> x[0] -= UNIT_SIZE;
                case 'R' -> x[0] += UNIT_SIZE;
            }
            if(cross) {
                if (x[0] < 0)
                    x[0] = SCREEN_WIDTH;
                if (x[0] > SCREEN_WIDTH)
                    x[0] = 0;
                if (y[0] < 0)
                    y[0] = SCREEN_HEIGHT;
                if (y[0] > SCREEN_HEIGHT)
                    y[0] = 0;
            }
        }

        public void checkApple() {
            if ((x[0] == appleX) && (y[0] == appleY)) {
                bodyParts++;
                applesEaten++;
                newApple();
            }
        }

        public void checkCollisions() {
            for (int i = bodyParts; i > 0; i--) {
                if ((x[0] == x[i]) && (y[0] == y[i])) {
                    running = false;
                    break;
                }
            }
            if (!cross) {
                if (x[0] < 0)
                    running = false;
                if (x[0] > SCREEN_WIDTH)
                    running = false;
                if (y[0] < 0)
                    running = false;
                if (y[0] > SCREEN_HEIGHT)
                    running = false;
            }

            if (!running)
                timer.stop();
        }

        public void gameOver(Graphics g) {
            g.setColor(new Color(140, 255, 240));
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics1 = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());

            g.setColor(new Color(255, 77, 66));
            g.setFont(new Font("Ink Free", Font.BOLD, 75));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

            new start();
            dispose();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (running) {
                move();
                checkApple();
                checkCollisions();
            }
            repaint();
        }

        public class MyKeyAdapter extends KeyAdapter {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> {
                        if (direction != 'R')
                            direction = 'L';
                    }
                    case KeyEvent.VK_RIGHT -> {
                        if (direction != 'L')
                            direction = 'R';
                    }
                    case KeyEvent.VK_UP -> {
                        if (direction != 'D')
                            direction = 'U';
                    }
                    case KeyEvent.VK_DOWN -> {
                        if (direction != 'U')
                            direction = 'D';
                    }
                }
            }
        }
    }

    public static class start extends JFrame {
        JPanel score;
        JPanel crossPanel;
        JLabel label1;
        JLabel label2;
        JCheckBox crossCheck;
        Boolean cross = false;
        JPanel panel;
        JButton startBtn;
        JButton closeBtn;

        start() {
            super("Snake");
            score = new JPanel();
            crossPanel = new JPanel();
            label1 = new JLabel("Score: " + applesEaten + "   ");
            if (applesEaten > highestScore)
                highestScore = applesEaten;
            label2 = new JLabel("Highest Score: " + highestScore);
            score.add(label1);
            score.add(label2);
            crossCheck = new JCheckBox("Enable Crossing");
            crossCheck.addItemListener(e -> cross = true);
            crossPanel.add(crossCheck);

            panel = new JPanel(new FlowLayout());
            startBtn = new JButton("Start");
            panel.add(startBtn);
            startBtn.addActionListener(e -> {
                new SnakeGame(cross);
                setVisible(false);
            });
            closeBtn = new JButton("Close");
            panel.add(closeBtn);
            closeBtn.addActionListener(e -> {
                setVisible(false);
                System.exit(1);
            });

            setLayout(new BorderLayout());
            add(score, BorderLayout.NORTH);
            add(crossPanel, BorderLayout.CENTER);
            add(panel, BorderLayout.SOUTH);
            setSize(220, 130);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setResizable(false);
            setVisible(true);
            setLocationRelativeTo(null);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(start::new);
    }
}
