package lifesim;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import javax.swing.*;
import java.util.Random;

public class ClickerWork {

    private int score = 0;
    private static final int reward = 5;
    private static final int totalTime = 20; 
    private int timeLeft = totalTime;
    private static final int penalty = 1; 
    
    private static RoundButton redButton;
    private Player player;
    public void start(Player player) {
        this.player = player;
        
        if (player.getStamina() < 50) {
            System.out.println("\nYou are too tired to work. You need at least 30 stamina.");
            return;
        }

        player.decreaseStamina((short) 50);
        System.out.println("You spent 50 stamina to work!"
                + "Now you have " + player.getStamina() + " stamina");
        
        createGUI();
    }
    private void createGUI() {
        JFrame frame = new JFrame("3 green circles");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(null);
        frame.setVisible(true);

        Random rand = new Random();

        // Score and Time panels
        JLabel infoLabel = new JLabel("Score: 0   Time: 20");
        infoLabel.setBounds(10, 10, 300, 30);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(infoLabel);

        RoundButton[] buttons = new RoundButton[3];

        // green buttons
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new RoundButton("Press me");
            buttons[i].setSize(100, 100);
            buttons[i].setBackground(Color.GREEN);
            buttons[i].setForeground(Color.WHITE);
            frame.add(buttons[i]);
            moveToRandomPosition(buttons[i], frame, rand);

            buttons[i].addActionListener(e -> {
                if (timeLeft <= 0) return;

                score += reward;
                infoLabel.setText("Score: " + score + "   Time: " + timeLeft);

                for (RoundButton b : buttons) {
                    b.setVisible(false);
                }

                redButton.setVisible(false);

                relocateAllButtons(buttons, frame, rand);

            });
        }

        redButton = new RoundButton("Penalty");
        redButton.setSize(100, 100);
        redButton.setBackground(Color.RED);
        redButton.setForeground(Color.WHITE);
        redButton.setBounds(250, 250, 100, 100); // reb button position
        frame.add(redButton);

        redButton.addActionListener(e -> {
            if (timeLeft > 0) {
                timeLeft -= penalty;
                if (timeLeft < 0) timeLeft = 0;
                infoLabel.setText("Score: " + score + "   Time: " + timeLeft);

                for (RoundButton b : buttons) {
                    b.setVisible(false);
                }
                redButton.setVisible(false);

                relocateAllButtons(buttons, frame, rand);

            }
        });

        Timer countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                infoLabel.setText("Score: " + score + "   Time: " + timeLeft);
                if (timeLeft <= 0) {
                    ((Timer) e.getSource()).stop();
                    
                    player.earnMoney(score);
                    
                    JOptionPane.showMessageDialog(frame, "Time is out!\nYou earned: " + score + " EUR");
                    frame.dispose();
                }
            }
        });
        countdownTimer.start();
    }

    static void moveToRandomPosition(JButton button, JFrame frame, Random rand) {
        int maxX = frame.getWidth() - button.getWidth() - 16;
        int maxY = frame.getHeight() - button.getHeight() - 39;
        int x = rand.nextInt(Math.max(1, maxX));
        int y = rand.nextInt(Math.max(1, maxY));
        button.setLocation(x, y);
    }
    
    private void relocateAllButtons(RoundButton[] greenButtons, JFrame frame, Random rand) {
        for (RoundButton b : greenButtons) {
            moveToRandomPosition(b, frame, rand);
            b.setVisible(true);
        }
        moveToRandomPosition(redButton, frame, rand);
        redButton.setVisible(true);
    }
    
    
    
}

class RoundButton extends JButton {

    public RoundButton(String label) {
        super(label);
        setOpaque(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape circle = new Ellipse2D.Float(0, 0, getWidth(), getHeight());

        if (getModel().isArmed()) {
            g2.setColor(Color.LIGHT_GRAY);
        } else {
            g2.setColor(getBackground());
        }

        g2.fill(circle);

        FontMetrics fm = g2.getFontMetrics();
        Rectangle r = getBounds();
        int x = (r.width - fm.stringWidth(getText())) / 2;
        int y = (r.height + fm.getAscent() - fm.getDescent()) / 2;
        g2.setColor(getForeground());
        g2.drawString(getText(), x, y);

        g2.dispose();
    }

    @Override
    public boolean contains(int x, int y) {
        Ellipse2D circle = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
        return circle.contains(x, y);
    }
    
    
}
