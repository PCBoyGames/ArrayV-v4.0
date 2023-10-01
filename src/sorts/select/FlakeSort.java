package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 *
 */
public class FlakeSort extends Sort {

    public FlakeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Flake");
        this.setRunAllSortsName("Flake Sort");
        this.setRunSortName("Flakesort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int medOf3(int[] array, int i0, int i1, int i2) {
        int tmp;
        if (Reads.compareIndices(array, i0, i1, 1, true) > 0) {
            tmp = i1;
            i1 = i0;
        } else tmp = i0;
        if (Reads.compareIndices(array, i1, i2, 1, true) > 0) {
            if (Reads.compareIndices(array, tmp, i2, 1, true) > 0) return tmp;
            return i2;
        }
        return i1;
    }

    // Easy patch to avoid self-swaps.
    public void swap(int[] array, int a, int b, double pause, boolean mark, boolean aux) {
        if (a != b) Writes.swap(array, a, b, pause, mark, aux);
    }

    // partition -> [a][E > piv][i][E == piv][j][E < piv][b]
    // returns -> i and j ^
    public int[] partition(int[] array, int a, int b, int piv) {
        int i1 = a, i = a - 1, j = b, j1 = b;
        while (true) {
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
            if (i >= j) {
                if (i1 == b) return new int[] { a, b };
                if (j < i) j++;
                if (i1 - a > i - i1) {
                    int i2 = i;
                    i = a;
                    while (i1 < i2) swap(array, i++, i1++, 1, true, false);
                } else while (i1 > a) swap(array, --i, --i1, 1, true, false);
                if (b - j1 > j1 - j) {
                    int j2 = j;
                    j = b;
                    while (j1 > j2) swap(array, --j, --j1, 1, true, false);
                } else while (j1 < b) swap(array, j++, j1++, 1, true, false);
                return new int[] { i, j };
            }
            // The patch is not needed here, because it never swaps when i == j.
            Writes.swap(array, i, j, 1, true, false);
            Highlights.clearMark(2);
        }
    }

    boolean pivCmp(int[] array, int i, int piv, boolean seekHi) {
        int cmp = Reads.compareIndexValue(array, i, piv, 0, true);
        return seekHi ? cmp < 0 : cmp > 0;
    }

    void selectionSort(int[] array, int a, int b, int p, int pivDisqualify, boolean seekHi) {
        if (b - a < 2) return;
        for (int i = a; i < b; i++) {
            int sel = a;
            while (pivCmp(array, sel, pivDisqualify, seekHi)) sel++;
            for (int j = sel + 1; j < b; j++)
                if (!pivCmp(array, j, pivDisqualify, seekHi))
                    if (Reads.compareIndices(array, j, sel, 0.05, true) < 0) sel = j;
            Writes.swap(array, p + (i - a), sel, 0.5, true, false);
        }
        for (int i = a; i < b; i++) Writes.swap(array, i, p + (i - a), 0.5, true, false);
    }

    void selectionSort(int[] array, int a, int b) {
        for (int i = a; i < b - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < b; j++)
                if (Reads.compareIndices(array, minIdx, j, 0.05, true) > 0) minIdx = j;
            if (minIdx != i) Writes.swap(array, i, minIdx, 0.5, true, false);
        }
    }

    public void sort(int[] array, int a, int b) {
        while (b - a > 8) {
            int pIdx = medOf3(array, a, a + (b - a - 1) / 2, b - 1);
            int piv = array[pIdx];
            int[] pr = partition(array, a, b, piv);
            if (pr[1] - pr[0] == b - a) return;
            if (b - pr[1] < pr[0] - a) {
                selectionSort(array, pr[1], b, a, piv, true);
                b = pr[0];
            } else {
                selectionSort(array, a, pr[0], pr[1], piv, false);
                a = pr[1];
            }
        }
        selectionSort(array, a, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
