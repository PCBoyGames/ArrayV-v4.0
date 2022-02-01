package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class AdaptiveFibonacciClamberSort extends Sort {
    public AdaptiveFibonacciClamberSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive Fibonacci Clamber");
        this.setRunAllSortsName("Adaptive Fibonacci Clamber Sort");
        this.setRunSortName("Adaptive Fibonacci Clambersort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int fibonacciSearch(int[] array, int start, int end, int item) {
        int fibM2 = 0;
        int fibM1 = 1;
        int fibM = 1;
        while (fibM <= end - start) {
            fibM2 = fibM1;
            fibM1 = fibM;
            fibM = fibM2 + fibM1;
        }
        int offset = start - 1;
        while (fibM > 1) {
            int i = Math.min(offset + fibM2, end);
            Highlights.markArray(1, offset + 1);
            Highlights.markArray(2, i);
            Delays.sleep(1);
            if (Reads.compareValues(array[i], item) <= 0) {
                fibM = fibM1;
                fibM1 = fibM2;
                fibM2 = fibM - fibM1;
                offset = i;
            } else {
                fibM = fibM2;
                fibM1 -= fibM2;
                fibM2 = fibM - fibM1;
            }
        }
        int position = offset + 1;
        Highlights.clearMark(2);
        Highlights.markArray(1, position);
        Delays.sleep(1);
        if (Reads.compareValues(array[position], item) <= 0) {
            position++;
            Highlights.markArray(1, position);
            Delays.sleep(1);
        }
        return position;
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
                left = fibonacciSearch(array, 0, right - 1, array[right]);
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