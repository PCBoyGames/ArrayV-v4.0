package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

Coded for ArrayV by Ayako-chan

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 *
 */
public class CombBogoSort extends BogoSorting {

    public CombBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Comb Bogo");
        this.setRunAllSortsName("Comb Bogo Sort");
        this.setRunSortName("Comb Bogosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(true);
    }

    public void sort(int[] array, int a, int b) {
        while (!this.isRangeSorted(array, a, b, false, true)) {
            boolean noswap = true;
            while (noswap) {
                int gap = BogoSorting.randInt(1, b - a);
                if (BogoSorting.randBoolean()) {
                    for (int i = a; (gap + i) < b; i++) {
                        if (Reads.compareIndices(array, i, i + gap, this.delay, true) > 0) {
                            Writes.swap(array, i, i + gap, this.delay, true, false);
                            noswap = false;
                        }
                    }
                } else {
                    for (int i = b - 1; i >= a + gap; i--) {
                        if (Reads.compareIndices(array, i - gap, i, this.delay, true) > 0) {
                            Writes.swap(array, i - gap, i, this.delay, true, false);
                            noswap = false;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
