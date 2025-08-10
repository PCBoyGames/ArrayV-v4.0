package sorts.hybrid;

import main.ArrayVisualizer;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class NaBismuthSortNoBuffers extends NaBismuthSort {
    public NaBismuthSortNoBuffers(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Natural Bismuth [WIP] (No Main Buffers)");
        this.setRunAllSortsName("Natural Bismuth Sort (No Main Buffers)");
        this.setRunSortName("Natural Bismuth Sort (No Main Buffers)");
    }

    @Override
    public void runSort(int[] array, int currentLength, int getSize) {
        nabismuthSort(array, 0, currentLength, getSize, false, false, true, 0);
    }
}