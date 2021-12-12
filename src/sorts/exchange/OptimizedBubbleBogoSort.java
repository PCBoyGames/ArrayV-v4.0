package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class OptimizedBubbleBogoSort extends BogoSorting {
    public OptimizedBubbleBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Bubble Bogo");
        this.setRunAllSortsName("Optimized Bubble Bogo Sort");
        this.setRunSortName("Optimized Bubble Bogosort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        delay = 0.05;
        int start = 0;
        int end = currentLength - 1;
        while (Reads.compareIndices(array, start, start + 1, delay, true) <= 0 && start <= end) start++;
        while (Reads.compareIndices(array, end - 1, end, delay, true) <= 0 && start <= end) end--;
        while (start <= end) {
            int index = randInt(start, end);
            Highlights.markArray(3, start);
            Highlights.markArray(4, end);
            if (Reads.compareIndices(array, index, index + 1, delay, true) > 0) {
                Writes.swap(array, index, index + 1, delay, true, false);
                if (index == start) {
                    if (start > 0) start--;
                    while (Reads.compareIndices(array, start, start + 1, delay, true) <= 0 && start <= end) start++;
                }
                if (index == end - 1) {
                    if (end < currentLength - 1) end++;
                    while (Reads.compareIndices(array, end - 1, end, delay, true) <= 0 && start <= end) end--;
                }
            }
        }
    }
}
