package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

Coded for ArrayV by Ayako-chan

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 *
 */
public final class ExchangeCocktailBomoSort extends BogoSorting {

    public ExchangeCocktailBomoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Exchange Cocktail Bomo");
        this.setRunAllSortsName("Exchange Cocktail Bomo Sort");
        this.setRunSortName("Exchange Cocktail Bomosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(false);
    }

    public void pull(int[] array, int a, int b) {
        if (a < b) {
            for (int i = a; i < b; i++) {
                if (Reads.compareIndices(array, i, i + 1, this.delay, true) > 0) {
                    Writes.swap(array, i, i + 1, delay, true, false);
                }
            }
        } else {
            for (int i = a; i > b; i--) {
                if (Reads.compareIndices(array, i, i - 1, this.delay, true) < 0) {
                    Writes.swap(array, i, i - 1, delay, true, false);
                }
            }
        }
    }

    public void sort(int[] array, int a, int b) {
        int min = a, max = b;
        while (min < max-1) {
            if (this.isRangeSorted(array, min, max, false, true))
                break;
            if (this.isMinSorted(array, min, max)) {
                Highlights.markArray(3, min);
                ++min;
                continue;
            }
            if (this.isMaxSorted(array, min, max)) {
                Highlights.markArray(4, max-1);
                --max;
                continue;
            }
            pull(array, BogoSorting.randInt(min, max), BogoSorting.randInt(min, max));
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
