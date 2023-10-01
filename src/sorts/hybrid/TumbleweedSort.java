package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class TumbleweedSort extends Sort {
    public TumbleweedSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Tumbleweed");
        this.setRunAllSortsName("Tumbleweed Sort");
        this.setRunSortName("Tumbleweed Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int left = 1;
        boolean anyless = false;
        while (left < currentLength - 1) {
            int lowesthigh = left;
            int right = left + 1;
            for (; right <= currentLength; right++) {
                if (Reads.compareIndices(array, left - 1, right - 1, 0.005, true) <= 0) {
                    if (lowesthigh == left) lowesthigh = right - 1;
                    else if (Reads.compareIndices(array, lowesthigh - 1, right - 1, 0.005, true) > 0) lowesthigh = right - 1;
                }
            }
            if (lowesthigh == left) Writes.multiSwap(array, left - 1, currentLength - 1, 0.005, true, false);
            else {
                if (lowesthigh == left + 1) {
                    right = left + 1;
                    anyless = false;
                    while (right <= currentLength && !anyless) {
                        if (Reads.compareIndices(array, left - 1, right - 1, 0.005, true) > 0) anyless = true;
                        else right++;
                    }
                    if (!anyless) left++;
                    else Writes.multiSwap(array, left - 1, currentLength - 1, 0.005, true, false);
                } else Writes.multiSwap(array, left - 1, lowesthigh - 1, 0.005, true, false);
            }
        }
    }
}