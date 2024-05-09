package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Harumi
in collaboration with aphitorite and Gaming32

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Harumi
 * @author aphitorite
 * @author Gaming32
 *
 */
public final class MiniTimSort extends Sort {

    public MiniTimSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Mini Tim");
        this.setRunAllSortsName("Mini Tim Sort");
        this.setRunSortName("Mini Timsort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    public static int getMinLevel(int n) {
        while (n >= 32) n = (n - 1) / 2 + 1;
        return n;
    }

    final int M = 7;

    int highlight = 0;

    protected int leftBinSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, highlight + m);
            Delays.sleep(0.5);
            if (Reads.compareValues(val, array[m]) <= 0)
                b = m;
            else
                a = m + 1;
        }
        return a;
    }

    protected int rightBinSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, highlight + m);
            Delays.sleep(0.5);
            if (Reads.compareValues(val, array[m]) < 0)
                b = m;
            else
                a = m + 1;
        }
        return a;
    }

    protected int leftExpSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) > 0) i *= 2;
        return leftBinSearch(array, a + i / 2, Math.min(b, a - 1 + i), val);
    }

    protected int rightExpSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0) i *= 2;
        return rightBinSearch(array, Math.max(a, b - i + 1), b - i / 2, val);
    }

    protected int leftBoundSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0) i *= 2;
        return rightBinSearch(array, a + i / 2, Math.min(b, a - 1 + i), val);
    }

    protected int rightBoundSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0) i *= 2;
        return leftBinSearch(array, Math.max(a, b - i + 1), b - i / 2, val);
    }

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        if (a > b) {
            int temp = array[a];
            do
                Writes.write(array, a, array[--a], 0.25, true, false);
            while (a > b);
            Writes.write(array, b, temp, 0.25, true, false);
        }
    }

    protected void insertSort(int[] array, int a, int b) {
        int i = a + 1;
        if (i >= b) return;
        if (Reads.compareIndices(array, i - 1, i++, 0.5, true) > 0) {
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0) i++;
            if (i - a < 4) Writes.swap(array, a, i - 1, 1.0, true, false);
            else Writes.reversal(array, a, i - 1, 1.0, true, false);
        } else while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0) i++;
        Highlights.clearMark(2);
        for (; i < b; i++) insertTo(array, i, rightExpSearch(array, a, i, array[i]));
    }

    // galloping mode code refactored from TimSorting.java
    protected void mergeFW(int[] array, int[] tmp, int a, int m, int b) {
        int len1 = m - a, t = a;
        Highlights.clearMark(2);
        Writes.arraycopy(array, a, tmp, 0, len1, 1, true, true);
        int i = 0, mGallop = M, l = 0, r = 0;
        while (true) {
            do
                if (Reads.compareValues(tmp[i], array[m]) <= 0) {
                    Writes.write(array, a++, tmp[i++], 1, true, false);
                    l++;
                    r = 0;
                    if (i == len1)
                        return;
                } else {
                    Highlights.markArray(2, m);
                    Writes.write(array, a++, array[m++], 1, true, false);
                    r++;
                    l = 0;
                    if (m == b) {
                        while (i < len1)
                            Writes.write(array, a++, tmp[i++], 1, true, false);
                        return;
                    }
                }
            while ((l | r) < mGallop);
            do {
                l = leftExpSearch(array, m, b, tmp[i]) - m;
                for (int j = 0; j < l; j++)
                    Writes.write(array, a++, array[m++], 1, true, false);
                Writes.write(array, a++, tmp[i++], 1, true, false);
                if (i == len1)
                    return;
                if (m == b) {
                    while (i < len1)
                        Writes.write(array, a++, tmp[i++], 1, true, false);
                    return;
                }
                highlight = t;
                r = leftBoundSearch(tmp, i, len1, array[m]) - i;
                highlight = 0;
                for (int j = 0; j < r; j++)
                    Writes.write(array, a++, tmp[i++], 1, true, false);
                Writes.write(array, a++, array[m++], 1, true, false);
                if (i == len1)
                    return;
                if (m == b) {
                    while (i < len1)
                        Writes.write(array, a++, tmp[i++], 1, true, false);
                    return;
                }
                mGallop--;
            } while ((l | r) >= M);
            if (mGallop < 0)
                mGallop = 0;
            mGallop += 2;
        }
    }

    protected void mergeBW(int[] array, int[] tmp, int a, int m, int b) {
        int len2 = b - m, t = a;
        Highlights.clearMark(2);
        Writes.arraycopy(array, m, tmp, 0, len2, 1, true, true);
        int i = len2 - 1, mGallop = M, l = 0, r = 0;
        m--;
        while (true) {
            do
                if (Reads.compareValues(tmp[i], array[m]) >= 0) {
                    Writes.write(array, --b, tmp[i--], 1, true, false);
                    l++;
                    r = 0;
                    if (i < 0)
                        return;
                } else {
                    Highlights.markArray(2, m);
                    Writes.write(array, --b, array[m--], 1, true, false);
                    r++;
                    l = 0;
                    if (m < a) {
                        while (i >= 0)
                            Writes.write(array, --b, tmp[i--], 1, true, false);
                        return;
                    }
                }
            while ((l | r) < mGallop);
            do {
                l = (m + 1) - rightExpSearch(array, a, m + 1, tmp[i]);
                for (int j = 0; j < l; j++)
                    Writes.write(array, --b, array[m--], 1, true, false);
                Writes.write(array, --b, tmp[i--], 1, true, false);
                if (i < 0)
                    return;
                if (m < a) {
                    while (i >= 0)
                        Writes.write(array, --b, tmp[i--], 1, true, false);
                    return;
                }
                highlight = t;
                r = (i + 1) - rightBoundSearch(tmp, 0, i + 1, array[m]);
                highlight = 0;
                for (int j = 0; j < r; j++)
                    Writes.write(array, --b, tmp[i--], 1, true, false);
                Writes.write(array, --b, array[m--], 1, true, false);
                if (i < 0)
                    return;
                if (m < a) {
                    while (i >= 0)
                        Writes.write(array, --b, tmp[i--], 1, true, false);
                    return;
                }
            } while ((l | r) >= M);
            if (mGallop < 0)
                mGallop = 0;
            mGallop += 2;
        }
    }

    public void smartMerge(int[] array, int[] tmp, int a, int m, int b) {
        if (Reads.compareValues(array[m - 1], array[m]) <= 0)
            return;
        a = leftBoundSearch(array, a, m, array[m]);
        b = rightBoundSearch(array, m, b, array[m - 1]);
        if (b - m < m - a)
            mergeBW(array, tmp, a, m, b);
        else
            mergeFW(array, tmp, a, m, b);
    }

    protected int findRun(int[] array, int a, int b, int mRun) {
        int i = a + 1;
        boolean dir;
        if (i < b)
            dir = Reads.compareIndices(array, i - 1, i++, 0.5, true) <= 0;
        else
            dir = true;
        if (dir)
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0)
                i++;
        else {
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0)
                i++;
            if (i - a < 4)
                Writes.swap(array, a, i - 1, 1.0, true, false);
            else
                Writes.reversal(array, a, i - 1, 1.0, true, false);
        }
        Highlights.clearMark(2);
        while (i - a < mRun && i < b) {
            insertTo(array, i, rightExpSearch(array, a, i, array[i]));
            i++;
        }
        return i;
    }
    
    public void mergeSortWithBuf(int[] array, int[] buf, int a, int b) {
        if (b - a < 32) {
            insertSort(array, a, b);
            return;
        }
        int mRun = getMinLevel(b - a);
        int[] runs = Writes.createExternalArray((b - a - 1) / mRun + 2);
        int r = a, rf = 0;
        while (r < b) {
            Writes.write(runs, rf++, r, 0.5, false, true);
            r = findRun(array, r, b, mRun);
        }
        Writes.write(runs, rf, b, 0.5, false, true);
        while (rf > 1) {
            int j = 0;
            for (int i = 0; i < rf; i+=2, j++) {
                if (i + 1 < rf) smartMerge(array, buf, runs[i], runs[i+1], i + 2 >= rf ? b : runs[i + 2]);
                if (j != i) Writes.write(runs, j, runs[i], 0.5, false, true);
            }
            rf = j;
        }
        Writes.deleteExternalArray(runs);
    }
    
    public void mergeSort(int[] array, int a, int b) {
        int[] buf = Writes.createExternalArray((b - a) / 2);
        mergeSortWithBuf(array, buf, a, b);
        Writes.deleteExternalArray(buf);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);

    }

}
