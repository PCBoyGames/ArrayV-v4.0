package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class OptimizedZipperSort extends Sort {
    public OptimizedZipperSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Zipper");
        this.setRunAllSortsName("Optimized Zipper Sort");
        this.setRunSortName("Optimized Zippersort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    protected int log2(int x) {
        int n = 1;
        while (1 << n < x) n++;
        if (1 << n > x) n--;
        return n;
    }
    
    protected int binarySearch(int[] array, int a, int b, int value) {
        while (a < b) {
            int m = a + ((b - a) / 2);
            Highlights.markArray(1, a);
            Highlights.markArray(3, m);
            Highlights.markArray(2, b);
            Delays.sleep(0.1);
            if (Reads.compareValues(value, array[m]) < 0) b = m;
            else a = m + 1;
        }
        Highlights.clearMark(3);
        return a;
    }
    
    private void ending(int[] array, int first, int currentLength) {
        int right = first;
        if (right < 1) right++;
        while (right < currentLength) {
            Highlights.markArray(1, right - 1);
            Highlights.markArray(2, right);
            Delays.sleep(0.1);
            if (Reads.compareValues(array[right - 1], array[right]) > 0) {
                int left = binarySearch(array, 0, right - 1, array[right]);
                while (left < right) {
                    Writes.swap(array, left, right, 0.05, true, false);
                    left++;
                }
            }
            right++;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int i = 0;
        int gap = currentLength;
        int first = 0;
        while (gap > Math.max(2 * log2(currentLength), Math.sqrt(currentLength))) {
            gap = 1;
            i = first > 1 ? first - 1 : 0;
            while (i + gap < currentLength) {
                if (Reads.compareIndices(array, i, i + gap, 0.05, true) > 0) {
                    Writes.swap(array, i, i + gap, 0.1, true, false);
                    if (gap == 1) first = i;
                    gap++;
                } else i++;
            }
        }
        if (gap != 1) ending(array, first, currentLength);
    }
}