package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class AggravatedAssaultSort extends BogoSorting {
    
    int min;
    
    public AggravatedAssaultSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Aggravated Assault");
        this.setRunAllSortsName("Aggravated Assault Sort");
        this.setRunSortName("Aggravated Assaultsort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(10);
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

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        if (!isArraySorted(array, currentLength)) {
            min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            for (int i = 0; i < currentLength; i++) {
                if (array[i] < min) min = array[i];
                if (array[i] > max) max = array[i];
            }
            int[] init = Writes.createExternalArray(currentLength);
            Writes.arraycopy(array, 0, init, 0, currentLength, delay, true, true);
            boolean finalized = false;
            while (!finalized) {
                for (int i = 0; i < currentLength; i++) Writes.write(array, i, BogoSorting.randInt(min, max + 1), delay, true, false);
                while (!isArraySorted(array, currentLength)) for (int i = 0; i < currentLength; i++) Writes.write(array, i, BogoSorting.randInt(min, max + 1), delay, true, false);
                int a = BogoSorting.randInt(0, currentLength);
                int b = BogoSorting.randInt(0, currentLength);
                while (a == b) b = BogoSorting.randInt(0, currentLength);
                Writes.swap(array, a, b, delay, true, false);
                finalized = isAnagram(array, init, currentLength);
            }
        }
    }
}