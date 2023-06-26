package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with PCBoy

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author PCBoy
 *
 */
public final class PDRoomSort extends Sort {

    public PDRoomSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pattern-Defeating Room");
        this.setRunAllSortsName("Pattern-Defeating Room Sort");
        this.setRunSortName("Pattern-Defeating Roomsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    int first, last;
    
    protected void stableSegmentReversal(int[] array, int start, int end) {
        if (end - start < 3) Writes.swap(array, start, end, 0.75, true, false);
        else Writes.reversal(array, start, end, 0.75, true, false);
        int i = start;
        int left;
        int right;
        while (i < end) {
            left = i;
            while (i < end && Reads.compareIndices(array, i, i + 1, 0.5, true) == 0) i++;
            right = i;
            if (left != right) {
                if (right - left < 3) Writes.swap(array, left, right, 0.75, true, false);
                else Writes.reversal(array, left, right, 0.75, true, false);
            }
            i++;
        }
    }
    
    protected int findReverseRun(int[] array, int start, int end) {
        int reverse = start;
        boolean lessunique = false;
        boolean different = false;
        int cmp = Reads.compareIndices(array, reverse, reverse + 1, 0.5, true);
        while (reverse + 1 < end && cmp >= 0) {
            if (cmp == 0)
                lessunique = true;
            else
                different = true;
            reverse++;
            if (reverse + 1 < end)
                cmp = Reads.compareIndices(array, reverse, reverse + 1, 0.5, true);
        }
        if (reverse > start && different) {
            if (lessunique)
                stableSegmentReversal(array, start, reverse);
            else if (reverse < start + 3)
                Writes.swap(array, start, reverse, 0.75, true, false);
            else
                Writes.reversal(array, start, reverse, 0.75, true, false);
        }
        return reverse;
    }
    
    protected int binSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.015);
            if (Reads.compareValues(val, array[m]) < 0)
                b = m;
            else
                a = m + 1;
        }
        return a;
    }
    
    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        if (a != b) {
            int temp = array[a];
            int d = (a > b) ? -1 : 1;
            for (int i = a; i != b; i += d)
                Writes.write(array, i, array[i + d], 0.015, true, false);
            Writes.write(array, b, temp, 0.015, true, false);
        }
    }

    protected boolean binsert(int[] array, int a, int g, int b) {
        boolean change = false;
        boolean firstFound = false;
        for (int i = a + 1; i < b; i++) {
            if (Reads.compareIndices(array, i - 1, i, 0.015, true) > 0) {
                change = true;
                int item = array[i];
                int left = Math.max(i - g, a);
                if (Reads.compareValues(array[left], item) <= 0)
                    left = binSearch(array, left + 1, i - 1, item);
                Highlights.clearAllMarks();
                Highlights.markArray(2, left);
                insertTo(array, i, left);
                if (!firstFound) {
                    first = i;
                    firstFound = true;
                }
                Highlights.clearAllMarks();
                last = i;
            }
        }
        return change;
    }
    
    public void sort(int[] array, int a, int b) {
        first = a;
        last = b;
        if (findReverseRun(array, a, b) + 1 < b) {
            boolean change = true;
            int end = b;
            int g = (int) Math.sqrt(b - a) + 1;
            while (change) {
                change = binsert(array, first, g, end);
                end = last;
                first = Math.max(first - g - 1, a);
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
