package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class AdaptiveExponentialClamberSort extends Sort {
    public AdaptiveExponentialClamberSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive Exponential Clamber");
        this.setRunAllSortsName("Adaptive Exponential Clamber Sort");
        this.setRunSortName("Adaptive Exponential Clambersort");
        this.setCategory("Exchange Sorts");
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
            Delays.sleep(1);
            if (Reads.compareValues(value, array[m]) < 0) b = m;
            else a = m + 1;
        }
        Highlights.clearMark(3);
        return a;
    }

    protected int exponentSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while(b - i >= a && Reads.compareValues(val, array[b-i]) < 0) {
            Highlights.markArray(1, b - i);
            Delays.sleep(1);
            i *= 2;
        }
        return binarySearch(array, Math.max(a, b - i + 1), b - i / 2, val);
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
                Highlights.clearMark(2);
                left = exponentSearch(array, 0, right - 1, array[right]);
                while (left < right) {
                    Writes.swap(array, left, right, 0.2, true, false);
                    left++;
                }
            }
            right++;
        }
    }
}