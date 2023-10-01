package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class TernaryIndexQuickSort extends Sort {

    public TernaryIndexQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Ternary Index Quick");
        this.setRunAllSortsName("Ternary Index Quick Sort");
        this.setRunSortName("Ternary Index Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // median of 3
    protected int medOf3(int[] array, int l0, int l1, int l2) {
        int t;
        if (Reads.compareIndices(array, l0, l1, 1, true) > 0) {
            t = l0; l0 = l1; l1 = t;
        }
        if (Reads.compareIndices(array, l1, l2, 1, true) > 0) {
            t = l1; l1 = l2; l2 = t;
            if (Reads.compareIndices(array, l0, l1, 1, true) > 0) {
                return l0;
            }
        }
        return l1;
    }

    // median of medians with customizable depth
    protected int medOfMed(int[] array, int start, int end, int depth) {
        if (end-start < 9 || depth <= 0) {
            return medOf3(array, start, start+(end-start)/2, end);
        }
        int div = (end - start) / 8;
        int m0 = medOfMed(array, start, start + 2 * div, --depth);
        int m1 = medOfMed(array, start + 3 * div, start + 5 * div, depth);
        int m2 = medOfMed(array, start + 6 * div, end, depth);
        return medOf3(array, m0, m1, m2);
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

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], 0.5, true, false);
        if (a != b)
            Writes.write(array, b, temp, 0.5, true, false);
    }

    public void insertSort(int[] array, int a, int b) {
        int i = a + 1;
        if (i < b) {
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
        }
        Highlights.clearMark(2);
        for (; i < b; i++)
            insertTo(array, i, expSearch(array, a, i, array[i]));
    }

    protected void indexSort(int[] array, int[] idx, int a, int b) {
        for (int i = 0; i < b - a; i++) {
            int nxt = idx[i];
            int tmp = array[a + i];
            boolean change = false;
            while (Reads.compareOriginalValues(i, nxt) != 0) {
                // Writes.swap(array, a + i, a + nxt, 0.5, true, false);
                int tmp1 = array[a + nxt];
                // array[a + nxt] = tmp;
                Writes.write(array, a + nxt, tmp, 0.5, true, false);
                tmp = tmp1;
                int tmp2 = idx[nxt];
                Writes.write(idx, nxt, nxt, 0.5, false, true);
                nxt = tmp2;
                change = true;
            }
            if (change) {
                Writes.write(array, a + i, tmp, 0.5, true, false);
                Writes.write(idx, i, nxt, 0.5, false, true);
            }
        }
    }

    protected int[] partition(int[] array, int[] idx, int a, int b, int piv) {
        int[] ptrs = new int[4];
        ptrs[0] = a;
        for (int i = a; i < b; i++) {
            int cmp = Reads.compareIndexValue(array, i, piv, 0.5, true);
            if (cmp < 0) {
                Writes.write(idx, i - a, 0, 0.5, false, true);
                ptrs[0]++;
            } else if (cmp == 0) {
                Writes.write(idx, i - a, 1, 0.5, false, true);
                ptrs[1]++;
            } else {
                Writes.write(idx, i - a, 2, 0.5, false, true);
                ptrs[2]++;
            }
        }
        for (int i = 1; i < ptrs.length;i++) {
            ptrs[i] += ptrs[i-1];
        }
        for (int i = b - a - 1; i >= 0; i--)
            Writes.write(idx, i, --ptrs[idx[i]] - a, 0.5, false, true);
        indexSort(array, idx, a, b);
        return new int[] {ptrs[1], ptrs[2]};
    }

    protected void sortHelper(int[] array, int[] idx, int a, int b) {
        while (b - a > 16) {
            int pIdx = medOfMed(array, a, b - 1, (int) (Math.log(b - a) / Math.log(9)));
            int[] p = partition(array, idx, a, b, array[pIdx]);
            if (p[1] - p[0] == b - a)
                return;
            if (b - p[1] < p[0] - a) {
                sortHelper(array, idx, p[1], b);
                b = p[0];
            } else {
                sortHelper(array, idx, a, p[0]);
                a = p[1];
            }
        }
        insertSort(array, a, b);
    }

    public void quickSort(int[] array, int a, int b) {
        int[] idx = Writes.createExternalArray(b - a);
        sortHelper(array, idx, a, b);
        Writes.deleteExternalArray(idx);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
