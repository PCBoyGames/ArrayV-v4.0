package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Haruki

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Haruki
 *
 */
public class BubbleQuickSort extends Sort {

    public BubbleQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Bubble Quick");
        this.setRunAllSortsName("Bubble Quick Sort");
        this.setRunSortName("Bubble Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int medOf3(int[] array, int i0, int i1, int i2) {
        int t;
        if(Reads.compareIndices(array, i0, i1, 1, true) > 0) {
            t = i1;
            i1 = i0;
        } else
            t = i0;
        if(Reads.compareIndices(array, i1, i2, 1, true) > 0) {
            if(Reads.compareIndices(array, t, i2, 1, true) > 0)
                return t;
            return i2;
        }
        return i1;
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
    
    protected int pivCmpHelper(int v, int piv) {
        int c = Reads.compareValues(v, piv);
        if (c > 0) return 1;
        if (c < 0) return -1;
        return 0;
    }

    protected int pivCmp(int[] array, int a, int b, int piv) {
        Highlights.markArray(1, a);
        Highlights.markArray(2, b);
        int c1 = pivCmpHelper(array[a], piv);
        int c2 = pivCmpHelper(array[b], piv);
        if (c1 > c2) return 1;
        if (c1 < c2) return -1;
        return 0;
    }

    protected int[] partition(int[] array, int a, int b, int piv) {
        int c = 1;
        for (int i = b - 1; i > a; i -= c) {
            c = 1;
            for (int j = a; j < i; j++) {
                if (pivCmp(array, j, j + 1, piv) > 0) {
                    Writes.swap(array, j, j + 1, 1, true, false);
                    c = 1;
                } else
                    c++;
            }
        }
        int rIdx = binSearch(array, a, b, piv, false);
        return new int[] { binSearch(array, a, rIdx, piv, true), rIdx };
    }
    
    protected void sortHelper(int[] array, int a, int b) {
        while (b - a > 2) {
            int pivIdx = medOf3(array, a, a + (b - a) / 2, b - 1);
            int[] pr = partition(array, a, b, array[pivIdx]);
            if (pr[0] == a && pr[1] == b) return;
            if (b - pr[1] < pr[0] - a) {
                sortHelper(array, pr[1], b);
                b = pr[0];
            } else {
                sortHelper(array, a, pr[0]);
                a = pr[1];
            }
        }
        if (b - a == 2) {
            if (Reads.compareIndices(array, a, a + 1, 1, true) > 0)
                Writes.swap(array, a, a + 1, 1, true, false);
        }
    }
    
    public void quickSort(int[] array, int a, int b) {
        sortHelper(array, a, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
