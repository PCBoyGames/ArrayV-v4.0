package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.HeapSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class ScrollHeapHunterSort extends HeapSorting {
    public ScrollHeapHunterSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Scroll Heap Hunter");
        this.setRunAllSortsName("Scroll Heap Hunter Sort");
        this.setRunSortName("Scroll Heap Hunter Sort");
        this.setCategory("Impractical Sorts");
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
                for (int i = k; i >= j; i--) {
                    Highlights.markArray(3, i + 1);
                    Highlights.markArray(4, j);
                    heapify(array, i, k + 1, 0.005, false);
                }
            }
            j++;
            Highlights.clearAllMarks();
        }
    }
}