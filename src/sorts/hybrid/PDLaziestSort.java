package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

/*

Coded for ArrayV by Harumi
extending code by aphitorite

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Harumi
 * @author aphitorite
 *
 */
public class PDLaziestSort extends Sort {

    public PDLaziestSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pattern-Defeating Laziest Stable");
        this.setRunAllSortsName("Pattern-Defeating Laziest Stable Sort");
        this.setRunSortName("Pattern-Defeating Laziest Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void rotate(int[] array, int a, int m, int b) {
        Highlights.clearAllMarks();
        IndexedRotations.adaptable(array, a, m, b, 1.0, true, false);
    }

    private int binSearch(int[] array, int a, int b, int val, boolean left) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            int c = Reads.compareValues(val, array[m]);
            if (c < 0 || (left && c == 0)) b = m;
            else a = m + 1;
        }
        return a;
    }

    private int leftExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) > 0) i *= 2;
        else while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0) i *= 2;
        return binSearch(array, a + i / 2, Math.min(b, a - 1 + i), val, left);
    }

    protected int rightExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0) i *= 2;
        else while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0) i *= 2;
        return binSearch(array, Math.max(a, b - i + 1), b - i / 2, val, left);
    }

    protected int findRun(int[] array, int a, int b, double sleep, boolean auxwrite) {
        int i = a + 1;
        if (i >= b) return i;
        boolean dir = Reads.compareIndices(array, i - 1, i++, sleep, true) <= 0;
        while (i < b) {
            if (dir ^ Reads.compareIndices(array, i - 1, i, sleep, true) <= 0) break;
            i++;
        }
        if (!dir) {
            if (i - a < 4) Writes.swap(array, a, i - 1, sleep, true, auxwrite);
            else Writes.reversal(array, a, i - 1, sleep, true, auxwrite);
        }
        Highlights.clearMark(2);
        return i;
    }

    private void insertionSort(int[] array, int a, int b, double sleep, boolean auxwrite) {
        for (int i = findRun(array, a, b, sleep, auxwrite); i < b; i++) {
            int current = array[i];
            int dest = rightExpSearch(array, a, i, current, false);
            int pos = i - 1;
            while (pos >= dest) {
                Writes.write(array, pos + 1, array[pos], sleep, true, auxwrite);
                pos--;
            }
            if (pos + 1 < i) Writes.write(array, pos + 1, current, sleep, true, auxwrite);
        }
    }

    private void inPlaceMerge(int[] array, int a, int m, int b) {
        if (Reads.compareIndices(array, m - 1, m, 0, false) <= 0) return;
        a = leftExpSearch(array, a, m, array[m], false);
        while (a < m && m < b) {
            int i = leftExpSearch(array, m, b, array[a], true);
            rotate(array, a, m, i);
            int t = i - m;
            m = i;
            a += t + 1;
            if (m >= b) break;
            a = leftExpSearch(array, a, m, array[m], false);
        }
    }

    public void laziestStableSort(int[] array, int start, int end) {
        int len = end - start;
        if (len <= 16) {
            insertionSort(array, start, end, 0.5, false);
            return;
        }
        int i, blockLen = Math.max(16, (int)Math.sqrt(len));
        for (i = start; i+2*blockLen < end; i+=blockLen) {
            insertionSort(array, i, i + blockLen, 0.5, false);
        }
        insertionSort(array, i, end, 0.5, false);
        while (i-blockLen >= start) {
            this.inPlaceMerge(array, i-blockLen, i, end);
            i-=blockLen;
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        this.laziestStableSort(array, 0, sortLength);

    }

}
