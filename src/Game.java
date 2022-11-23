import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Game {
    GamePanel gamePanel;
    JButton startStopButton;
    JButton nextButton;
    JButton clearButton;
    JButton settingsButton;
    JButton backButton;
    JButton saveButton;

    JPanel mainPanel = new JPanel();
    JPanel settingsPanel = new JPanel();

    JPanel cards;

    Timer timer;
    CardLayout cardLayout;

    JFrame GameFrame;
    public Game() {
        //cardLayout = new CardLayout();
        cards = new JPanel(new CardLayout());

        GameFrame = new JFrame("Game Of Life");

        GameFrame.setSize(800, 500);
        GameFrame.setVisible(true);
        GameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        timer = new Timer(100, new TimerListener());
        gamePanel = new GamePanel();

        JButton loadStateButton = new JButton("Load");
        JButton saveStateButton = new JButton("Save");

        startStopButton = new JButton("Start");
        nextButton = new JButton("Next");
        clearButton = new JButton("Clear");
        settingsButton = new JButton("Settings");
        backButton = new JButton("Back");
        saveButton = new JButton("Save");


        saveStateButton.addActionListener(e -> {
            try {
                gamePanel.Serialize();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        loadStateButton.addActionListener(e -> {
            try {
                gamePanel.Deserialize();
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        startStopButton.addActionListener(e -> {
            if(startStopButton.getText().equals("Start")) {
                startStopButton.setText("Stop");
                saveStateButton.setEnabled(false);
                loadStateButton.setEnabled(false);
                nextButton.setEnabled(false);
                clearButton.setEnabled(false);
                settingsButton.setEnabled(false);
                timer.start();
            }
            else {
                startStopButton.setText("Start");
                saveStateButton.setEnabled(true);
                loadStateButton.setEnabled(true);
                nextButton.setEnabled(true);
                clearButton.setEnabled(true);
                settingsButton.setEnabled(true);
                timer.stop();
            }
        });


        nextButton.addActionListener(e -> gamePanel.step());

        clearButton.addActionListener(e -> gamePanel.clear());

        settingsButton.addActionListener(e -> cardLayout.show(cards, "SETTINGS"));





        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(gamePanel, BorderLayout.NORTH);
        buttonPanel.add(saveStateButton);
        buttonPanel.add(loadStateButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(startStopButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(settingsButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);


        cards.add(mainPanel, "MAIN");
        cards.add(settingsPanel, "SETTINGS");
        cardLayout = (CardLayout)cards.getLayout();

        GameFrame.add(cards);
        cardLayout.show(cards, "MAIN");



        settingsPanel.setLayout(new BorderLayout());


        SettingsTable SettingsTableModel = new SettingsTable(gamePanel.B, gamePanel.S);
        JTable settingTable = new JTable(SettingsTableModel);
        settingsPanel.add(new JScrollPane(settingTable));

        saveButton.addActionListener(e -> gamePanel.setBS(SettingsTableModel.getB(), SettingsTableModel.getS()));

        backButton.addActionListener(e -> {
            SettingsTableModel.pushBackValues(gamePanel.B, gamePanel.S);
            cardLayout.show(cards, "MAIN");
        });


        JPanel settingsButtonPanel = new JPanel();
        settingsButtonPanel.setLayout(new FlowLayout());



        settingsButtonPanel.add(backButton);
        settingsButtonPanel.add(saveButton);
        settingsPanel.add(settingsButtonPanel, BorderLayout.SOUTH);


        GameFrame.pack();
    }

    public class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            gamePanel.step();
        }
    }


    public static void main(String[] args) {
        new Game();
    }
}