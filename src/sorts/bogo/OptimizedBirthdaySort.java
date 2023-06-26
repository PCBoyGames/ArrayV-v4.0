package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class OptimizedBirthdaySort extends BogoSorting {
    public OptimizedBirthdaySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Birthday");
        this.setRunAllSortsName("Optimized Birthday Sort");
        this.setRunSortName("Optimized Birthday Sort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < currentLength; i++) {
            if (array[i] < min) min = array[i];
            if (array[i] > max) max = array[i];
        }
        int size = max - min + 1;
        int[] holes = Writes.createExternalArray(size);
        for (int x = 0; x < currentLength; x++) {
            Highlights.markArray(2, x);
            Writes.write(holes, array[x] - min, holes[array[x] - min] + 1, 1, true, true);
        }
        Highlights.clearMark(2);
        for (int count = 0, j = 0; count < size; count++) {
            for (int i = 0; i < holes[count]; i++, j++) {
                Highlights.markArray(1, j);
                Delays.sleep(1);
                while (count + min != array[j]) Writes.write(array, j, randInt(j > 0 ? array[j - 1] : min, max + 1), 0.1, true, false);
            }
        }
        Writes.deleteExternalArray(holes);
    }
}