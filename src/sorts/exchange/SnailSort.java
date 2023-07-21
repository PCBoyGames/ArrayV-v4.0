package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class SnailSort extends Sort {
    public SnailSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Snail");
        this.setRunAllSortsName("Snail Sort");
        this.setRunSortName("Snailsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int i = 0;
        while (i < currentLength - 2) {
            int m = i;
            if (Reads.compareValues(array[i], array[i + 2]) == 1) {
                Writes.swap(array, i, i + 2, 1, true, false);
                i = 0;
            }
            Highlights.markArray(1, m);
            Highlights.markArray(2, m + 2);
            if (i == 0) m = 0;
            if (Reads.compareValues(array[i], array[i + 1]) == 1) {
                Writes.swap(array, i, i + 1, 1, true, false);
                i = 0;
            }
            Highlights.markArray(1, m);
            Highlights.markArray(2, m + 1);
            i++;
        }
        boolean sorted = false;

        while (!sorted) {
            sorted = true;
            for (i = 0; i < currentLength - 1; i++) {
                if (Reads.compareValues(array[i], array[i + 1]) == 1) {
                    Writes.swap(array, i, i + 1, 0.075, true, false);
                    sorted = false;
                }

                Highlights.markArray(1, i);
                Highlights.markArray(2, i + 1);
                Delays.sleep(0.05);
            }
        }
    }
}