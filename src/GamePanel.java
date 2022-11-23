import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class GamePanel extends JPanel {

    int xPanel = 800;
    int yPanel = 400;
    int cellSize = 10;

    ArrayList<Integer> B = new ArrayList<Integer>(9);
    ArrayList<Integer> S = new ArrayList<Integer>(9);

    JTable GameTable = new JTable(yPanel / cellSize , xPanel / cellSize);
    LifeGrid lifeGrid = new LifeGrid(yPanel/cellSize, xPanel/cellSize);

    Timer timer;

    public GamePanel() {
        setSize(xPanel, yPanel);
        add(GameTable);
        GameTable.addMouseListener(new CellListener());

        GameTable.setDefaultRenderer(Object.class, new CellRenderer());

        tableInit();
        GameTable.doLayout();

        B.clear();
        S.clear();
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

    public void step() {
        lifeGrid.nextState(B, S);
        GameTable.repaint();
    }


    public class CellListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            int row, col;
            super.mouseClicked(e);
            row = GameTable.getSelectedRow();
            col = GameTable.getSelectedColumn();

            lifeGrid.setCell(row, col, !lifeGrid.getCell(row,col));

            GameTable.repaint();
        }
    }
    


    public class CellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean   isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if(lifeGrid.getCell(row, column)) {
                c.setBackground(Color.gray);
            }
            else
                c.setBackground(Color.BLACK);
            return c;
        }
    }

    public void clear() {
        lifeGrid = new LifeGrid(yPanel / cellSize, xPanel / cellSize);

        GameTable.repaint();
    }

    public void setBS(ArrayList<Integer> born, ArrayList<Integer> survives) {
        B.clear();
        B.addAll(born);

        S.clear();
        S.addAll(survives);
    }


    public void Serialize() throws IOException {
        FileOutputStream fileOut =
                new FileOutputStream("GameState.txt");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);

        out.writeObject(lifeGrid);
        out.close();
        fileOut.close();
    }

    public void Deserialize() throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream("GameState.txt");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        lifeGrid = (LifeGrid) in.readObject();
        in.close();
        fileIn.close();
        GameTable.repaint();
    }

}
