import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Time;

public class GameFrame extends JFrame{
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
    public GameFrame() {
        //cardLayout = new CardLayout();
        cards = new JPanel(new CardLayout());

        setSize(800, 500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        timer = new Timer(100, new TimerListener());
        gamePanel = new GamePanel();


        startStopButton = new JButton("Start");
        nextButton = new JButton("Next");
        clearButton = new JButton("Clear");
        settingsButton = new JButton("Settings");
        backButton = new JButton("Back");
        saveButton = new JButton("Save");

        startStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(startStopButton.getText().equals("Start")) {
                    startStopButton.setText("Stop");
                    nextButton.setEnabled(false);
                    clearButton.setEnabled(false);
                    settingsButton.setEnabled(false);
                    timer.start();
                }
                else {
                    startStopButton.setText("Start");
                    nextButton.setEnabled(true);
                    clearButton.setEnabled(true);
                    settingsButton.setEnabled(true);
                    timer.stop();
                }
            }
        });


        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.step();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.clear();
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "SETTINGS");
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cards, "MAIN");
            }
        });



        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(gamePanel, BorderLayout.NORTH);
        buttonPanel.add(clearButton);
        buttonPanel.add(startStopButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(settingsButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);


        cards.add(mainPanel, "MAIN");
        cards.add(settingsPanel, "SETTINGS");
        cardLayout = (CardLayout)cards.getLayout();

        add(cards);
        cardLayout.show(cards, "MAIN");



        settingsPanel.setLayout(new BorderLayout());


        /*JLabel bLabel = new JLabel("Born:");
        JLabel sLabel = new JLabel("Survives:");
        settingsPanel.add(bLabel, BorderLayout.NORTH);
        settingsPanel.add(sLabel, BorderLayout.NORTH);*/


        SettingsTable data = new SettingsTable(gamePanel.B, gamePanel.S);
        JTable settingTable = new JTable(data);
        settingsPanel.add(settingTable, BorderLayout.NORTH);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.setBS(data.getB(), data.getS());
            }
        });


        JPanel settingsButtonPanel = new JPanel();
        settingsButtonPanel.setLayout(new FlowLayout());



        settingsButtonPanel.add(backButton);
        settingsButtonPanel.add(saveButton);
        settingsPanel.add(settingsButtonPanel, BorderLayout.SOUTH);


        pack();
    }

    public class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            gamePanel.step();
        }
    }


    public static void main(String[] args) {
        new GameFrame();
    }
}