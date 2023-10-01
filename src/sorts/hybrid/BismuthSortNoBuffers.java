package sorts.hybrid;

import main.ArrayVisualizer;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class BismuthSortNoBuffers extends BismuthSort {
    public BismuthSortNoBuffers(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Bismuth [WIP] (No Buffers)");
        this.setRunAllSortsName("Bismuth Sort (No Buffers)");
        this.setRunSortName("Bismuth Sort (No Buffers)");
    }

    @Override
    public void runSort(int[] array, int currentLength, int getSize) {
        bismuthSort(array, 0, currentLength, getSize, false);
    }
}