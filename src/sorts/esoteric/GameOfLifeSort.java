package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES
IN COLLABORATION WITH NAOAN1201 AND POTZKO

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

What have we done...?

*/
public class GameOfLifeSort extends MadhouseTools {

    int flip = -1;
    int cellsAlive = 1;

    public GameOfLifeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Game of Life");
        this.setRunAllSortsName("Game of Life Sort");
        this.setRunSortName("Game of Life Sort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    protected boolean cellCompare(int j) {
        Reads.addComparison();
        if (j == 0) return true;
        return false;
    }

    protected int checkCells(int[] board, int boardSize, int i) {
        int alive = 8;
        int[] dims = getRectangleDimensions(boardSize);
        boolean left = isValidRectangleAction(dims[0], dims[1], i, i - 1);
        boolean down = isValidRectangleAction(dims[0], dims[1], i, i + dims[0]);
        boolean up = isValidRectangleAction(dims[0], dims[1], i, i - dims[0]);
        boolean right = isValidRectangleAction(dims[0], dims[1], i, i + 1);
        if (left) {if (cellCompare(board[i - 1])) alive--;}
        else alive--;
        if (down && left) {if (cellCompare(board[i + dims[0] - 1])) alive--;}
        else alive--;
        if (down) {if (cellCompare(board[i + dims[0]])) alive--;}
        else alive--;
        if (down && right) {if (cellCompare(board[i + dims[0] + 1])) alive--;}
        else alive--;
        if (right) {if (cellCompare(board[i + 1])) alive--;}
        else alive--;
        if (right && up) {if (cellCompare(board[i - dims[0] + 1])) alive--;}
        else alive--;
        if (up) {if (cellCompare(board[i - dims[0]])) alive--;}
        else alive--;
        if (left && up) {if (cellCompare(board[i - dims[0] - 1])) alive--;}
        else alive--;
        return alive;
    }

    protected void flipState(int[] board, int[] items, int i, int boardSize, boolean toAlive) {
        Writes.write(board, i, toAlive ? boardSize / 2 : 0, 0, true, false);
        if (flip == -1) flip = i;
        else {
            int a = flip;
            int b = i;
            if (a > b) {
                int t = a;
                a = b;
                b = t;
            }
            if (Reads.compareIndices(items, a, b, 0, true) > 0) Writes.swap(items, a, b, 0, true, true);
            flip = -1;
        }
    }

    protected boolean iterate(int[] board, int[] items, int boardSize) {
        cellsAlive = 0;
        int[] temp = Writes.createExternalArray(boardSize);
        Writes.arraycopy(board, 0, temp, 0, boardSize, 0, true, true);
        boolean active = false;
        for (int i = 0; i < boardSize; i++) {
            int cellsAround = checkCells(temp, boardSize, i);
            Highlights.clearAllMarks();
            Highlights.markArray(1, i);
            if (cellCompare(board[i])) {if (cellsAround == 3) flipState(board, items, i, boardSize, active = true);}
            else {
                cellsAlive++;
                if (cellsAround < 2 || cellsAround > 3) {
                    active = true;
                    flipState(board, items, i, boardSize, false);
                }
            }
        }
        Writes.deleteExternalArray(temp);
        return active;
    }

    protected void life(int[] board, int[] items, int boardSize) {
        for (int i = 0; i < boardSize; i++) Writes.write(board, i, randInt(0, 2) * boardSize / 2, 0.1, true, false);
        cellsAlive = 1;
        boolean active = true;
        for (int i = 0; i < boardSize && cellsAlive > 0 && active; i++) {
            active = iterate(board, items, boardSize);
            Delays.sleep(10);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int[] items = Writes.createExternalArray(currentLength);
        Writes.arraycopy(array, 0, items, 0, currentLength, 1, true, true);
        while (!isSorted(items, 0, currentLength)) life(array, items, currentLength);
        Writes.arraycopy(items, 0, array, 0, currentLength, 1, true, false);
        Writes.deleteExternalArray(items);
    }
}