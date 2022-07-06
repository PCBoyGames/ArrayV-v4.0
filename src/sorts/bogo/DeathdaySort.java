package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class DeathdaySort extends BogoSorting {

    int min;
    int max;
    boolean maxallow;

    public DeathdaySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Deathday");
        this.setRunAllSortsName("Deathday Sort");
        this.setRunSortName("Deathday Sort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(12);
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

    protected boolean analyzeMax(int[] array, int currentLength) {
        int select = 0;
        while (Reads.compareValues(max, array[select]) != 0) select++;
        for (int i = select + 1; i < currentLength; i++) {
            if (Reads.compareIndices(array, select, i, delay, true) == 0) return true;
        }
        return false;
    }

    protected boolean attempt(int[] array, int currentLength) {
        // Decorative.
        for (int i = 1; i < currentLength; i++) array[i] = min;
        for (int i = 1; i < currentLength; i++) {
            if (!maxallow) if (Reads.compareValues(array[i - 1], max) == 0) return false;
            if (Reads.compareValues(array[i - 1], max) == 0) {
                for (; i < currentLength; i++) Writes.write(array, i, max, 0, true, false);
                break;
            }
            Writes.write(array, i, BogoSorting.randInt(array[i - 1], max + 1), delay, true, false);
        }
        if (Reads.compareValues(array[currentLength - 1], max) != 0) return false;
        return true;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        if (!isArraySorted(array, currentLength)) {
            min = Integer.MAX_VALUE;
            max = Integer.MIN_VALUE;
            for (int i = 0; i < currentLength; i++) {
                if (array[i] < min) min = array[i];
                if (array[i] > max) max = array[i];
            }
            int[] init = Writes.createExternalArray(currentLength);
            Writes.arraycopy(array, 0, init, 0, currentLength, delay, true, true);
            boolean finalized = false;
            maxallow = analyzeMax(init, currentLength);
            // Decorative.
            array[0] = min;
            while (!finalized) {
                Highlights.clearAllMarks();
                attempt(array, currentLength);
                while (!attempt(array, currentLength));
                finalized = isAnagram(array, init, currentLength);
            }
        }
    }
}