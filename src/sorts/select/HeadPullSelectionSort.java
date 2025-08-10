package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Flanlaina

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Flanlaina
 *
 */
public class HeadPullSelectionSort extends Sort {

    public HeadPullSelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Head Pull Selection");
        this.setRunAllSortsName("Head Pull Selection Sort");
        this.setRunSortName("Head Pull Selection Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void selectionSort(int[] array, int a, int b) {
        for (int i = a; i < b; i++) {
            int maxIdx = i;
            for (int j = i + 1; j < b; j++) {
                if (Reads.compareIndices(array, maxIdx, j, 0.0625, true) <= 0) {
                    maxIdx = j;
                    Highlights.markArray(3, j);
                }
            }
            Highlights.clearMark(3);
            Writes.multiSwap(array, maxIdx, a, 0.0625, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        selectionSort(array, 0, sortLength);

    }

}
