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

    Timer timer;
    public GameFrame() {

        setSize(800, 500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        timer = new Timer(100, new TimerListener());
        gamePanel = new GamePanel();


        startStopButton = new JButton("Start");
        nextButton = new JButton("Next");

        startStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(startStopButton.getText().equals("Start")) {
                    startStopButton.setText("Stop");
                    nextButton.setEnabled(false);
                    timer.start();
                }
                else {
                    startStopButton.setText("Start");
                    nextButton.setEnabled(true);
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


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        add(gamePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.add(startStopButton);
        buttonPanel.add(nextButton);




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