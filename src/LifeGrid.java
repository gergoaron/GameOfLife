import java.io.*;
import java.util.ArrayList;

public class LifeGrid implements Serializable {

    boolean[][] lifeGrid;

    public LifeGrid(int y, int x) {
        lifeGrid = new boolean[y][x];
    }

    public void setCell(int y, int x, boolean state) {
        lifeGrid[y][x] = state;
    }

    public boolean getCell(int y, int x) {
        return lifeGrid[y][x];
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

    public void nextState(ArrayList<Integer> B, ArrayList<Integer> S) {
        boolean[][] nextGrid = new boolean[lifeGrid.length][lifeGrid[0].length];
        boolean cell;
        int neighbors;

        for(int i = 0; i < lifeGrid.length; i ++) {
            for(int j = 0; j < lifeGrid[0].length; j ++) {
                neighbors = countNeighbors(i, j);
                if(lifeGrid[i][j]) {
                    nextGrid[i][j] = S.contains(neighbors);
                }
                else {
                    if(B.contains(neighbors))
                        nextGrid[i][j] = true;
                    else
                        nextGrid[i][j] = false;
                }
            }
        }
        lifeGrid = nextGrid;
    }
}
