package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class CocktailSandpaperSort extends Sort {
    public CocktailSandpaperSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Cocktail Sandpaper");
        this.setRunAllSortsName("Cocktail Sandpaper Sort");
        this.setRunSortName("Cocktail Sandpapersort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 0; i < currentLength - 1; i++) {
            for (int j = i + 1; j < currentLength; j++) if (Reads.compareIndices(array, i, j, 0.05, true) > 0) Writes.swap(array, i, j, 0.05, true, false);
            i++;
            for (int j = currentLength - 1; j > i; j--) if (Reads.compareIndices(array, i, j, 0.05, true) > 0) Writes.swap(array, i, j, 0.05, true, false);
        }
    }
}
