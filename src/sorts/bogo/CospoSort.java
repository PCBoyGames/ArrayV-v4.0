package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class CospoSort extends BogoSorting {
    public CospoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Cospo");
        this.setRunAllSortsName("Cospo Sort");
        this.setRunSortName("Cosposort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(true);
    }

    protected void sortexternal(int[] array, int[] correct, int currentLength) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < currentLength; i++) {
            if (array[i] < min) min = array[i];
            if (array[i] > max) max = array[i];
        }
        int size = max - min + 1;
        int[] holes = Writes.createExternalArray(size);
        for (int x = 0; x < currentLength; x++) {
            Highlights.markArray(1, x);
            Writes.write(holes, array[x] - min, holes[array[x] - min] + 1, 1, false, true);
        }
        Highlights.clearMark(2);
        for (int count = 0, j = 0; count < size; count++) {
            for (int i = 0; i < holes[count]; i++, j++) {
                Writes.write(correct, j, count + min, 1, false, true);
                Highlights.markArray(1, j + 1);
            }
        }
        Writes.deleteExternalArray(holes);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        delay = 0.25;
        int[] correct = Writes.createExternalArray(currentLength);
        sortexternal(array, correct, currentLength);
        int spot1 = 0;
        int spot2 = 0;
        int verifyL = 0;
        int verifyR = currentLength - 1;
        while (Reads.compareValues(array[verifyL], correct[verifyL]) == 0) {
            Highlights.markArray(1, verifyL);
            Delays.sleep(delay);
            verifyL++;
            if (verifyL == verifyR) break;
        }
        if (verifyL != verifyR) {
            while (Reads.compareValues(array[verifyR], correct[verifyR]) == 0) {
                Highlights.markArray(1, verifyR);
                Delays.sleep(delay);
                verifyR--;
            }
        }
        while (verifyL < verifyR) {
            Highlights.clearAllMarks();
            boolean spot1found = false;
            while (!spot1found) {
                spot1 = randInt(verifyL, verifyR + 1);
                Highlights.markArray(1, spot1);
                Delays.sleep(0);
                spot1found = Reads.compareValues(array[spot1], correct[spot1]) != 0;
            }
            boolean spot2found = false;
            while (!spot2found) {
                spot2 = randInt(verifyL, verifyR + 1);
                Highlights.markArray(1, spot2);
                Delays.sleep(0);
                if (spot1 != spot2) spot2found = Reads.compareValues(array[spot2], correct[spot2]) != 0;
            }
            Writes.swap(array, spot1, spot2, delay, true, false);
            Highlights.clearAllMarks();
            while (Reads.compareValues(array[verifyL], correct[verifyL]) == 0) {
                Highlights.markArray(1, verifyL);
                Delays.sleep(delay);
                verifyL++;
                if (verifyL == verifyR) break;
            }
            if (verifyL != verifyR) {
                while (Reads.compareValues(array[verifyR], correct[verifyR]) == 0) {
                    Highlights.markArray(1, verifyR);
                    Delays.sleep(delay);
                    verifyR--;
                }
            }
        }
    }
}