import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.TimerTask;

public class GamePanel extends JPanel {

    int xPanel = 800;
    int yPanel = 400;
    int cellSize = 10;

    ArrayList<Integer> B = new ArrayList<Integer>();
    ArrayList<Integer> S = new ArrayList<Integer>();

    JTable GameTable = new JTable(yPanel / cellSize , xPanel / cellSize);

    boolean[][] lifeGrid = new boolean[yPanel / cellSize][xPanel / cellSize];

    Timer timer;

    public GamePanel() {
        setSize(xPanel, yPanel);
        add(GameTable);
        GameTable.addMouseListener(new CellListener());

        GameTable.setDefaultRenderer(Object.class, new CellRenderer());

        tableInit();
        GameTable.doLayout();

        B.add(3);
        S.add(2);
        S.add(3);
    }

    public void tableInit() {
        DefaultTableModel UneditableTableModel = new DefaultTableModel( yPanel / cellSize, xPanel / cellSize) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        GameTable.setModel(UneditableTableModel);

        GameTable.setSize(xPanel, yPanel);
        GameTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for(int i = 0; i < xPanel / cellSize; i ++) {
            GameTable.getColumnModel().getColumn(i).setMaxWidth(1);
        }

        GameTable.setShowGrid(true);
        GameTable.setCellSelectionEnabled(false);
        GameTable.setGridColor(Color.darkGray);
        GameTable.setBackground(Color.black);
    }

    int countNeighbors(int y, int x) {
        int neighbors = 0;

        //Check left side neighbors
        if(x > 0) {
            if(lifeGrid[y][x - 1]) neighbors ++;
            if(y > 0 && lifeGrid[y - 1][x - 1]) neighbors ++;
            if(y < lifeGrid.length - 1 && lifeGrid[y + 1][x - 1]) neighbors ++;
        }

        //Check right side neighbors
        if(x < lifeGrid[0].length - 1) {
            if(lifeGrid[y][x + 1]) neighbors ++;
            if(y > 0 && lifeGrid[y - 1][x + 1]) neighbors ++;
            if(y < lifeGrid.length - 1 && lifeGrid[y + 1][x + 1]) neighbors ++;
        }

        //Check upper and under neighbor
        if(y > 0 && lifeGrid[y - 1 ][x]) neighbors ++;
        if(y < lifeGrid.length - 1 && lifeGrid[y + 1][x]) neighbors ++;

        return neighbors;
    }

    public void step() {
        lifeGrid = nextState();
        GameTable.repaint();
    }

    public boolean[][] nextState() {
        boolean[][] nextGrid = new boolean[yPanel / cellSize][xPanel / cellSize];
        boolean cell;
        int neighbors;

        for(int i = 0; i < lifeGrid.length; i ++) {
            for(int j = 0; j < lifeGrid[0].length; j ++) {
                neighbors = countNeighbors(i, j);
                if(lifeGrid[i][j]) {
                    if(S.contains(neighbors))
                        nextGrid[i][j] = true;
                    else
                        nextGrid[i][j] = false;
                }
                else {
                    if(B.contains(neighbors))
                        nextGrid[i][j] = true;
                    else
                        nextGrid[i][j] = false;
                }
            }
        }
        return nextGrid;
    }

    class Tick extends TimerTask {
        public void run() {
            step();
        }
    }

    public class CellListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            int row, col;
            super.mouseClicked(e);
            row = GameTable.getSelectedRow();
            col = GameTable.getSelectedColumn();
            System.out.println(lifeGrid[row][col]);
            lifeGrid[row][col] = !lifeGrid[row][col];
            System.out.println(lifeGrid[row][col]);
            GameTable.repaint();
        }
    }
    


    public class CellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean   isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if(lifeGrid[row][column]) {
                c.setBackground(Color.GRAY);
            }
            else
                c.setBackground(Color.BLACK);
            return c;
        }

    }
}
