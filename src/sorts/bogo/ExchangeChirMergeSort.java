package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

Coded for ArrayV by Kiriko-chan
in collaboration with PCBoy

-----------------------------
- Sorting Algorithm Scarlet -
-----------------------------

 */

/**
 * @author Kiriko-chan
 * @author PCBoy
 *
 */
public class ExchangeChirMergeSort extends BogoSorting {

    public ExchangeChirMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Exchange Chir Merge");
        this.setRunAllSortsName("Exchange Chir Merge Sort");
        this.setRunSortName("Exchange Chir Mergesort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(true);
    }

    protected void bogoCompSwap(int[] array, int a, int b) {
        for (int i = a; i < b; i++) {
            int j = BogoSorting.randInt(i, b);
            if (Reads.compareIndices(array, i, j, this.delay, true) > 0) {
                Writes.swap(array, i, j, this.delay, true, false);
            }
        }
    }

    protected void bogoCompSwapBW(int[] array, int a, int b) {
        for (int i = b - 1; i > a; i--) {
            int j = BogoSorting.randInt(a, i);
            if (Reads.compareIndices(array, i, j, this.delay, true) < 0) {
                Writes.swap(array, i, j, this.delay, true, false);
            }
        }
    }

    public void pull(int[] array, int a, int b) {
        if (a < b) {
            for (int i = a; i < b; i++) {
                if (Reads.compareIndices(array, i, i + 1, this.delay, true) > 0) {
                    Writes.swap(array, i, i + 1, delay, true, false);
                }
            }
        } else {
            for (int i = a; i > b; i--) {
                if (Reads.compareIndices(array, i, i - 1, this.delay, true) < 0) {
                    Writes.swap(array, i, i - 1, delay, true, false);
                }
            }
        }
    }

    public void exchangeChir(int[] array, int a, int b) {
        while (!this.isRangeSorted(array, a, b, false, true)) {
            int choice = BogoSorting.randInt(0, 8);
            // Exchange Bogo
            if (choice == 0) {
                int d = BogoSorting.randInt(1, 3);
                if (d == 1)
                    bogoCompSwap(array, a, b);
                else
                    bogoCompSwapBW(array, a, b);
            }
            //Exchange Bozo
            if (choice == 1) {
                int index1 = BogoSorting.randInt(a, b), index2 = BogoSorting.randInt(a, b);
                int comp = Reads.compareIndices(array, index1, index2, this.delay, true);
                if (index1 < index2 ? comp > 0 : comp < 0)
                    Writes.swap(array, index1, index2, this.delay, true, false);
            }
            //Bubble Bogo
            if (choice == 2) {
                int i = BogoSorting.randInt(a, b - 1);
                if (Reads.compareIndices(array, i, i + 1, this.delay, true) > 0)
                    Writes.swap(array, i, i + 1, this.delay, true, false);
            }
            //Exchange Bojo
            if (choice == 3) {
                int index1 = BogoSorting.randInt(a, b), index2 = BogoSorting.randInt(a, b);
                int comp = Reads.compareIndices(array, index1, index2, this.delay, true);
                if (index1 < index2 ? comp > 0 : comp < 0)
                    Writes.reversal(array, index1, index2, this.delay, true, false);
            }
            //Exchange Bovo
            if (choice == 4) {
                pull(array, BogoSorting.randInt(a, b), a);
            }
            //Exchange Vogo
            if (choice == 5) {
                pull(array, BogoSorting.randInt(a, b), b - 1);
            }
            //Exchange Bomo
            if (choice == 6) {
                pull(array, BogoSorting.randInt(a, b), BogoSorting.randInt(a, b));
            }
            //Exchange Slice Bogo
            if (choice == 7) {
                int i1 = BogoSorting.randInt(a, b);
                int i2 = BogoSorting.randInt(a, b);
                int temp;
                if (i1 > i2) {
                    temp = i1;
                    i1 = i2;
                    i2 = temp;
                }
                int d = BogoSorting.randInt(1, 3);
                if (d == 1)
                    bogoCompSwap(array, i1, i2);
                else
                    bogoCompSwapBW(array, i1, i2);
            }
        }
    }

    public void sort(int[] array, int a, int b, int depth) {
        Writes.recordDepth(depth);
        if (b - a < 2)
            return;
        int m = a + (b - a) / 2;
        Writes.recursion();
        sort(array, a, m, depth + 1);
        Writes.recursion();
        sort(array, m, b, depth + 1);
        exchangeChir(array, a, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        delay = 0.1;
        sort(array, 0, sortLength, 0);
    }
}
