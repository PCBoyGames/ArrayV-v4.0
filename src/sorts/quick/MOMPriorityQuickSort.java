package sorts.quick;

import java.util.PriorityQueue;
import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
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
public final class MOMPriorityQuickSort extends Sort {

    public MOMPriorityQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("MOM Priority Quick");
        this.setRunAllSortsName("Median-of-Medians Priority Quick Sort");
        this.setRunSortName("Median-of-Medians Priority Quicksort");
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
            return this.b-this.a;
        }

        public int compare(Partition y) {
            return (this.length() < y.length()) ? 1 : -1;
        }

        @Override
        public int compareTo(Partition y) {
            return (this.length() < y.length()) ? 1 : -1;
        }
    }

    InsertionSort insSort;

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

    void medianOfMedians(int[] array, int a, int b, int s) {
        int end = b, start = a, i, j;
        boolean ad = true;

        while (end - start > 1) {
            j = start;
            Highlights.markArray(2, j);
            for (i = start; i + 2 * s <= end; i += s) {
                this.insSort.customInsertSort(array, i, i + s, 0.25, false);
                Writes.swap(array, j++, i + s / 2, 1.0, false, false);
                Highlights.markArray(2, j);
            }
            if (i < end) {
                this.insSort.customInsertSort(array, i, end, 0.25, false);
                Writes.swap(array, j++, i + (end - (ad ? 1 : 0) - i) / 2, 1.0, false, false);
                Highlights.markArray(2, j);
                if ((end - i) % 2 == 0)
                    ad = !ad;
            }
            end = j;
        }
    }

    public int partition(int[] array, int a, int b, int p) {
        int i = a, j = b;
        Highlights.markArray(3, p);

        while(true) {
            do {
                i++;
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
            }
            while(i < j && Reads.compareIndices(array, i, p, 0, false) < 0);

            do {
                j--;
                Highlights.markArray(2, j);
                Delays.sleep(0.5);
            }
            while(j >= i && Reads.compareIndices(array, j, p, 0, false) > 0);

            if(i < j) Writes.swap(array, i, j, 1, true, false);
            else {
                Writes.swap(array, p, j, 1, true, false);
                Highlights.clearMark(3);
                return j;
            }
        }
    }

    public void quickSort(int[] array, int start, int end) {
        this.insSort = new InsertionSort(this.arrayVisualizer);
        if(end - start < this.threshold) {
            this.insSort.customInsertSort(array, start, end, 0.5, false);
            return;
        }
        PriorityQueue<Partition> q = new PriorityQueue<>((end - start - 1) / this.threshold + 1);
        q.offer(new Partition(start, end));
        while (q.size() > 0) {
            Partition p = q.poll();
            int a = p.a, b = p.b, len = p.length();
            medianOfThree(array, a, b);
            int m = partition(array, a, b, a);
            int l = m - a, r = b - m - 1;
            if((double) l / len < 0.0625 || (double) r / len < 0.0625) {
                medianOfMedians(array, a, b, 5);
                m = partition(array, a, b, a);
            }
            if(m - a >= threshold)
                q.offer(new Partition(a, m));
            else
                insSort.customInsertSort(array, a, m, 0.5, false);
            if(b - m - 1 >= threshold)
                q.offer(new Partition(m + 1, b));
            else
                insSort.customInsertSort(array, m + 1, b, 0.5, false);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
