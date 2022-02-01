package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class InRunBinaryInsertionSort extends Sort {

    public InRunBinaryInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("In-Run Binary Insertion");
        this.setRunAllSortsName("In-Run Binary Insertion Sort");
        this.setRunSortName("In-Run Binary Insertsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void stableSegmentReversal(int[] array, int start, int end, double delay, boolean aux) {
        if (end - start < 3) Writes.swap(array, start, end, delay, true, aux);
        else Writes.reversal(array, start, end, delay, true, aux);
        int i = start;
        int left;
        int right;
        while (i < end) {
            left = i;
            while (Reads.compareIndices(array, i, i + 1, delay, true) == 0 && i < end) i++;
            right = i;
            if (left != right) {
                if (right - left < 3) Writes.swap(array, left, right, delay, true, aux);
                else Writes.reversal(array, left, right, delay, true, aux);
            }
            i++;
        }
    }

    protected int pd(int[] array, int start, int end, double delay, boolean aux) {
        int forward = start;
        int cmp = Reads.compareIndices(array, forward, forward + 1, delay, true);
        while (cmp <= 0 && forward + 1 < end) {
            forward++;
            if (forward + 1 < end) cmp = Reads.compareIndices(array, forward, forward + 1, delay, true);
        }
        int reverse = forward == start ? start + 1 : start;
        if (forward == start) {
            boolean lessunique = false;
            boolean different = false;
            cmp = Reads.compareIndices(array, reverse, reverse + 1, delay, true);
            while (cmp >= 0 && reverse + 1 < end) {
                if (cmp == 0) lessunique = true;
                else different = true;
                reverse++;
                if (reverse + 1 < end) cmp = Reads.compareIndices(array, reverse, reverse + 1, delay, true);
            }
            if (reverse > 0) {
                if (lessunique && different) stableSegmentReversal(array, start, reverse, delay, aux);
                else if (reverse < 3) Writes.swap(array, start, reverse, delay, true, aux);
                else Writes.reversal(array, start, reverse, delay, true, aux);
            }
        }
        return Math.max(forward, reverse);
    }

    protected int binarySearch(int[] array, int a, int b, int value, double delay) {
        while (a < b) {
            int m = a + ((b - a) / 2);
            Highlights.markArray(1, a);
            Highlights.markArray(3, m);
            Highlights.markArray(2, b);
            Delays.sleep(delay);
            if (Reads.compareValues(value, array[m]) < 0) b = m;
            else a = m + 1;
        }
        Highlights.clearMark(3);
        return a;
    }

    public void irbinsert(int[] array, int start, int end, double delay, boolean dopd, boolean aux) {
        int pattern = start;
        if (dopd) pattern = pd(array, start, end, delay, aux);
        Highlights.clearAllMarks();
        for (int i = pattern + 1; i < end; i++) {
            if (Reads.compareValues(array[i - 1], array[i]) > 0) {
                int item = array[i];
                int left = binarySearch(array, start, i - 1, item, delay);
                Highlights.clearAllMarks();
                Highlights.markArray(2, left);
                boolean w = false;
                for (int right = i; right > left; right--) {
                    Writes.write(array, right, array[right - 1], delay / 20, true, aux);
                    w = true;
                }
                if (w) Writes.write(array, left, item, delay, true, aux);
                Highlights.clearAllMarks();
            } else {
                Highlights.markArray(1, i);
                Delays.sleep(delay);
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int constantdiv) throws Exception {
        irbinsert(array, 0, currentLength, 1, true, false);
    }
}