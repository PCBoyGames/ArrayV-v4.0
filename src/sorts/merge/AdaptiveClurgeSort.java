package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class AdaptiveClurgeSort extends Sort {
    public AdaptiveClurgeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive Clurge");
        this.setRunAllSortsName("Adaptive Clurge Sort");
        this.setRunSortName("Adaptive Clurge Sort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void method(int[] array, int start, int len) {
        int searchbound = start;
        boolean broken = false;
        for (int right = start + (len / 2); right < start + len && !broken; right++) {
            if (Reads.compareIndices(array, right - 1, right, 1, true) > 0) {
                while (Reads.compareIndices(array, searchbound, right, 1, true) <= 0) searchbound++;
                for (int left = searchbound; left < right; left++) Writes.swap(array, left, right, 0.2, true, false);
                searchbound++;
            } else broken = true;
        }
    }

    protected void nonpow2(int[] array, int start, int len, int mid) {
        int searchbound = start;
        for (int right = mid; right < start + len; right++) {
            if (Reads.compareIndices(array, right - 1, right, 1, true) > 0) {
                int set = searchbound != start ? searchbound - 1 : start;
                if (Reads.compareIndices(array, set, right, 1, true) <= 0) while (Reads.compareIndices(array, searchbound, right, 1, true) <= 0) searchbound++;
                else {
                    searchbound = start;
                    while (Reads.compareIndices(array, searchbound, right, 1, true) <= 0) searchbound++;
                }
                for (int left = searchbound; left < right; left++) Writes.swap(array, left, right, 0.2, true, false);
                searchbound++;
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        int len = 2;
        for (; len < currentLength; len *= 2) for (int index = 0; index + len - 1 < currentLength; index += len) method(array, index, len);
        if (len == currentLength) method(array, 0, currentLength);
        else nonpow2(array, 0, currentLength, len / 2);
    }
}