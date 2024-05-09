package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

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
public class OOPLaziestSort extends Sort {

    public OOPLaziestSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Out-of-Place Laziest Stable");
        this.setRunAllSortsName("Out-of-Place Laziest Stable Sort");
        this.setRunSortName("Out-of-Place Laziest Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        boolean change = false;
        while (a > b) Writes.write(array, a, array[--a], 0.5, change = true, false);
        if (change) Writes.write(array, b, temp, 0.5, true, false);
    }

    private int rightBinSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = a + (b - a) / 2;
            if (Reads.compareValues(val, array[m]) < 0) b = m;
            else a = m+1;
        }
        return a;
    }

    protected int findRun(int[] array, int a, int b) {
        int i = a + 1;
        if (i >= b) return i;
        boolean dir = Reads.compareIndices(array, i - 1, i++, 0.5, true) <= 0;
        while (i < b) {
            if (dir ^ Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0) break;
            i++;
        }
        if (!dir) {
            if (i - a < 4) Writes.swap(array, a, i - 1, 0.5, true, false);
            else Writes.reversal(array, a, i - 1, 0.5, true, false);
        }
        Highlights.clearMark(2);
        return i;
    }

    private void binaryInsertion(int[] array, int a, int b) {
        for (int i = findRun(array, a, b); i < b; i++)
            this.insertTo(array, i, this.rightBinSearch(array, a, i, array[i]));
    }

    protected void merge(int[] array, int[] tmp, int a, int m, int b) {
        if (Reads.compareIndices(array, m - 1, m, 0, false) <= 0) return;
        int s = m - a;
        Writes.arraycopy(array, a, tmp, 0, s, 1, true, true);
        int i = 0, j = m;
        while (i < s && j < b) {
            Highlights.markArray(2, j);
            if (Reads.compareValues(tmp[i], array[j]) <= 0)
                Writes.write(array, a++, tmp[i++], 1, true, false);
            else
                Writes.write(array, a++, array[j++], 1, true, false);
        }
        Highlights.clearMark(2);
        while (i < s) Writes.write(array, a++, tmp[i++], 1, true, false);
    }

    public void laziestStableSort(int[] array, int start, int end) {
        int len = end - start;
        if (len <= 16) {
            this.binaryInsertion(array, start, end);
            return;
        }

        int i, blockLen = Math.max(16, (int)Math.sqrt(len));
        for (i = start; i+2*blockLen < end; i+=blockLen) {
            this.binaryInsertion(array, i, i+blockLen);
        }
        this.binaryInsertion(array, i, end);
        int[] temp = Writes.createExternalArray(blockLen);

        while (i-blockLen >= start) {
            this.merge(array, temp, i-blockLen, i, end);
            i-=blockLen;
        }
        Writes.deleteExternalArray(temp);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        laziestStableSort(array, 0, sortLength);

    }

}
