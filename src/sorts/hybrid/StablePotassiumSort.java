package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite, Distray and Potassium

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author aphitorite
 * @author Distray
 * @author Potassium
 *
 */
public class StablePotassiumSort extends Sort {

    public StablePotassiumSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Stable Potassium");
        setRunAllSortsName("Stable Potassium Sort");
        setRunSortName("Stable Potassiumsort");
        setCategory("Hybrid Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);
    }

    protected void cycleReverse(int[] array, int a, int m, int b) {
        int p0 = a, p1 = m - 1, p2 = m, p3 = b - 1;
        int tmp;
        while (p0 < p1 && p2 < p3) {
            tmp = array[p1];
            Writes.write(array, p1--, array[p0], 0.5, true, false);
            Writes.write(array, p0++, array[p2], 0.5, true, false);
            Writes.write(array, p2++, array[p3], 0.5, true, false);
            Writes.write(array, p3--, tmp, 0.5, true, false);
        }
        while (p0 < p1) {
            tmp = array[p1];
            Writes.write(array, p1--, array[p0], 0.5, true, false);
            Writes.write(array, p0++, array[p3], 0.5, true, false);
            Writes.write(array, p3--, tmp, 0.5, true, false);
        }
        while (p2 < p3) {
            tmp = array[p2];
            Writes.write(array, p2++, array[p3], 0.5, true, false);
            Writes.write(array, p3--, array[p0], 0.5, true, false);
            Writes.write(array, p0++, tmp, 0.5, true, false);
        }
        if (p0 < p3) { // don't count reversals that don't do anything
            if (p3 - p0 >= 3)
                Writes.reversal(array, p0, p3, 1, true, false);
            else
                Writes.swap(array, p0, p3, 1, true, false);
            Highlights.clearMark(2);
        }
    }

    void multiSwap(int[] array, int a, int b, int len) {
        for (int i = 0; i < len; i++)
            Writes.swap(array, a + i, b + i, 1, true, false);
    }

