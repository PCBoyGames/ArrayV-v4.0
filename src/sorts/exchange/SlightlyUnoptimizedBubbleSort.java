package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class SlightlyUnoptimizedBubbleSort extends Sort {
    public SlightlyUnoptimizedBubbleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Slightly Unoptimized Bubble");
        this.setRunAllSortsName("Slightly Unoptimized Bubble Sort");
        this.setRunSortName("Slightly Unoptimized Bubblesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int i = length - 1; i > 0; i--) for (int j = 0; j < i; j++) if (Reads.compareIndices(array, j, j + 1, 0.025, true) == 1) Writes.swap(array, j, j + 1, 0.075, true, false);
    }
}