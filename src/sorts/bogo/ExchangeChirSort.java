package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class ExchangeChirSort extends BogoSorting {
    public ExchangeChirSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Exchange Chir");
        this.setRunAllSortsName("Exchange Chir Sort");
        this.setRunSortName("Exchange Chirsort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(32768);
        this.setBogoSort(true);
    }

    protected void bogoCompSwap(int[] array, int a, int b) {
        for (int i = a; i < b; i++) {
            int j = randInt(i, b);
            if (Reads.compareIndices(array, i, j, delay, true) > 0) Writes.swap(array, i, j, delay, true, false);
        }
    }

    protected void bogoCompSwapBW(int[] array, int a, int b) {
        for (int i = b - 1; i > a; i--) {
            int j = randInt(a, i);
            if (Reads.compareIndices(array, i, j, delay, true) < 0) Writes.swap(array, i, j, delay, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        delay = 0.1;
        while (!isArraySorted(array, currentLength)) {
            int choice = randInt(1, 9);

            // Exchange Bogo
            if (choice == 1) {
                int d = randInt(1, 3);
                if (d == 1) bogoCompSwap(array, 0, currentLength);
                else bogoCompSwapBW(array, 0, currentLength);
            }

            // Exchange Bozo
            if (choice == 2) {
                int i1 = randInt(0, currentLength);
                int i2 = randInt(0, currentLength);
                int temp;
                if (i1 > i2) {
                    temp = i1;
                    i1 = i2;
                    i2 = temp;
                }
                if (Reads.compareIndices(array, i1, i2, delay, true) > 0) Writes.swap(array, i1, i2, delay, true, false);
            }

            // Exchange Bovo
            if (choice == 3) {
                int pull = randInt(0, currentLength - 1);
                while (pull >= 0) {
                    if (Reads.compareIndices(array, pull, pull + 1, delay, true) > 0) Writes.swap(array, pull, pull + 1, delay, true, false);
                    pull--;
                }
            }

            // Exchange Vogo
            if (choice == 4) {
                int pull = randInt(0, currentLength - 1);
                while (pull + 1 < currentLength) {
                    if (Reads.compareIndices(array, pull, pull + 1, delay, true) > 0) Writes.swap(array, pull, pull + 1, delay, true, false);
                    pull++;
                }
            }

            // Exchange Bojo
            if (choice == 5) {
                int i1 = randInt(0, currentLength);
                int i2 = randInt(0, currentLength);
                int temp;
                if (i1 > i2) {
                    temp = i1;
                    i1 = i2;
                    i2 = temp;
                }
                if (Reads.compareIndices(array, i1, i2, delay, true) > 0) Writes.reversal(array, i1, i2, delay, true, false);
            }

            // Bubble Bogo
            if (choice == 6) {
                int i = randInt(0, currentLength - 1);
                if (Reads.compareIndices(array, i, i + 1, delay, true) > 0) Writes.swap(array, i, i + 1, delay, true, false);
            }

            // Exchange Bomo
            if (choice == 7) {
                int start = randInt(0, currentLength - 1);
                int end = randInt(0, currentLength - 1);
                if (start > end) for (int i = start; i > end + 1; i--) if (Reads.compareIndices(array, i - 1, i, delay, true) > 0) Writes.swap(array, i, i - 1, delay, true, false);
                if (end > start) for (int i = start; i < end + 1; i++) if (Reads.compareIndices(array, i, i + 1, delay, true) > 0) Writes.swap(array, i, i + 1, delay, true, false);
            }

            // Exchange Goro
            if (choice == 8) {
                int i1 = randInt(0, currentLength);
                int i2 = randInt(0, currentLength);
                int temp;
                if (i1 > i2) {
                    temp = i1;
                    i1 = i2;
                    i2 = temp;
                }
                int d = randInt(1, 3);
                if (d == 1) bogoCompSwap(array, i1, i2);
                else bogoCompSwapBW(array, i1, i2);
            }
        }
    }
}