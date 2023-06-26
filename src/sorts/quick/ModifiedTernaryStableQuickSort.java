package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class ModifiedTernaryStableQuickSort extends Sort {

    public ModifiedTernaryStableQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Modified Ternary Stable Quick");
        this.setRunAllSortsName("Modified Ternary Stable Quick Sort");
        this.setRunSortName("Modified Ternary Stable Quicksort");
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

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], 0.5, true, false);
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

    int pivCmp(int v, int piv) {
        int c = Reads.compareValues(v, piv);
        if (c > 0)
            return 2;
        if (c < 0)
            return 0;
        return 1;
    }

    protected int[] partition(int[] array, int[] buf, int a, int b, int piv) {
        int[] ptrs = new int[4];
        for (int i = a; i < b; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.5);
            Writes.write(buf, i - a, array[i], 0.5, false, true);
            int c = pivCmp(array[i], piv);
            ptrs[c]++;
        }
        for (int i = 1; i < ptrs.length; i++) {
            ptrs[i] += ptrs[i-1];
        }
        for (int i = b - a - 1; i >= 0; i--) {
            int c = pivCmp(buf[i], piv);
            Writes.write(array, a + --ptrs[c], buf[i], 1, true, false);
        }
        for (int i = 1; i < ptrs.length; i++)
            ptrs[i] += a;
        return new int[] {ptrs[1], ptrs[2]};
    }

    protected void sortHelper(int[] array, int[] buf, int a, int b) {
        while (b - a > 16) {
            int pIdx = medOfMed(array, a, b - 1, (int) (Math.log(b - a) / Math.log(9)));
            int[] p = partition(array, buf, a, b, array[pIdx]);
            if (p[1] - p[0] == b - a)
                return;
            if (b - p[1] < p[0] - a) {
                sortHelper(array, buf, p[1], b);
                b = p[0];
            } else {
                sortHelper(array, buf, a, p[0]);
                a = p[1];
            }
        }
        insertSort(array, a, b);
    }

    public void quickSort(int[] array, int a, int b) {
        int[] buf = Writes.createExternalArray(b - a);
        sortHelper(array, buf, a, b);
        Writes.deleteExternalArray(buf);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
