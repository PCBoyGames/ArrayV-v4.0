package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class PowSetSort extends Sort {

    int min;
    int max;

    public PowSetSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Power Set");
        this.setRunAllSortsName("Power Set Sort");
        this.setRunSortName("Power Set Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(14);
        this.setBogoSort(false);
    }

    protected int stablereturn(int a) {
        return arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(a) : a;
    }

    protected boolean noDupes(int[] array, int len) {
        int size = max - min + 1;
        int[] holes = new int[size];
        for (int x = 0; x < len; x++) {
            if (holes[stablereturn(array[x]) - min] == 1) return false;
            else holes[stablereturn(array[x]) - min] = 1;
        }
        return true;
    }

    protected boolean isAnagram(int[] input, int[] letters, int currentLength) {
        boolean anagram = true;
        int[] test = Writes.createExternalArray(currentLength);
        Writes.arraycopy(input, 0, test, 0, currentLength, 0, false, true);
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
            if (any) Writes.write(test, select, min - 1, 0, false, true);
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
        min = Integer.MAX_VALUE;
        max = Integer.MIN_VALUE;
        for (int i = 0; i < currentLength; i++) {
            if (stablereturn(array[i]) < min) min = stablereturn(array[i]);
            if (stablereturn(array[i]) > max) max = stablereturn(array[i]);
        }
        boolean equals = !noDupes(array, currentLength);
        int[] init = Writes.createExternalArray(currentLength);
        Writes.arraycopy(array, 0, init, 0, currentLength, 0, false, true);
        for (int i = 0; i < currentLength; i++) Writes.write(array, i, min, 0.1, true, false);
        boolean finalized = isAnagram(array, init, currentLength);
        while (!finalized) {
            if (equals) {
                boolean goback = false;
                for (int i = currentLength - 1; i > 0; i--) {
                    if (Reads.compareValues(array[i], max) >= 0) Writes.write(array, i, min, 0.1, goback = true, false);
                    else {
                        Writes.write(array, i, array[i] + 1, 0.1, true, false);
                        break;
                    }
                }
                if (goback) for (int i = 0; i + 1 < currentLength; i++) if (Reads.compareValues(array[i], array[i + 1]) > 0) while (i + 1 < currentLength) Writes.write(array, i + 1, array[i++], 0.1, true, false);
            } else {
                boolean loop = true;
                while (loop) {
                    boolean goback = false;
                    for (int i = currentLength - 1; i > 0; i--) {
                        if (Reads.compareValues(array[i], max) >= 0) Writes.write(array, i, min, 0.1, goback = true, false);
                        else {
                            Writes.write(array, i, array[i] + 1, 0.1, true, false);
                            break;
                        }
                    }
                    if (goback) {
                        for (int i = 0; i + 1 < currentLength; i++) {
                            if (Reads.compareValues(array[i], array[i + 1]) > 0) {
                                while (i + 1 < currentLength) {
                                    if (array[i] + 1 <= max) Writes.write(array, i + 1, array[i++] + 1, 0.1, true, false);
                                    else {
                                        Writes.write(array, i + 1, max + 1, 0.1, true, false);
                                        while (array[i + 1] - array[i] == 1) i--;
                                        Writes.write(array, i, array[i] + 1, 0.1, true, false);
                                        while (i + 1 < currentLength) Writes.write(array, i + 1, array[i++] + 1, 0.1, true, false);
                                    }
                                }
                            }
                        }
                    }
                    if (Reads.compareValues(array[currentLength - 1], max) <= 0) loop = false;
                }
            }
            finalized = isAnagram(array, init, currentLength);
        }
    }
}
