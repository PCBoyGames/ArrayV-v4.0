package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class SwaplessPushSort extends Sort {
    public SwaplessPushSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Swapless Push");
        this.setRunAllSortsName("Swapless Push Sort");
        this.setRunSortName("Swapless Pushsort");
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
                    Highlights.clearMark(2);
                    if (!anyswaps) first = i;
                    Writes.insert(array, i - 1 + gap, i - 1, 0.01, anyswaps = true, false);
                    gap++;
                } else i++;
            }
        }
    }
}