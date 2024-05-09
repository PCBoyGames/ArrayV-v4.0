package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Harumi
extending code by Lancewer

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Harumi
 * @author Lancewer
 *
 */
public class BisurgeSortParallel extends Sort {

    public BisurgeSortParallel(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Bisurge (Parallel)");
        this.setRunAllSortsName("Parallel Bisurge Sort");
        this.setRunSortName("Parallel Bisurgesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int[] array;

    protected class SortThread extends Thread {
        protected int a, b;
        SortThread(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public void run() {
            BisurgeSortParallel.this.sortHelper(a, b);
        }
    }

    protected int rightBinSearch(int a, int b, int val) {
        while (a < b) {
            int m = a+(b-a)/2;
            Highlights.markArray(2, m);
            Delays.sleep(0.125);
            if (Reads.compareValues(val, array[m]) < 0)
                b = m;
            else
                a = m+1;
        }
        return a;
    }

    protected void insertTo(int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        boolean change = false;
        while (a > b) Writes.write(array, a, array[--a], 0.125, change = true, false);
        if (change) Writes.write(array, b, temp, 0.125, true, false);
    }

    protected void bisurgeInsert(int a, int m, int b) {
        int binStart = a, lastPos = a;
        for (int i = m; i < b; i++) {
            if (binStart - i == 1 && Reads.compareIndices(array, binStart, i, 0.125, true) > 0) break;
            int j = rightBinSearch(lastPos, i, array[i]);
            lastPos = j;
            binStart = j;
            insertTo(i, j);
        }
    }

    protected void sortHelper(int a, int b) {
        if (b - a < 2)
            return;
        int m = a + (b - a) / 2;
        SortThread left = new SortThread(a, m);
        SortThread right = new SortThread(m, b);
        left.start();
        right.start();
        try {
            left.join();
            right.join();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        bisurgeInsert(a, m, b);
    }

    public void sort(int[] array, int a, int b) {
        this.array = array;
        sortHelper(a, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
