package sorts.hybrid;

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
public final class IttoSort extends Sort {

    public IttoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Itto");
        this.setRunAllSortsName("Itto Sort");
        this.setRunSortName("Ittosort");
        this.setCategory("Hybrid Sorts");
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

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], 0.5, true, false);
        if (a != b)
            Writes.write(array, b, temp, 0.5, true, false);
    }

    protected boolean buildRuns(int[] array, int a, int b, int mRun) {
        int i = a + 1, j = a;
        boolean noSort = true;
        while (i < b) {
            if (Reads.compareIndices(array, i - 1, i++, 1, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) > 0) i++;
                if (i - j < 4)
                    Writes.swap(array, j, i - 1, 1.0, true, false);
                else
                    Writes.reversal(array, j, i - 1, 1.0, true, false);
            } else
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) <= 0) i++;
            if (i < b) {
                noSort = false;
                j = i - (i - j - 1) % mRun - 1;
            }
            while (i - j < mRun && i < b) {
                insertTo(array, i, rightExpSearch(array, j, i, array[i], false));
                i++;
            }
            j = i++;
        }
        return noSort;
    }

    protected void mergeFWExt(int[] array, int[] tmp, int a, int m, int b) {
        int s = m - a;
        Writes.arraycopy(array, a, tmp, 0, s, 1, true, true);
        int i = 0, j = m;
        while (i < s && j < b)
            if (Reads.compareValues(tmp[i], array[j]) <= 0)
                Writes.write(array, a++, tmp[i++], 1, true, false);
            else
                Writes.write(array, a++, array[j++], 1, true, false);
        while (i < s)
            Writes.write(array, a++, tmp[i++], 1, true, false);
    }

    protected void mergeBWExt(int[] array, int[] tmp, int a, int m, int b) {
        int s = b - m;
        Writes.arraycopy(array, m, tmp, 0, s, 1, true, true);
        int i = s - 1, j = m - 1;
        while (i >= 0 && j >= a)
            if (Reads.compareValues(tmp[i], array[j]) >= 0)
                Writes.write(array, --b, tmp[i--], 1, true, false);
            else
                Writes.write(array, --b, array[j--], 1, true, false);
        while (i >= 0)
            Writes.write(array, --b, tmp[i--], 1, true, false);
    }

    protected void merge(int[] array, int[] buf, int a, int m, int b) {
        if (a >= m || m >= b || Reads.compareValues(array[m - 1], array[m]) <= 0)
            return;
        a = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        Highlights.clearMark(2);
        if (m - a > b - m)
            mergeBWExt(array, buf, a, m, b);
        else
            mergeFWExt(array, buf, a, m, b);
    }

    protected void lazyBufferedMergeBW(int[] array, int[] buf, int a, int m, int b) {
        if (a >= m || m >= b || Reads.compareValues(array[m - 1], array[m]) <= 0)
            return;
        int s = b - m;
        Writes.arraycopy(array, m, buf, 0, s, 1, true, true);
        int i = s - 1, j = m - 1, k = b - 1;
        while (i >= 0 && j >= a) {
            if (Reads.compareValues(array[j], buf[i]) > 0) {
                int q = rightExpSearch(array, a, j+1, buf[i], false);
                while (j >= q) Writes.write(array, k--, array[j--], 1, true, false);
            }
            Writes.write(array, k--, buf[i--], 1, true, false);
        }
        while (i >= 0)
            Writes.write(array, k--, buf[i--], 1, true, false);
    }

    public void mergeSort(int[] array, int a, int b) {
        int len = b - a;
        if (len <= 64) {
            int j = len;
            while (j >= 32)
                j = (j + 1) / 2;
            if (buildRuns(array, a, b, j))
                return;
            int[] buf = Writes.createExternalArray(len / 2);
            int i;
            for (; j < len; j *= 2) {
                for (i = a; i + 2 * j <= b; i += 2 * j)
                    merge(array, buf, i, i + j, i + 2 * j);
                if (i + j < b)
                    merge(array, buf, i, i + j, b);
            }
            Writes.deleteExternalArray(buf);
            return;
        }
        int bLen, mRun;
        for (bLen = 1; (bLen*bLen*bLen)/len < len; bLen *= 2);
        for (mRun = 1; (mRun*mRun*mRun)/len < len; mRun++);
        mRun = (16*mRun) / bLen;
        bLen = (bLen*mRun) / 16;
        if (buildRuns(array, a, b, mRun))
            return;
        int[] buf = Writes.createExternalArray(bLen);
        int i, j = mRun;
        for (; j < bLen; j *= 2)
            for (i = a; i + j < b; i += 2 * j)
                merge(array, buf, i, i + j, Math.min(i + 2 * j, b));
        for (i = a + bLen; i < b; i += bLen)
            lazyBufferedMergeBW(array, buf, a, i, Math.min(i + bLen, b));
        Writes.deleteExternalArray(buf);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);

    }

}
