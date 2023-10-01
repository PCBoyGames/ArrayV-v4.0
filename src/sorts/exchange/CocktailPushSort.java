package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class CocktailPushSort extends Sort {
    public CocktailPushSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Cocktail Push");
        this.setRunAllSortsName("Cocktail Push Sort");
        this.setRunSortName("Cocktail Pushsort");
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
        boolean anyswaps = true;
        while (anyswaps) {
            anyswaps = false;
            int i = 1;
            int gap = 1;
            while (i + gap <= currentLength) {
                if (Reads.compareIndices(array, i - 1, (i - 1) + gap, 0.01, true) > 0) {
                    for (int j = 1; j <= gap; j++) Writes.swap(array, i - 1, (i - 1) + j, 0.01, anyswaps = true, false);
                    gap++;
                } else i++;
            }
            i = currentLength;
            gap = 1;
            while (i - gap > 0) {
                if (Reads.compareIndices(array, (i - 1) - gap, i - 1, 0.01, true) > 0) {
                    for (int j = 1; j <= gap; j++) Writes.swap(array, i - 1, (i - 1) - j, 0.01, anyswaps = true, false);
                    gap++;
                } else i--;
            }
        }
    }
}