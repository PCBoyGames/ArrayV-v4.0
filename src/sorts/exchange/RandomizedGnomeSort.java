package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class RandomizedGnomeSort extends BogoSorting {
    public RandomizedGnomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Randomized Gnome");
        this.setRunAllSortsName("Randomized Gnome Sort");
        this.setRunSortName("Randomized Gnomesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int s = 0;
        while (s < currentLength && Reads.compareIndices(array, s, s + 1, 0.1, true) <= 0) s++;
        for (int i = s; i < currentLength; i++) {
            Writes.multiSwap(array, randInt(i, currentLength), i, 0.1, true, false);
            int j = i - 1;
            while (j >= 0 && Reads.compareIndices(array, j, j + 1, 0.1, true) > 0) {
                Writes.swap(array, j, j + 1, 0.1, true, false);
                j--;
            }
        }
    }
}