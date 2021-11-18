package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class InstinctBubbleSort extends Sort {
    public InstinctBubbleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Instinct Bubble");
        this.setRunAllSortsName("Instinct Bubble Sort");
        this.setRunSortName("Instinct Bubblesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int consecSorted = 1;
        int start;
        int firstswap = 1;
        boolean anyswaps = false;
        for (int j = currentLength - 1; j > 0; j -= consecSorted) {
            if (firstswap - 1 < 0) start = 0;
            else start = firstswap - 1;
            anyswaps = false;
            consecSorted = 1;
            for (int i = start; i < j; i++) {
                Highlights.markArray(1, i);
                Highlights.markArray(2, i + 1);
                Delays.sleep(0.05);
                Writes.swap(array, i, i + 1, 0.05, true, false);     
                if (Reads.compareIndices(array, i, i + 1, 0.05, true) >= 0) {
                    Writes.swap(array, i, i + 1, 0.05, true, false);
                    consecSorted++;
                } else {
                    if (!anyswaps) firstswap = i;
                    anyswaps = true;
                    consecSorted = 1;
                }
            }
        }
    }
}
