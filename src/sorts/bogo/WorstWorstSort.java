package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

Nightmares.

*/
public class WorstWorstSort extends BogoSorting {

    int min;

    public WorstWorstSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Worst Worst");
        this.setRunAllSortsName("Worst Worst Sort");
        this.setRunSortName("Worst Worstsort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(2);
        this.setBogoSort(true);
    }

    protected boolean isAnagram(int[] input, int[] letters, int currentLength) {
        boolean anagram = true;
        int[] test = Writes.createExternalArray(currentLength);
        Writes.arraycopy(input, 0, test, 0, currentLength, delay, true, true);
        for (int i = 0; i < currentLength; i++) {
            int select = 0;
            boolean any = false;
            for (int j = 0; j < currentLength; j++) {
                if (Reads.compareValues(letters[i], test[j]) == 0) {
                    select = j;
                    any = true;
                    break;
                }
            }
            if (any) Writes.write(test, select, min - 1, delay, true, true);
            else {
                anagram = false;
                break;
            }
        }
        Writes.deleteExternalArray(test);
        return anagram;
    }

    protected void overrideLength(int n) {
        double sleepRatio = Delays.getSleepRatio();
        arrayVisualizer.setCurrentLength(n);
        Delays.setSleepRatio(sleepRatio);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        if (!isArraySorted(array, currentLength)) {
            int initlen = currentLength;
            int[] init = Writes.createExternalArray(currentLength);
            Writes.arraycopy(array, 0, init, 0, currentLength, delay, true, true);
            boolean finalized = false;
            while (!finalized) {
                currentLength = randInt(1, (int) Math.pow(2, ArrayVisualizer.MAX_LENGTH_POWER) + 1);
                overrideLength(currentLength);
                for (int i = 0; i < currentLength; i++) Writes.write(array, i, randInt(Integer.MIN_VALUE, Integer.MAX_VALUE), delay, true, false);
                while (!isArraySorted(array, currentLength)) {
                    currentLength = randInt(1, (int) Math.pow(2, ArrayVisualizer.MAX_LENGTH_POWER));
                    overrideLength(currentLength);
                    for (int i = 0; i < currentLength; i++) array[i] = -1;
                    for (int i = 0; i < currentLength; i++) Writes.write(array, i, randInt(Integer.MIN_VALUE, Integer.MAX_VALUE), delay, true, false);
                }
                if (currentLength == initlen) finalized = isAnagram(array, init, currentLength);
                if (!finalized) for (int i = 0; i < currentLength; i++) array[i] = -1;
            }
        }
    }
}