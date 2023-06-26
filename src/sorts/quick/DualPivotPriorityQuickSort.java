package sorts.quick;

import java.util.PriorityQueue;

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
public final class DualPivotPriorityQuickSort extends Sort {

    public DualPivotPriorityQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Dual-Pivot Priority Quick");
        this.setRunAllSortsName("Dual-Pivot Priority Quick Sort");
        this.setRunSortName("Dual-Pivot Priority Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    class Partition implements Comparable<Partition> {
        public int a, b;
        public int divisor;

        public Partition(int a, int b, int divisor) {
            this.a = a;
            this.b = b;
            this.divisor = divisor;
        }

        public int length() {
            return this.b - this.a;
        }

        @Override
        public int compareTo(Partition y) {
            int len0 = this.length(), len1 = y.length();
            if (len0 < len1)
                return 1;
            if (len0 > len1)
                return -1;
            return 0;
        }
    }

    int threshold = 32;

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        if (a != b) {
            int temp = array[a];
            int d = (a > b) ? -1 : 1;
            for (int i = a; i != b; i += d)
                Writes.write(array, i, array[i + d], 0.5, true, false);
            Writes.write(array, b, temp, 0.5, true, false);
        }
    }

    protected int expSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0) i *= 2;
        int a1 = Math.max(a, b - i + 1), b1 = b - i / 2;
        while (a1 < b1) {
            int m = a1 + (b1 - a1) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            if (Reads.compareValues(val, array[m]) < 0) b1 = m;
            else a1 = m + 1;
        }
        return a1;
    }

    protected void insertSort(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i++)
            insertTo(array, i, expSearch(array, a, i, array[i]));
    }

    // Easy patch to avoid self-swaps.
    public void swap(int[] array, int a, int b, double pause, boolean mark, boolean aux) {
        if (a != b) Writes.swap(array, a, b, pause, mark, aux);
    }
    
    protected int[] partitionTernary(int[] array, int a, int b, int piv) {
        int i = a, j = b;
        for (int k = i; k < j; k++) {
            if (Reads.compareIndexValue(array, k, piv, 0.5, true) < 0)
                swap(array, k, i++, 0.5, true, false);
            else if (Reads.compareIndexValue(array, k, piv, 0.5, true) > 0) {
                do {
                    j--;
                    Highlights.markArray(3, j);
                    Delays.sleep(0.5);
                } while (k < j && Reads.compareIndexValue(array, j, piv, 0.5, false) > 0);
                swap(array, k, j, 0.5, true, false);
                if (Reads.compareIndexValue(array, k, piv, 0.5, true) < 0)
                    swap(array, k, i++, 0.5, true, false);
            }
        }
        Highlights.clearAllMarks();
        return new int[] { i, j };
    }

    protected int[] partition(int[] array, int a, int b, int piv1, int piv2) {
        int i = a, j = b;
        for (int k = i; k < j; k++) {
            if (Reads.compareIndexValue(array, k, piv1, 0.5, true) <= 0)
                swap(array, k, i++, 0.5, true, false);
            else if (Reads.compareIndexValue(array, k, piv2, 0.5, true) >= 0) {
                do {
                    j--;
                    Highlights.markArray(3, j);
                    Delays.sleep(0.5);
                } while (k < j && Reads.compareIndexValue(array, j, piv2, 0.5, false) >= 0);
                swap(array, k, j, 0.5, true, false);
                if (Reads.compareIndexValue(array, k, piv1, 0.5, true) <= 0)
                    swap(array, k, i++, 0.5, true, false);
            }
        }
        Highlights.clearAllMarks();
        return new int[] { i, j };
    }
    
    void consumePartition(int[] array, PriorityQueue<Partition> queue, int a, int b, int d) {
        if (b - a > threshold) queue.offer(new Partition(a, b, d));
        else insertSort(array, a, b);
    }

    /*
     * Taihennami's trick with Dual-Pivot Quicksort:
     * - If pivot1 == pivot2:
     *   partition: [E < pivot1][E == pivot1][E > pivot1]
     *   recurse on [E < pivot1] and [E > pivot1]
     * - Otherwise:
     *   partition: [E <= pivot1][pivot1 < E < pivot2][E >= pivot2]
     *   recurse on [E <= pivot1], [pivot1 < E < pivot2] and [E >= pivot2]
     */
    void innerSort(int[] array, int left, int right) {
        if (right - left <= this.threshold) {
            insertSort(array, left, right);
            return;
        }
        PriorityQueue<Partition> queue = new PriorityQueue<>((right - left - 1) / this.threshold + 1);
        queue.offer(new Partition(left, right, 3));
        while (queue.size() > 0) {
            Partition part = queue.poll();
            int a = part.a, b = part.b, n = part.length(), d = part.divisor;
            int piv1, piv2, s = n / d;
            int pivCmp = Reads.compareIndices(array, a + s, b - 1 - s, 0, false);
            if (pivCmp > 0) {
                piv1 = array[b - 1 - s];
                piv2 = array[a + s];
            } else {
                piv1 = array[a + s];
                piv2 = array[b - 1 - s];
            }
            int[] pr;
            if (pivCmp == 0) pr = partitionTernary(array, a, b, piv1);
            else pr = partition(array, a, b, piv1, piv2);
            int m1 = pr[0], m2 = pr[1];
            if (pivCmp == 0 && m2 - m1 == b - a) continue;
            if (Math.min(m1 - a, Math.min(m2 - m1, b - m2)) <= this.threshold) d++;
            consumePartition(array, queue, a, m1, d);
            if (pivCmp != 0)
                consumePartition(array, queue, m1, m2, d);
            consumePartition(array, queue, m2, b, d);
        }
    }
    
    public void quickSort(int[] array, int a, int b) {
        int z = 0, e = 0;
        for (int i = a; i < b - 1; i++) {
            int cmp = Reads.compareIndices(array, i, i + 1, 0.5, true);
            z += cmp > 0 ? 1 : 0;
            e += cmp == 0 ? 1 : 0;
        }
        if (z == 0) return;
        if (z + e == b - a - 1) {
            if (b - a < 4) Writes.swap(array, a, b - 1, 0.75, true, false);
            else Writes.reversal(array, a, b - 1, 0.75, true, false);
            return;
        }
        innerSort(array, a, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
