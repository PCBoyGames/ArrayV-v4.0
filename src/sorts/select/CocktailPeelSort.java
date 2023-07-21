package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class CocktailPeelSort extends Sort {
    public CocktailPeelSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Cocktail Peel");
        this.setRunAllSortsName("Cocktail Peel Sort");
        this.setRunSortName("Cocktail Peelsort");
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
        for (int left = 0; left < currentLength; left++) {
            int stacked = 0;
            for (int right = currentLength - 1; right > left; right--) {
                if (Reads.compareIndices(array, left, right + stacked, 0.05, true) > 0) {
                    Highlights.markArray(3, left);
                    Writes.insert(array, right + stacked, left, 0.05, true, false);
                    stacked++;
                    Highlights.clearMark(3);
                }
            }
            left++;
            for (int right = left + 1; right < currentLength; right++) {
                if (Reads.compareIndices(array, left, right, 0.05, true) > 0) {
                    Highlights.markArray(3, left);
                    Writes.insert(array, right, left, 0.05, true, false);
                    Highlights.clearMark(3);
                }
            }
        }
    }
}