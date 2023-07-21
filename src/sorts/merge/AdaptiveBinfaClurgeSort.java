package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class AdaptiveBinfaClurgeSort extends Sort {
    public AdaptiveBinfaClurgeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive Clurge (Binary Fallback)");
        this.setRunAllSortsName("Adaptive Binfa Clurge Sort");
        this.setRunSortName("Adaptive Binfa Clurge Sort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int binarySearch(int[] array, int a, int b, int value) {
        while (a < b) {
            int m = a + ((b - a) / 2);
            Highlights.markArray(1, a);
            Highlights.markArray(3, m);
            Highlights.markArray(2, b);
            Delays.sleep(4);
            if (Reads.compareValues(value, array[m]) < 0) b = m;
            else a = m + 1;
        }
        Highlights.clearMark(3);
        return a;
    }

    protected void method(int[] array, int start, int len) {
        int searchbound = start;
        boolean broken = false;
        for (int right = start + (len / 2); right < start + len && !broken; right++) {
            if (Reads.compareIndices(array, right - 1, right, 1, true) > 0) {
                int comp = Reads.compareIndices(array, searchbound, right, 1, true);
                for (int check = 0; check < Math.cbrt(len) && comp <= 0; check++) {
                    searchbound++;
                    comp = Reads.compareIndices(array, searchbound, right, 1, true);
                }
                if (comp <= 0) searchbound = binarySearch(array, searchbound, right - 1, array[right]);
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
                if (Reads.compareIndices(array, set, right, 1, true) > 0) searchbound = start;
                int comp = Reads.compareIndices(array, searchbound, right, 1, true);
                for (int check = 0; check < Math.cbrt(len) && comp <= 0; check++) {
                    searchbound++;
                    comp = Reads.compareIndices(array, searchbound, right, 1, true);
                }
                if (comp <= 0) searchbound = binarySearch(array, searchbound, right - 1, array[right]);
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