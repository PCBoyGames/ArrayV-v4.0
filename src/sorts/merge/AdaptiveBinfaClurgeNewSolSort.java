package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class AdaptiveBinfaClurgeNewSolSort extends Sort {
    public AdaptiveBinfaClurgeNewSolSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive Clurge (Binary Fallback) (New Solution)");
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
        int left = start;
        int right = start + (len / 2);
        int searchbound = left;
        boolean broken = false;
        if (Reads.compareValues(array[right - 1], array[right]) > 0) {
            while (right < start + len && !broken) {
                Highlights.markArray(1, right - 1);
                Highlights.markArray(2, right);
                Highlights.markArray(3, searchbound);
                Delays.sleep(1);
                int check = 0;
                int comp = Reads.compareValues(array[searchbound], array[right]);
                while (check < Math.cbrt(len) && comp <= 0) {
                    searchbound++;
                    Highlights.markArray(3, searchbound);
                    Delays.sleep(1);
                    check++;
                    comp = Reads.compareValues(array[searchbound], array[right]);
                }
                if (comp <= 0) searchbound = binarySearch(array, searchbound, right, array[right]);
                if (searchbound > right) broken = true;
                else {
                    left = searchbound;
                    Highlights.markArray(3, searchbound);
                    while (left < right) {
                        Writes.swap(array, left, right, 0.2, true, false);
                        left++;
                    }
                    Highlights.clearMark(1);
                    searchbound++;
                }
                right++;
            }
        } else {
            Highlights.markArray(1, right - 1);
            Highlights.markArray(2, right);
            Highlights.clearMark(3);
            Delays.sleep(2);
        }
    }
    
    protected void nonpow2(int[] array, int start, int len, int mid) {
        int left = start;
        int right = mid;
        int searchbound = left;
        if (Reads.compareValues(array[right - 1], array[right]) > 0) {
            while (right < start + len) {
                Highlights.markArray(1, right - 1);
                Highlights.markArray(2, right);
                if (Reads.compareValues(array[right - 1], array[right]) > 0) {
                    Highlights.markArray(3, searchbound);
                    Delays.sleep(1);
                    int set = searchbound != start ? searchbound - 1 : start;
                    if (Reads.compareValues(array[set], array[right]) > 0) {
                        searchbound = start;
                    }
                    int check = 0;
                    int comp = Reads.compareValues(array[searchbound], array[right]);
                    while (check < Math.cbrt(len) && comp <= 0) {
                        searchbound++;
                        Highlights.markArray(3, searchbound);
                        Delays.sleep(1);
                        check++;
                        comp = Reads.compareValues(array[searchbound], array[right]);
                    }
                    if (comp <= 0) searchbound = binarySearch(array, searchbound, right - 1, array[right]);
                    left = searchbound;
                    Highlights.markArray(3, searchbound);
                    while (left < right) {
                        Writes.swap(array, left, right, 0.2, true, false);
                        left++;
                    }
                    Highlights.clearMark(1);
                    searchbound++;
                } else {
                    Delays.sleep(2);
                    Highlights.clearMark(3);
                }
                right++;
            }
        } else {
            Highlights.markArray(1, right - 1);
            Highlights.markArray(2, right);
            Highlights.clearMark(3);
            Delays.sleep(2);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        int len = 2;
        int index = 0;
        while (len < currentLength) {
            index = 0;
            while (index + len - 1 < currentLength) {
                if (len == 2) {
                    Highlights.markArray(1, index);
                    Highlights.markArray(2, index + 1);
                    Delays.sleep(1);
                    if (Reads.compareValues(array[index], array[index + 1]) > 0) Writes.swap(array, index, index + 1, 0.2, true, false);
                }
                else method(array, index, len);
                Highlights.clearAllMarks();
                index += len;
            }
            len *= 2;
        }
        if (len == currentLength) {
            method(array, 0, currentLength);
        } else {
            nonpow2(array, 0, currentLength, len / 2);
        }
    }
}