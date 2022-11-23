import javax.swing.table.AbstractTableModel;
import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;

public class SettingsTable extends AbstractTableModel {

    ArrayList<Integer> S = new ArrayList<Integer>(9);
    ArrayList<Integer> B = new ArrayList<Integer>(9);

    String[] cName = {"Rule", "0", "1", "2", "3", "4", "5", "6", "7", "8"};

    public SettingsTable(ArrayList<Integer> born, ArrayList<Integer> survives) {
        B.addAll(born);
        S.addAll(survives);
    }

    public ArrayList<Integer> getB() {
        return B;
    }

    public ArrayList<Integer> getS() {
        return S;
    }

    @Override
    public int getRowCount() {
        return 2;
    }

    @Override
    public int getColumnCount() {
        return cName.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return cName[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex == 0) {
            if(rowIndex == 0)
                return "Born";
            return "Survives";
        }
        if(rowIndex == 0) {
            return B.contains(columnIndex - 1);
        }
        return S.contains(columnIndex - 1);
    }

    @Override
    public Class getColumnClass(int column) {
        switch (column) {
            case 0:
                return String.class;
            default:
                return Boolean.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }

    @Override
    public void setValueAt(Object Value, int rowIndex, int columnIndex) {
        if(columnIndex > 0) {
            if(rowIndex == 0) {
                if(!B.contains(columnIndex - 1))
                    B.add(columnIndex - 1);
                else {
                    B.remove(B.indexOf(columnIndex - 1));
                }
            }
            if(!S.contains(columnIndex - 1))
                S.add(columnIndex - 1);
            else {
                S.remove(S.indexOf(columnIndex - 1));
            }
        }
    }

    public void pushBackValues(ArrayList<Integer> born, ArrayList<Integer> survives) {
        B.clear();
        B.addAll(born);
        S.clear();
        S.addAll(survives);
    }
}
