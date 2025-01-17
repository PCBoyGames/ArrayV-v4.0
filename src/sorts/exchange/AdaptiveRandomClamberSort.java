package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class AdaptiveRandomClamberSort extends BogoSorting {
    public AdaptiveRandomClamberSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive Random Clamber");
        this.setRunAllSortsName("Adaptive Random Clamber Sort");
        this.setRunSortName("Adaptive Random Clambersort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int randomSearch(int[] array, int a, int b, int value) {
        while (a < b) {
            int m = randInt(a, b);
            Highlights.markArray(1, a);
            Highlights.markArray(3, m);
            Highlights.markArray(2, b);
            Delays.sleep(1);
            if (Reads.compareValues(value, array[m]) < 0) b = m;
            else a = m + 1;
        }
        Highlights.clearMark(3);
        return a;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int right = 1; right < currentLength; right++) {
            if (Reads.compareIndices(array, right - 1, right, 1, true) > 0) {
                int left = randomSearch(array, 0, right - 1, array[right]);
                while (left < right) Writes.swap(array, left++, right, 0.2, true, false);
            }
        }
    }
}