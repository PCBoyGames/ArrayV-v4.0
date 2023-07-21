package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class AdaptiveClurgeNewSolSort extends Sort {
    public AdaptiveClurgeNewSolSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive Clurge (New Solution)");
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
        int right = start + (len / 2);
        int searchbound = start;
        boolean broken = false;
        if (Reads.compareIndices(array, right - 1, right, 1, true) > 0) {
            for (; right < start + len && !broken; right++) {
                while (Reads.compareIndices(array, searchbound, right, 1, true) <= 0 && searchbound < right) searchbound++;
                if (searchbound == right) broken = true;
                else {
                    for (int left = searchbound; left < right; left++) Writes.swap(array, left, right, 0.2, true, false);
                    searchbound++;
                }
            }
        }
    }

    protected void nonpow2(int[] array, int start, int len, int mid) {
        int right = mid;
        int searchbound = start;
        if (Reads.compareIndices(array, right - 1, right, 1, true) > 0) {
            for (; right < start + len; right++, searchbound++) {
                int set = searchbound != start ? searchbound - 1 : start;
                if (Reads.compareIndices(array, set, right, 0.1, true) <= 0) while (Reads.compareValues(array[searchbound], array[right]) <= 0) searchbound++;
                else {
                    searchbound = start;
                    while (Reads.compareIndices(array, searchbound, right, 1, true) <= 0) searchbound++;
                }
                for (int left = searchbound; left < right; left++) Writes.swap(array, left, right, 0.2, true, false);
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        int len = 2;
        for (; len < currentLength; len *= 2) {
            for (int index = 0; index + len - 1 < currentLength; index += len) {
                if (len == 2) {if (Reads.compareIndices(array, index, index + 1, 1, true) > 0) Writes.swap(array, index, index + 1, 0.2, true, false);}
                else method(array, index, len);
            }
        }
        if (len == currentLength) method(array, 0, currentLength);
        else nonpow2(array, 0, currentLength, len / 2);
    }
}