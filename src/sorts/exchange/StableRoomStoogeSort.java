package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Harumi
in collaboration with thatsOven

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Harumi
 * @author thatsOven
 *
 */
public class StableRoomStoogeSort extends Sort {

    public StableRoomStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Stable Room Stooge");
        this.setRunAllSortsName("Stable Room Stooge Sort");
        this.setRunSortName("Stable Room Stoogesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public boolean isSorted(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i++)
            if (Reads.compareIndices(array, i, i - 1, 0, true) < 0) return false;
        return true;
    }

    private void stoogeBubble(int[] array, int start, int end) {
        if (end - start + 1 == 2) {
            if (Reads.compareIndices(array, start, end, 0.0025, true) > 0) {
                Writes.swap(array, start, end, 0.005, true, false);
            }
        } else if (end - start + 1 > 2) {
            int third = (end - start + 1) / 3;
            stoogeBubble(array, start, end - third);
            stoogeBubble(array, start + third, end);
        }
    }

    public void sort(int[] array, int a, int b) {
        while (!isSorted(array, a, b)) stoogeBubble(array, a, b - 1);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
