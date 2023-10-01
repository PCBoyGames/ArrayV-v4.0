package sorts.misc;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

Self-explanatory.

 */

public class CubicPancakeSort extends Sort {
    public CubicPancakeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Cubic Pancake");
        this.setRunAllSortsName("Cubic Pancake Sorting");
        this.setRunSortName("Cubic Pancake Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(128);
        this.setBogoSort(false);
    }

    // Salvaged from Naoan Sort.
    private void cubicReversal(int[] array, int i, int j) {
        for (int x = j; x > i; x--) {
            for (int y = i; y < x; y++) {
                Writes.reversal(array, i, y, 0.01, true, false);
                Writes.reversal(array, i + 1, y + 1, 0.01, true, false);
                Writes.reversal(array, i, y + 1, 0.01, true, false);
                Writes.reversal(array, i, y - 1, 0.01, true, false);
            }
        }
    }

    private boolean sorted(int[] array, int length) {
        for (int i = 0; i < length; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.025);

            if (Reads.compareValues(array[i], array[i + 1]) > 0) return false;
        }
        return true;
    }

    private int findMax(int[] arr, int end) {
        int index = 0, max = Integer.MIN_VALUE;
        for (int i = 0; i <= end; i++) {
            Highlights.markArray(1, i);

            if (Reads.compareValues(arr[i], max) == 1) {
                max = arr[i];
                index = i;
                Highlights.markArray(2, i);
            }

            Delays.sleep(0.025);
            Highlights.clearMark(1);
        }
        return index;
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int i = length - 1; i >= 0; i--) {
            if (!this.sorted(array, i)) {
                int index = this.findMax(array, i);

                if (index == 0) {
                    cubicReversal(array, 0, i);
                }
                else if (index != i) {
                    cubicReversal(array, 0, index);
                    cubicReversal(array, 0, i);
                }
            }
            else break;
        }
    }
}
