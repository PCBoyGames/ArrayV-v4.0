package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class ChirSort extends BogoSorting {
    public ChirSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Chir");
        this.setRunAllSortsName("Chir Sort");
        this.setRunSortName("Chirsort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(11);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        while (!isArraySorted(array, currentLength)) {
            int choice = randInt(1, 11);

            // Bogo
            if (choice == 1) bogoSwap(array, 0, currentLength, false);

            // Bozo
            if (choice == 2) Writes.swap(array, randInt(0, currentLength), randInt(0, currentLength), delay, true, false);

            // Bovo
            if (choice == 3) Writes.multiSwap(array, randInt(0, currentLength - 1), 0, delay, true, false);

            // Vogo
            if (choice == 4) Writes.multiSwap(array, randInt(0, currentLength - 1), currentLength - 1, delay, true, false);

            // Bojo
            if (choice == 5) {
                int i1 = randInt(0, currentLength);
                int i2 = randInt(0, currentLength);
                int temp;
                if (i1 > i2) {
                    temp = i1;
                    i1 = i2;
                    i2 = temp;
                }
                Writes.reversal(array, i1, i2, delay, true, false);
            }

            // Boko
            if (choice == 6) {
                int i = randInt(0, currentLength - 1);
                Writes.swap(array, i, i + 1, delay, true, false);
            }

            // Bomo
            if (choice == 7) Writes.multiSwap(array, randInt(0, currentLength - 1), randInt(0, currentLength - 1), delay, true, false);

            // Slice Bogo
            if (choice == 8) {
                int i1 = randInt(0, currentLength);
                int i2 = randInt(0, currentLength);
                int temp;
                if (i1 > i2) {
                    temp = i1;
                    i1 = i2;
                    i2 = temp;
                }
                bogoSwap(array, i1, i2, false);
            }

            // Baka (suited for number 9)
            if (choice == 9) Writes.swap(array, 0, randInt(1, currentLength), delay, true, false);

            // Nibi
            if (choice == 10) Writes.swap(array, currentLength - 1, randInt(0, currentLength - 1), delay, true, false);
        }
    }
}