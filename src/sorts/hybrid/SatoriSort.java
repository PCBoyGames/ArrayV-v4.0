package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

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
public class SatoriSort extends Sort {

    public SatoriSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Satori");
        this.setRunAllSortsName("Satori Sort");
        this.setRunSortName("Satorisort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    static int ceilCbrt(int n) {
        int r = 0;
        while (r * r * r < n) r++;
        return r;
    }

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d) Writes.write(array, i, array[i + d], 0.5, true, false);
        if (a != b) Writes.write(array, b, temp, 0.5, true, false);
    }

    protected void rotate(int[] array, int a, int m, int b) {
        Highlights.clearAllMarks();
        IndexedRotations.cycleReverse(array, a, m, b, 1, true, false);
    }

    protected int binSearch(int[] array, int a, int b, int val, boolean left) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            int c = Reads.compareValues(val, array[m]);
            if (c < 0 || (left && c == 0)) b = m;
            else                           a = m + 1;
        }
        return a;
    }

    protected int leftExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >  0) i *= 2;
        else      while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0) i *= 2;
        int a1 = a + i / 2, b1 = Math.min(b, a - 1 + i);
        return binSearch(array, a1, b1, val, left);
    }

    protected int rightExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0) i *= 2;
        else      while (b - i >= a && Reads.compareValues(val, array[b - i]) <  0) i *= 2;
        int a1 = Math.max(a, b - i + 1), b1 = b - i / 2;
        return binSearch(array, a1, b1, val, left);
    }

    protected boolean checkReverseBounds(int[] array, int a, int m, int b) {
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return true;
        }
        return false;
    }

    protected boolean boundCheck(int[] array, int a, int m, int b) {
        if (a >= m || m >= b) return true;
        return Reads.compareValues(array[m - 1], array[m]) <= 0 || checkReverseBounds(array, a, m, b);
    }

    protected boolean buildRuns(int[] array, int a, int b, int mRun) {
        int i = a + 1, j = a;
        boolean noSort = true;
        while (i < b) {
            if (Reads.compareIndices(array, i - 1, i++, 1, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) > 0) i++;
                if (i - j < 4) Writes.swap(array, j, i - 1, 1.0, true, false);
                else Writes.reversal(array, j, i - 1, 1.0, true, false);
            } else while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) <= 0) i++;
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

    protected void inPlaceMergeFW(int[] array, int a, int m, int b) {
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

    protected void inPlaceMergeBW(int[] array, int a, int m, int b) {
        while (b > m && m > a) {
            int i = rightExpSearch(array, a, m, array[b - 1], false);
            rotate(array, i, m, b);
            int t = m - i;
            m = i;
            b -= t + 1;
            if (m <= a) break;
            b = rightExpSearch(array, m, b, array[m - 1], true);
        }
    }

    public void inPlaceMerge(int[] array, int a, int m, int b, boolean bnd) {
        if (bnd) {
            if (boundCheck(array, a, m, b)) return;
            a = leftExpSearch(array, a, m, array[m], false);
            b = rightExpSearch(array, m, b, array[m - 1], true);
            if (checkReverseBounds(array, a, m, b)) return;
        }
        if (b - m <= m - a) inPlaceMergeBW(array, a, m, b);
        else                inPlaceMergeFW(array, a, m, b);
    }

    protected void fragmentedMergeFW(int[] array, int a, int m, int b, int s) {
        while (m - a > s) {
            int rPos;
            int dist;
            int a1 = a + s;
            rPos = binSearch(array, m, b, array[a1], true);
            rotate(array, a1, m, rPos);
            dist = rPos - m;
            a1 += dist;
            m += dist;
            inPlaceMerge(array, a, a1 - dist, a1, true);
            a = a1;
        }
        inPlaceMerge(array, a, m, b, true);
    }

    protected void fragmentedMergeBW(int[] array, int a, int m, int b, int s) {
        while (b - m > s) {
            int rPos;
            int dist;
            int b1 = b - s;
            rPos = binSearch(array, a, m, array[b1 - 1], false);
            rotate(array, rPos, m, b1);
            dist = m - rPos;
            b1 -= dist;
            m -= dist;
            inPlaceMerge(array, b1, b1 + dist, b, true);
            b = b1;
        }
        inPlaceMerge(array, a, m, b, true);
    }

    protected void fragmentedMerge(int[] array, int a, int m, int b, int s) {
        if (boundCheck(array, a, m, b)) return;
        a = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if (checkReverseBounds(array, a, m, b)) return;
        if (b - m <= m - a) fragmentedMergeBW(array, a, m, b, s);
        else                fragmentedMergeFW(array, a, m, b, s);
    }

    public void mergeSort(int[] array, int a, int b) {
        int len = b - a;
        if (len < 32) {
            // insertion sort
            buildRuns(array, a, b, b - a);
            return;
        }
        int s = ceilCbrt(len), s1 = s*s;
        if (buildRuns(array, a, b, s)) return;
        for (int i = a + s, j = a; i < b; i += s) {
            if (i - j == s1) {
                j += s1;
                i += s;
            }
            inPlaceMerge(array, j, i, Math.min(i + s, b), true);
        }
        for (int i = a + s1; i < b; i += s1) {
            fragmentedMerge(array, a, i, Math.min(i + s1, b), s);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);

    }

}
