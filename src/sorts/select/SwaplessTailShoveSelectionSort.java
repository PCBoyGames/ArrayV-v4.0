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
public class SwaplessTailShoveSelectionSort extends Sort {
    public SwaplessTailShoveSelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Swapless Tail Shove Selection");
        this.setRunAllSortsName("Swapless Tail Shove Selection Sort");
        this.setRunSortName("Swapless Tail Shove Selection Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], 0.0625, true, false);
        if (a != b)
            Writes.write(array, b, temp, 0.0625, true, false);
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
            insertTo(array, minIdx, b - 1);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        selectionSort(array, 0, sortLength);
    }
}
