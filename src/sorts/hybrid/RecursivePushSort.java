package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class RecursivePushSort extends Sort {
    public RecursivePushSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Recursive Push");
        this.setRunAllSortsName("Recursive Push Sort");
        this.setRunSortName("Recursive Pushsort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void method(int[] array, int start, int end, int depth) {
        Writes.recordDepth(depth);
        boolean anyswaps = false;
        int i = start;
        int first = start;
        int gap = 1;
        while (i + gap <= end) {
            Highlights.markArray(1, i - 1);
            Highlights.markArray(2, (i - 1) + gap);
            Delays.sleep(0.01);
            if (Reads.compareValues(array[i - 1], array[i + gap - 1]) > 0) {
                if (!anyswaps) first = i;
                anyswaps = true;
                Highlights.clearMark(2);
                int item = array[i + gap - 1];
                for (int j = gap; j > 0; j--) Writes.write(array, i + j - 1, array[i + j - 2], 0.01, true, false);
                Writes.write(array, i - 1, item, 0.01, true, false);
                gap++;
            } else i++;
        }
        if (anyswaps) {
            if (end - i > 1) {
                Writes.recursion();
                method(array, i, end, depth + 1);
            }
            if ((i - 1) - first > 1) {
                Writes.recursion();
                method(array, first, i, depth + 1);
            }
        }
    }

    protected int sorted(int[] array, int start, int currentLength) {
        int check = currentLength;
        for (int i = start - 1 > 0 ? start - 1 : 0; i < currentLength - 1; i++) {
            Highlights.markArray(1, i);
            Highlights.markArray(2, i + 1);
            Delays.sleep(0.25);
            if (Reads.compareValues(array[i], array[i + 1]) > 0) {
                check = i;
                break;
            }
        }
        return check;
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

    protected void binsert(int[] array, int start, int currentLength) {
        for (int i = start; i < currentLength; i++) {
            if (Reads.compareValues(array[i - 1], array[i]) > 0) {
                int item = array[i];
                int left = binarySearch(array, 0, i - 1, item);
                Highlights.clearAllMarks();
                Highlights.markArray(2, left);
                for (int right = i; right > left; right--) Writes.write(array, right, array[right - 1], 0.05, true, false);
                Writes.write(array, left, item, 0.05, true, false);
                Highlights.clearAllMarks();
            } else {
                Highlights.markArray(1, i);
                Delays.sleep(0.25);
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int lastcheck = sorted(array, 0, currentLength);
        int runs = 1;
        while (lastcheck != currentLength && runs < Math.cbrt(currentLength)) {
            method(array, lastcheck + 1, currentLength, 0);
            lastcheck = sorted(array, lastcheck, currentLength);
            runs++;
        }
        Highlights.clearAllMarks();
        binsert(array, lastcheck, currentLength);
    }
}