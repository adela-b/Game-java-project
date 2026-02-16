package com.mycompany.battle_quest;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Battle_Quest extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private String playerChoice;
    private final Random random = new Random();

    public Battle_Quest() {
        setTitle("⚔ Battle Quest ⚔");
        setSize(700, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createCharacterSelectPanel(), "Select");
        mainPanel.add(createFightPanel(), "Fight");

        add(mainPanel);
        setVisible(true);
    }

    private ImageIcon loadIcon(String resourcePath, int w, int h) {
        java.net.URL url = getClass().getResource(resourcePath);
        if (url == null) {
            System.out.println("NOT FOUND: " + resourcePath);
            return new ImageIcon(); // empty icon so app still runs
        }
        Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private JPanel createCharacterSelectPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(25, 25, 35));

        JLabel title = new JLabel("Choose Your Fighter", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 40));
        title.setForeground(new Color(255, 215, 100));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);

        JPanel charsPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        charsPanel.setBackground(new Color(25, 25, 35));
        charsPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        
        charsPanel.add(createCharacterOption("Alex", "/images/warrior.png", 100, 150));
        charsPanel.add(createCharacterOption("Marvel", "/images/wizard.png", 100, 150));

        panel.add(charsPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCharacterOption(String name, String imgResourcePath, int minPower, int maxPower) {
        JPanel box = new JPanel(new BorderLayout());
        box.setBackground(new Color(40, 40, 55));
        box.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 100), 3, true));

        JLabel img = new JLabel(loadIcon(imgResourcePath, 200, 200), SwingConstants.CENTER);
        box.add(img, BorderLayout.CENTER);

        JLabel label = new JLabel(name, SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 26));
        label.setForeground(Color.WHITE);
        box.add(label, BorderLayout.NORTH);

        JLabel powerLabel = new JLabel("Suggested Power Range: " + minPower + " - " + maxPower, SwingConstants.CENTER);
        powerLabel.setForeground(new Color(255, 215, 100));

        JButton chooseBtn = new JButton("Choose " + name);
        chooseBtn.setFont(new Font("Serif", Font.BOLD, 16));
        chooseBtn.setBackground(new Color(255, 215, 100));
        chooseBtn.setFocusPainted(false);
        chooseBtn.addActionListener(e -> askPlayerPower(name, minPower, maxPower));

        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBackground(new Color(40, 40, 55));
        powerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        chooseBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottom.add(Box.createRigidArea(new Dimension(0, 8)));
        bottom.add(powerLabel);
        bottom.add(Box.createRigidArea(new Dimension(0, 8)));
        bottom.add(chooseBtn);
        bottom.add(Box.createRigidArea(new Dimension(0, 8)));

        box.add(bottom, BorderLayout.SOUTH);

        return box;
    }

    private void askPlayerPower(String choice, int minPower, int maxPower) {
        String input = JOptionPane.showInputDialog(
                this,
                "Enter " + choice + "'s Power (" + minPower + " - " + maxPower + "):",
                "Set Power",
                JOptionPane.PLAIN_MESSAGE
        );

        if (input == null) return;

        try {
            int power = Integer.parseInt(input.trim());
            if (power < minPower || power > maxPower) {
                JOptionPane.showMessageDialog(this, "Power must be between " + minPower + " and " + maxPower + "!");
                return;
            }
            startBattle(choice, power);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number!");
        }
    }

    private JPanel createFightPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(25, 25, 35));

    
        java.net.URL fightUrl = getClass().getResource("/images/fight.gif");
        if (fightUrl == null) {
            System.out.println("NOT FOUND: /images/fight.gif");
        }
        JLabel fightGif = new JLabel(fightUrl == null ? new ImageIcon() : new ImageIcon(fightUrl), SwingConstants.CENTER);
        panel.add(fightGif, BorderLayout.CENTER);

        JLabel getReady = new JLabel("⚡ Battle in Progress ⚡", SwingConstants.CENTER);
        getReady.setFont(new Font("Serif", Font.BOLD, 28));
        getReady.setForeground(new Color(255, 215, 100));
        panel.add(getReady, BorderLayout.NORTH);

        return panel;
    }

    private void startBattle(String choice, int power) {
        Weapon playerWeapon = new Weapon(choice.equals("Alex") ? "Sword" : "Staff");
        Weapon enemyWeapon = new Weapon(choice.equals("Alex") ? "Staff" : "Sword");

        Character player;
        Character enemy;

        if (choice.equals("Alex")) {
            player = new Warrior("Alex", power, playerWeapon);
            enemy = new Wizard("Marvel", 100 + random.nextInt(51), enemyWeapon); // 100-150
        } else {
            player = new Wizard("Marvel", power, playerWeapon);
            enemy = new Warrior("Alex", 100 + random.nextInt(51), enemyWeapon);
        }

        List<Character> battleParticipants = new ArrayList<>();
        battleParticipants.add(player);
        battleParticipants.add(enemy);

        cardLayout.show(mainPanel, "Fight");

        Timer timer = new Timer(3000, e -> {
            for (Character c : battleParticipants) {
                Character target = (c == player) ? enemy : player;
                c.attack(target);
            }
            showWinnerPopup(player, enemy);
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void showWinnerPopup(Character player, Character enemy) {
        String resultText;
        boolean playerWins;

        if (player.getHealth() > enemy.getHealth()) {
            playerWins = true;
            resultText = "🎉 YOU WIN!";
        } else if (player.getHealth() < enemy.getHealth()) {
            playerWins = false;
            resultText = "☠ YOU WERE DEFEATED!";
        } else {
            playerWins = new Random().nextBoolean();
            resultText = "🤝 It's a tie! Random winner decided!";
        }

        Character winner = playerWins ? player : enemy;
        // ✅ winner image from resources
        String winnerImage = (winner instanceof Warrior) ? "/images/warrior.png" : "/images/wizard.png";

        JFrame popup = new JFrame("Battle Result");
        popup.setSize(500, 600);
        popup.setLocationRelativeTo(this);
        popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        popup.setLayout(new BorderLayout());
        popup.getContentPane().setBackground(new Color(25, 25, 35));

        JLabel winnerImg = new JLabel(loadIcon(winnerImage, 250, 250), SwingConstants.CENTER);
        popup.add(winnerImg, BorderLayout.CENTER);

        JLabel result = new JLabel(resultText, SwingConstants.CENTER);
        result.setFont(new Font("Serif", Font.BOLD, 32));
        result.setForeground(new Color(255, 215, 100));
        popup.add(result, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(new Color(25, 25, 35));

        JLabel stats = new JLabel("<html><center>" +
                player.getName() + "'s Health: " + player.getHealth() +
                "<br>" + enemy.getName() + "'s Health: " + enemy.getHealth() +
                "</center></html>", SwingConstants.CENTER);
        stats.setFont(new Font("Serif", Font.PLAIN, 20));
        stats.setForeground(Color.WHITE);
        stats.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(stats);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton playAgain = new JButton("Play Again");
        playAgain.setFont(new Font("Serif", Font.BOLD, 18));
        playAgain.setBackground(new Color(255, 215, 100));
        playAgain.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgain.addActionListener(ev -> {
            popup.dispose();
            restartGame();
        });
        bottomPanel.add(playAgain);

        popup.add(bottomPanel, BorderLayout.SOUTH);
        popup.setVisible(true);
    }

    private void restartGame() {
        playerChoice = null;
        cardLayout.show(mainPanel, "Select");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Battle_Quest::new);
    }
}
