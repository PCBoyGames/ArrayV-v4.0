package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class FallSort extends Sort {
    public FallSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Falling");
        this.setRunAllSortsName("Falling Sort");
        this.setRunSortName("Fallsort");
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
        while (left <= currentLength) {
            int highestlow = 0;
            for (int right = left + 1; right <= currentLength; right++) {
                if (Reads.compareIndices(array, left - 1, right - 1, 0.001, true) > 0) {
                    if (highestlow == 0) highestlow = right;
                    else if (Reads.compareIndices(array, highestlow - 1, right - 1, 0.001, true) < 0) highestlow = right;
                }
            }
            if (highestlow == 0) left++;
            else Writes.swap(array, left - 1, highestlow - 1, 0.001, true, false);
        }
    }
}