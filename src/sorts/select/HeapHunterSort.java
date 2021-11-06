package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.HeapSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class HeapHunterSort extends HeapSorting {
    public HeapHunterSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Heap Hunter");
        this.setRunAllSortsName("Heap Hunter Sort");
        this.setRunSortName("Heap Hunter Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int j = 0;
        while (j < currentLength) {
            int k = currentLength - 1;
            while (Reads.compareIndices(array, j, k, 0.005, true) <= 0 && k > j) k--;
            if (k != j) {
                Highlights.markArray(3, j);
                heapify(array, j, k + 1, 0.1, false);
            }
            j++;
            Highlights.clearAllMarks();
        }
    }
}