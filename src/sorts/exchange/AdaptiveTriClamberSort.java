package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class AdaptiveTriClamberSort extends Sort {
    public AdaptiveTriClamberSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive TriSearch Clamber");
        this.setRunAllSortsName("Adaptive TriSearch Clamber Sort");
        this.setRunSortName("Adaptive TriSearch Clambersort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int triSearch(int[] arr, int l, int h, int val, int depth) {
        Writes.recordDepth(depth);
        int mid = l + ((h-l) / 2);
        Highlights.markArray(1, l);
        Highlights.markArray(2, h);
        Highlights.markArray(3, mid);
        Delays.sleep(1);
        if (Reads.compareValues(val, arr[l]) < 0) return l;
        else {
            if (Reads.compareValues(val, arr[h]) < 0) {
                if (Reads.compareValues(val, arr[mid]) < 0) {
                    Writes.recursion();
                    return triSearch(arr, l+1, mid-1, val, depth + 1);
                }
                else {
                    Writes.recursion();
                    return triSearch(arr, mid+1, h-1, val, depth + 1);
                }
            } else return h+1;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int left = 0;
        int right = 1;
        while (right < currentLength) {
            Highlights.markArray(1, right - 1);
            Highlights.markArray(2, right);
            Delays.sleep(1);
            if (Reads.compareValues(array[right - 1], array[right]) > 0) {
                left = triSearch(array, 0, right - 1, array[right], 0);
                Highlights.clearMark(3);
                while (left < right) {
                    Writes.swap(array, left, right, 0.2, true, false);
                    left++;
                }
            }
            right++;
        }
    }
}