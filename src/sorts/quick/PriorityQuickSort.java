package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import sorts.insert.BinaryInsertionSort;
import java.util.PriorityQueue;

final public class PriorityQuickSort extends Sort {
    public PriorityQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Priority Quick");
        this.setRunAllSortsName("Priority Quick Sort");
        this.setRunSortName("Priority Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private final int MIN = 32;

    private final class Partition implements Comparable<Partition> {
        public int a, b;

        public Partition(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public int length() {
            return this.b-this.a;
        }

        @Override
        public int compareTo(Partition y) {
            return (this.length() < y.length()) ? 1 : -1;
        }
    }

    private void medianOfThree(int[] array, int a, int b) {
        int m = a+(b-1-a)/2;

        if (Reads.compareIndices(array, a, m, 1, true) == 1)
            Writes.swap(array, a, m, 1, true, false);

        if (Reads.compareIndices(array, m, b-1, 1, true) == 1) {
            Writes.swap(array, m, b-1, 1, true, false);

            if (Reads.compareIndices(array, a, m, 1, true) == 1)
                return;
        }

        Writes.swap(array, a, m, 1, true, false);
    }

    private int partition(int[] array, int a, int b) {
        int i = a, j = b;

        this.medianOfThree(array, a, b);
        Highlights.markArray(3, a);

        while (true) {
            do {
                i++;
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
            }
            while (i < j && Reads.compareIndices(array, i, a, 0, false) == -1);

            do {
                j--;
                Highlights.markArray(2, j);
                Delays.sleep(0.5);
            }
            while (j >= i && Reads.compareIndices(array, j, a, 0, false) == 1);

            if (i < j) Writes.swap(array, i, j, 1, true, false);
            else {
                Writes.swap(array, a, j, 1, true, false);
                return j;
            }
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);
        PriorityQueue<Partition> queue = new PriorityQueue<>((length-1)/this.MIN+1);
        queue.offer(new Partition(0, length));

        while (queue.size() > 0) {
            Partition part = queue.poll();

            int a = part.a, b = part.b;
            int p = this.partition(array, a, b);

            if (p-a   >= this.MIN) queue.offer(new Partition(a, p));
            else                  smallSort.customBinaryInsert(array, a, p, 0.25);
            if (b-p-1 >= this.MIN) queue.offer(new Partition(p+1, b));
            else                  smallSort.customBinaryInsert(array, p+1, b, 0.25);
        }
    }
}
