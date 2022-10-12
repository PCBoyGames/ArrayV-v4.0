package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class LimitedSwapSort extends Sort {

    public LimitedSwapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Limited Swaps Per Pass");
        this.setRunAllSortsName("Limited Swaps Per Pass Sort");
        this.setRunSortName("Limited Swaps Per Pass Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the limit for this sort:\n0 = Automatic", 0);
    }

    protected int log2(int x) {
        int n = 1;
        while (1 << n < x) n++;
        if (1 << n > x) n--;
        return n;
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 0) return 0;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int swaps) {
        if (swaps == 0) swaps = currentLength / 16 >= 2 ? currentLength / 16 : currentLength / 2;
        boolean pass = false;
        int start = 0;
        int end = currentLength;
        while (!pass) {
            pass = true;
            int swapped = 0;
            for (int i = start; i + 1 < end && i + 1 < currentLength && swapped < swaps; i++) {
                if (Reads.compareIndices(array, i, i + 1, 0.01, true) > 0) {
                    pass = false;
                    Writes.swap(array, i, i + 1, 0.01, true, false);
                    if (swapped == 0) start = i;
                    swapped++;
                    if (i + 2 == end) if (end < currentLength) end++;
                }
            }
            if (pass) break;
            if (start > 0) start--;
            swapped = 0;
            for (int i = end - 2; i >= start && i >= 0 && swapped < swaps; i--) {
                if (Reads.compareIndices(array, i, i + 1, 0.01, true) > 0) {
                    pass = false;
                    Writes.swap(array, i, i + 1, 0.01, true, false);
                    if (swapped == 0) end = i + 2;
                    swapped++;
                    if (i == start) if (start > 0) start--;
                }
            }
            if (end < currentLength) end++;
        }
    }
}