package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author aphitorite
 *
 */
public final class NaturalIndexMergeSort extends Sort {

    public NaturalIndexMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Natural Index Merge");
        this.setRunAllSortsName("Natural Index Merge Sort");
        this.setRunSortName("Natural Index Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int binSearch(int[] array, int a, int b, int val, boolean left) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            int c = Reads.compareValues(val, array[m]);
            if (c < 0 || (left && c == 0))
                b = m;
            else
                a = m + 1;
        }
        return a;
    }

    protected int leftExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left)
            while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) > 0)
                i *= 2;
        else
            while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0)
                i *= 2;
        int a1 = a + i / 2, b1 = Math.min(b, a - 1 + i);
        return binSearch(array, a1, b1, val, left);
    }

    protected int rightExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left)
            while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0)
                i *= 2;
        else
            while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0)
                i *= 2;
        int a1 = Math.max(a, b - i + 1), b1 = b - i / 2;
        return binSearch(array, a1, b1, val, left);
    }

    protected void indexSort(int[] array, int[] keys, int a, int b) {
        for (int i = 0; i < b - a; i++) {
            Highlights.markArray(2, i);
            if (Reads.compareOriginalValues(i, keys[i]) != 0) {
                int t = array[a+i];
                int j = i, next = keys[i];

                do {
                    Writes.write(array, a+j, array[a+next], 0.0, true, false);
                    Writes.write(keys, j, j, 0.5, true, true);

                    j = next;
                    next = keys[next];
                }
                while (Reads.compareOriginalValues(next, i) != 0);

                Writes.write(array, a+j, t, 0.0, true, false);
                Writes.write(keys, j, j, 0.5, true, true);
            }
        }
        Highlights.clearMark(2);
    }

    protected void merge(int[] array, int[] idx, int a, int m, int b) {
        if (Reads.compareIndices(array, m - 1, m, 0.0, true) <= 0)
            return;
        a = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        int i = a, j = m, c = 0;
        while (i < m || j < b) {
            if (i < m)
                Highlights.markArray(1, i);
            else
                Highlights.clearMark(1);
            if (j < b)
                Highlights.markArray(2, j);
            else
                Highlights.clearMark(2);
            if (i < m && (j >= b || Reads.compareIndices(array, i, j, 0, false) <= 0))
                Writes.write(idx, c, i++ - a, 0.5, false, true);
            else
                Writes.write(idx, c, j++ - a, 0.5, false, true);
            c++;
        }
        Highlights.clearMark(2);
        indexSort(array, idx, a, b);
    }

    protected int findRun(int[] array, int a, int b) {
        int i = a + 1;
        if (i < b)
            if (Reads.compareIndices(array, i - 1, i++, 0.5, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0)
                    i++;
                if (i - a < 4)
                    Writes.swap(array, a, i - 1, 1.0, true, false);
                else
                    Writes.reversal(array, a, i - 1, 1.0, true, false);
            } else
                while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0)
                    i++;
        Highlights.clearMark(2);
        return i;
    }

    public void mergeSort(int[] array, int a, int b) {
        int[] buf = Writes.createExternalArray(b - a);
        int i, j, k;
        while (true) {
            i = findRun(array, a, b);
            if (i >= b)
                break;
            j = findRun(array, i, b);
            merge(array, buf, a, i, j);
            Highlights.clearMark(2);
            if (j >= b)
                break;
            k = j;
            while (true) {
                i = findRun(array, k, b);
                if (i >= b)
                    break;
                j = findRun(array, i, b);
                merge(array, buf, k, i, j);
                if (j >= b)
                    break;
                k = j;
            }
        }
        Writes.deleteExternalArray(buf);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);

    }

}
