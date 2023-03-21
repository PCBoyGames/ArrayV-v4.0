package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite and PCBoy

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author aphitorite
 * @author PCBoy
 *
 */
public final class UnstableTernarySingularityQuickSort extends Sort {

    public UnstableTernarySingularityQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Unstable Ternary Singularity Quick");
        this.setRunAllSortsName("Unstable Ternary Singularity Quick Sort");
        this.setRunSortName("Unstable Ternary Singularity Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public static int log2(int n) {
        int log = 0;
        while ((n >>= 1) != 0)
            ++log;
        return log;
    }

    int depthlimit;
    int insertlimit;
    int replimit;

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        for (int i = a; i > b; i--)
            Writes.write(array, i, array[i - 1], 0.5, true, false);
        if (a != b)
            Writes.write(array, b, temp, 0.5, true, false);
    }

    protected int expSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0)
            i *= 2;
        int a1 = Math.max(a, b - i + 1), b1 = b - i / 2;
        while (a1 < b1) {
            int m = a1 + (b1 - a1) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            if (Reads.compareValues(val, array[m]) < 0)
                b1 = m;
            else
                a1 = m + 1;
        }
        return a1;
    }

    protected void insertSort(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i++)
            insertTo(array, i, expSearch(array, a, i, array[i]));
    }

    protected int findReverseRun(int[] array, int start, int end) {
        int reverse = start;
        boolean different = false;
        int cmp = Reads.compareIndices(array, reverse, reverse + 1, 0.5, true);
        while (cmp >= 0 && reverse + 1 < end) {
            if (cmp == 1) different = true;
            reverse++;
            if (reverse + 1 < end)
                cmp = Reads.compareIndices(array, reverse, reverse + 1, 0.5, true);
        }
        if (reverse > start && different) {
            if (reverse < start + 3) Writes.swap(array, start, reverse, 0.75, true, false);
            else Writes.reversal(array, start, reverse, 0.75, true, false);
        }
        return reverse;
    }

    protected void sift(int[] array, int start, int root, int len, int tmp) {
        int j = root;
        while (2*j+1 < len) {
            j=2*j+1;
            if (j+1 < len && Reads.compareValues(array[start+j], array[start+j+1]) < 0) {
                j++;
            }
        }
        while (Reads.compareValueIndex(array, tmp, start+j, 0.25, true) > 0) {
            j=(j-1)/2;
        }
        for (int t2; j>root; j=(j-1)/2) {
            t2=array[start+j];
            Writes.write(array, start+j, tmp, 0.5, true, false);
            tmp=t2;
        }
        Writes.write(array, start+root, tmp, 0.5, true, false);
    }

    protected void heap(int[] array, int start, int end) {
        int p=end-start;
        for (int j=(p-1)/2; j>=0; j--) {
            this.sift(array, start, j, p, array[start+j]);
        }
        for (int j=p-1; j>0; j--) {
            int t=array[start+j];
            Writes.write(array, start+j, array[start], 1, true, false);
            this.sift(array, start, 0, j, t);
        }
    }

    // Easy patch to avoid self-swaps.
    public void swap(int[] array, int a, int b, double pause, boolean mark, boolean aux) {
        if (a != b)
            Writes.swap(array, a, b, pause, mark, aux);
    }

    protected int[] partition(int[] array, int a, int b, int piv) {
        //partition -> [a][E > piv][i][E == piv][j][E < piv][b]
        //returns   -> i and j ^
        int i1 = a, i = a-1, j = b, j1 = b;
        for (;;) {
            while (++i < j) {
                int cmp = Reads.compareIndexValue(array, i, piv, 0.5, true);
                if (cmp == 0) swap(array, i1++, i, 1, true, false);
                else if (cmp > 0) break;
            }
            Highlights.clearMark(2);
            while (--j > i) {
                int cmp = Reads.compareIndexValue(array, j, piv, 0.5, true);
                if (cmp == 0) swap(array, --j1, j, 1, true, false);
                else if (cmp < 0) break;
            }
            Highlights.clearMark(2);
            if (i < j) {
                // The patch is not needed here, because it never swaps when i == j.
                Writes.swap(array, i, j, 1, true, false);
                Highlights.clearMark(2);
            }
            else {
                if (i1 == b) return new int[] {a, b};
                else if (j < i) j++;
                while (i1 > a) swap(array, --i, --i1, 1, true, false);
                while (j1 < b) swap(array, j++, j1++, 1, true, false);
                break;
            }
        }
        return new int[] {i, j};
    }

    protected void sortHelper(int[] array, int a, int b, int depth, int rep, int d) {
        Writes.recordDepth(d);
        while (b - a > insertlimit) {
            if (depth >= depthlimit || rep >= 4) {
                heap(array, a, b);
                return;
            }
            int pIdx = a + 1;
            while (pIdx < b && Reads.compareIndices(array, pIdx - 1, pIdx, 0.125, true) <= 0)
                pIdx++;
            if (pIdx >= b)
                return;
            int[] p = partition(array, a, b, array[pIdx - 1]);
            if (p[1] - p[0] == b - a)
                return;
            rep = Math.min(b - p[1], p[0] - a) <= replimit ? rep + 1 : 0;
            depth++;
            if (b - p[1] < p[0] - a) {
                Writes.recursion();
                sortHelper(array, p[1], b, depth, rep, d + 1);
                b = p[0];
            } else {
                Writes.recursion();
                sortHelper(array, a, p[0], depth, rep, d + 1);
                a = p[1];
            }
        }
        insertSort(array, a, b);
    }

    public void quickSort(int[] array, int a, int b) {
        depthlimit = (int) Math.min(Math.sqrt(b - a), 2 * log2(b - a));
        insertlimit = Math.max(depthlimit / 2, 16);
        replimit = Math.max(depthlimit / 4, 2);
        if (findReverseRun(array, a, b) + 1 < b)
            sortHelper(array, a, b, 0, 0, 0);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
