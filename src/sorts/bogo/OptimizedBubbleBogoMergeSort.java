package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class OptimizedBubbleBogoMergeSort extends BogoSorting {
    public OptimizedBubbleBogoMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Bubble Bogo Merge");
        this.setRunAllSortsName("Optimized Bubble Bogo Merge Sort");
        this.setRunSortName("Optimized Bubble Bogo Mergesort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void method(int[] array, int a, int b) {
        int start = a;
        int end = a + b - 1;
        while (Reads.compareIndices(array, start, start + 1, delay, true) <= 0 && start <= end) start++;
        while (Reads.compareIndices(array, end - 1, end, delay, true) <= 0 && start <= end) end--;
        while (start <= end) {
            int index = randInt(start, end);
            Highlights.markArray(3, start);
            Highlights.markArray(4, end);
            if (Reads.compareIndices(array, index, index + 1, delay, true) > 0) {
                Writes.swap(array, index, index + 1, delay, true, false);
                if (index == start) {
                    if (start > a) start--;
                    while (Reads.compareIndices(array, start, start + 1, delay, true) <= 0 && start <= end) start++;
                }
                if (index == end - 1) {
                    if (end < a + b - 1) end++;
                    while (Reads.compareIndices(array, end - 1, end, delay, true) <= 0 && start <= end) end--;
                }
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        delay = 0.05;
        int len = 2;
        int index = 0;
        while (len < currentLength) {
            index = 0;
            while (index + len - 1 < currentLength) {
                if (len == 2) {
                    if (Reads.compareIndices(array, index, index + 1, delay, true) > 0) Writes.swap(array, index, index + 1, delay, true, false);
                }
                else method(array, index, len);
                Highlights.clearAllMarks();
                index += len;
            }
            len *= 2;
        }
        method(array, 0, currentLength);
    }
}