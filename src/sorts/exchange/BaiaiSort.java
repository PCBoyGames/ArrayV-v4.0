package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Made by Kuvina Saydaki(?)

Ported to ArrayV by gooflang.

 */

public final class BaiaiSort extends Sort {
    public BaiaiSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Baiai");
        this.setRunAllSortsName("Baiai Sort");
        this.setRunSortName("Baiaisort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        boolean sorted = false;
        for (int i = 0; i < currentLength; i++) {
            for (int j = 0; j < i; j++) {
                if (Reads.compareValues(array[j], array[j+1]) > 0) {
                    Writes.swap(array, j, j+1, 1, true, false);
                }
            }
        }
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < currentLength-1; i++) {
                if (Reads.compareValues(array[i], array[i+1]) > 0) {
                    Writes.swap(array, i, i+1, 1, true, sorted = false);
                }
            }
        }
    }
}
