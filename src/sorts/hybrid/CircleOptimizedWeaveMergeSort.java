package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.merge.QuadSort;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class CircleOptimizedWeaveMergeSort extends Sort {

    QuadSort quad = new QuadSort(arrayVisualizer);

    public CircleOptimizedWeaveMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Circle Optimized Weave Merge");
        this.setRunAllSortsName("Circle Optimized Weave Merge Sort");
        this.setRunSortName("Circle Optimized Weave Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private void bitReversal(int[] array, int a, int b) {
        int len = b - a, m = 0;
        int d1 = len >> 1, d2 = d1 + (d1 >> 1);
        for (int i = 1; i < len - 1; i++) {
            int j = d1;
            for (int k = i, n = d2; (k & 1) == 0; j -= n, k >>= 1, n >>= 1);
            m += j;
            if (m > i) Writes.swap(array, a + i, a + m, 1, true, false);
        }
    }

    protected void weave(int[] array, int start, int len) {
        bitReversal(array, start, start + (len / 2));
        bitReversal(array, start + (len / 2), start + len);
        bitReversal(array, start, start + len);
    }

    protected void circle(int[] array, int a, int b) {
        int left = a;
        int right = b;
        while (left < right) {
            Highlights.markArray(1, left);
            Highlights.markArray(2, right);
            Delays.sleep(0.25);
            if (Reads.compareValues(array[left], array[right]) > 0) Writes.swap(array, left, right, 0.25, true, false);
            left++;
            right--;
        }
    }

    protected void circlepass(int[] array, int start, int len) {
        int gap = len;
        while (gap > 1) {
            int offset = 0;
            while (offset + (gap - 1) < len) {
                circle(array, start + offset, start + offset + (gap - 1));
                offset += gap;
            }
            gap /= 2;
        }
    }

    protected void method(int[] array, int start, int len) {
        if (Reads.compareIndices(array, start + (len / 2) - 1, start + (len / 2), 0.25, true) > 0) {
            weave(array, start, len);
            circlepass(array, start, len);
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
                    Delays.sleep(0.25);
                    if (Reads.compareValues(array[index], array[index + 1]) > 0) Writes.swap(array, index, index + 1, 0.25, true, false);
                }
                else method(array, index, len);
                Highlights.clearAllMarks();
                index += len;
            }
            len *= 2;
        }
        if (len == currentLength) method(array, 0, currentLength);
        else quad.runSort(array, currentLength, 0);
    }
}