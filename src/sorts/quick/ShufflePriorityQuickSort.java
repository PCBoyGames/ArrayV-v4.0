package sorts.quick;

import java.util.Random;

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
public class ShufflePriorityQuickSort extends Sort {

    public ShufflePriorityQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Shuffle Priority Quick");
        this.setRunAllSortsName("Shuffle Priority Quick Sort");
        this.setRunSortName("Shuffle Priority Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    class Partition implements Comparable<Partition> {
        public int a, b, badAllowed;

        public Partition(int a, int b, int badAllowed) {
            this.a = a;
            this.b = b;
            this.badAllowed = badAllowed;
        }

        public int length() {
            return this.b - this.a;
        }

        public int compare(Partition y) {
            return (this.length() < y.length()) ? 1 : -1;
        }

        @Override
        public int compareTo(Partition y) {
            return (this.length() < y.length()) ? 1 : -1;
        }
    }

    int threshold = 32;

    public static int log2(int n) {
        int log = 0;
        while ((n >>= 1) != 0)
            ++log;
        return log;
    }

    // Easy patch to avoid self-swaps.
    public void swap(int[] array, int a, int b, double pause, boolean mark, boolean aux) {
        if (a != b)
            Writes.swap(array, a, b, pause, mark, aux);
    }

    void medianOfThree(int[] array, int a, int b) {
        int m = a + (b - 1 - a) / 2;

        if (Reads.compareIndices(array, a, m, 1, true) > 0)
            Writes.swap(array, a, m, 1, true, false);

        if (Reads.compareIndices(array, m, b - 1, 1, true) > 0) {
            Writes.swap(array, m, b - 1, 1, true, false);

            if (Reads.compareIndices(array, a, m, 1, true) > 0)
                return;
        }

        Writes.swap(array, a, m, 1, true, false);
    }

    public void shuffle(int[] array, int a, int b) {
        Random rng = new Random();
        for (int i = a; i < b; i++) {
            swap(array, i, i + rng.nextInt(b - i), 0.75, true, false);
        }
    }

    public int partition(int[] array, int a, int b, int p) {
        int i = a, j = b;
        Highlights.markArray(3, p);

        while (true) {
            do {
                i++;
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
            } while (i < j && Reads.compareIndices(array, i, p, 0, false) < 0);

            do {
                j--;
                Highlights.markArray(2, j);
                Delays.sleep(0.5);
            } while (j >= i && Reads.compareIndices(array, j, p, 0, false) > 0);

            if (i < j)
                Writes.swap(array, i, j, 1, true, false);
            else {
                swap(array, p, j, 1, true, false);
                Highlights.clearMark(3);
                return j;
            }
        }
    }

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        boolean change = false;
        while (a > b) {
            Writes.write(array, a, array[--a], 0.25, true, false);
            change = true;
        }
        if (change)
            Writes.write(array, b, temp, 0.25, true, false);
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

    private void siftDown(int[] array, int val, int i, int p, int n) {
        while (4 * i + 1 < n) {
            int max = val;
            int next = i, child = 4 * i + 1;
            for (int j = child; j < Math.min(child + 4, n); j++) {
                if (Reads.compareValues(array[p + j], max) > 0) {
                    max = array[p + j];
                    next = j;
                }
            }
            if (next == i)
                break;
            Writes.write(array, p + i, max, 1, true, false);
            i = next;
        }
        Writes.write(array, p + i, val, 1, true, false);
    }

    protected void heapSort(int[] array, int a, int b) {
        int n = b - a;
        for (int i = (n - 1) / 4; i >= 0; i--)
            this.siftDown(array, array[a + i], i, a, n);
        for (int i = n - 1; i > 0; i--) {
            Highlights.markArray(2, a + i);
            int t = array[a + i];
            Writes.write(array, a + i, array[a], 1, false, false);
            this.siftDown(array, t, 0, a, i);
        }
    }

    protected void innerSort(int[] array, int start, int end) {
        if (end - start <= this.threshold) {
            insertSort(array, start, end);
            return;
        }
        PriorityQueue<Partition> q = new PriorityQueue<>((end - start - 1) / this.threshold + 1);
        q.offer(new Partition(start, end, log2(end - start)));
        sortLoop:
        while (q.size() > 0) {
            Partition part = q.poll();
            int a = part.a, b = part.b, len = part.length();
            int badAllowed = part.badAllowed;
            medianOfThree(array, a, b);
            int m = partition(array, a, b, a);
            int l = m - a, r = b - m - 1;
            while (l < len / 16 || r < len / 16) {
                if (--badAllowed <= 0) {
                    heapSort(array, a, b);
                    continue sortLoop;
                }
                shuffle(array, a, b);
                medianOfThree(array, a, b);
                m = partition(array, a, b, a);
                l = m - a; r = b - m - 1;
            }
            if (l >= threshold)
                q.offer(new Partition(a, m, badAllowed));
            else
                insertSort(array, a, m);
            if (r >= threshold)
                q.offer(new Partition(m + 1, b, badAllowed));
            else
                insertSort(array, m + 1, b);
        }
    }

    public void quickSort(int[] array, int a, int b) {
        int z = 0, e = 0;
        for (int i = a; i < b - 1; i++) {
            int cmp = Reads.compareIndices(array, i, i + 1, 0.5, true);
            z += cmp > 0 ? 1 : 0;
            e += cmp == 0 ? 1 : 0;
        }
        if (z == 0)
            return;
        if (z + e == b - a - 1) {
            if (b - a < 4) {
                Writes.swap(array, a, b - 1, 0.75, true, false);
            } else
                Writes.reversal(array, a, b - 1, 0.75, true, false);
            return;
        }
        innerSort(array, a, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
