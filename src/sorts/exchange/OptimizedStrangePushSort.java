package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class OptimizedStrangePushSort extends Sort {
    public OptimizedStrangePushSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Strange Push");
        this.setRunAllSortsName("Optimized Strange Push Sort");
        this.setRunSortName("Optimized Strange Pushsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the base for this sort:", 2);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 2) return 2;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        boolean anyswaps = true;
        int i;
        int first = 1;
        while (anyswaps) {
            anyswaps = false;
            if (first > 1) i = first - 1;
            else i = 1;
            int gap = 1;
            while (i + gap <= currentLength) {
                if (Reads.compareIndices(array, i - 1, (i - 1) + gap, 0.01, true) > 0) {
                    if (!anyswaps) first = i;
                    for (int j = 1; j <= gap; j++) Writes.swap(array, i - 1, (i - 1) + j, 0.01, anyswaps = true, false);
                    gap *= base;
                } else i++;
            }
        }
    }
}