package sorts.quick;

import java.util.Random;

import java.util.PriorityQueue;
import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Kiriko-chan
in collaboration with aphitorite

-----------------------------
- Sorting Algorithm Scarlet -
-----------------------------

 */

/**
 * @author Kiriko-chan
 * @author aphitorite
 *
 */
public final class ShufflePriorityQuickSort extends Sort {

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
        public int a, b;

        public Partition(int a, int b) {
            this.a = a;
            this.b = b;
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
            Writes.swap(array, i, i + rng.nextInt(b - i), 0.75, true, false);
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
                Writes.swap(array, p, j, 1, true, false);
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
        for(int i = a + 1; i < b; i++)
            insertTo(array, i, expSearch(array, a, i, array[i]));
    }

    public void quickSort(int[] array, int start, int end) {
        if(end - start < this.threshold) {
            insertSort(array, start, end);
            return;
        }
        PriorityQueue<Partition> q = new PriorityQueue<>((end - start - 1) / this.threshold + 1);
        q.offer(new Partition(start, end));
        while(q.size() > 0) {
            Partition part = q.poll();
            int a = part.a, b = part.b, len = part.length();
            medianOfThree(array, a, b);
            int m = partition(array, a, b, a);
            int l = m - a, r = b - m - 1;
            while(l < len / 16 || r < len / 16) {
                shuffle(array, a, b);
                m = partition(array, a, b, a);
                l = m - a; r = b - m - 1;
            }
            if(l >= threshold)
                q.offer(new Partition(a, m));
            else
                insertSort(array, a, m);
            if(r >= threshold)
                q.offer(new Partition(m + 1, b));
            else
                insertSort(array, m + 1, b);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
