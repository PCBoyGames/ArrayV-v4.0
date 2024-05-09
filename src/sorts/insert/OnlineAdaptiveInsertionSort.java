package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class OnlineAdaptiveInsertionSort extends Sort {
    public OnlineAdaptiveInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Online Adaptive Insertion");
        this.setRunAllSortsName("Online Adaptive Insertion Sort");
        this.setRunSortName("Online Adaptive Insertsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void onlineAdaInsert(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i++) {
            if (Reads.compareValues(array[i-1], array[i]) <= 0) continue;
            if (Reads.compareValues(array[a], array[i]) > 0) {
                Writes.insert(array, i, a, 0.5, true, false);
                continue;
            }
            int j = i - 1;
            int t = array[i];
            while (Reads.compareValues(array[j], t) > 0) {
                Writes.write(array, j+1, array[j], 0.5, true, false);
                j--;
            }
            Writes.write(array, j+1, t, 0.5, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        onlineAdaInsert(array, 0, currentLength);
    }
}

