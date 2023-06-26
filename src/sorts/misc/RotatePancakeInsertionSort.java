package sorts.misc;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with Gaming32

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author Gaming32
 *
 */
public final class RotatePancakeInsertionSort extends Sort {

    public RotatePancakeInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Rotate Pancake Insertion");
        this.setRunAllSortsName("Rotate Pancake Insertion Sort");
        this.setRunSortName("Rotate Pancake Insertionsort");
        this.setCategory("Miscellaneous Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    int offset;

    int compare(int[] array, int a, int b, double sleep, boolean mark) {
        return Reads.compareIndices(array, offset + a, offset + b, sleep, mark);
    }

    void flip(int[] array, int idx) {
        if (idx <= 0) return;
        if (idx >= 3)
            Writes.reversal(array, offset, offset + idx, 0.125, true, false);
        else
            Writes.swap(array, offset, offset + idx, 0.125, true, false);
    }

    public void insertSort(int[] array, int a, int b) {
        int len = b - a;
        this.offset = a;
        boolean invert = false;
        int l, r, m;
        for (int i=1; i<len; i++) {
            // if the next element is in place, skip it
            if (invert ^ compare(array, i-1, i, 1, true) <= 0) {
                continue;
            }
            // if next element goes at start, flip sorted area
            if (invert ^ compare(array, 0, i, 1, true) > 0) {
                flip(array, i - 1);
                invert = !invert;
                continue;
            }
            // binary search for next element's position
            l = 0;
            r = i;
            while (l < r) {
                m = l + (r - l) / 2;
                if (invert ^ compare(array, m, i, 0.5, true) > 0) {
                    r = m;
                } else {
                    l = m + 1;
                }
            }
            // use pancake flips to insert into target position
            flip(array, l-1);
            flip(array, i-1);
            flip(array, i);
            flip(array, l);
        }
        if (invert) // The array is reversed
            flip(array, len - 1);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        insertSort(array, 0, sortLength);

    }

}
