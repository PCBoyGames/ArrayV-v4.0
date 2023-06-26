package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with PCBoy

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author PCBoy
 *
 */
public final class DoubleSandpaperSort extends Sort {

    public DoubleSandpaperSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Double Sandpaper");
        this.setRunAllSortsName("Double Sandpaper Sort");
        this.setRunSortName("Double Sandpapersort");
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
        int left = 0;
        int right = currentLength - 1;
        int i = 1;
        while (left <= right) {
            i = left;
            while (i < right) {
                if (Reads.compareIndices(array, left, i, 0.05, true) > 0)
                    Writes.swap(array, left, i, 0.05, true, false);
                if (Reads.compareIndices(array, i, right, 0.05, true) > 0)
                    Writes.swap(array, i, right, 0.05, true, false);
                i++;
            }
            left++;
            right--;
        }

    }

}
