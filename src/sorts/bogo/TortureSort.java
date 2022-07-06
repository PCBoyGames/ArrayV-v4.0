package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class TortureSort extends BogoSorting {

    int min;
    int max;

    public TortureSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Torture");
        this.setRunAllSortsName("Torture Sort");
        this.setRunSortName("Torturesort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8);
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

    protected void handleItems(int[] array, int currentLength) {
        int i = BogoSorting.randInt(0, currentLength);
        int j = BogoSorting.randInt(0, currentLength);
        while (j == i) j = BogoSorting.randInt(0, currentLength);
        Writes.swap(array, i, j, delay, true, false);
        if (BogoSorting.randBoolean()) Writes.write(array, i, BogoSorting.randInt(min, max + 1), delay, true, false);
        else Writes.write(array, j, BogoSorting.randInt(min, max + 1), delay, true, false);
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
            while (!finalized) {
                handleItems(array, currentLength);
                while (!isArraySorted(array, currentLength)) for (int i = 0; i < currentLength; i++) handleItems(array, currentLength);
                finalized = isAnagram(array, init, currentLength);
            }
        }
    }
}