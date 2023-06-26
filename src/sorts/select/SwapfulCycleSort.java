package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class SwapfulCycleSort extends Sort {

    public SwapfulCycleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Swapful Cycle");
        this.setRunAllSortsName("Swapful Cycle Sort");
        this.setRunSortName("Swapful Cyclesort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    int countLesser(int[] array, int a, int b, int idx) {
        int r = a;

        for (int i = a+1; i < b; i++) {
            Highlights.markArray(1, r);
            Highlights.markArray(2, i);
            Delays.sleep(0.01);

            r += Reads.compareValues(array[i], array[idx]) < 0 ? 1 : 0;
        }
        Highlights.clearMark(2);
        return r;
    }

    public void sort(int[] array, int a, int b) {
        for (int i = a; i < b - 1; i++) {
            int r = countLesser(array, i, b, i);
            if (r != i) {
                do {
                    while (Reads.compareIndices(array, r, i, 0.01, true) == 0) r++;
                    Writes.swap(array, i, r, 0.02, true, false);
                    r = countLesser(array, i, b, i);
                } while (r != i);
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
