package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

mediocre merge sort

 */

public class RiftSortParallel extends Sort {
    public RiftSortParallel(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Rift (Parallel)");
        this.setRunAllSortsName("Parallel Rift Sort");
        this.setRunSortName("Parallel Riftsort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int[] array;

    private class RiftSort extends Thread {
        private int a, b, d;
        public RiftSort(int a, int b, int d) {
            this.a = a;
            this.b = b;
            this.d = d;
        }
        public void run() {
            RiftSortParallel.this.riftSorter(a, b, d);
        }
    }

    public void riftSorter(int a, int b, int d) {
        Writes.recordDepth(d++);
        if (a >= b) return;
        int m = (b-a)/2;
        Writes.recursion();
        RiftSort l = new RiftSort(a, a+m, d);
        Writes.recursion();
        RiftSort r = new RiftSort(b-m, b, d);
        l.start();
        r.start();
        try {
            l.join();
            r.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        int m1 = (b-a+1)/2;
        for (int i = 0; i < m1; i++)
            for (int j = a+i; j < a+m1; j++)
                if (Reads.compareIndices(array, j, j+m1-i, 0.5, true) > 0)
                    Writes.swap(array, j, j+m1-i, 0.5, true, false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.array = array;
        this.riftSorter(0, currentLength-1, 0);
    }
}