    void multiSwapBW(int[] array, int a, int b, int len) {
        for (int i = len - 1; i >= 0; i--)
            Writes.swap(array, a + i, b + i, 1, true, false);
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

    protected void rotate(int[] array, int a, int m, int b) {
        Highlights.clearAllMarks();
        if (a >= m || m >= b)
            return;
        int l = m - a, r = b - m;
        if (l % r == 0 || r % l == 0) {
            while (l > 1 && r > 1) {
                if (r < l) {
                    this.multiSwap(array, m - r, m, r);
                    b -= r;
                    m -= r;
                    l -= r;
                } else {
                    this.multiSwap(array, a, m, l);
                    a += l;
                    m += l;
                    r -= l;
                }
            }
            if (r == 1)
                this.insertTo(array, m, a);
            else if (l == 1)
                this.insertTo(array, a, b - 1);
        } else
            cycleReverse(array, a, m, b);
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

    protected int expSearch(int[] array, int a, int b, int val, boolean dir, boolean left) {
        int i = 1;
        int a1, b1;
        if (dir) {
            if (left)
                while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) > 0)
                    i *= 2;
            else
                while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0)
                    i *= 2;
            a1 = a + i / 2;
            b1 = Math.min(b, a - 1 + i);
        } else {
            if (left)
                while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0)
                    i *= 2;
            else
                while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0)
                    i *= 2;
            a1 = Math.max(a, b - i + 1);
            b1 = b - i / 2;
        }
        return binSearch(array, a1, b1, val, left);
    }

    protected void insertSort(int[] array, int a, int b) {
        int i = a + 1;
        if (i >= b)
            return;
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
        for (; i < b; i++)
            insertTo(array, i, expSearch(array, a, i, array[i], false, false));
    }

    protected void inPlaceMergeFW(int[] array, int a, int m, int b) {
        while (a < m && m < b) {
            int i = expSearch(array, m, b, array[a], true, true);
            rotate(array, a, m, i);
            int t = i - m;
            m = i;
            a += t + 1;
            if (a >= m)
                break;
            a = expSearch(array, a, m, array[m], true, false);
        }
    }

    protected void inPlaceMergeBW(int[] array, int a, int m, int b) {
        while (b > m && m > a) {
            int i = expSearch(array, a, m, array[b - 1], false, false);
            rotate(array, i, m, b);
            int t = m - i;
            m = i;
            b -= t + 1;
            if (m <= a)
                break;
            b = expSearch(array, m, b, array[m - 1], false, true);
        }
    }

    protected boolean merge(int[] array, int a, int m, int b) {
        if (Reads.compareValues(array[m - 1], array[m]) <= 0)
            return false;
        a = expSearch(array, a, m, array[m], true, false);
        b = expSearch(array, m, b, array[m - 1], false, true);
        if (m - a > b - m)
            inPlaceMergeBW(array, a, m, b);
        else
            inPlaceMergeFW(array, a, m, b);
        return true;
    }

    protected boolean diamondMerge(int[] array, int a, int m, int b, int block) {
        if (a >= m || m >= b)
            return true;
        if (Math.min(m - a, b - m) <= block)
            return merge(array, a, m, b);
        int q = (Math.min(m - a, b - m) - 1) / 2 + 1;
        if (diamondMerge(array, m - q, m, m + q, block)) {
            diamondMerge(array, a, m - q, m, block);
            diamondMerge(array, m, m + q, b, block);
            diamondMerge(array, a + q, m, b - q, block);
            return true;
        }
        return false;
    }

    protected void stoogeSort(int[] array, int a, int b, int block) {
        if (b - a <= block) {
            insertSort(array, a, b);
            return;
        }
        int m = a + (b - a) / 2;
        stoogeSort(array, a, m, block);
        stoogeSort(array, m, b, block);
        diamondMerge(array, a, m, b, block);
    }

    protected int[] partition(int[] array, int a, int b, int piv) {
        if (b - a < 2) {
            int[] court = new int[] { a, a };
            int cmp = Reads.compareValues(array[a], piv);
            if (cmp < 0) {
                court[0]++;
                court[1]++;
            } else if (cmp == 0)
                court[1]++;
            return court;
        }
        int m = a + (b - a) / 2;
        int[] l = partition(array, a, m, piv);
        int[] r = partition(array, m, b, piv);
        int l1 = l[0] - a, l2 = l[1] - l[0];
        int r1 = r[0] - m, r2 = r[1] - r[0];
        rotate(array, a + l1, m, m + r1);
        rotate(array, a + l1 + l2 + r1, m + r1, m + r1 + r2);
        return new int[] { a + l1 + r1, a + l1 + r1 + l2 + r2 };
    }

    protected int medOf3(int[] array, int l0, int l1, int l2) {
        int t;
        if (Reads.compareIndices(array, l0, l1, 1, true) > 0) {
            t = l0;
            l0 = l1;
            l1 = t;
        }
        if (Reads.compareIndices(array, l1, l2, 1, true) > 0) {
            t = l1;
            l1 = l2;
            l2 = t;
            if (Reads.compareIndices(array, l0, l1, 1, true) > 0)
                return l0;
        }
        return l1;
    }

    // median of medians with customizable depth
    protected int medOfMed(int[] array, int start, int end, int depth) {
        if (end - start < 9 || depth <= 0)
            return medOf3(array, start, start + (end - start) / 2, end);
        int div = (end - start) / 8;
        int m0 = medOfMed(array, start, start + 2 * div, --depth);
        int m1 = medOfMed(array, start + 3 * div, start + 5 * div, depth);
        int m2 = medOfMed(array, start + 6 * div, end, depth);
        return medOf3(array, m0, m1, m2);
    }

    public boolean getSortedRuns(int[] array, int a, int b) {
        Highlights.clearAllMarks();
        boolean reverseSorted = true;
        boolean sorted = true;
        int comp;

        for (int i = a; i < b - 1; i++) {
            comp = Reads.compareIndices(array, i, i + 1, 0.5, true);
            if (comp > 0)
                sorted = false;
            else
                reverseSorted = false;
            if ((!reverseSorted) && (!sorted))
                return false;
        }

        if (reverseSorted && !sorted) {
            Writes.reversal(array, a, b - 1, 1, true, false);
            sorted = true;
        }

        return sorted;
    }

    protected void sortHelper(int[] array, int a, int b, int threshold) {
        while (b - a > threshold) {
            if (getSortedRuns(array, a, b))
                return;
            int pIdx = medOfMed(array, a, b - 1, (int) (Math.log(b - a) / Math.log(6)));
            int[] p = partition(array, a, b, array[pIdx]);
            if (p[1] - p[0] == b - a)
                return;
            if (b - p[1] < p[0] - a) {
                sortHelper(array, p[1], b, threshold);
                b = p[0];
            } else {
                sortHelper(array, a, p[0], threshold);
                a = p[1];
            }
        }
        stoogeSort(array, a, b, (int) Math.sqrt(b - a - 1) + 1);
    }

    public void sort(int[] array, int a, int b) {
        sortHelper(array, a, b, (int) Math.pow(b - a, 0.67));
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
