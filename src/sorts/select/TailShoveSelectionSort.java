package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

+---------------------------+
| SORTING ALGORITHM SCARLET |
+---------------------------+
|    A sorting algorithm    |
|    studio by Flanlaina    |
|    (a.k.a Ayako-chan)     |
+---------------------------+

 */

/**
 * @author Flanlaina
 *
 */
public class TailShoveSelectionSort extends Sort {
    public TailShoveSelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Tail Shove Selection");
        this.setRunAllSortsName("Tail Shove Selection Sort");
        this.setRunSortName("Tail Shove Selection Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void selectionSort(int[] array, int a, int b) {
        for (int i = b - 1; i >= a; i--) {
            int minIdx = a;
            for (int j = a + 1; j <= i; j++) {
                if (Reads.compareIndices(array, minIdx, j, 0.0625, true) > 0) {
                    minIdx = j;
                    Highlights.markArray(3, j);
                }
            }
            Highlights.clearMark(3);
            Writes.multiSwap(array, minIdx, b - 1, 0.0625, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        selectionSort(array, 0, sortLength);
    }
}
