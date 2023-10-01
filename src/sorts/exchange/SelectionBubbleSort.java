package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class SelectionBubbleSort extends Sort {
    public SelectionBubbleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Selection Bubble");
        this.setRunAllSortsName("_fluffyy's Selection Bubble Sort");
        this.setRunSortName("_fluffyy's Selection Bubble Sort");
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
        int orig_i;
        boolean swap;
        for (int i=0; i < currentLength-1; i++) {
            orig_i = i;
            while (true) {
                i = orig_i;
                swap = false;
                for (int j=i+1; j < currentLength; j++) {
                    if (Reads.compareValues(array[i], array[j]) > 0) {
                        Writes.swap(array, i, j, 1, true, false);
                        swap = true;
                        i = j;
                    }
                }
                if (!swap) break;
            }
        }
    }
}